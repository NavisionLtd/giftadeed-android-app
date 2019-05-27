package giftadeed.kshantechsoft.com.giftadeed.Group;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.leo.simplearcloader.SimpleArcDialog;
import com.sendbird.android.GroupChannel;
import com.sendbird.android.GroupChannelListQuery;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;
import com.sendbird.android.UserListQuery;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.ResponseBody;

import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import giftadeed.kshantechsoft.com.giftadeed.Bug.Bugreport;
import giftadeed.kshantechsoft.com.giftadeed.Login.LoginActivity;
import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.SendBirdChat.Interfaces.RemoveMemberFromChannel;
import giftadeed.kshantechsoft.com.giftadeed.SendBirdChat.Pojo.RemoveUserFromClub;
import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.TagaNeed;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsActivity;
import giftadeed.kshantechsoft.com.giftadeed.Utils.DBGAD;
import giftadeed.kshantechsoft.com.giftadeed.Utils.SessionManager;
import giftadeed.kshantechsoft.com.giftadeed.Utils.ToastPopUp;
import giftadeed.kshantechsoft.com.giftadeed.Utils.Validation;
import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class ManageGroupMemberFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, GoogleApiClient.OnConnectionFailedListener {
    View rootview;
    FragmentActivity myContext;
    static FragmentManager fragmgr;
    SimpleArcDialog mDialog;
    SessionManager sessionManager;
    String strUser_ID;
    String receivedGid = "", receivedGname = "";
    EditText editsearch;
    TextView noGroupMember, memberCount;
    ListView listView;
    FilterGroupMemberAdapter adapter;
    ArrayList<GroupMember> groupMemberList;
    private AlertDialog alertDialog;
    private GoogleApiClient mGoogleApiClient;
    String member_count, groupCreatorId, clickedMemberId;
    SwipeRefreshLayout swipeRefreshLayout;
    private UserListQuery mUserListQuery;
    private List<String> lstCurrentMembersInfo = new ArrayList<>();
    private List<GroupListInfo> lstGetChannelsList = new ArrayList<>();
    private List<String> lstGetSelectedMemberId = new ArrayList<>();
    private String strSelectedChannelUrl = "";
    private  List<String> lstUsersForRemove = new ArrayList<>();
    RemoveUserFromClub model_obj;

    public static ManageGroupMemberFragment newInstance(int sectionNumber) {
        ManageGroupMemberFragment fragment = new ManageGroupMemberFragment();
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.manage_group_member_layout, container, false);
        sessionManager = new SessionManager(getActivity());
        TaggedneedsActivity.updateTitle(getResources().getString(R.string.member_list));
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
        mGoogleApiClient = ((TaggedneedsActivity) getActivity()).mGoogleApiClient;
        HashMap<String, String> user = sessionManager.getUserDetails();
        strUser_ID = user.get(sessionManager.USER_ID);
        HashMap<String, String> group = sessionManager.getSelectedGroupDetails();
        receivedGid = group.get(sessionManager.GROUP_ID);
        receivedGname = group.get(sessionManager.GROUP_NAME);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            groupCreatorId = bundle.getString("groupCreatorId");
        }

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
                                            listView.setAdapter(null);
                                            getMemberListForAdmin(strUser_ID, receivedGid, "1");
                                            getChannelsDetails();
                                            loadNextUserList();
                                        }
                                    }
                                }
        );

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                clickedMemberId = groupMemberList.get(i).getMemberid();
                if (groupMemberList.get(i).getPrivilege().equals("M")) {
                    // group member
                    showDialogM();
                } else if (groupMemberList.get(i).getPrivilege().equals("C")) {
                    // no options to select for group creator
                } else if (groupMemberList.get(i).getPrivilege().equals("A")) {
                    // group admin
                    showDialogA();
                }
            }
        });

        // Capture Text in EditText
        editsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = editsearch.getText().toString().toLowerCase(Locale.getDefault());
                adapter.filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
            }
        });

        TaggedneedsActivity.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroupDetailsFragment groupDetailsFragment = new GroupDetailsFragment();
                fragmgr.beginTransaction().replace(R.id.content_frame, groupDetailsFragment).commit();
            }
        });
        return rootview;
    }

    //--------------------------Initilizing the UI variables--------------------------------------------
    private void init() {
        editsearch = (EditText) rootview.findViewById(R.id.et_search_member);
        noGroupMember = (TextView) rootview.findViewById(R.id.tv_no_group_members);
        memberCount = (TextView) rootview.findViewById(R.id.tv_member_count);
        swipeRefreshLayout = (SwipeRefreshLayout) rootview.findViewById(R.id.manage_swipe_refresh_layout);
        listView = (ListView) rootview.findViewById(R.id.listview_groupmembers);
    }

    private void showDialogM() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle(getResources().getString(R.string.select_action));
        String[] optionDialogItems = {
                getResources().getString(R.string.remove_member),
                getResources().getString(R.string.make_admin)};
        dialog.setItems(optionDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                                LayoutInflater li = LayoutInflater.from(getContext());
                                View confirmDialog = li.inflate(R.layout.giftneeddialog, null);
                                Button dialogconfirm = (Button) confirmDialog.findViewById(R.id.btn_submit_mobileno);
                                Button dialogcancel = (Button) confirmDialog.findViewById(R.id.btn_Cancel_mobileno);
                                TextView dialogtext = (TextView) confirmDialog.findViewById(R.id.txtgiftneeddialog);
                                dialogtext.setText(getResources().getString(R.string.remove_member_dialog));
                                alert.setView(confirmDialog);
                                alert.setCancelable(false);
                                alertDialog = alert.create();
                                alertDialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
                                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                alertDialog.show();
                                dialogconfirm.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
//                                        Toast.makeText(getContext(), "Member removed successfully", Toast.LENGTH_SHORT).show();

                                        // call remove member api
                                        if (!(Validation.isOnline(getActivity()))) {
                                            ToastPopUp.show(getActivity(), getString(R.string.network_validation));
                                        } else {
                                            removeMember(strUser_ID, clickedMemberId, receivedGid);
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
                                break;
                            case 1:
                                if (!(Validation.isOnline(getActivity()))) {
                                    ToastPopUp.show(getActivity(), getString(R.string.network_validation));
                                } else {
                                    makeGroupAdmin(strUser_ID, clickedMemberId, receivedGid);
                                }
                                break;
                        }
                    }
                });
        dialog.show();
    }

    private void showDialogA() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle(getResources().getString(R.string.select_action));
        String[] optionDialogItems = {
                getResources().getString(R.string.remove_member),
                getResources().getString(R.string.dismiss_admin)};
        dialog.setItems(optionDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                                LayoutInflater li = LayoutInflater.from(getContext());
                                View confirmDialog = li.inflate(R.layout.giftneeddialog, null);
                                Button dialogconfirm = (Button) confirmDialog.findViewById(R.id.btn_submit_mobileno);
                                Button dialogcancel = (Button) confirmDialog.findViewById(R.id.btn_Cancel_mobileno);
                                TextView dialogtext = (TextView) confirmDialog.findViewById(R.id.txtgiftneeddialog);
                                dialogtext.setText(getResources().getString(R.string.remove_member_dialog));
                                alert.setView(confirmDialog);
                                alert.setCancelable(false);
                                alertDialog = alert.create();
                                alertDialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
                                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                alertDialog.show();
                                dialogconfirm.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
//                                        Toast.makeText(getContext(), "Member removed successfully", Toast.LENGTH_SHORT).show();

                                        // call remove member api
                                        if (!(Validation.isOnline(getActivity()))) {
                                            ToastPopUp.show(getActivity(), getString(R.string.network_validation));
                                        } else {
                                            removeMember(strUser_ID, clickedMemberId, receivedGid);
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
                                break;
                            case 1:
                                if (!(Validation.isOnline(getActivity()))) {
                                    ToastPopUp.show(getActivity(), getString(R.string.network_validation));
                                } else {
                                    dismissAsAdmin(strUser_ID, clickedMemberId, receivedGid);
                                }
                                break;
                        }
                    }
                });
        dialog.show();
    }

    private void removeMember(String userid, final String memberid, final String groupid) {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        RemoveMemberInterface service = retrofit.create(RemoveMemberInterface.class);
        Call<GroupResponseStatus> call = service.sendData(userid, memberid, groupid);
        Log.d("remove_mem_inputparams", userid + "," + memberid + "," + groupid);
        call.enqueue(new Callback<GroupResponseStatus>() {
            @Override
            public void onResponse(Response<GroupResponseStatus> response, Retrofit retrofit) {
                Log.d("response_remove_member", "" + response.body());
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
                        if (!(Validation.isOnline(getActivity()))) {
                            ToastPopUp.show(getActivity(), getString(R.string.network_validation));
                        } else {
                            if (groupResponseStatus.getStatus() == 1) {
                                Toast.makeText(getContext(), getResources().getString(R.string.member_removed), Toast.LENGTH_SHORT).show();

                                //remove member from sendbird channel. Concat with GRP for Group and CLB for Collaboration
                                String channel_name = receivedGname + " - GRP" + receivedGid;
                                if (lstGetChannelsList.size() != 0) {
                                    for (int i = 0; i < lstGetChannelsList.size(); i++) {
                                        if (lstGetChannelsList.get(i).getmChannelName().equals(channel_name)) {
                                            strSelectedChannelUrl = lstGetChannelsList.get(i).getmUrl().toString();
                                        }
                                    }

                                    if (lstCurrentMembersInfo != null && lstCurrentMembersInfo.size() != 0) {
                                        for (final String strUserId : lstCurrentMembersInfo) {
                                            if (strUserId.equals(memberid)) {
                                                lstGetSelectedMemberId.add(strUserId); //add data to selected member string
                                                lstUsersForRemove.add(strUserId);
                                            }
                                        }
                                    } else {
//                                        Toast.makeText(getContext(), "Member(s) is not available.Please Login Again", Toast.LENGTH_SHORT).show();
                                    }
                                    if (lstUsersForRemove.size() != 0) {
                                        model_obj = new RemoveUserFromClub();
                                        model_obj.setUserIds(lstUsersForRemove);
                                    }
                                    callUpdateSendBird(strSelectedChannelUrl, model_obj);
                                }
                                editsearch.setText("");
                                getMemberListForAdmin(strUser_ID, receivedGid, "1");
                            } else if (groupResponseStatus.getStatus() == 0) {
                                Toast.makeText(getContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.d("response_remove_member", "" + e.getMessage());
                    StringWriter writer = new StringWriter();
                    e.printStackTrace(new PrintWriter(writer));
                    Bugreport bg = new Bugreport();
                    bg.sendbug(writer.toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("response_remove_member", "" + t.getMessage());
                ToastPopUp.show(myContext, getString(R.string.server_response_error));
            }
        });
    }

    private void makeGroupAdmin(String userid, String memberid, String groupid) {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        AssignAdminInterface service = retrofit.create(AssignAdminInterface.class);
        Call<GroupResponseStatus> call = service.sendData(userid, memberid, groupid);
        call.enqueue(new Callback<GroupResponseStatus>() {
            @Override
            public void onResponse(Response<GroupResponseStatus> response, Retrofit retrofit) {
                Log.d("response_assign_admin", "" + response.body());
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
                        if (!(Validation.isOnline(getActivity()))) {
                            ToastPopUp.show(getActivity(), getString(R.string.network_validation));
                        } else {
                            if (groupResponseStatus.getStatus() == 1) {
                                Toast.makeText(getContext(), getResources().getString(R.string.admin_assigned), Toast.LENGTH_SHORT).show();
                                editsearch.setText("");
                                getMemberListForAdmin(strUser_ID, receivedGid, "1");
                            } else if (groupResponseStatus.getStatus() == 0) {
                                Toast.makeText(getContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.d("response_assign_admin", "" + e.getMessage());
                    StringWriter writer = new StringWriter();
                    e.printStackTrace(new PrintWriter(writer));
                    Bugreport bg = new Bugreport();
                    bg.sendbug(writer.toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("response_assign_admin", "" + t.getMessage());
                ToastPopUp.show(myContext, getString(R.string.server_response_error));
            }
        });
    }

    private void dismissAsAdmin(String userid, String memberid, String groupid) {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        DismissAdminInterface service = retrofit.create(DismissAdminInterface.class);
        Call<GroupResponseStatus> call = service.sendData(userid, memberid, groupid);
        call.enqueue(new Callback<GroupResponseStatus>() {
            @Override
            public void onResponse(Response<GroupResponseStatus> response, Retrofit retrofit) {
                Log.d("response_dismiss_admin", "" + response.body());
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
                        if (!(Validation.isOnline(getActivity()))) {
                            ToastPopUp.show(getActivity(), getString(R.string.network_validation));
                        } else {
                            if (groupResponseStatus.getStatus() == 1) {
                                Toast.makeText(getContext(), getResources().getString(R.string.admin_dismissed), Toast.LENGTH_SHORT).show();
                                editsearch.setText("");
                                getMemberListForAdmin(strUser_ID, receivedGid, "1");
                            } else if (groupResponseStatus.getStatus() == 0) {
                                Toast.makeText(getContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.d("response_dismiss_admin", "" + e.getMessage());
                    StringWriter writer = new StringWriter();
                    e.printStackTrace(new PrintWriter(writer));
                    Bugreport bg = new Bugreport();
                    bg.sendbug(writer.toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("response_dismiss_admin", "" + t.getMessage());
                ToastPopUp.show(myContext, getString(R.string.server_response_error));
            }
        });
    }

    //---------------------get member list for admin from server-----------------------------------------------
    public void getMemberListForAdmin(String user_id, String group_id, String role) {
        groupMemberList = new ArrayList<GroupMember>();
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        MemberListInterface service = retrofit.create(MemberListInterface.class);
        Call<Memberlist> call = service.sendData(user_id, group_id, role);
        Log.d("input_params", user_id + ":" + group_id + ":" + role);
        call.enqueue(new Callback<Memberlist>() {
            @Override
            public void onResponse(Response<Memberlist> response, Retrofit retrofit) {
                swipeRefreshLayout.setRefreshing(false);
                Log.d("response_memberlist", "" + response.body());
                try {
                    Memberlist memberDetails = response.body();
                    int isblock = 0;
                    try {
                        isblock = memberDetails.getIsBlocked();
                    } catch (Exception e) {
                        isblock = 0;
                    }
                    if (isblock == 1) {
                        swipeRefreshLayout.setRefreshing(false);
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
                        member_count = memberDetails.getTotalMembers();
                        int size = memberDetails.getMemlist().size();
                        if (size > 0) {
                            for (int i = 0; i < size; i++) {
                                GroupMember member = new GroupMember();
                                member.setMemberid(memberDetails.getMemlist().get(i).getMemberid());
                                member.setMembername(memberDetails.getMemlist().get(i).getMembername());
                                member.setPrivilege(memberDetails.getMemlist().get(i).getPrivilege());
                                member.setMemberemail(memberDetails.getMemlist().get(i).getMemberemail());
                                groupMemberList.add(member);
                            }

                            if (groupMemberList.size() > 0) {
                                memberCount.setVisibility(View.VISIBLE);
                                memberCount.setText(member_count);
                                noGroupMember.setText(getResources().getString(R.string.total_members));
                                editsearch.setVisibility(View.VISIBLE);
                                listView.setVisibility(View.VISIBLE);
                                adapter = new FilterGroupMemberAdapter(myContext, groupMemberList);
                                listView.setAdapter(adapter);
                            } else {
                                listView.setVisibility(View.GONE);
                                memberCount.setVisibility(View.GONE);
                                editsearch.setVisibility(View.GONE);
                                noGroupMember.setText(getResources().getString(R.string.no_members));
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.d("response_memberlist", "" + e.getMessage());
                    StringWriter writer = new StringWriter();
                    e.printStackTrace(new PrintWriter(writer));
                    Bugreport bg = new Bugreport();
                    bg.sendbug(writer.toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Log.d("response_memberlist", "" + t.getMessage());
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
                    GroupDetailsFragment groupDetailsFragment = new GroupDetailsFragment();
                    fragmgr.beginTransaction().replace(R.id.content_frame, groupDetailsFragment).commit();
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

    @Override
    public void onRefresh() {
        if (!(Validation.isOnline(getActivity()))) {
            swipeRefreshLayout.setRefreshing(false);
            ToastPopUp.show(getActivity(), getString(R.string.network_validation));
        } else {
            swipeRefreshLayout.setRefreshing(true);
            listView.setAdapter(null);
            getMemberListForAdmin(strUser_ID, receivedGid, "1");
            getChannelsDetails();
            loadNextUserList();
        }
    }

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
                            }
                        }
                    }
                });
            }
        });
    }

    private void loadNextUserList() {
        SendBird.connect(strUser_ID, new SendBird.ConnectHandler() {
            @Override
            public void onConnected(User user, SendBirdException e) {
                if (e != null) {
                    return;
                }
                mUserListQuery = SendBird.createUserListQuery();
                // mUserListQuery.setLimit(100);
                mUserListQuery.next(new UserListQuery.UserListQueryResultHandler() {
                    @Override
                    public void onResult(List<User> list, SendBirdException e) {
                        if (e != null) {
                            // Error!
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
}
