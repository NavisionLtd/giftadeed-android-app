package giftadeed.kshantechsoft.com.giftadeed.Collaboration;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.sendbird.android.GroupChannel;
import com.sendbird.android.GroupChannelListQuery;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;
import com.sendbird.android.UserListQuery;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.ResponseBody;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import giftadeed.kshantechsoft.com.giftadeed.Bug.Bugreport;
import giftadeed.kshantechsoft.com.giftadeed.Group.AddGroupMemberFragment;
import giftadeed.kshantechsoft.com.giftadeed.Group.CreateGroupFragment;
import giftadeed.kshantechsoft.com.giftadeed.Group.DeleteGroupInterface;
import giftadeed.kshantechsoft.com.giftadeed.Group.ExitGroupInterface;
import giftadeed.kshantechsoft.com.giftadeed.Group.GroupCollabFrag;
import giftadeed.kshantechsoft.com.giftadeed.Group.GroupInfoFragment;
import giftadeed.kshantechsoft.com.giftadeed.Group.GroupInfoInterface;
import giftadeed.kshantechsoft.com.giftadeed.Group.GroupInfoPOJO;
import giftadeed.kshantechsoft.com.giftadeed.Group.GroupListInfo;
import giftadeed.kshantechsoft.com.giftadeed.Group.GroupResponseStatus;
import giftadeed.kshantechsoft.com.giftadeed.Group.GrouptagsInterface;
import giftadeed.kshantechsoft.com.giftadeed.Group.ManageGroupMemberFragment;
import giftadeed.kshantechsoft.com.giftadeed.Login.LoginActivity;
import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.SendBirdChat.Interfaces.DeleteChannelGroup;
import giftadeed.kshantechsoft.com.giftadeed.SendBirdChat.Interfaces.RemoveMemberFromChannel;
import giftadeed.kshantechsoft.com.giftadeed.SendBirdChat.Pojo.RemoveUserFromClub;
import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.GPSTracker;
import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.TagaNeed;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.NeedListAdapter;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.RowData;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsActivity;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.list_Model.Taggedlist;
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

public class CollabDetailsFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener {
    View rootview;
    RecyclerView recyclerView;
    List<RowData> item_list;
    FragmentActivity myContext;
    static FragmentManager fragmgr;
    SimpleArcDialog mDialog;
    SessionManager sessionManager;
    TextView collabName, memberCount, noRecordsFound;
    LinearLayout countLayout;
    String strUser_ID;
    private AlertDialog alertDialog;
    String receivedCid = "", receivedCname = "";
    private GoogleApiClient mGoogleApiClient;
    String collabCreatorId = "", admin_ids = "";
    ArrayList<String> split = new ArrayList<String>();
    int isMember = 1;
    float radius_set;
    DatabaseAccess databaseAccess;
    private UserListQuery mUserListQuery;
    private List<String> lstCurrentMembersInfo = new ArrayList<>();
    private List<GroupListInfo> lstGetChannelsList = new ArrayList<>();
    private List<String> lstGetSelectedMemberId = new ArrayList<>();
    private String fetchedChannelUrl;
    private String strSelectedChannelUrl = "";
    private List<String> lstUsersForRemove = new ArrayList<>();
    RemoveUserFromClub model_obj;

    public static CollabDetailsFragment newInstance(int sectionNumber) {
        CollabDetailsFragment fragment = new CollabDetailsFragment();
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.collab_details_layout, container, false);
        sessionManager = new SessionManager(getActivity());
        HashMap<String, String> user = sessionManager.getUserDetails();
        strUser_ID = user.get(sessionManager.USER_ID);
        HashMap<String, String> group = sessionManager.getSelectedColabDetails();
        receivedCid = group.get(sessionManager.COLAB_ID);
        receivedCname = group.get(sessionManager.COLAB_NAME);

        TaggedneedsActivity.updateTitle(getResources().getString(R.string.collab_details));
        TaggedneedsActivity.fragname = TagaNeed.newInstance(0);
        FragmentManager fragManager = myContext.getSupportFragmentManager();
        fragmgr = getFragmentManager();
        mDialog = new SimpleArcDialog(getContext());
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
        databaseAccess = DatabaseAccess.getInstance(getContext());
        databaseAccess.open();
        getChannelsDetails();
        loadNextUserList();
        collabName.setText(receivedCname);
        radius_set = sessionManager.getradius();
        if (!(Validation.isOnline(getActivity()))) {
            ToastPopUp.show(getActivity(), getString(R.string.network_validation));
        } else {
//            get_Tag_data();
            collabInfo(strUser_ID, receivedCid);
        }
        mGoogleApiClient = ((TaggedneedsActivity) getActivity()).mGoogleApiClient;
        TaggedneedsActivity.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroupCollabFrag groupCollabFrag = new GroupCollabFrag();
                fragmgr.beginTransaction().replace(R.id.content_frame, groupCollabFrag).commit();
            }
        });
        return rootview;
    }

    //--------------------------Initializing the UI variables--------------------------------------------
    private void init() {
        collabName = (TextView) rootview.findViewById(R.id.tv_collab_name);
        countLayout = (LinearLayout) rootview.findViewById(R.id.count_layout);
        memberCount = (TextView) rootview.findViewById(R.id.collabdetails_member_count);
        noRecordsFound = (TextView) rootview.findViewById(R.id.no_members);
        recyclerView = (RecyclerView) rootview.findViewById(R.id.collab_member_list);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
    }

    public void get_Tag_data() {
        item_list = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        GrouptagsInterface service = retrofit.create(GrouptagsInterface.class);
        Call<List<Taggedlist>> call = service.fetchData(strUser_ID, receivedCid);
        Log.d("input_grouptags", strUser_ID + " : " + receivedCid);
        call.enqueue(new Callback<List<Taggedlist>>() {
            @Override
            public void onResponse(Response<List<Taggedlist>> response, Retrofit retrofit) {
                // listData=null;
                try {
                    List<Taggedlist> res = response.body();
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
                        List<Taggedlist> taggedlists = response.body();
                        int size = taggedlists.size();
                        double current_latitude = new GPSTracker(getContext()).getLatitude();
                        double current_longitude = new GPSTracker(getContext()).getLongitude();
                        Location myLocation = new Location("My Location");
                        myLocation.setLatitude(current_latitude);
                        myLocation.setLongitude(current_longitude);

                        if (size > 0) {
                            for (int j = 0; j < size; j++) {
                                String str_geo_point = taggedlists.get(j).getGeopoint();
                                String[] words = str_geo_point.split(",");
                                if (words.length > 1) {
                                    Location tagLocation2 = new Location("tag Location");
                                    tagLocation2.setLatitude(Double.parseDouble(words[0]));
                                    tagLocation2.setLongitude(Double.parseDouble(words[1]));
                                    float dist1 = myLocation.distanceTo(tagLocation2);
                                    if (dist1 < radius_set) {
                                        RowData rowData = new RowData();
                                        rowData.setTitle(taggedlists.get(j).getNeedName());
                                        rowData.setAddress(taggedlists.get(j).getAddress());
                                        rowData.setDate(taggedlists.get(j).getTaggedDatetime());
                                        rowData.setImagepath(taggedlists.get(j).getTaggedPhotoPath());
                                        rowData.setDistance(dist1);
                                        rowData.setCharacterPath(taggedlists.get(j).getCharacterPath());
                                        rowData.setFname(taggedlists.get(j).getFname());
                                        rowData.setLname(taggedlists.get(j).getLname());
                                        rowData.setPrivacy(taggedlists.get(j).getPrivacy());
                                        rowData.setNeedName(taggedlists.get(j).getNeedName());
                                        rowData.setTotalTaggedCreditPoints(taggedlists.get(j).getTotalTaggedCreditPoints());
                                        rowData.setTotalFulfilledCreditPoints(taggedlists.get(j).getTotalFulfilledCreditPoints());
                                        rowData.setUserID(taggedlists.get(j).getUserID());
                                        rowData.setCatType(taggedlists.get(j).getCatType());
                                        rowData.setGetIconPath(taggedlists.get(j).getIconPath());
                                        rowData.setTaggedID(taggedlists.get(j).getTaggedID());
                                        rowData.setGeopoint(taggedlists.get(j).getGeopoint());
                                        rowData.setTaggedPhotoPath(taggedlists.get(j).getTaggedPhotoPath());
                                        rowData.setDescription(taggedlists.get(j).getDescription());
                                        rowData.setViews(taggedlists.get(j).getViews());
                                        rowData.setEndorse(taggedlists.get(j).getEndorse());
                                        rowData.setAllGroups("");
                                        rowData.setUser_group_ids("");
                                        item_list.add(rowData);
                                    }
                                }
                                if (item_list.size() == 0) {
                                    recyclerView.setVisibility(View.GONE);
                                    countLayout.setVisibility(View.GONE);
                                    noRecordsFound.setVisibility(View.VISIBLE);
                                } else {
                                    noRecordsFound.setVisibility(View.GONE);
                                    countLayout.setVisibility(View.VISIBLE);
                                    memberCount.setText(String.valueOf(item_list.size()));
                                    recyclerView.setVisibility(View.VISIBLE);
                                    recyclerView.setAdapter(new NeedListAdapter(item_list, getActivity(), "group"));
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    StringWriter writer = new StringWriter();
                    e.printStackTrace(new PrintWriter(writer));
                    Bugreport bg = new Bugreport();
                    bg.sendbug(writer.toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                ToastPopUp.show(myContext, getString(R.string.server_response_error));
            }
        });
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_group_details, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (collabCreatorId.equals(strUser_ID)) {
            //group creator
            getActivity().invalidateOptionsMenu();
            menu.findItem(R.id.action_group_info).setVisible(false);
            menu.findItem(R.id.action_exit_group).setVisible(false);
        } else if (admin_ids.length() > 0) {
            for (int i = 0; i < split.size(); i++) {
                if (split.get(i).equals(strUser_ID)) {
                    //group admin
                    getActivity().invalidateOptionsMenu();
                    menu.findItem(R.id.action_delete_group).setVisible(false);
                    menu.findItem(R.id.action_group_info).setVisible(false);
//                    menu.findItem(R.id.action_exit_group).setVisible(false);
                    isMember = 0;
                    break;
                }
            }
            if (isMember == 1) {
                //group member
                getActivity().invalidateOptionsMenu();
                menu.findItem(R.id.action_add_member).setVisible(false);
                menu.findItem(R.id.action_member_list).setVisible(false);
                menu.findItem(R.id.action_edit_group).setVisible(false);
                menu.findItem(R.id.action_delete_group).setVisible(false);
            }
        } else {
            //group member
            getActivity().invalidateOptionsMenu();
            menu.findItem(R.id.action_add_member).setVisible(false);
            menu.findItem(R.id.action_member_list).setVisible(false);
            menu.findItem(R.id.action_edit_group).setVisible(false);
            menu.findItem(R.id.action_delete_group).setVisible(false);
        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_add_member:
                AddGroupMemberFragment addGroupMemberFragment = new AddGroupMemberFragment();
                Bundle bundle2 = new Bundle();
                bundle2.putString("gid", receivedCid);
                addGroupMemberFragment.setArguments(bundle2);
                fragmgr.beginTransaction().replace(R.id.content_frame, addGroupMemberFragment).commit();
                return true;

            case R.id.action_member_list:
                ManageGroupMemberFragment manageGroupMemberFragment = new ManageGroupMemberFragment();
                Bundle bundle = new Bundle();
                bundle.putString("groupCreatorId", collabCreatorId);
                manageGroupMemberFragment.setArguments(bundle);
                fragmgr.beginTransaction().replace(R.id.content_frame, manageGroupMemberFragment).commit();
                return true;

            case R.id.action_edit_group:
                CreateGroupFragment createGroupFragment = new CreateGroupFragment();
                fragmgr.beginTransaction().replace(R.id.content_frame, createGroupFragment).commit();
                return true;

            case R.id.action_delete_group:
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                LayoutInflater li = LayoutInflater.from(getContext());
                View confirmDialog = li.inflate(R.layout.giftneeddialog, null);
                Button dialogconfirm = (Button) confirmDialog.findViewById(R.id.btn_submit_mobileno);
                Button dialogcancel = (Button) confirmDialog.findViewById(R.id.btn_Cancel_mobileno);
                TextView dialogtext = (TextView) confirmDialog.findViewById(R.id.txtgiftneeddialog);
                dialogtext.setText(getResources().getString(R.string.delete_grp_msg));
                alert.setView(confirmDialog);
                alert.setCancelable(false);
                alertDialog = alert.create();
                alertDialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
                dialogconfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // call delete group api
                        if (!(Validation.isOnline(getActivity()))) {
                            ToastPopUp.show(getActivity(), getString(R.string.network_validation));
                        } else {
                            deleteCollab(receivedCid, strUser_ID);
                        }
                        alertDialog.dismiss();
                    }
                });
                dialogcancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                return true;

            case R.id.action_group_info:
                GroupInfoFragment groupInfoFragment = new GroupInfoFragment();
                fragmgr.beginTransaction().replace(R.id.content_frame, groupInfoFragment).commit();
                return true;

            case R.id.action_exit_group:
                AlertDialog.Builder alertexit = new AlertDialog.Builder(getContext());
                LayoutInflater lin = LayoutInflater.from(getContext());
                View confirmDialog1 = lin.inflate(R.layout.giftneeddialog, null);
                Button dialogconfirm1 = (Button) confirmDialog1.findViewById(R.id.btn_submit_mobileno);
                Button dialogcancel1 = (Button) confirmDialog1.findViewById(R.id.btn_Cancel_mobileno);
                TextView dialogtext1 = (TextView) confirmDialog1.findViewById(R.id.txtgiftneeddialog);
                dialogtext1.setText(getResources().getString(R.string.exit_grp_msg));
                alertexit.setView(confirmDialog1);
                alertexit.setCancelable(false);
                alertDialog = alertexit.create();
                alertDialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
                dialogconfirm1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                         call exit group api
                        if (!(Validation.isOnline(getActivity()))) {
                            ToastPopUp.show(getActivity(), getString(R.string.network_validation));
                        } else {
                            exitCollab(receivedCid, strUser_ID);
                        }
                        alertDialog.dismiss();
                    }
                });
                dialogcancel1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //---------------------getting group info from server-----------------------------------------------
    public void collabInfo(String user_id, String group_id) {
        mDialog.setConfiguration(new ArcConfiguration(getContext()));
        mDialog.show();
        mDialog.setCancelable(false);
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        CollabInfoInterface service = retrofit.create(CollabInfoInterface.class);
        Call<List<GroupInfoPOJO>> call = service.sendData(user_id, group_id);
        Log.d("clbinfo_input_params", user_id + ":" + group_id);
        call.enqueue(new Callback<List<GroupInfoPOJO>>() {
            @Override
            public void onResponse(Response<List<GroupInfoPOJO>> response, Retrofit retrofit) {
                mDialog.dismiss();
                Log.d("responsecollabinfo", "" + response.body());
                try {
                    List<GroupInfoPOJO> groupInfoPOJO = response.body();
                    int isblock = 0;
                    try {
                        isblock = groupInfoPOJO.get(0).getIsBlocked();
                    } catch (Exception e) {
                        isblock = 0;
                    }
                    if (isblock == 1) {
                        mDialog.dismiss();
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
//                        groupName.setText(groupInfoPOJO.get(0).getGroup_name());
                        admin_ids = groupInfoPOJO.get(0).getAdmin_ids();
                        Log.d("admin_ids", "" + admin_ids);
                        if (!admin_ids.equals("")) {
                            if (admin_ids.contains(",")) {
                                Collections.addAll(split, admin_ids.split(","));
                                Log.d("split_admin_ids", "" + split.toString());
                            } else {
                                split.add(admin_ids);
                                Log.d("split_admin_id", "" + split.toString());
                            }
                        }

                        collabCreatorId = groupInfoPOJO.get(0).getCreator_id();
//                        createdBy.setText("Created by - " + groupInfoPOJO.get(0).getCreator_name());
//                        createdDate.setText("Created date - " + groupInfoPOJO.get(0).getCreate_date());
//                        if (groupInfoPOJO.get(0).getGroup_desc().length() == 0) {
//                            groupDesc.setVisibility(View.GONE);
//                        } else {
//                            groupDesc.setVisibility(View.VISIBLE);
//                            groupDesc.setText(groupInfoPOJO.get(0).getGroup_desc());
//                        }
                        sessionManager.createGroupDetails("", receivedCid, groupInfoPOJO.get(0).getGroup_name(), groupInfoPOJO.get(0).getGroup_desc(), groupInfoPOJO.get(0).getGroup_image());
                    }
                } catch (Exception e) {
                    mDialog.dismiss();
                    Log.d("responsecollabinfo", "" + e.getMessage());
                    StringWriter writer = new StringWriter();
                    e.printStackTrace(new PrintWriter(writer));
                    Bugreport bg = new Bugreport();
                    bg.sendbug(writer.toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                mDialog.dismiss();
                Log.d("responsecollabinfo", "" + t.getMessage());
                ToastPopUp.show(myContext, getString(R.string.server_response_error));
            }
        });
    }

    //---------------------group delete only for group creator-----------------------------------------------
    public void deleteCollab(final String groupid, String user_id) {
        mDialog.setConfiguration(new ArcConfiguration(getContext()));
        mDialog.show();
        mDialog.setCancelable(false);
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        DeleteGroupInterface service = retrofit.create(DeleteGroupInterface.class);
        Call<GroupResponseStatus> call = service.sendData(groupid, user_id);
        call.enqueue(new Callback<GroupResponseStatus>() {
            @Override
            public void onResponse(Response<GroupResponseStatus> response, Retrofit retrofit) {
                mDialog.dismiss();
                Log.d("responsegroup", "" + response.body());
                try {
                    GroupResponseStatus groupResponseStatus = response.body();
                    int isblock = 0;
                    try {
                        isblock = groupResponseStatus.getIsBlocked();
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
                        if (groupResponseStatus.getStatus() == 1) {
                            Toast.makeText(getContext(), getResources().getString(R.string.group_deleted), Toast.LENGTH_SHORT).show();
                            databaseAccess.Delete_Group(groupid);

                            //delete channel from sendbird. Concat with GRP for Group and CLB for Collaboration
                            String channelName = receivedCname + " - CLB" + groupid;
                            filterGroupChannel(channelName); //fetch data based on given club name
                            callDeleteGrpChannels(fetchedChannelUrl); //will delete channels based on certain url of grp

                            //move to group list
                            GroupCollabFrag groupCollabFrag = new GroupCollabFrag();
                            fragmgr.beginTransaction().replace(R.id.content_frame, groupCollabFrag).commit();
                        } else if (groupResponseStatus.getStatus() == 0) {
                            Toast.makeText(getContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    mDialog.dismiss();
                    Log.d("responsegroup", "" + e.getMessage());
                    StringWriter writer = new StringWriter();
                    e.printStackTrace(new PrintWriter(writer));
                    Bugreport bg = new Bugreport();
                    bg.sendbug(writer.toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                mDialog.dismiss();
                Log.d("responsegroup", "" + t.getMessage());
                ToastPopUp.show(myContext, getString(R.string.server_response_error));
            }
        });
    }

    //---------------------exit group only for group member or group admin-----------------------------------------------
    public void exitCollab(final String groupid, final String user_id) {
        mDialog.setConfiguration(new ArcConfiguration(getContext()));
        mDialog.show();
        mDialog.setCancelable(false);
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        ExitGroupInterface service = retrofit.create(ExitGroupInterface.class);
        Call<GroupResponseStatus> call = service.sendData(groupid, user_id);
        call.enqueue(new Callback<GroupResponseStatus>() {
            @Override
            public void onResponse(Response<GroupResponseStatus> response, Retrofit retrofit) {
                mDialog.dismiss();
                Log.d("exit_responsegroup", "" + response.body());
                try {
                    GroupResponseStatus groupResponseStatus = response.body();
                    int isblock = 0;
                    try {
                        isblock = groupResponseStatus.getIsBlocked();
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
                        if (groupResponseStatus.getStatus() == 1) {
                            Toast.makeText(getContext(), getResources().getString(R.string.exit_group_msg), Toast.LENGTH_SHORT).show();
                            databaseAccess.Delete_Group(groupid);

                            //remove member from sendbird channel. Concat with GRP for Group and CLB for Collaboration
                            String channel_name = receivedCname + " - CLB" + groupid;
                            if (lstGetChannelsList.size() != 0) {
                                for (int i = 0; i < lstGetChannelsList.size(); i++) {
                                    if (lstGetChannelsList.get(i).getmChannelName().equals(channel_name)) {
                                        strSelectedChannelUrl = lstGetChannelsList.get(i).getmUrl().toString();
                                    }
                                }

                                if (lstCurrentMembersInfo != null && lstCurrentMembersInfo.size() != 0) {
                                    for (final String strUserId : lstCurrentMembersInfo) {
                                        if (strUserId.equals(user_id)) {
                                            lstGetSelectedMemberId.add(strUserId); //add data to selected member string
                                            lstUsersForRemove.add(strUserId);
                                        }
                                    }
                                } else {
//                                    Toast.makeText(getContext(), "Member(s) is not available.Please Login Again", Toast.LENGTH_SHORT).show();
                                }
                                if (lstUsersForRemove.size() != 0) {
                                    model_obj = new RemoveUserFromClub();
                                    model_obj.setUserIds(lstUsersForRemove);
                                }
                                callUpdateSendBird(strSelectedChannelUrl, model_obj);
                            }

                            //move to group list
                            GroupCollabFrag groupCollabFrag = new GroupCollabFrag();
                            fragmgr.beginTransaction().replace(R.id.content_frame, groupCollabFrag).commit();
                        } else if (groupResponseStatus.getStatus() == 0) {
                            Toast.makeText(getContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    mDialog.dismiss();
                    Log.d("exit_responsegroup", "" + e.getMessage());
                    StringWriter writer = new StringWriter();
                    e.printStackTrace(new PrintWriter(writer));
                    Bugreport bg = new Bugreport();
                    bg.sendbug(writer.toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                mDialog.dismiss();
                Log.d("exit_responsegroup", "" + t.getMessage());
                ToastPopUp.show(myContext, getString(R.string.server_response_error));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                    GroupCollabFrag groupCollabFrag = new GroupCollabFrag();
                    fragmgr.beginTransaction().replace(R.id.content_frame, groupCollabFrag).commit();
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

    //channel details
    public void getChannelsDetails() {
        //always use connect() along with any method of chat #phase 2 requirement 27 feb 2018 Nilesh
        SendBird.connect(strUser_ID, new SendBird.ConnectHandler() {
            @Override
            public void onConnected(User user, SendBirdException e) {
                if (e != null) {
                    // Error.
                    return;
                }
                GroupChannelListQuery channelListQuery = GroupChannel.createMyGroupChannelListQuery();
                channelListQuery.setIncludeEmpty(true);
                channelListQuery.next(new GroupChannelListQuery.GroupChannelListQueryResultHandler() {
                    @Override
                    public void onResult(List<GroupChannel> list, SendBirdException e) {
                        if (e != null) {
                            // Error.
                            return;

                        }
                        if (list != null) {
                            for (int i = 0; i < list.size(); i++) {
                                lstGetChannelsList.add(new GroupListInfo(list.get(i).getData().toString(), list.get(i).getName().toString(), list.get(i).getUrl().toString()));
                                System.out.println("ff" + list.get(i).getName());
                            }
                        }
                    }
                });
            }
        });
    }

    //***********************************transform*************************************************
    public void filterGroupChannel(String clubname) {
        System.out.println("clbname line n0 3950        " + clubname);
        if (lstGetChannelsList != null && lstGetChannelsList.size() != 0) {
            for (int i = 0; i < lstGetChannelsList.size(); i++) {
                if (lstGetChannelsList.get(i).getmChannelName().equals(clubname)) {
                    fetchedChannelUrl = lstGetChannelsList.get(i).getmUrl();
                    Log.d("UPDATE", "" + lstGetChannelsList.get(i).getmUrl());
                }
            }
        }
    }

    //==================Delete Group channel from sendbird===============================================
    public void callDeleteGrpChannels(String urlOfChannel) {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WebServices.MANI_SENDBRD_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //call interface
        DeleteChannelGroup service = retrofit.create(DeleteChannelGroup.class);
        Call<Object> call = service.deleteChannels(urlOfChannel);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Response<Object> response, Retrofit retrofit) {
                if (response.code() == 200) {
                    Log.d("DLCLUB", "Success.");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
                Log.d("DLCLUB", "Not Success." + t.getMessage());
            }
        });
    }

    public void callUpdateSendBird(String urlOfChannel, RemoveUserFromClub model_obj) {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WebServices.MANI_SENDBRD_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JSONObject jsonOBJ = new JSONObject();
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
        //call interface
        RemoveMemberFromChannel service = retrofit.create(RemoveMemberFromChannel.class);
        Log.d("JOBJ", "" + jsonOBJ);
        Call<ResponseBody> call = service.removeMembers(urlOfChannel, model_obj);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                if (response.code() == 200) {
                    Log.d("UPCLUB", "Success.");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
                Log.d("UPCLUB", "Not Success." + t.getMessage());
            }
        });
    }

    private void loadNextUserList() {
        SendBird.connect(strUser_ID, new SendBird.ConnectHandler() {
            @Override
            public void onConnected(User user, SendBirdException e) {
                if (e != null) {
                    // Error.
                    return;
                }
                mUserListQuery = SendBird.createUserListQuery();
                // mUserListQuery.setLimit(100);
                mUserListQuery.next(new UserListQuery.UserListQueryResultHandler() {
                    @Override
                    public void onResult(List<User> list, SendBirdException e) {
                        if (e != null) {
                            return;
                        }
                        if (list != null) {
                            for (int i = 0; i < list.size(); i++) {
                                lstCurrentMembersInfo.add(list.get(i).getUserId().toString());
                                System.out.println("Data ciming from" + list.get(i).getUserId().toString());
                            }
                        }
                    }
                });
            }
        });
    }
}