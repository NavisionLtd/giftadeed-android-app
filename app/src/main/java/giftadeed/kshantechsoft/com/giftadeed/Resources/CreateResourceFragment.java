package giftadeed.kshantechsoft.com.giftadeed.Resources;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.squareup.okhttp.OkHttpClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

import giftadeed.kshantechsoft.com.giftadeed.Group.GroupPOJO;
import giftadeed.kshantechsoft.com.giftadeed.Group.GroupResponseStatus;
import giftadeed.kshantechsoft.com.giftadeed.Group.GroupsInterface;
import giftadeed.kshantechsoft.com.giftadeed.Login.LoginActivity;
import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.CategoryInterface;
import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.CategoryType;
import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.CustomNeedtype;
import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.GPSTracker;
import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.Needtype;
import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.SuggestSubType;
import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.SuggestType;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsActivity;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsFrag;
import giftadeed.kshantechsoft.com.giftadeed.Utils.DBGAD;
import giftadeed.kshantechsoft.com.giftadeed.Utils.SessionManager;
import giftadeed.kshantechsoft.com.giftadeed.Utils.SharedPrefManager;
import giftadeed.kshantechsoft.com.giftadeed.Utils.ToastPopUp;
import giftadeed.kshantechsoft.com.giftadeed.Utils.Validation;
import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class CreateResourceFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener {
    private ArrayList<GroupPOJO> groupArrayList;
    public static ArrayList<String> selectedGroups = new ArrayList<String>();
    public static ArrayList<String> selectedGrpNames = new ArrayList<String>();
    String formattedTypeIds = "", formattedTypePref = "", formattedSubTypeIds = "", formattedSubTypePref = "", formattedUserGroups = ""; // for removing brackets [ and ]
    ArrayList<String> subTypePref = new ArrayList<String>();
    ArrayList<String> subtypePrefId = new ArrayList<String>();
    ArrayList<String> typePref = new ArrayList<String>();
    ArrayList<String> typePrefId = new ArrayList<String>();
    ArrayList<String> customTypePrefId = new ArrayList<String>();
    View rootview;
    FragmentActivity myContext;
    static FragmentManager fragmgr;
    SimpleArcDialog simpleArcDialog;
    EditText selectGroup, resourceCat, resourceSubCat, resourceName, resourceLocation, resourceDesc, edselectAudiance;
    LinearLayout resSubCatLayout;
    Button btnCreateResource;
    SessionManager sessionManager;
    String strUser_ID;
    String receivedGid = "", receivedRid = "", callingFrom = "";
    private ArrayList<Needtype> categories = new ArrayList<Needtype>();
    private ArrayList<CustomNeedtype> customCategories = new ArrayList<>();
    private ArrayList<MultiSubCategories> subcategories = new ArrayList<MultiSubCategories>();
    String strCharacter_Path, strNeedmapping_ID, strGroupmapping_ID, strGroupmapping_Name;
    ImageView catImage;
    String latitude_source, longitude_source;
    public String str_Geopint, lat, longi, itemname, itemid;
    String checkedOtherGrp = "N";
    private GoogleApiClient mGoogleApiClient;

    public static CreateResourceFragment newInstance(int sectionNumber) {
        CreateResourceFragment fragment = new CreateResourceFragment();
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.create_resource_layout, container, false);
        sessionManager = new SessionManager(getActivity());
        TaggedneedsActivity.fragname = CreateResourceFragment.newInstance(0);
        FragmentManager fragManager = myContext.getSupportFragmentManager();
        fragmgr = getFragmentManager();
        simpleArcDialog = new SimpleArcDialog(getContext());
        TaggedneedsActivity.imgappbarcamera.setVisibility(View.GONE);
        TaggedneedsActivity.imgappbarsetting.setVisibility(View.GONE);
        TaggedneedsActivity.imgfilter.setVisibility(View.GONE);
        TaggedneedsActivity.imgShare.setVisibility(View.GONE);
        TaggedneedsActivity.editprofile.setVisibility(View.GONE);
        TaggedneedsActivity.saveprofile.setVisibility(View.GONE);
        TaggedneedsActivity.toggle.setDrawerIndicatorEnabled(true);
        TaggedneedsActivity.back.setVisibility(View.GONE);
        TaggedneedsActivity.imgHamburger.setVisibility(View.GONE);
        TaggedneedsActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        init();
        mGoogleApiClient = ((TaggedneedsActivity) getActivity()).mGoogleApiClient;
        HashMap<String, String> user = sessionManager.getUserDetails();
        strUser_ID = user.get(sessionManager.USER_ID);
        HashMap<String, String> group = sessionManager.getSelectedGroupDetails();
        receivedGid = group.get(sessionManager.GROUP_ID);
        HashMap<String, String> resource = sessionManager.getSelectedResourceDetails();
        callingFrom = resource.get(sessionManager.RES_CALL_FROM);
        receivedRid = resource.get(sessionManager.RESOURCE_ID);
        lat = String.valueOf(new GPSTracker(myContext).latitude);
        longi = String.valueOf(new GPSTracker(myContext).longitude);
        getAddress(lat, longi);

        if (callingFrom.equals("create")) {
            //from create menu
            TaggedneedsActivity.updateTitle(getResources().getString(R.string.create_res));
        } else {
            //from edit menu
//            groupName.setText(receivedGname);
//            groupDesc.setText(receivedGdesc);
            TaggedneedsActivity.updateTitle(getResources().getString(R.string.edit_resource));
        }

        selectGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(Validation.isOnline(getActivity()))) {
                    ToastPopUp.show(getActivity(), getString(R.string.network_validation));
                } else {
                    getOwnedGroupList(strUser_ID);
                }
            }
        });

        resourceCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (categories.size() > 0) {
                    showCatDialog();
                } else {
                    if (selectGroup.getText().length() < 1) {
                        ToastPopUp.displayToast(getContext(), getResources().getString(R.string.select_res_group));
                    } else {
                        getCategory(strGroupmapping_ID);
                    }
                }
            }
        });

        resourceSubCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String strCategory = resourceCat.getText().toString();
                if (formattedTypeIds.length() < 1) {
                    ToastPopUp.displayToast(getContext(), getResources().getString(R.string.select_category));
                } else {
                    if (subcategories.size() > 0) {
                        showSubCatDialog();
                    } else {
                        if (!(Validation.isOnline(getActivity()))) {
                            ToastPopUp.show(getActivity(), getString(R.string.network_validation));
                        } else {
                            getSubCategory("no");
                        }
                    }
                }
            }
        });

        resourceLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAutocompleteActivity(1);
            }
        });

        edselectAudiance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(Validation.isOnline(getActivity()))) {
                    ToastPopUp.show(getActivity(), getString(R.string.network_validation));
                } else {
                    getUserGroups(strUser_ID);
                }
            }
        });

        btnCreateResource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectGroup.getText().length() == 0) {
                    ToastPopUp.displayToast(getContext(), getResources().getString(R.string.select_res_group));
                } else if (resourceCat.getText().length() == 0) {
                    ToastPopUp.displayToast(getContext(), getResources().getString(R.string.select_category));
                } else if ((resourceSubCat.getText().length() < 1) && (subcategories.size() > 0)) {
                    ToastPopUp.displayToast(getContext(), getResources().getString(R.string.select_sub_category));
                } else if (resourceLocation.getText().length() == 0) {
                    ToastPopUp.displayToast(getContext(), getResources().getString(R.string.select_location));
                } else if (resourceName.getText().length() == 0) {
                    resourceName.requestFocus();
                    resourceName.setFocusable(true);
                    resourceName.setError("Resource name required");
                } else if (edselectAudiance.getText().length() == 0) {
                    ToastPopUp.displayToast(getContext(), getResources().getString(R.string.select_audiance));
                } else {
                    formattedUserGroups = selectedGroups.toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\s+", "");
                    Log.d("formattedUserGroups", "" + formattedUserGroups);
                    //call api
                    if (!(Validation.isOnline(getActivity()))) {
                        ToastPopUp.show(getActivity(), getString(R.string.network_validation));
                    } else {
                        if (checkedOtherGrp.equals("Y")) {
                            // if all groups is selected then send blank user selected groups
                            createResource(strUser_ID, strGroupmapping_ID, resourceName.getText().toString(), resourceDesc.getText().toString(), formattedSubTypeIds, str_Geopint, resourceLocation.getText().toString(), "", checkedOtherGrp);
                        } else {
                            createResource(strUser_ID, strGroupmapping_ID, resourceName.getText().toString(), resourceDesc.getText().toString(), formattedSubTypeIds, str_Geopint, resourceLocation.getText().toString(), formattedUserGroups, checkedOtherGrp);
                        }
                    }
                }
            }
        });
        return rootview;
    }

    //--------------------------Initilizing the UI variables--------------------------------------------
    private void init() {
        selectedGroups = new ArrayList<String>();
        selectedGrpNames = new ArrayList<String>();
        catImage = (ImageView) rootview.findViewById(R.id.resource_selected_cat_image);
        selectGroup = (EditText) rootview.findViewById(R.id.tv_select_group);
        resourceName = (EditText) rootview.findViewById(R.id.editText_resource_name);
        resourceCat = (EditText) rootview.findViewById(R.id.resource_select_cat);
        resourceSubCat = (EditText) rootview.findViewById(R.id.tv_select_sub_categories);
        resSubCatLayout = (LinearLayout) rootview.findViewById(R.id.res_sub_cat_layout);
        resourceDesc = (EditText) rootview.findViewById(R.id.editText_resource_desc);
        btnCreateResource = (Button) rootview.findViewById(R.id.button_create_resource);
        resourceLocation = (EditText) rootview.findViewById(R.id.resource_location);
        edselectAudiance = (EditText) rootview.findViewById(R.id.select_resource_audience);
    }

    //--------------------------getting user owned groups from server------------------------------------------
    public void getOwnedGroupList(String userid) {
        groupArrayList = new ArrayList<>();
        simpleArcDialog.setConfiguration(new ArcConfiguration(getContext()));
        simpleArcDialog.show();
        simpleArcDialog.setCancelable(false);
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        OwnedGroupsInterface service = retrofit.create(OwnedGroupsInterface.class);
        Call<List<GroupPOJO>> call = service.sendData(userid);
        call.enqueue(new Callback<List<GroupPOJO>>() {
            @Override
            public void onResponse(Response<List<GroupPOJO>> response, Retrofit retrofit) {
                simpleArcDialog.dismiss();
                Log.d("response_ownedgrouplist", "" + response.body());
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
                                groupArrayList.add(groupPOJO);
                            }
                        } catch (Exception e) {

                        }
                        final Dialog dialog = new Dialog(getContext());
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setCancelable(false);
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.setContentView(R.layout.groups_dialog);
                        EditText edsearch = (EditText) dialog.findViewById(R.id.search_from_grouplist);
                        edsearch.setVisibility(View.GONE);
                        ListView ownedgrouplist = (ListView) dialog.findViewById(R.id.owned_group_list);
                        Button cancel = (Button) dialog.findViewById(R.id.group_cancel);
                        ownedgrouplist.setAdapter(new OwnedGroupsAdapter(groupArrayList, getContext()));
                        ownedgrouplist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                try {
                                    if (groupArrayList.size() > 0) {
                                        if (groupArrayList.get(i).getGroup_name().length() > 30) {
                                            selectGroup.setText(groupArrayList.get(i).getGroup_name().substring(0, 29) + "...");
                                        } else {
                                            selectGroup.setText(groupArrayList.get(i).getGroup_name());
                                        }
                                        strGroupmapping_ID = groupArrayList.get(i).getGroup_id();
                                        strGroupmapping_Name = groupArrayList.get(i).getGroup_name();
                                    }
                                } catch (Exception e) {
//                                    StringWriter writer = new StringWriter();
//                                    e.printStackTrace(new PrintWriter(writer));
//                                    Bugreport bg = new Bugreport();
//                                    bg.sendbug(writer.toString());
                                }

                                // reset categories
                                categories = new ArrayList<Needtype>();
                                resourceCat.setText("");
                                resourceCat.clearFocus();

                                // reset sub-categories
                                subcategories = new ArrayList<MultiSubCategories>();
                                resSubCatLayout.setVisibility(View.VISIBLE);
                                resourceSubCat.setText("");
                                resourceSubCat.clearFocus();

                                // reset audience
                                groupArrayList = new ArrayList<GroupPOJO>();
                                edselectAudiance.setEnabled(true);
                                edselectAudiance.setText("");
                                edselectAudiance.clearFocus();

                                dialog.dismiss();
                            }
                        });
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                        simpleArcDialog.dismiss();
                        dialog.show();
                    }
                } catch (Exception e) {
                    Log.d("response_ownedgrouplist", "" + e.getMessage());
//                    StringWriter writer = new StringWriter();
//                    e.printStackTrace(new PrintWriter(writer));
//                    Bugreport bg = new Bugreport();
//                    bg.sendbug(writer.toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                simpleArcDialog.dismiss();
                ToastPopUp.show(myContext, getString(R.string.server_response_error));
            }
        });
    }

    //--------------------------getting categories from server------------------------------------------
    public void getCategory(String groupid) {
        simpleArcDialog.setConfiguration(new ArcConfiguration(getContext()));
        simpleArcDialog.show();
        simpleArcDialog.setCancelable(false);
        categories = new ArrayList<>();
        customCategories = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        CategoryInterface service = retrofit.create(CategoryInterface.class);
        Call<CategoryType> call = service.sendData(groupid);
        call.enqueue(new Callback<CategoryType>() {
            @Override
            public void onResponse(Response<CategoryType> response, Retrofit retrofit) {
                simpleArcDialog.dismiss();
                CategoryType categoryType = response.body();
                categories.clear();
                customCategories.clear();
                try {
                    for (int i = 0; i < categoryType.getNeedtype().size(); i++) {
                        Needtype needtype = new Needtype();
                        needtype.setNeedMappingID(categoryType.getNeedtype().get(i).getNeedMappingID());
                        needtype.setNeedName(categoryType.getNeedtype().get(i).getNeedName());
                        needtype.setChecked(false);
                        categories.add(needtype);
                    }
                } catch (Exception e) {

                }

                try {
                    for (int i = 0; i < categoryType.getCustomneedtype().size(); i++) {
                        CustomNeedtype customNeedtype = new CustomNeedtype();
                        customNeedtype.setNeedMappingID(categoryType.getCustomneedtype().get(i).getNeedMappingID().toString());
                        customNeedtype.setNeedName(categoryType.getCustomneedtype().get(i).getNeedName().toString());
                        customNeedtype.setType(categoryType.getCustomneedtype().get(i).getType());
                        customNeedtype.setIconPath(categoryType.getCustomneedtype().get(i).getIconPath());
                        customNeedtype.setCharacterPath(categoryType.getCustomneedtype().get(i).getCharacterPath());
                        customNeedtype.setChecked(false);
                        customCategories.add(customNeedtype);
                    }
                } catch (Exception e) {

                }
                Log.d("response_categories", "" + categories.size());
                Log.d("response_custom_cat", "" + customCategories.size());
                showCatDialog();
            }

            @Override
            public void onFailure(Throwable t) {
                simpleArcDialog.dismiss();
                Log.d("multitype_error", t.getMessage());
                ToastPopUp.show(myContext, getString(R.string.server_response_error));
            }
        });
    }

    public void showCatDialog() {
        final Dialog dialog = new Dialog(getContext());
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.main_category_dialog);
        dialog.getWindow().setLayout((6 * width) / 7, (4 * height) / 5);
        final RecyclerView categorylist = (RecyclerView) dialog.findViewById(R.id.main_category_list);
        TextView tvheading1 = (TextView) dialog.findViewById(R.id.res_cat_heading);
        TextView tvheading2 = (TextView) dialog.findViewById(R.id.res_custom_cat_heading);
        final RecyclerView custom_categorylist = (RecyclerView) dialog.findViewById(R.id.res_custom_category_list);
        LinearLayoutManager layoutManager, layoutManager1;
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        layoutManager1 = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        categorylist.addItemDecoration(new DividerItemDecoration(categorylist.getContext(), DividerItemDecoration.VERTICAL));
        categorylist.setLayoutManager(layoutManager);
        custom_categorylist.addItemDecoration(new DividerItemDecoration(custom_categorylist.getContext(), DividerItemDecoration.VERTICAL));
        custom_categorylist.setLayoutManager(layoutManager1);
        Button btnok = (Button) dialog.findViewById(R.id.main_category_ok);
        Button cancel = (Button) dialog.findViewById(R.id.main_category_cancel);
        Log.d("mulcatlist_size", "" + categories.size());
        if (categories.size() > 0) {
            tvheading1.setVisibility(View.VISIBLE);
            categorylist.setVisibility(View.VISIBLE);
            btnok.setVisibility(View.VISIBLE);
            cancel.setVisibility(View.VISIBLE);
            categorylist.setAdapter(new ResourceMultiCatAdapter(categories, getContext()));
        } else {
            tvheading1.setVisibility(View.GONE);
            btnok.setVisibility(View.GONE);
            cancel.setVisibility(View.VISIBLE);
            categorylist.setVisibility(View.GONE);
        }
        if (customCategories.size() > 0) {
            tvheading2.setVisibility(View.VISIBLE);
            custom_categorylist.setVisibility(View.VISIBLE);
            custom_categorylist.setAdapter(new ResourceMultiCustomCatAdapter(customCategories, getContext()));
        } else {
            tvheading2.setVisibility(View.GONE);
            custom_categorylist.setVisibility(View.GONE);
        }

        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                typePrefId = new ArrayList<String>();
                typePref = new ArrayList<String>();
                customTypePrefId = new ArrayList<>();
                for (int i = 0; i < categories.size(); i++) {
//                            Log.d("multicat", categories.get(i).getNeedName() + ":" + categories.get(i).getChecked());
                    if (categories.get(i).getChecked() == true) {
                        typePrefId.add(categories.get(i).getNeedMappingID()); // add position of the row
                        typePref.add(categories.get(i).getNeedName());
                    }
                }
                for (int i = 0; i < customCategories.size(); i++) {
//                            Log.d("multicat", categories.get(i).getNeedName() + ":" + categories.get(i).getChecked());
                    if (customCategories.get(i).getChecked() == true) {
                        typePrefId.add(customCategories.get(i).getNeedMappingID()); // add position of the row
                        typePref.add(customCategories.get(i).getNeedName());
                        customTypePrefId.add(customCategories.get(i).getNeedMappingID());
                    }
                }
                dialog.dismiss();
                if (categories.size() > 0) {
                    formattedTypeIds = typePrefId.toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\s+", "");
                    formattedTypePref = typePref.toString().replaceAll("\\[", "").replaceAll("\\]", "");
                    Log.d("formattedTypeIds", "" + formattedTypeIds);
                    subcategories = new ArrayList<MultiSubCategories>();
                    resSubCatLayout.setVisibility(View.VISIBLE);
                    if (formattedTypePref.length() > 25) {
                        resourceCat.setText(formattedTypePref.substring(0, 24) + "...");
                        resourceSubCat.setText("");
                        resourceSubCat.clearFocus();
                    } else {
                        resourceCat.setText(formattedTypePref);
                        resourceSubCat.setText("");
                        resourceSubCat.clearFocus();
                    }
                }

                if (customTypePrefId.size() > 0) {
                    //if user select custom category then audience will be his from group only. Audience click will be disabled
                    edselectAudiance.setText(strGroupmapping_Name);
                    edselectAudiance.setEnabled(false);
//                    resSubCatLayout.setVisibility(View.GONE);
                } else {
                    //Reset select audience
//                    resSubCatLayout.setVisibility(View.VISIBLE);
                    edselectAudiance.setEnabled(true);
                    groupArrayList = new ArrayList<>();
                    edselectAudiance.setText("");
                    edselectAudiance.clearFocus();
                }

                //call getsubcategory to check subcategories available for selected main categories
                getSubCategory("yes");
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    //--------------------------getting sub categories from server------------------------------------------
    public void getSubCategory(final String callingFromCat) {
        simpleArcDialog.setConfiguration(new ArcConfiguration(getContext()));
        simpleArcDialog.show();
        simpleArcDialog.setCancelable(false);
        subcategories = new ArrayList<>();
        resSubCatLayout.setVisibility(View.VISIBLE);
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        MultiSubCategoryInterface service = retrofit.create(MultiSubCategoryInterface.class);
        Call<List<MultiSubCategories>> call = service.sendData(strUser_ID, formattedTypeIds);
        call.enqueue(new Callback<List<MultiSubCategories>>() {
            @Override
            public void onResponse(Response<List<MultiSubCategories>> response, Retrofit retrofit) {
                simpleArcDialog.dismiss();
                List<MultiSubCategories> subCategoryType = response.body();
                subcategories.clear();
                try {
                    for (int i = 0; i < subCategoryType.size(); i++) {
                        MultiSubCategories subCat = new MultiSubCategories();
                        subCat.setNeedname(subCategoryType.get(i).getNeedname());
                        subCat.setSubCatId(subCategoryType.get(i).getSubCatId());
                        subCat.setSubCatName(subCategoryType.get(i).getSubCatName());
                        subCat.setChecked(false);
                        subcategories.add(subCat);
                    }
                } catch (Exception e) {

                }
                if (callingFromCat.equals("yes")) {
                    //dont show subcat dialog
                } else {
                    showSubCatDialog();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                simpleArcDialog.dismiss();
                Log.d("subtype_error", t.getMessage());
                ToastPopUp.show(myContext, getString(R.string.server_response_error));
            }
        });
    }

    public void showSubCatDialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.sub_category_dialog);
        final RecyclerView subcategorylist = (RecyclerView) dialog.findViewById(R.id.sub_category_list);
        subcategorylist.setVisibility(View.VISIBLE);
        LinearLayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        subcategorylist.addItemDecoration(new DividerItemDecoration(subcategorylist.getContext(), DividerItemDecoration.VERTICAL));
        subcategorylist.setLayoutManager(layoutManager);
        TextView tvMsg = (TextView) dialog.findViewById(R.id.txt_msg);
        Button ok = (Button) dialog.findViewById(R.id.sub_category_ok);
        Button cancel = (Button) dialog.findViewById(R.id.sub_category_cancel);
        Button btnSuggestSubType = (Button) dialog.findViewById(R.id.suggest_sub_type);
        Log.d("subcatlist_size", "" + subcategories.size());
        if (subcategories.size() > 0) {
            tvMsg.setText("Select preferences for number of people");
            subcategorylist.setVisibility(View.VISIBLE);
            ok.setVisibility(View.VISIBLE);
            cancel.setVisibility(View.VISIBLE);
            subcategorylist.setAdapter(new ResourceSubCatAdapter(subcategories, getContext()));
        } else {
            tvMsg.setText("No subtypes found");
            ok.setVisibility(View.GONE);
            cancel.setVisibility(View.VISIBLE);
            subcategorylist.setVisibility(View.GONE);
        }

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subtypePrefId = new ArrayList<String>();
                subTypePref = new ArrayList<String>();
                for (int i = 0; i < subcategories.size(); i++) {
//                            Log.d("i" + i, subcategories.get(i).getSubCatName() + ":" + subcategories.get(i).getChecked());
                    if (subcategories.get(i).getChecked() == true) {
                        subtypePrefId.add(subcategories.get(i).getSubCatId());
                        subTypePref.add(subcategories.get(i).getSubCatName()); // add position of the row
                    }
                }
                dialog.dismiss();
                if (subcategories.size() > 0) {
                    formattedSubTypeIds = subtypePrefId.toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\s+", "");
                    formattedSubTypePref = subTypePref.toString().replaceAll("\\[", "").replaceAll("\\]", "");
                    Log.d("formattedSubTypeIds", "" + formattedSubTypeIds);
                    if (formattedSubTypePref.length() > 25) {
                        resourceSubCat.setText(formattedSubTypePref.substring(0, 24) + "...");
                    } else {
                        resourceSubCat.setText(formattedSubTypePref);
                    }
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnSuggestSubType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setContentView(R.layout.suggest_subtype_dialog);
                Spinner spinnerType = (Spinner) dialog.findViewById(R.id.spinner_main_type);
                final EditText et_suggest_type = (EditText) dialog.findViewById(R.id.et_suggested_type);
                Button ok = (Button) dialog.findViewById(R.id.suggest_ok);
                Button cancel = (Button) dialog.findViewById(R.id.suggest_cancel);
                ArrayAdapter<Needtype> adapter = new ArrayAdapter<Needtype>(getContext(), android.R.layout.simple_spinner_item, categories);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerType.setAdapter(adapter);
                spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                        itemid = categories.get(position).getNeedMappingID();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (et_suggest_type.getText().length() == 0) {
                            Toast.makeText(getContext(), getResources().getString(R.string.enter_suggession), Toast.LENGTH_SHORT).show();
                        } else {
                            // call suggest subtype api
                            itemname = et_suggest_type.getText().toString();
                            Log.d("suggestion", itemid + itemname);
                            if (!(Validation.isNetworkAvailable(getContext()))) {
                                Toast.makeText(getContext(), getResources().getString(R.string.network_validation), Toast.LENGTH_SHORT).show();
                            } else {
                                suggestSubType();
                            }
                            dialog.dismiss();
                        }
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        dialog.show();
    }

    //--------------------------suggest sub-type------------------------------------------
    public void suggestSubType() {
        simpleArcDialog.setConfiguration(new ArcConfiguration(getContext()));
        simpleArcDialog.show();
        simpleArcDialog.setCancelable(false);
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        SuggestSubType service = retrofit.create(SuggestSubType.class);
        Call<SuggestType> call = service.sendData(itemid, itemname);
        call.enqueue(new Callback<SuggestType>() {
            @Override
            public void onResponse(Response<SuggestType> response, Retrofit retrofit) {
                simpleArcDialog.dismiss();
                try {
                    Integer successstatus = response.body().getStatus();
                    if (successstatus == 0) {
                        Toast.makeText(getContext(), getResources().getString(R.string.suggession_error), Toast.LENGTH_SHORT).show();
                    } else if (successstatus == 1) {
                        Toast.makeText(getContext(), getResources().getString(R.string.subtype_success), Toast.LENGTH_SHORT).show();
                    } else if (successstatus == 2) {
                        Toast.makeText(getContext(), getResources().getString(R.string.subtype_already_exist), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.d("suggest_exception", "" + e.getMessage());
//                    StringWriter writer = new StringWriter();
//                    e.printStackTrace(new PrintWriter(writer));
//                    Bugreport bg = new Bugreport();
//                    bg.sendbug(writer.toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                simpleArcDialog.dismiss();
                ToastPopUp.show(getContext(), getString(R.string.server_response_error));
            }
        });
    }

    private void openAutocompleteActivity(int requestcode_MyLoc_MyDest) {
        try {
            // The autocomplete activity requires Google Play Services to be available. The intent
            // builder checks this and throws an exception if it is not the case.
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .build(myContext);
            startActivityForResult(intent, requestcode_MyLoc_MyDest);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                // retrive the data by using getPlace() method.
                Place place = PlaceAutocomplete.getPlace(myContext, data);
                resourceLocation.setText(place.getName() + ",\n" +
                        place.getAddress() + "\n" + place.getPhoneNumber());
                //  Toast.makeText(getApplicationContext(), "select latlong "+place.getLatLng(), Toast.LENGTH_LONG).show();
                StringTokenizer st = new StringTokenizer("" + place.getLatLng(), ",");
                int i = 0;
                String strLat = "0.0", strLong = "0.0";
                while (st.hasMoreTokens()) {
                    String strValue = st.nextToken();
                    //Log.d(TAG, strValue);
                    ++i;
                    if (i == 1) {
                        latitude_source = strValue.substring(10);
                        //    Log.d(TAG, "********** Latitude = " + strLat);
                    } else if (i == 2) {
                        longitude_source = strValue.substring(0, (strValue.length() - 1));
                        //Log.d(TAG, "********** Longitude = " + strLong);
                    }
                }
                str_Geopint = latitude_source + "," + longitude_source;
                Log.d("Geopoints after change", str_Geopint);
                //  Toast.makeText(getApplicationContext(), "select lat"+strLat, Toast.LENGTH_LONG).show();
               /* Location src_loc=new Location(String.valueOf(place.getLatLng()));
                Toast.makeText(getApplicationContext(), "select lat "+src_loc.getLatitude(), Toast.LENGTH_LONG).show();*/
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(myContext,
                        data);
                // TODO: Handle the error.
                Log.e("Tag", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    //-----------------get address-----------------
    public void getAddress(final String latitude, final String longitude) {
        simpleArcDialog.setConfiguration(new ArcConfiguration(getContext()));
        simpleArcDialog.show();
        simpleArcDialog.setCancelable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebServices.GET_ADDRESS,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        simpleArcDialog.dismiss();
                        resourceLocation.setText(s);
                        Log.d("address", s);
                        str_Geopint = latitude + "," + longitude;
                        Log.d("Geopoints", str_Geopint);
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ToastPopUp.show(myContext, getString(R.string.server_response_error));
                        simpleArcDialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new Hashtable<String, String>();
                params.put("latitude", latitude);
                params.put("longitude", longitude);
                return params;
            }
        };
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    //--------------------------getting user groups from server------------------------------------------
    public void getUserGroups(String user_id) {
        simpleArcDialog.setConfiguration(new ArcConfiguration(getContext()));
        simpleArcDialog.show();
        simpleArcDialog.setCancelable(false);
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
                simpleArcDialog.dismiss();
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
                                groupArrayList.add(groupPOJO);
                            }
                        } catch (Exception e) {
//                            StringWriter writer = new StringWriter();
//                            e.printStackTrace(new PrintWriter(writer));
//                            Bugreport bg = new Bugreport();
//                            bg.sendbug(writer.toString());
                        }

                        final Dialog dialog = new Dialog(getContext());
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setCancelable(false);
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.setContentView(R.layout.user_orgs_dialog_resource);
                        TextView txtHead = (TextView) dialog.findViewById(R.id.txt_head_1);
                        ListView userorglist = (ListView) dialog.findViewById(R.id.user_orgs_list_1);
                        final CheckBox chkOtherOrg = (CheckBox) dialog.findViewById(R.id.chk_all_other_orgs_1);
                        Button ok = (Button) dialog.findViewById(R.id.user_org_ok_1);
                        Button cancel = (Button) dialog.findViewById(R.id.user_org_cancel_1);
                        Log.d("groupArrayList_size", "" + groupArrayList.size());
                        if (groupArrayList.size() > 0) {
                            txtHead.setVisibility(View.VISIBLE);
                            userorglist.setVisibility(View.VISIBLE);
                            userorglist.setAdapter(new UserGroupListAdapter(groupArrayList, getContext()));
                        } else {
                            txtHead.setVisibility(View.GONE);
                            userorglist.setVisibility(View.GONE);
                        }

                        if (checkedOtherGrp.equals("Y")) {
                            chkOtherOrg.setChecked(true);
                        }

                        chkOtherOrg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                if (chkOtherOrg.isChecked()) {
                                    checkedOtherGrp = "Y";
                                } else {
                                    checkedOtherGrp = "N";
                                }
                            }
                        });

                        ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
//                            Toast.makeText(getContext(), String.valueOf(selectedUserGroups), Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                if (selectedGrpNames.size() > 0) {
                                    if (selectedGrpNames.get(0).length() > 15) {
                                        String txt = selectedGrpNames.get(0).substring(0, 14) + "... ";
                                        int count = selectedGrpNames.size();
                                        if (checkedOtherGrp.equals("Y")) {
                                            count++;
                                        }
                                        if (count > 1) {
                                            edselectAudiance.setText(txt + " + " + String.valueOf(count - 1) + " more");
                                        } else {
                                            edselectAudiance.setText(txt);
                                        }
                                    } else {
                                        String txt = selectedGrpNames.get(0);
                                        int count = selectedGrpNames.size();
                                        if (checkedOtherGrp.equals("Y")) {
                                            count++;
                                        }
                                        if (count > 1) {
                                            edselectAudiance.setText(txt + " + " + String.valueOf(count - 1) + " more");
                                        } else {
                                            edselectAudiance.setText(txt);
                                        }
                                    }
                                } else {
                                    if (checkedOtherGrp.equals("Y")) {
                                        edselectAudiance.setText(chkOtherOrg.getText());
                                    } else {
                                        edselectAudiance.setText("");
                                        edselectAudiance.clearFocus();
                                    }
                                }
                            }
                        });

                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                } catch (Exception e) {
                    simpleArcDialog.dismiss();
                    Log.d("response_grouplist", "" + e.getMessage());
//                    StringWriter writer = new StringWriter();
//                    e.printStackTrace(new PrintWriter(writer));
//                    Bugreport bg = new Bugreport();
//                    bg.sendbug(writer.toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                simpleArcDialog.dismiss();
                Log.d("getorgs_error", "" + t.getMessage());
                ToastPopUp.show(myContext, getString(R.string.server_response_error));
            }
        });
    }

    //---------------------sending group details to server-----------------------------------------------
    public void createResource(String userid, String groupid, String resname, String desc, String subtype, String geopoint, String address, String groupids, String allgrps) {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        ArcConfiguration configuration = new ArcConfiguration(getContext());
        configuration.setText("Creating resource..");
        simpleArcDialog.setConfiguration(configuration);
        simpleArcDialog.show();
        simpleArcDialog.setCancelable(false);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        CreateResourceInterface service = retrofit.create(CreateResourceInterface.class);
        Call<GroupResponseStatus> call = service.sendData(userid, groupid, resname, desc, subtype, geopoint, address, groupids, allgrps);
        call.enqueue(new Callback<GroupResponseStatus>() {
            @Override
            public void onResponse(Response<GroupResponseStatus> response, Retrofit retrofit) {
                simpleArcDialog.dismiss();
                Log.d("responseresource", "" + response.body());
                try {
                    GroupResponseStatus groupResponseStatus = response.body();
                    int isblock = 0;
                    try {
                        isblock = groupResponseStatus.getIsBlocked();
                    } catch (Exception e) {
                        isblock = 0;
                    }
                    if (isblock == 1) {
                        simpleArcDialog.dismiss();
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
                        if (groupResponseStatus.getStatus() == 1) {
                            Toast.makeText(getContext(), getResources().getString(R.string.res_success), Toast.LENGTH_SHORT).show();
                            // move to mapview
                            fragmgr = getFragmentManager();
                            fragmgr.beginTransaction().replace(R.id.content_frame, TaggedneedsFrag.newInstance(1)).commit();
                        } else if (groupResponseStatus.getStatus() == 0) {
                            Toast.makeText(getContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    Log.d("responsegroup", "" + e.getMessage());
//                    StringWriter writer = new StringWriter();
//                    e.printStackTrace(new PrintWriter(writer));
//                    Bugreport bg = new Bugreport();
//                    bg.sendbug(writer.toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                simpleArcDialog.dismiss();
                Log.d("responseresource", "" + t.getMessage());
                ToastPopUp.show(myContext, getString(R.string.server_response_error));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                    Bundle bundle = new Bundle();
                    int i = 3;
                    bundle.putString("tab", "tab1");
                    TaggedneedsFrag mainHomeFragment = new TaggedneedsFrag();
                    mainHomeFragment.setArguments(bundle);
                    android.support.v4.app.FragmentTransaction fragmentTransaction =
                            getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, mainHomeFragment);
                    fragmentTransaction.commit();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        mGoogleApiClient.connect();
    }
}
