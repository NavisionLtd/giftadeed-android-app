package giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.squareup.okhttp.OkHttpClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import giftadeed.kshantechsoft.com.giftadeed.Filter.CategoryPOJO;
import giftadeed.kshantechsoft.com.giftadeed.Group.GroupPOJO;
import giftadeed.kshantechsoft.com.giftadeed.Group.GroupsInterface;
import giftadeed.kshantechsoft.com.giftadeed.Login.LoginActivity;
import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.CategoryInterface;
import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.CategoryType;
import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.GPSTracker;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.list_Model.Modeltaglist;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.list_Model.taglistInterface;
import giftadeed.kshantechsoft.com.giftadeed.Utils.DBGAD;
import giftadeed.kshantechsoft.com.giftadeed.Utils.DatabaseAccess;
import giftadeed.kshantechsoft.com.giftadeed.Utils.SessionManager;
import giftadeed.kshantechsoft.com.giftadeed.Utils.ToastPopUp;
import giftadeed.kshantechsoft.com.giftadeed.Utils.Validation;
import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by I-Sys on 30-Nov-16.
 */

////////////////////////////////////////////////////////////////////
//                                                               //
//     Shows list of tags in your area                          //
/////////////////////////////////////////////////////////////////
public class Tab2 extends android.support.v4.app.Fragment implements SwipeRefreshLayout.OnRefreshListener, GoogleApiClient.OnConnectionFailedListener {
    View rootview;
    RecyclerView recyclerView;
    List<RowData> item_list;
    float radius_set;
    String strUser_ID;
    NeedListAdapter needListAdapter;
    SessionManager sessionManager;
    static android.support.v4.app.FragmentManager fragmgr;
    FragmentActivity myContext;
    String filter_category = Validation.FILTER_CATEGORY;
    String filter_groups = Validation.FILTER_GROUP_IDS;
    TextView txtlist_count, textView;
    private GoogleApiClient mGoogleApiClient;
    DatabaseAccess databaseAccess;
    ArrayList<GroupPOJO> groupArrayList;
    ArrayList<CategoryPOJO> categories;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.tab2, container, false);
        fragmgr = getFragmentManager();
//        mDialog = new SimpleArcDialog(getContext());
        TaggedneedsActivity.imgappbarcamera.setVisibility(View.VISIBLE);
        TaggedneedsActivity.imgappbarsetting.setVisibility(View.VISIBLE);
        TaggedneedsActivity.imgfilter.setVisibility(View.GONE);
        TaggedneedsActivity.imgShare.setVisibility(View.GONE);
        swipeRefreshLayout = (SwipeRefreshLayout) rootview.findViewById(R.id.swipe_refresh_layout_deeds);
        recyclerView = (RecyclerView) rootview.findViewById(R.id.needslistrecycler);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        txtlist_count = (TextView) rootview.findViewById(R.id.txtlist_count);
        textView = (TextView) rootview.findViewById(R.id.textView3);
        // myContext = (FragmentActivity) getContext();
        //item_tab2=Tab1.item;
        /*needListAdapter = new NeedListAdapter(item_tab2, getActivity(), Tab2.this);
        recyclerView.setAdapter(needListAdapter);*/
        mGoogleApiClient = ((TaggedneedsActivity) getActivity()).mGoogleApiClient;
        sessionManager = new SessionManager(getActivity());
        HashMap<String, String> user = sessionManager.getUserDetails();
        strUser_ID = user.get(sessionManager.USER_ID);
        // get_Tag_data(strUser_ID);
        radius_set = sessionManager.getradius();
        databaseAccess = DatabaseAccess.getInstance(getContext());
        databaseAccess.open();
        swipeRefreshLayout.setOnRefreshListener(this);
        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (!(Validation.isOnline(getActivity()))) {
                                            swipeRefreshLayout.setRefreshing(false);
                                            ToastPopUp.show(getActivity(), getString(R.string.network_validation));
                                        } else {
                                            swipeRefreshLayout.setRefreshing(true);
                                            get_Tag_data();
                                            getGroupList(strUser_ID);
                                            getCategory();
                                        }
                                    }
                                }
        );
//        get_Tag_data();
//        getGroupList(strUser_ID);
        rootview.getRootView().setFocusableInTouchMode(true);
        rootview.getRootView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                int i = 1;
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    fragmgr = getFragmentManager();
                    // finish();
                    // fragmentManager.beginTransaction().replace( R.id.Myprofile_frame,TaggedneedsFrag.newInstance(i)).commit();
                    fragmgr.beginTransaction().replace(R.id.content_frame, TaggedneedsFrag.newInstance(i)).commit();
                    return true;
                }
                return false;
            }
        });
        return rootview;
    }


    public void get_Tag_data() {
        //item_list.clear();
        sessionManager = new SessionManager(getActivity());
        HashMap<String, String> user = sessionManager.getUserDetails();
        String user_id = user.get(sessionManager.USER_ID);
        item_list = new ArrayList<>();
        // final ProgressDialog progressDialog = new ProgressDialog(getContext());
//        progressDialog.show();
        //  mDialog.setConfiguration(new ArcConfiguration(getActivity()));
        // mDialog.show();
        // mDialog.setCancelable(false);
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        taglistInterface service = retrofit.create(taglistInterface.class);
        Call<Modeltaglist> call = service.fetchData(user_id);
        call.enqueue(new Callback<Modeltaglist>() {
            @Override
            public void onResponse(Response<Modeltaglist> response, Retrofit retrofit) {
                // listData=null;
                swipeRefreshLayout.setRefreshing(false);
                try {
                    Modeltaglist model = new Modeltaglist();
                    model = response.body();
                    int isblock = 0;
                    try {
                        isblock = model.getIsBlocked();
                    } catch (Exception e) {
                        isblock = 0;
                    }
                    if (isblock == 1) {
                        FacebookSdk.sdkInitialize(getActivity());
                        Toast.makeText(getContext(), getResources().getString(R.string.block_toast), Toast.LENGTH_SHORT).show();
                        sessionManager.createUserCredentialSession(null, null, null);
                        LoginManager.getInstance().logOut();
                        int i = new DBGAD(getContext()).delete_row_message();
                        sessionManager.set_notification_status("ON");
                        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                                new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(Status status) {
                                        //updateUI(false);
                                    }
                                });
                        Intent loginintent = new Intent(getActivity(), LoginActivity.class);
                        loginintent.putExtra("message", "Charity");
                        startActivity(loginintent);
                    } else {
                        Modeltaglist result = new Modeltaglist();
                        result = response.body();
                        int size = result.getTaggedlist().size();
                        double current_latitude = new GPSTracker(getContext()).getLatitude();
//
//                // Getting longitude of the current location
                        double current_longitude = new GPSTracker(getContext()).getLongitude();
                        Location myLocation = new Location("My Location");
                        myLocation.setLatitude(current_latitude);
                        myLocation.setLongitude(current_longitude);

                        //   Log.d("TAG", "" + size);
//---------------------adding data

                        if (size > 0) {
                            for (int j = 0; j < size; j++) {
                                String audience_all_groups = model.getTaggedlist().get(j).getAll_groups();  // Y or N
                                String audience_selected_groups = model.getTaggedlist().get(j).getUserGrpIds();  // group ids
                                String str_geo_point = result.getTaggedlist().get(j).getGeopoint();
                                String[] words = str_geo_point.split(",");
                                Location tagLocation2 = new Location("tag Location");
                                tagLocation2.setLatitude(Double.parseDouble(words[0]));
                                tagLocation2.setLongitude(Double.parseDouble(words[1]));
                                float dist1 = myLocation.distanceTo(tagLocation2);
                                if (dist1 < radius_set) {
                                    if ((filter_category.equals(model.getTaggedlist().get(j).getNeedName()) && filter_groups.equals("All")) || (filter_category.equals("All") && filter_groups.equals("All"))) {
                                        RowData rowData = new RowData();
                                        rowData.setTitle(result.getTaggedlist().get(j).getNeedName());
                                        rowData.setAddress(result.getTaggedlist().get(j).getAddress());
                                        rowData.setDate(result.getTaggedlist().get(j).getTaggedDatetime());
                                        rowData.setImagepath(result.getTaggedlist().get(j).getTaggedPhotoPath());
                                        rowData.setDistance(dist1);
                                        rowData.setCharacterPath(result.getTaggedlist().get(j).getCharacterPath());
                                        rowData.setFname(result.getTaggedlist().get(j).getFname());
                                        rowData.setLname(result.getTaggedlist().get(j).getLname());
                                        rowData.setPrivacy(result.getTaggedlist().get(j).getPrivacy());
                                        rowData.setNeedName(result.getTaggedlist().get(j).getNeedName());
                                        rowData.setTotalTaggedCreditPoints(result.getTaggedlist().get(j).getTotalTaggedCreditPoints());
                                        rowData.setTotalFulfilledCreditPoints(result.getTaggedlist().get(j).getTotalFulfilledCreditPoints());
                                        rowData.setUserID(result.getTaggedlist().get(j).getUserID());
                                        rowData.setTaggedID(result.getTaggedlist().get(j).getTaggedID());
                                        rowData.setCatType(model.getTaggedlist().get(j).getCatType());
                                        rowData.setGeopoint(result.getTaggedlist().get(j).getGeopoint());
                                        rowData.setGetIconPath(model.getTaggedlist().get(j).getIconPath());
                                        rowData.setTaggedPhotoPath(result.getTaggedlist().get(j).getTaggedPhotoPath());
                                        rowData.setDescription(result.getTaggedlist().get(j).getDescription());
                                        rowData.setViews(result.getTaggedlist().get(j).getViews());
                                        rowData.setEndorse(result.getTaggedlist().get(j).getEndorse());
                                        rowData.setAllGroups(model.getTaggedlist().get(j).getAll_groups());
                                        rowData.setUser_group_ids(model.getTaggedlist().get(j).getUserGrpIds());
                                        item_list.add(rowData);
                                    } else if ((filter_category.equals(model.getTaggedlist().get(j).getNeedName()) && (!filter_groups.equals("All"))) || (filter_category.equals("All") && (!filter_groups.equals("All")))) {
                                        String[] split_filter_groups = filter_groups.split(",");       // filter selected groupids
                                        ArrayList<String> filterGroupList = new ArrayList<String>(Arrays.asList(split_filter_groups)); // arraylist for filter selected groupids

                                        String[] split_audience_groupids = audience_selected_groups.split(","); // server audience groupids
                                        ArrayList<String> audienceGroupList = new ArrayList<String>(Arrays.asList(split_audience_groupids)); // arraylist server audience groupids

                                        if (model.getTaggedlist().get(j).getFromGroupID() != null) {  // check for "from groupid"... if null means tagger is individual user
                                            if (filterGroupList.contains(model.getTaggedlist().get(j).getFromGroupID())) {  // check for filter settings having tagger's group id
                                                if (audience_all_groups.equals("N")) { // check for tagger selected all groups audience
                                                    if (audienceGroupList.contains(model.getTaggedlist().get(j).getFromGroupID())) {
                                                        RowData rowData = new RowData();
                                                        rowData.setTitle(result.getTaggedlist().get(j).getNeedName());
                                                        rowData.setAddress(result.getTaggedlist().get(j).getAddress());
                                                        rowData.setDate(result.getTaggedlist().get(j).getTaggedDatetime());
                                                        rowData.setImagepath(result.getTaggedlist().get(j).getTaggedPhotoPath());
                                                        rowData.setDistance(dist1);
                                                        rowData.setCharacterPath(result.getTaggedlist().get(j).getCharacterPath());
                                                        rowData.setFname(result.getTaggedlist().get(j).getFname());
                                                        rowData.setLname(result.getTaggedlist().get(j).getLname());
                                                        rowData.setPrivacy(result.getTaggedlist().get(j).getPrivacy());
                                                        rowData.setCatType(model.getTaggedlist().get(j).getCatType());
                                                        rowData.setGetIconPath(model.getTaggedlist().get(j).getIconPath());
                                                        rowData.setNeedName(result.getTaggedlist().get(j).getNeedName());
                                                        rowData.setTotalTaggedCreditPoints(result.getTaggedlist().get(j).getTotalTaggedCreditPoints());
                                                        rowData.setTotalFulfilledCreditPoints(result.getTaggedlist().get(j).getTotalFulfilledCreditPoints());
                                                        rowData.setUserID(result.getTaggedlist().get(j).getUserID());
                                                        rowData.setTaggedID(result.getTaggedlist().get(j).getTaggedID());
                                                        rowData.setGeopoint(result.getTaggedlist().get(j).getGeopoint());
                                                        rowData.setTaggedPhotoPath(result.getTaggedlist().get(j).getTaggedPhotoPath());
                                                        rowData.setDescription(result.getTaggedlist().get(j).getDescription());
                                                        rowData.setViews(result.getTaggedlist().get(j).getViews());
                                                        rowData.setEndorse(result.getTaggedlist().get(j).getEndorse());
                                                        rowData.setAllGroups(model.getTaggedlist().get(j).getAll_groups());
                                                        rowData.setUser_group_ids(model.getTaggedlist().get(j).getUserGrpIds());
                                                        item_list.add(rowData);
                                                    }
                                                } else {
                                                    RowData rowData = new RowData();
                                                    rowData.setTitle(result.getTaggedlist().get(j).getNeedName());
                                                    rowData.setAddress(result.getTaggedlist().get(j).getAddress());
                                                    rowData.setDate(result.getTaggedlist().get(j).getTaggedDatetime());
                                                    rowData.setImagepath(result.getTaggedlist().get(j).getTaggedPhotoPath());
                                                    rowData.setDistance(dist1);
                                                    rowData.setCharacterPath(result.getTaggedlist().get(j).getCharacterPath());
                                                    rowData.setFname(result.getTaggedlist().get(j).getFname());
                                                    rowData.setLname(result.getTaggedlist().get(j).getLname());
                                                    rowData.setPrivacy(result.getTaggedlist().get(j).getPrivacy());
                                                    rowData.setCatType(model.getTaggedlist().get(j).getCatType());
                                                    rowData.setGetIconPath(model.getTaggedlist().get(j).getIconPath());
                                                    rowData.setNeedName(result.getTaggedlist().get(j).getNeedName());
                                                    rowData.setTotalTaggedCreditPoints(result.getTaggedlist().get(j).getTotalTaggedCreditPoints());
                                                    rowData.setTotalFulfilledCreditPoints(result.getTaggedlist().get(j).getTotalFulfilledCreditPoints());
                                                    rowData.setUserID(result.getTaggedlist().get(j).getUserID());
                                                    rowData.setTaggedID(result.getTaggedlist().get(j).getTaggedID());
                                                    rowData.setGeopoint(result.getTaggedlist().get(j).getGeopoint());
                                                    rowData.setTaggedPhotoPath(result.getTaggedlist().get(j).getTaggedPhotoPath());
                                                    rowData.setDescription(result.getTaggedlist().get(j).getDescription());
                                                    rowData.setViews(result.getTaggedlist().get(j).getViews());
                                                    rowData.setEndorse(result.getTaggedlist().get(j).getEndorse());
                                                    rowData.setAllGroups(model.getTaggedlist().get(j).getAll_groups());
                                                    rowData.setUser_group_ids(model.getTaggedlist().get(j).getUserGrpIds());
                                                    item_list.add(rowData);
                                                }
                                            }
                                        }
                                    }
                                }

                                if (item_list.size() == 0) {
                                    recyclerView.setVisibility(View.GONE);
                                    txtlist_count.setVisibility(View.GONE);
                                    textView.setText(getResources().getString(R.string.no_needy));
                                } else {
                                    recyclerView.setVisibility(View.VISIBLE);
                                    txtlist_count.setVisibility(View.VISIBLE);
                                    needListAdapter = new NeedListAdapter(item_list, getActivity(), "tab2");
                                    recyclerView.setAdapter(needListAdapter);
                                    txtlist_count.setText(String.valueOf(item_list.size()));
                                    if (item_list.size() > 1) {
                                        textView.setText(getResources().getString(R.string.people_looking_2));
                                    } else {
                                        textView.setText(getResources().getString(R.string.people_looking_1));
                                    }
                                }
                            }
                        } else {
                            textView.setText(getResources().getString(R.string.no_needy));
                        }
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                ToastPopUp.show(getContext(), getString(R.string.server_response_error));
            }
        });
    }

    //---------------------get the list of joined and created groups from server-----------------------------------------------
    public void getGroupList(String user_id) {
        groupArrayList = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        GroupsInterface service = retrofit.create(GroupsInterface.class);
        Call<List<GroupPOJO>> call = service.sendData(user_id);
        call.enqueue(new Callback<List<GroupPOJO>>() {
            @Override
            public void onResponse(Response<List<GroupPOJO>> response, Retrofit retrofit) {
                Log.d("response_grouplist", "" + response.body());
                try {
                    List<GroupPOJO> res = response.body();
                    int isblock = 0;
                    try {
                        isblock = res.get(0).getIsBlocked();
                    } catch (Exception e) {
                        isblock = 0;
                    }
                    if (isblock == 1) {
                        FacebookSdk.sdkInitialize(getActivity());
                        Toast.makeText(getContext(), getResources().getString(R.string.block_toast), Toast.LENGTH_SHORT).show();
                        sessionManager.createUserCredentialSession(null, null, null);
                        LoginManager.getInstance().logOut();
                        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                                new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(Status status) {
                                        //updateUI(false);
                                    }
                                });
                        int i = new DBGAD(getContext()).delete_row_message();
                        sessionManager.set_notification_status("ON");
                        Intent loginintent = new Intent(getActivity(), LoginActivity.class);
                        loginintent.putExtra("message", "Charity");
                        startActivity(loginintent);
                    } else {
                        List<GroupPOJO> groupPOJOS = response.body();
                        groupArrayList.clear();
                        try {
                            for (int i = 0; i < groupPOJOS.size(); i++) {
                                GroupPOJO groupPOJO = new GroupPOJO();
                                groupPOJO.setGroup_id(groupPOJOS.get(i).getGroup_id());
                                groupPOJO.setGroup_name(groupPOJOS.get(i).getGroup_name());
                                groupPOJO.setGroup_image(groupPOJOS.get(i).getGroup_image());
                                groupPOJO.setChecked(true);
                                groupArrayList.add(groupPOJO);
                            }
                        } catch (Exception e) {

                        }

                        if (groupArrayList.size() <= 0) {
                            databaseAccess.deleteAllGroups();
                        } else {
                            for (int i = 0; i < groupArrayList.size(); i++) {
                                if (!databaseAccess.groupidExist(groupArrayList.get(i).getGroup_id())) {
                                    databaseAccess.Create_Group(groupArrayList.get(i).getGroup_id(), groupArrayList.get(i).getGroup_name(), groupArrayList.get(i).getGroup_image(), "true");
                                }
                            }

                            ArrayList<String> abc = new ArrayList<>();
                            for (int i = 0; i < groupArrayList.size(); i++) {
                                abc.add(groupArrayList.get(i).getGroup_id());
                            }
                            Log.d("abc", abc.toString());
                            String formatted = abc.toString().replaceAll("\\[", "").replaceAll("\\]", "");
                            databaseAccess.groupidNotExist(formatted);
                        }

                        Log.d("LocalDbGroupSize", "" + databaseAccess.getAllGroups().size());
                    }
                } catch (Exception e) {
                    Log.d("response_grouplist", "" + e.getMessage());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("response_grouplist", "" + t.getMessage());
                ToastPopUp.show(myContext, getString(R.string.server_response_error));
            }
        });
    }

    //---------------------get the list of categories from server-----------------------------------------------
    public void getCategory() {
        categories = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        CategoryInterface service = retrofit.create(CategoryInterface.class);
        Call<CategoryType> call = service.sendData("");
        call.enqueue(new Callback<CategoryType>() {
            @Override
            public void onResponse(Response<CategoryType> response, Retrofit retrofit) {
                CategoryType categoryType = response.body();
                categories.clear();
                if (categoryType.getNeedtype().size() > 0) {
                    for (int i = 0; i < categoryType.getNeedtype().size(); i++) {
                        CategoryPOJO categoryPOJO = new CategoryPOJO();
                        categoryPOJO.setId(categoryType.getNeedtype().get(i).getNeedMappingID());
                        categoryPOJO.setName(categoryType.getNeedtype().get(i).getNeedName());
                        categories.add(categoryPOJO);
                    }
                }
                if (categories.size() <= 0) {
                    databaseAccess.deleteAllCategory();
                } else {
                    for (int i = 0; i < categories.size(); i++) {
                        if (!databaseAccess.catIdExist(categories.get(i).getId())) {
                            databaseAccess.Create_Category(categories.get(i).getId(), categories.get(i).getName(), "true");
                        }
                    }

                    ArrayList<String> abc = new ArrayList<>();
                    for (int i = 0; i < categories.size(); i++) {
                        abc.add(categories.get(i).getId());
                    }
                    Log.d("abc", abc.toString());
                    String formatted = abc.toString().replaceAll("\\[", "").replaceAll("\\]", "");
                    databaseAccess.catIdNotExist(formatted);
                }
                Log.d("LocalDbCatSize", "" + databaseAccess.getAllCategories().size());
            }

            @Override
            public void onFailure(Throwable t) {
                ToastPopUp.show(getActivity(), getString(R.string.server_response_error));
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onRefresh() {
        if (!(Validation.isOnline(getActivity()))) {
            swipeRefreshLayout.setRefreshing(false);
            ToastPopUp.show(getActivity(), getString(R.string.network_validation));
        } else {
            swipeRefreshLayout.setRefreshing(true);
            get_Tag_data();
            getGroupList(strUser_ID);
            getCategory();
        }
    }
}