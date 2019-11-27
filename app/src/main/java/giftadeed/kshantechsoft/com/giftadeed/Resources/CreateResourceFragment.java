package giftadeed.kshantechsoft.com.giftadeed.Resources;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.squareup.okhttp.OkHttpClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

import giftadeed.kshantechsoft.com.giftadeed.Collaboration.CollabResponseStatus;
import giftadeed.kshantechsoft.com.giftadeed.Group.GroupPOJO;
import giftadeed.kshantechsoft.com.giftadeed.Group.GroupResponseStatus;
import giftadeed.kshantechsoft.com.giftadeed.Group.GroupsInterface;
import giftadeed.kshantechsoft.com.giftadeed.Login.LoginActivity;
import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.CategoryInterface;
import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.CategoryType;
import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.CustomNeedtype;
import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.GPSTracker;
import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.GetAddressInterface;
import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.GetAddressResponse;
import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.Needtype;
import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.SuggestSubType;
import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.SuggestType;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsActivity;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsFrag;
import giftadeed.kshantechsoft.com.giftadeed.Utils.SessionManager;
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
import static com.facebook.FacebookSdk.getApplicationContext;

public class CreateResourceFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener {
    private ArrayList<GroupPOJO> groupArrayList = new ArrayList<>();
    private ArrayList<GroupPOJO> ownedGroupsArrayList = new ArrayList<>();
    ArrayList<String> selectedGroups = new ArrayList<String>();
    ArrayList<String> selectedGrpNames = new ArrayList<String>();
    String formattedTypeIds = "", formattedTypePref = "", formattedCustomTypeIds = "", formattedSubTypeIds = "",
            formattedSubTypePref = "", formattedUserGroups = ""; // for removing brackets [ and ]
    ArrayList<String> subTypePref = new ArrayList<String>();
    ArrayList<String> subtypePrefId = new ArrayList<String>();
    ArrayList<String> catPrefNames = new ArrayList<String>();
    ArrayList<String> catPrefId = new ArrayList<String>();
    ArrayList<String> customTypePrefId = new ArrayList<String>();
    View rootview;
    FragmentActivity myContext;
    static FragmentManager fragmgr;
    SimpleArcDialog simpleArcDialog;
    EditText selectFromGroup, resourceCat, resourceSubCat, resourceName, resourceLocation, resourceDesc, edselectAudiance;
    LinearLayout resSubCatLayout;
    Button btnCreateResource;
    SessionManager sessionManager;
    String strUser_ID;
    String receivedGid = "", receivedRid = "", receivedRname = "", callingFrom = "", str_rescat_id = "", str_rescat = "",
            str_ressubcat_id = "", str_ressubcat = "", str_resdesc = "", str_address = "", str_rescreatorid = "",
            resAudGrpIds = "", resAudGrpNames = "", str_resCustomCat_id = "";
    private ArrayList<Needtype> categories = new ArrayList<Needtype>();
    private ArrayList<CustomNeedtype> customCategories = new ArrayList<>();
    private ArrayList<MultiSubCategories> subcategories = new ArrayList<MultiSubCategories>();
    String strGroupmapping_ID, strGroupmapping_Name, strDetailsFromGrpID = "", strDetailsFromGrpName = "";
    ImageView catImage;
    String latitude_source, longitude_source;
    public String str_Geopoints, lat, longi, itemname, itemid;
    String checkedAllGrps = "N";
    private GoogleApiClient mGoogleApiClient;
//    private List<String> resultResCatId;
//    private List<String> resultResSubCatId;
//    private List<String> resultResAudienceGrpId;

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
        fragmgr = getFragmentManager();
        simpleArcDialog = new SimpleArcDialog(getContext());
        TaggedneedsActivity.imgappbarcamera.setVisibility(View.GONE);
        TaggedneedsActivity.imgappbarsetting.setVisibility(View.GONE);
        TaggedneedsActivity.imgfilter.setVisibility(View.GONE);
        TaggedneedsActivity.imgShare.setVisibility(View.GONE);
        TaggedneedsActivity.editprofile.setVisibility(View.GONE);
        TaggedneedsActivity.saveprofile.setVisibility(View.GONE);
        TaggedneedsActivity.toggle.setDrawerIndicatorEnabled(false);
        TaggedneedsActivity.back.setVisibility(View.VISIBLE);
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
        receivedRname = resource.get(sessionManager.RESOURCE_NAME);
        lat = String.valueOf(new GPSTracker(myContext).latitude);
        longi = String.valueOf(new GPSTracker(myContext).longitude);
        catPrefId = new ArrayList<>();
        subtypePrefId = new ArrayList<>();
        selectedGroups = new ArrayList<>();
        Bundle bundle = this.getArguments();
        if (callingFrom.equals("create")) {
            //from create menu
            TaggedneedsActivity.updateTitle(getResources().getString(R.string.create_res));
//            getOwnedGroupList(strUser_ID);
            getAddress(lat, longi);
        } else {
            //from edit menu
            TaggedneedsActivity.updateTitle(getResources().getString(R.string.edit_resource));
            str_rescat_id = this.getArguments().getString("str_rescat_id");
            str_resCustomCat_id = this.getArguments().getString("str_resCustomCatId");
            str_rescat = this.getArguments().getString("str_rescat");
            str_ressubcat_id = this.getArguments().getString("str_ressubcat_id");
            str_ressubcat = this.getArguments().getString("str_ressubcat");
            str_resdesc = this.getArguments().getString("str_resdesc");
            str_Geopoints = this.getArguments().getString("str_geopoint");
            str_address = this.getArguments().getString("str_address");
            strGroupmapping_ID = this.getArguments().getString("str_grpid");
            strGroupmapping_Name = this.getArguments().getString("str_grpname");
            strDetailsFromGrpID = this.getArguments().getString("str_grpid");
            strDetailsFromGrpName = this.getArguments().getString("str_grpname");
            str_rescreatorid = this.getArguments().getString("str_rescreatorid");
            checkedAllGrps = this.getArguments().getString("resAllGrp");
            resAudGrpIds = this.getArguments().getString("resAudGrpIds");
            resAudGrpNames = this.getArguments().getString("resAudGrpNames");
            if (str_rescat_id.endsWith(",")) {
                str_rescat_id = str_rescat_id.substring(0, str_rescat_id.length() - 1);
            }
            if (str_rescat.endsWith(",")) {
                str_rescat = str_rescat.substring(0, str_rescat.length() - 1);
            }
            if (str_ressubcat_id.endsWith(",")) {
                str_ressubcat_id = str_ressubcat_id.substring(0, str_ressubcat_id.length() - 1);
            }
            if (str_ressubcat.endsWith(",")) {
                str_ressubcat = str_ressubcat.substring(0, str_ressubcat.length() - 1);
            }
            catPrefId = new ArrayList<String>(Arrays.asList(str_rescat_id.split("\\s*,\\s*")));
            subtypePrefId = new ArrayList<String>(Arrays.asList(str_ressubcat_id.split("\\s*,\\s*")));
            selectFromGroup.setText(strGroupmapping_Name);
            resourceCat.setText(str_rescat);
            resourceSubCat.setText(str_ressubcat);
            resourceName.setText(receivedRname);
            resourceDesc.setText(str_resdesc);
            resourceLocation.setText(str_address);
            if (checkedAllGrps.equals("Y")) {
                edselectAudiance.setText(getResources().getString(R.string.all_groups));
            } else {
                if (resAudGrpNames.length() > 25) {
                    edselectAudiance.setText(String.format("%s...", resAudGrpNames.substring(0, 24)));
                } else {
                    edselectAudiance.setText(resAudGrpNames);
                }
                if (resAudGrpIds.length() > 0) {
                    selectedGroups = new ArrayList<String>(Arrays.asList(resAudGrpIds.split("\\s*,\\s*")));
                }

                if (str_resCustomCat_id.length() > 0) {
                    customTypePrefId = new ArrayList<String>(Arrays.asList(str_resCustomCat_id.split("\\s*,\\s*")));
                    edselectAudiance.setEnabled(false);
                }
            }
        }

        selectFromGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(Validation.isNetworkAvailable(getActivity()))) {
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
                    if (selectFromGroup.getText().length() < 1) {
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
                if (resourceCat.getText().length() < 1) {
                    ToastPopUp.displayToast(getContext(), getResources().getString(R.string.select_category));
                } else {
                    if (callingFrom.equals("create")) {
                        if (subcategories.size() > 0) {
                            showSubCatDialog();
                        } else if ((catPrefId.size() < 1) && (customTypePrefId.size() > 0)) {
                            // only custom  category selected
                            showSubCatDialog();
                        } else {
                            if (!(Validation.isNetworkAvailable(getActivity()))) {
                                ToastPopUp.show(getActivity(), getString(R.string.network_validation));
                            } else {
                                getSubCategory("no");
                            }
                        }
                    } else {
//                        catPrefId = new ArrayList<String>();
//                        catPrefNames = new ArrayList<String>();

                        if (subcategories.size() > 0) {
                            showSubCatDialog();
                        } else if ((catPrefId.size() < 1) && (customTypePrefId.size() > 0)) {
                            // only custom  category selected
                            showSubCatDialog();
                        } else {
                            if (!(Validation.isNetworkAvailable(getActivity()))) {
                                ToastPopUp.show(getActivity(), getString(R.string.network_validation));
                            } else {
                                getSubCategory("no");
                            }
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
                if (groupArrayList.size() > 0) {
                    showAudienceDialog();
                } else {
                    if (!(Validation.isNetworkAvailable(getActivity()))) {
                        ToastPopUp.show(getActivity(), getString(R.string.network_validation));
                    } else {
                        getUserGroups(strUser_ID);
                    }
                }
            }
        });

        btnCreateResource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                btnCreateResource.startAnimation();
                if (selectFromGroup.getText().length() == 0) {
                    ToastPopUp.displayToast(getContext(), getResources().getString(R.string.select_res_group));
//                    btnCreateResource.doneLoadingAnimation(getResources().getColor(R.color.colorPrimary), BitmapFactory.decodeResource(getContext().getResources(),
//                            R.drawable.thumb_icon_over));
                } else if (resourceCat.getText().length() == 0) {
                    ToastPopUp.displayToast(getContext(), getResources().getString(R.string.select_category));
                } else if ((resourceSubCat.getText().length() < 1) && (subcategories.size() > 0)) {
                    ToastPopUp.displayToast(getContext(), getResources().getString(R.string.select_sub_category));
                } else if (resourceLocation.getText().length() == 0) {
                    ToastPopUp.displayToast(getContext(), getResources().getString(R.string.select_location));
                } else if (resourceName.getText().length() == 0) {
                    resourceName.requestFocus();
                    resourceName.setFocusable(true);
                    resourceName.setError(getResources().getString(R.string.res_name_req));
                } else if (edselectAudiance.getText().length() == 0) {
                    ToastPopUp.displayToast(getContext(), getResources().getString(R.string.select_audiance));
                } else {
                    formattedUserGroups = selectedGroups.toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\s+", "");
                    Log.d("formattedUserGroups", "" + formattedUserGroups);
                    formattedTypeIds = catPrefId.toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\s+", "");
                    formattedSubTypeIds = subtypePrefId.toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\s+", "");
                    formattedCustomTypeIds = customTypePrefId.toString().replaceAll("\\[", "").replaceAll("\\]", "");
                    //call api
                    if (!(Validation.isNetworkAvailable(getActivity()))) {
                        ToastPopUp.show(getActivity(), getString(R.string.network_validation));
                    } else {
//                        btnCreateResource.startAnimation();
                        if (checkedAllGrps.equals("Y")) {
                            if (callingFrom.equals("create")) {
                                // if all groups is selected then send blank user selected groups
                                createResource(strUser_ID, strGroupmapping_ID, resourceName.getText().toString(), resourceDesc.getText().toString(), formattedSubTypeIds, str_Geopoints, resourceLocation.getText().toString(), "", checkedAllGrps);
                            } else {
                                // call update resource api
                                updateResource(receivedRid, strUser_ID, strGroupmapping_ID, resourceName.getText().toString(), resourceDesc.getText().toString(), formattedSubTypeIds, str_Geopoints, resourceLocation.getText().toString(), "", checkedAllGrps);
                            }
                        } else {
                            if (customTypePrefId.size() > 0) {
                                // user selected custom category then audience will be from group
                                if (callingFrom.equals("create")) {
                                    createResource(strUser_ID, strGroupmapping_ID, resourceName.getText().toString(), resourceDesc.getText().toString(), formattedSubTypeIds, str_Geopoints, resourceLocation.getText().toString(), strGroupmapping_ID, checkedAllGrps);
                                } else {
                                    // call update resource api
                                    updateResource(receivedRid, strUser_ID, strGroupmapping_ID, resourceName.getText().toString(), resourceDesc.getText().toString(), formattedSubTypeIds, str_Geopoints, resourceLocation.getText().toString(), strGroupmapping_ID, checkedAllGrps);
                                }
                            } else {
                                if (callingFrom.equals("create")) {
                                    createResource(strUser_ID, strGroupmapping_ID, resourceName.getText().toString(), resourceDesc.getText().toString(), formattedSubTypeIds, str_Geopoints, resourceLocation.getText().toString(), formattedUserGroups, checkedAllGrps);
                                } else {
                                    // call update resource api
                                    updateResource(receivedRid, strUser_ID, strGroupmapping_ID, resourceName.getText().toString(), resourceDesc.getText().toString(), formattedSubTypeIds, str_Geopoints, resourceLocation.getText().toString(), formattedUserGroups, checkedAllGrps);
                                }
                            }
                        }
                    }
                }
            }
        });

        TaggedneedsActivity.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callingFrom.equals("create")) {
                    // move to resource list
                    fragmgr = getFragmentManager();
                    fragmgr.beginTransaction().replace(R.id.content_frame, ResourceListFragment.newInstance()).commit();
                } else {
                    // move to resource details
                    Bundle bundle = new Bundle();
                    bundle.putString("str_resid", receivedRid);
                    bundle.putString("callingFrom", callingFrom);
                    ResourceDetailsFrag resourceDetailsFrag = new ResourceDetailsFrag();
                    resourceDetailsFrag.setArguments(bundle);
                    FragmentTransaction fragmentTransaction =
                            getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, resourceDetailsFrag);
                    fragmentTransaction.commit();
                }
            }
        });
        return rootview;
    }

    //--------------------------Initilizing the UI variables-----------------------------
    private void init() {
        selectedGroups = new ArrayList<String>();
        selectedGrpNames = new ArrayList<String>();
        catImage = (ImageView) rootview.findViewById(R.id.resource_selected_cat_image);
        selectFromGroup = (EditText) rootview.findViewById(R.id.tv_select_from_group);
        resourceName = (EditText) rootview.findViewById(R.id.editText_resource_name);
        resourceCat = (EditText) rootview.findViewById(R.id.resource_select_cat);
        resourceSubCat = (EditText) rootview.findViewById(R.id.tv_select_sub_categories);
        resSubCatLayout = (LinearLayout) rootview.findViewById(R.id.res_sub_cat_layout);
        resourceDesc = (EditText) rootview.findViewById(R.id.editText_resource_desc);
        btnCreateResource = (Button) rootview.findViewById(R.id.button_create_resource);
        resourceLocation = (EditText) rootview.findViewById(R.id.resource_location);
        edselectAudiance = (EditText) rootview.findViewById(R.id.select_resource_audience);
//        btnCreateResource = (CircularProgressButton) rootview.findViewById(R.id.button_create_resource);
    }

    //--------------------------getting user owned groups from server--------------------
    public void getOwnedGroupList(String userid) {
        ownedGroupsArrayList = new ArrayList<>();
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
                        /*Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                                new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(Status status) {
                                        //updateUI(false);
                                    }
                                });*/
                        sessionManager.set_notification_status("ON");
                        Intent loginintent = new Intent(getActivity(), LoginActivity.class);
                        loginintent.putExtra("message", "Charity");
                        startActivity(loginintent);
                    } else {
                        List<GroupPOJO> groupPOJOS = response.body();
                        ownedGroupsArrayList.clear();
                        try {
                            for (int i = 0; i < groupPOJOS.size(); i++) {
                                GroupPOJO groupPOJO = new GroupPOJO();
                                groupPOJO.setGroup_id(groupPOJOS.get(i).getGroup_id());
                                groupPOJO.setGroup_name(groupPOJOS.get(i).getGroup_name());
                                ownedGroupsArrayList.add(groupPOJO);
                            }
                        } catch (Exception e) {

                        }

                        /*if (createResScreenload.equals("yes")) {
                            if (ownedGroupsArrayList.size() == 1) {
                                if (ownedGroupsArrayList.get(0).getGroup_name().length() > 30) {
                                    selectFromGroup.setText(ownedGroupsArrayList.get(0).getGroup_name().substring(0, 29) + "...");
                                } else {
                                    selectFromGroup.setText(ownedGroupsArrayList.get(0).getGroup_name());
                                }
                                strGroupmapping_ID = ownedGroupsArrayList.get(0).getGroup_id();
                                strGroupmapping_Name = ownedGroupsArrayList.get(0).getGroup_name();
                                selectFromGroup.setEnabled(false);
                                selectFromGroup.clearFocus();
                            } else {
                                selectFromGroup.setEnabled(true);
                                selectFromGroup.clearFocus();
                            }
                        } else {*/
                        final Dialog dialog = new Dialog(getContext());
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setCancelable(false);
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.setContentView(R.layout.groups_dialog);
                        ListView ownedgrouplist = (ListView) dialog.findViewById(R.id.owned_group_list);
                        Button cancel = (Button) dialog.findViewById(R.id.group_cancel);
                        ownedgrouplist.setAdapter(new OwnedGroupsAdapter(ownedGroupsArrayList, getContext()));
                        ownedgrouplist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                try {
                                    if (ownedGroupsArrayList.size() > 0) {
                                        if (ownedGroupsArrayList.get(i).getGroup_name().length() > 40) {
                                            selectFromGroup.setText(ownedGroupsArrayList.get(i).getGroup_name().substring(0, 39) + "...");
                                        } else {
                                            selectFromGroup.setText(ownedGroupsArrayList.get(i).getGroup_name());
                                        }
                                        strGroupmapping_ID = ownedGroupsArrayList.get(i).getGroup_id();
                                        strGroupmapping_Name = ownedGroupsArrayList.get(i).getGroup_name();
                                    }
                                } catch (Exception e) {
                                    Log.d("FromGrp_Exception", "" + e.getMessage());
                                }

                                if (callingFrom.equals("create")) {
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
                                } else {
                                    if (!strGroupmapping_ID.equals(strDetailsFromGrpID)) {
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

                                        // reset all form values
                                        catPrefId = new ArrayList<>(); // reset cat id received from details
                                        subtypePrefId = new ArrayList<>(); // reset subcat id received from details
//                                        selectedGroups = new ArrayList<>(); // reset audience group id received from details
                                        strDetailsFromGrpID = "";
                                        strDetailsFromGrpName = "";
                                        checkedAllGrps = "N"; // reset audience all groups
                                        selectedGroups = new ArrayList<String>(); // reset audience checked groups
                                        selectedGrpNames = new ArrayList<String>(); // reset audience checked groups
                                    }
                                }
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
                }
            }

            @Override
            public void onFailure(Throwable t) {
                simpleArcDialog.dismiss();
                ToastPopUp.show(myContext, getString(R.string.server_response_error));
            }
        });
    }

    //--------------------------getting categories from server---------------------------
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

                if (catPrefId.size() > 0) {
                    for (int i = 0; i < catPrefId.size(); i++) {
                        for (int j = 0; j < categories.size(); j++) {
                            if (catPrefId.get(i).equals(categories.get(j).getNeedMappingID())) {
                                categories.get(j).setChecked(true);
                            }
                        }
                    }
                }

                if (customTypePrefId.size() > 0) {
                    for (int i = 0; i < customTypePrefId.size(); i++) {
                        for (int j = 0; j < customCategories.size(); j++) {
                            if (customTypePrefId.get(i).equals(customCategories.get(j).getNeedMappingID())) {
                                customCategories.get(j).setChecked(true);
                            }
                        }
                    }
                }
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
        layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        layoutManager1 = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
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
            categorylist.setAdapter(new ResourceMultiCatAdapter(categories));
        } else {
            tvheading1.setVisibility(View.GONE);
            btnok.setVisibility(View.GONE);
            cancel.setVisibility(View.VISIBLE);
            categorylist.setVisibility(View.GONE);
        }
        if (customCategories.size() > 0) {
            tvheading2.setVisibility(View.VISIBLE);
            custom_categorylist.setVisibility(View.VISIBLE);
            custom_categorylist.setAdapter(new ResourceMultiCustomCatAdapter(customCategories));
        } else {
            tvheading2.setVisibility(View.GONE);
            custom_categorylist.setVisibility(View.GONE);
        }

        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                catPrefId = new ArrayList<String>();
                catPrefNames = new ArrayList<String>();
                customTypePrefId = new ArrayList<>();
                for (int i = 0; i < categories.size(); i++) {
//                            Log.d("multicat", categories.get(i).getNeedName() + ":" + categories.get(i).getChecked());
                    if (categories.get(i).getChecked()) {
                        catPrefId.add(categories.get(i).getNeedMappingID()); // add position of the row
                        catPrefNames.add(categories.get(i).getNeedName());
                    }
                }
                for (int i = 0; i < customCategories.size(); i++) {
//                            Log.d("multicat", categories.get(i).getNeedName() + ":" + categories.get(i).getChecked());
                    if (customCategories.get(i).getChecked()) {
//                        catPrefId.add(customCategories.get(i).getNeedMappingID()); // add position of the row
                        catPrefNames.add(customCategories.get(i).getNeedName());
                        customTypePrefId.add(customCategories.get(i).getNeedMappingID());
                    }
                }
                dialog.dismiss();

                if (categories.size() > 0) {
                    formattedTypeIds = catPrefId.toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\s+", "");
                    formattedTypePref = catPrefNames.toString().replaceAll("\\[", "").replaceAll("\\]", "");
                    formattedCustomTypeIds = customTypePrefId.toString().replaceAll("\\[", "").replaceAll("\\]", "");
                    Log.d("formattedTypeIds", "" + formattedTypeIds + ":" + formattedCustomTypeIds);
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
                    selectedGroups = new ArrayList<>();
                }

                if (catPrefId.size() > 0) {
                    //call getsubcategory to check subcategories available for selected main categories
                    getSubCategory("yes");
                    subtypePrefId = new ArrayList<>();
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

    //--------------------------getting sub categories from server-----------------------
    public void getSubCategory(final String callingFromCat) {
        formattedTypeIds = catPrefId.toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\s+", "");
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

                if (subtypePrefId.size() > 0) {
                    for (int i = 0; i < subtypePrefId.size(); i++) {
                        for (int j = 0; j < subcategories.size(); j++) {
                            if (subtypePrefId.get(i).equals(subcategories.get(j).getSubCatId())) {
                                subcategories.get(j).setChecked(true);
                            }
                        }
                    }
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
        layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        subcategorylist.addItemDecoration(new DividerItemDecoration(subcategorylist.getContext(), DividerItemDecoration.VERTICAL));
        subcategorylist.setLayoutManager(layoutManager);
        TextView tvMsg = (TextView) dialog.findViewById(R.id.txt_msg);
        Button ok = (Button) dialog.findViewById(R.id.sub_category_ok);
        Button cancel = (Button) dialog.findViewById(R.id.sub_category_cancel);
        TextView suggestSubType = (TextView) dialog.findViewById(R.id.suggest_sub_type);
        Log.d("subcatlist_size", "" + subcategories.size());
        if (subcategories.size() > 0) {
            tvMsg.setText(getResources().getString(R.string.select_pref_for_people));
            subcategorylist.setVisibility(View.VISIBLE);
            ok.setVisibility(View.VISIBLE);
            cancel.setVisibility(View.VISIBLE);
            subcategorylist.setAdapter(new ResourceSubCatAdapter(subcategories));
        } else {
            tvMsg.setText(getResources().getString(R.string.no_pref_found));
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
                    if (subcategories.get(i).getChecked()) {
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
        suggestSubType.setOnClickListener(new View.OnClickListener() {
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

    //--------------------------suggest sub-type-----------------------------------------
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
                str_Geopoints = latitude_source + "," + longitude_source;
                Log.d("Geopoints after change", str_Geopoints);
                //  Toast.makeText(getApplicationContext(), "select lat"+strLat, Toast.LENGTH_LONG).show();
//                Location src_loc = new Location(String.valueOf(place.getLatLng()));
//                Toast.makeText(getApplicationContext(), "select lat " + src_loc.getLatitude(), Toast.LENGTH_LONG).show();
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

    //-----------------get address---------------------------
    public void getAddress(final String latitude, final String longitude) {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        simpleArcDialog.setConfiguration(new ArcConfiguration(getContext()));
        simpleArcDialog.show();
        simpleArcDialog.setCancelable(false);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        GetAddressInterface service = retrofit.create(GetAddressInterface.class);
        Call<GetAddressResponse> call = service.sendData(latitude, longitude);
        call.enqueue(new Callback<GetAddressResponse>() {
            @Override
            public void onResponse(Response<GetAddressResponse> response, Retrofit retrofit) {
                simpleArcDialog.dismiss();
                Log.d("response_address", "" + response.body());
                try {
                    GetAddressResponse getAddressResponse = response.body();
                    if (getAddressResponse.getStatus() == 1) {
                        resourceLocation.setText(getAddressResponse.getAddress());
                        Log.d("address", "" + getAddressResponse.getAddress());
                        str_Geopoints = latitude + "," + longitude;
                        Log.d("Geopoints", str_Geopoints);
                    } else if (getAddressResponse.getStatus() == 0) {
                        Toast.makeText(getContext(), getAddressResponse.getErrorMsg(), Toast.LENGTH_SHORT).show();
                        resourceLocation.setText("");
                    }
                } catch (Exception e) {
                    Log.d("responsegroup", "" + e.getMessage());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                simpleArcDialog.dismiss();
                Log.d("response_address", "" + t.getMessage());
                ToastPopUp.show(myContext, getString(R.string.server_response_error));
            }
        });
    }

    //--------------------------getting user groups from server--------------------------
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
                        /*Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                                new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(Status status) {
                                        //updateUI(false);
                                    }
                                });*/

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
                                groupPOJO.setChecked(false);
                                groupArrayList.add(groupPOJO);
                            }
                        } catch (Exception e) {
//                            StringWriter writer = new StringWriter();
//                            e.printStackTrace(new PrintWriter(writer));
//                            Bugreport bg = new Bugreport();
//                            bg.sendbug(writer.toString());
                        }

                        if (selectedGroups.size() > 0) {
                            for (int i = 0; i < selectedGroups.size(); i++) {
                                for (int j = 0; j < groupArrayList.size(); j++) {
                                    if (selectedGroups.get(i).equals(groupArrayList.get(j).getGroup_id())) {
                                        groupArrayList.get(j).setChecked(true);
                                    }
                                }
                            }
                        }

                        showAudienceDialog();
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

    public void showAudienceDialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.user_orgs_dialog_resource);
        TextView txtHead = (TextView) dialog.findViewById(R.id.txt_head_1);
        RecyclerView userorglist = (RecyclerView) dialog.findViewById(R.id.user_orgs_list_1);
        LinearLayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        userorglist.addItemDecoration(new DividerItemDecoration(userorglist.getContext(), DividerItemDecoration.VERTICAL));
        userorglist.setLayoutManager(layoutManager);
        final CheckBox chkOtherOrg = (CheckBox) dialog.findViewById(R.id.chk_all_other_orgs_1);
        Button ok = (Button) dialog.findViewById(R.id.user_org_ok_1);
        Button cancel = (Button) dialog.findViewById(R.id.user_org_cancel_1);
        Log.d("groupArrayList_size", "" + groupArrayList.size());
        if (groupArrayList.size() > 0) {
            txtHead.setVisibility(View.VISIBLE);
            userorglist.setVisibility(View.VISIBLE);
            userorglist.setAdapter(new UserGroupListAdapter(groupArrayList));
        } else {
            txtHead.setVisibility(View.GONE);
            userorglist.setVisibility(View.GONE);
        }

        if (checkedAllGrps.equals("Y")) {
            chkOtherOrg.setChecked(true);
        }

        chkOtherOrg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (chkOtherOrg.isChecked()) {
                    checkedAllGrps = "Y";
                } else {
                    checkedAllGrps = "N";
                }
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedGroups = new ArrayList<String>();
                selectedGrpNames = new ArrayList<String>();
                for (int i = 0; i < groupArrayList.size(); i++) {
                    if (groupArrayList.get(i).isChecked()) {
                        selectedGroups.add(groupArrayList.get(i).getGroup_id()); // add position of the row
                        selectedGrpNames.add(groupArrayList.get(i).getGroup_name());
                    }
                }
                if ((selectedGrpNames.size() == 0) && (checkedAllGrps.equals("N"))) {
                    ToastPopUp.displayToast(getContext(), getResources().getString(R.string.select_audiance));
                } else {
                    dialog.dismiss();
                    if (selectedGrpNames.size() > 0) {
                        if (selectedGrpNames.get(0).length() > 25) {
                            String txt = selectedGrpNames.get(0).substring(0, 24) + "... ";
                            int count = selectedGrpNames.size();
                            if (checkedAllGrps.equals("Y")) {
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
                            if (checkedAllGrps.equals("Y")) {
                                count++;
                            }
                            if (count > 1) {
                                edselectAudiance.setText(txt + " + " + String.valueOf(count - 1) + " more");
                            } else {
                                edselectAudiance.setText(txt);
                            }
                        }
                    } else {
                        if (checkedAllGrps.equals("Y")) {
                            edselectAudiance.setText(chkOtherOrg.getText());
                        } else {
                            edselectAudiance.setText("");
                            edselectAudiance.clearFocus();
                        }
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

    //---------------------sending resource details to server----------------------------
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
        Log.d("create_res_input", userid + "," + groupid + "," + resname + "," + desc + "," + subtype + "," + geopoint + "," + address + "," + groupids + "," + allgrps + ":" + formattedTypeIds + ":" + formattedCustomTypeIds);
        Call<GroupResponseStatus> call = service.sendData(userid, groupid, resname, desc, subtype, geopoint, address, groupids, allgrps, formattedTypeIds, formattedCustomTypeIds);
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
                        /*Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                                new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(Status status) {
                                        //updateUI(false);
                                    }
                                });*/

                        sessionManager.set_notification_status("ON");
                        Intent loginintent = new Intent(getActivity(), LoginActivity.class);
                        loginintent.putExtra("message", "Charity");
                        startActivity(loginintent);
                    } else {
                        if (groupResponseStatus.getStatus() == 1) {
                            Toast.makeText(getContext(), groupResponseStatus.getSuccessMsg(), Toast.LENGTH_SHORT).show();
                            // move to mapview
                            fragmgr = getFragmentManager();
                            fragmgr.beginTransaction().replace(R.id.content_frame, TaggedneedsFrag.newInstance(1)).commit();
                        } else if (groupResponseStatus.getStatus() == 0) {
                            Toast.makeText(getContext(), groupResponseStatus.getErrorMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    Log.d("responseresource", "" + e.getMessage());
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

    //---------------------edit/update resource details------------------------
    public void updateResource(String resid, String userid, String groupid, String resname, String desc, String subtype, String geopoint, String address, String groupids, String allgrps) {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        ArcConfiguration configuration = new ArcConfiguration(getContext());
        configuration.setText("Updating resource...");
        simpleArcDialog.setConfiguration(configuration);
        simpleArcDialog.show();
        simpleArcDialog.setCancelable(false);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        UpdateResourceInterface service = retrofit.create(UpdateResourceInterface.class);
        Log.d("update_res_input", resid + "," + userid + "," + groupid + "," + resname + "," + desc + "," + subtype + "," + geopoint + "," + groupids + "," + allgrps + "," + formattedTypeIds + "," + formattedCustomTypeIds + "," + address);
        Call<CollabResponseStatus> call = service.sendData(resid, userid, groupid, resname, desc, subtype, geopoint, address, groupids, allgrps, formattedTypeIds, formattedCustomTypeIds);
        call.enqueue(new Callback<CollabResponseStatus>() {
            @Override
            public void onResponse(Response<CollabResponseStatus> response, Retrofit retrofit) {
                simpleArcDialog.dismiss();
                Log.d("responseresource", "" + response.body());
                try {
                    CollabResponseStatus collabResponseStatus = response.body();
                    int isblock = 0;
                    try {
                        isblock = collabResponseStatus.getIsBlocked();
                    } catch (Exception e) {
                        isblock = 0;
                    }
                    if (isblock == 1) {
                        simpleArcDialog.dismiss();
                        FacebookSdk.sdkInitialize(getActivity());
                        Toast.makeText(getContext(), getResources().getString(R.string.block_toast), Toast.LENGTH_SHORT).show();
                        sessionManager.createUserCredentialSession(null, null, null);
                        LoginManager.getInstance().logOut();
                        /*Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                                new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(Status status) {
                                        //updateUI(false);
                                    }
                                });*/

                        sessionManager.set_notification_status("ON");
                        Intent loginintent = new Intent(getActivity(), LoginActivity.class);
                        loginintent.putExtra("message", "Charity");
                        startActivity(loginintent);
                    } else {
                        if (collabResponseStatus.getStatus() == 1) {
                            Toast.makeText(getContext(), collabResponseStatus.getSuccessMsg(), Toast.LENGTH_SHORT).show();
                            // move to mapview
                            fragmgr = getFragmentManager();
                            fragmgr.beginTransaction().replace(R.id.content_frame, TaggedneedsFrag.newInstance(1)).commit();
                        } else if (collabResponseStatus.getStatus() == 0) {
                            Toast.makeText(getContext(), collabResponseStatus.getErrorMsg(), Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (Exception e) {
                    Log.d("responseresource", "" + e.getMessage());
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

                    if (callingFrom.equals("create")) {
                        // move to resource list
                        fragmgr = getFragmentManager();
                        fragmgr.beginTransaction().replace(R.id.content_frame, ResourceListFragment.newInstance()).commit();
                    } else {
                        // move to resource details
                        Bundle bundle = new Bundle();
                        bundle.putString("str_resid", receivedRid);
                        bundle.putString("callingFrom", callingFrom);
                        ResourceDetailsFrag resourceDetailsFrag = new ResourceDetailsFrag();
                        resourceDetailsFrag.setArguments(bundle);
                        FragmentTransaction fragmentTransaction =
                                getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame, resourceDetailsFrag);
                        fragmentTransaction.commit();
                    }

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
