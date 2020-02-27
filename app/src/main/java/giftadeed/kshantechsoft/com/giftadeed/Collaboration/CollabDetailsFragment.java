/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.Collaboration;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
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

import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import giftadeed.kshantechsoft.com.giftadeed.Bug.Bugreport;
import giftadeed.kshantechsoft.com.giftadeed.Group.GroupCollabFrag;
import giftadeed.kshantechsoft.com.giftadeed.Group.GroupListInfo;
import giftadeed.kshantechsoft.com.giftadeed.Login.LoginActivity;
import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.SendBirdChat.Interfaces.DeleteChannelGroup;
import giftadeed.kshantechsoft.com.giftadeed.SendBirdChat.Interfaces.RemoveMemberFromChannel;
import giftadeed.kshantechsoft.com.giftadeed.SendBirdChat.Pojo.RemoveUserFromClub;
import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.TagaNeed;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.RowData;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsActivity;
import giftadeed.kshantechsoft.com.giftadeed.Utils.SharedPrefManager;
import giftadeed.kshantechsoft.com.giftadeed.Utils.ToastPopUp;
import giftadeed.kshantechsoft.com.giftadeed.Utils.Validation;
import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class CollabDetailsFragment extends Fragment {
    View rootview;
    RecyclerView recyclerView;
    List<RowData> item_list;
    FragmentActivity myContext;
    static FragmentManager fragmgr;
    SimpleArcDialog mDialog;
    SharedPrefManager sharedPrefManager;
    TextView collabName, collabDesc, collabGroup, collabStartDate, memberCount, noRecordsFound;
    LinearLayout countLayout;
    String strUser_ID;
    private AlertDialog alertDialog;
    String receivedCid = "", receivedCname = "";
    String collabCreatorId = "", admin_ids = "";
    ArrayList<String> split = new ArrayList<String>();
    int isMember = 1;
    float radius_set;
    private UserListQuery mUserListQuery;
    private List<String> lstCurrentMembersInfo = new ArrayList<>();
    private List<GroupListInfo> lstGetChannelsList = new ArrayList<>();
    private List<String> lstGetSelectedMemberId = new ArrayList<>();
    private String fetchedChannelUrl;
    private String strSelectedChannelUrl = "";
    private List<String> lstUsersForRemove = new ArrayList<>();
    RemoveUserFromClub model_obj;

    public static CollabDetailsFragment newInstance(int sectionNumber) {
        return new CollabDetailsFragment();
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.collab_details_layout, container, false);
        sharedPrefManager = new SharedPrefManager(getActivity());
        HashMap<String, String> user = sharedPrefManager.getUserDetails();
        strUser_ID = user.get(SharedPrefManager.USER_ID);
        HashMap<String, String> collab = sharedPrefManager.getSelectedColabDetails();
        receivedCid = collab.get(SharedPrefManager.COLAB_ID);
        receivedCname = collab.get(SharedPrefManager.COLAB_NAME);
        TaggedneedsActivity.updateTitle(getResources().getString(R.string.collab_details));
        TaggedneedsActivity.fragname = TagaNeed.newInstance(0);
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
        getChannelsDetails();
        loadNextUserList();
        collabName.setText(receivedCname);
        radius_set = sharedPrefManager.getradius();
        if (!(Validation.isNetworkAvailable(getActivity()))) {
            ToastPopUp.show(getActivity(), getString(R.string.network_validation));
        } else {
            collabInfo(receivedCid);
        }
        TaggedneedsActivity.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("tab", "tab2");  // tab2 for collaborations
                GroupCollabFrag groupCollabFrag = new GroupCollabFrag();
                groupCollabFrag.setArguments(bundle);
                fragmgr.beginTransaction().replace(R.id.content_frame, groupCollabFrag).commit();
            }
        });
        return rootview;
    }

    //--------------------------Initializing the UI variables--------------------------------------------
    private void init() {
        collabName = (TextView) rootview.findViewById(R.id.tv_collab_name);
        collabDesc = (TextView) rootview.findViewById(R.id.tv_collab_desc);
        collabGroup = (TextView) rootview.findViewById(R.id.tv_collab_group_name);
        collabStartDate = (TextView) rootview.findViewById(R.id.tv_collab_start_date);
        countLayout = (LinearLayout) rootview.findViewById(R.id.count_layout);
        memberCount = (TextView) rootview.findViewById(R.id.collabdetails_member_count);
        noRecordsFound = (TextView) rootview.findViewById(R.id.no_members);
        recyclerView = (RecyclerView) rootview.findViewById(R.id.collab_member_list);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
    }

    //---------------------getting collab info from server-----------------------------------------------
    public void collabInfo(String colab_id) {
        mDialog.setConfiguration(new ArcConfiguration(getContext()));
        mDialog.show();
        mDialog.setCancelable(false);
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MAIN_API_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        CollabInfoInterface service = retrofit.create(CollabInfoInterface.class);
        Call<CollabPOJO> call = service.sendData(colab_id);
        Log.d("clbinfo_input_params", "" + colab_id);
        call.enqueue(new Callback<CollabPOJO>() {
            @Override
            public void onResponse(Response<CollabPOJO> response, Retrofit retrofit) {
                mDialog.dismiss();
                Log.d("responsecollabinfo", "" + response.body());
                try {
                    CollabPOJO collabPOJO = response.body();
                    int isblock = 0;
                    try {
                        isblock = collabPOJO.getIsBlocked();
                    } catch (Exception e) {
                        isblock = 0;
                    }
                    if (isblock == 1) {
                        mDialog.dismiss();
                        FacebookSdk.sdkInitialize(getActivity());
                        Toast.makeText(getContext(), getResources().getString(R.string.block_toast), Toast.LENGTH_SHORT).show();
                        sharedPrefManager.createUserCredentialSession(null, null, null);
                        LoginManager.getInstance().logOut();
                        /*Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                                new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(Status status) {
                                        //updateUI(false);
                                    }
                                });*/

                        sharedPrefManager.set_notification_status("ON");
                        Intent loginintent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(loginintent);
                    } else {
                        int colabstatus = collabPOJO.getStatus();
                        Log.d("colabstatus", "" + colabstatus);
                        if (colabstatus == 1) {
                            collabName.setText(collabPOJO.getColabinfo().getCollabname());
                            collabDesc.setText("Description : " + collabPOJO.getColabinfo().getCollabdesc());
                            collabGroup.setText("Created by : " + collabPOJO.getColabinfo().getGroupname() + " (" + collabPOJO.getColabinfo().getUsername() + ")");
                            collabStartDate.setText("Start date : " + collabPOJO.getColabinfo().getCollabstartDate());
                            collabCreatorId = collabPOJO.getColabinfo().getUserid();
                            sharedPrefManager.createColabDetails("", receivedCid, collabPOJO.getColabinfo().getCollabname(), collabPOJO.getColabinfo().getCollabdesc(), collabPOJO.getColabinfo().getGroupid(), collabPOJO.getColabinfo().getGroupname());
                        } else if (colabstatus == 0) {
                            Toast.makeText(getContext(), collabPOJO.getErrorMsg(), Toast.LENGTH_SHORT).show();
                        }
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

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_colab_details, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (collabCreatorId.equals(strUser_ID)) {
            //collaboration creator
            getActivity().invalidateOptionsMenu();
            menu.findItem(R.id.action_exit_colab).setVisible(false);
        } else {
            //collaboration member
            getActivity().invalidateOptionsMenu();
            menu.findItem(R.id.action_colab_add_member).setVisible(false);
            menu.findItem(R.id.action_edit_colab).setVisible(false);
            menu.findItem(R.id.action_delete_colab).setVisible(false);
        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_colab_add_member:
                AddCollabMemberFragment addCollabMemberFragment = new AddCollabMemberFragment();
                fragmgr.beginTransaction().replace(R.id.content_frame, addCollabMemberFragment).commit();
                return true;

            case R.id.action_colab_member_list:
                ManageCollabMemberFragment manageCollabMemberFragment = new ManageCollabMemberFragment();
                Bundle bundle = new Bundle();
                bundle.putString("collabCreatorId", collabCreatorId);
                manageCollabMemberFragment.setArguments(bundle);
                fragmgr.beginTransaction().replace(R.id.content_frame, manageCollabMemberFragment).commit();
                return true;

            case R.id.action_edit_colab:
                CreateCollabFragment createCollabFragment = new CreateCollabFragment();
                fragmgr.beginTransaction().replace(R.id.content_frame, createCollabFragment).commit();
                return true;

            case R.id.action_delete_colab:
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                LayoutInflater li = LayoutInflater.from(getContext());
                View confirmDialog = li.inflate(R.layout.giftneeddialog, null);
                Button dialogconfirm = (Button) confirmDialog.findViewById(R.id.btn_submit_mobileno);
                Button dialogcancel = (Button) confirmDialog.findViewById(R.id.btn_Cancel_mobileno);
                TextView dialogtext = (TextView) confirmDialog.findViewById(R.id.txtgiftneeddialog);
                dialogtext.setText(getResources().getString(R.string.delete_clb_msg));
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
                        if (!(Validation.isNetworkAvailable(getActivity()))) {
                            ToastPopUp.show(getActivity(), getString(R.string.network_validation));
                        } else {
                            deleteCollab(receivedCid);
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

            case R.id.action_exit_colab:
                AlertDialog.Builder alertexit = new AlertDialog.Builder(getContext());
                LayoutInflater lin = LayoutInflater.from(getContext());
                View confirmDialog1 = lin.inflate(R.layout.giftneeddialog, null);
                Button dialogconfirm1 = (Button) confirmDialog1.findViewById(R.id.btn_submit_mobileno);
                Button dialogcancel1 = (Button) confirmDialog1.findViewById(R.id.btn_Cancel_mobileno);
                TextView dialogtext1 = (TextView) confirmDialog1.findViewById(R.id.txtgiftneeddialog);
                dialogtext1.setText(getResources().getString(R.string.exit_clb_msg));
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
                        if (!(Validation.isNetworkAvailable(getActivity()))) {
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

    //---------------------group delete only for group creator-----------------------------------------------
    public void deleteCollab(final String collabid) {
        mDialog.setConfiguration(new ArcConfiguration(getContext()));
        mDialog.show();
        mDialog.setCancelable(false);
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MAIN_API_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        DeleteCollabInterface service = retrofit.create(DeleteCollabInterface.class);
        Call<CollabResponseStatus> call = service.sendData(collabid);
        call.enqueue(new Callback<CollabResponseStatus>() {
            @Override
            public void onResponse(Response<CollabResponseStatus> response, Retrofit retrofit) {
                mDialog.dismiss();
                Log.d("responsecollab", "" + response.body());
                try {
                    CollabResponseStatus collabResponseStatus = response.body();
                    int isblock = 0;
                    try {
                        isblock = collabResponseStatus.getIsBlocked();
                    } catch (Exception e) {
                        isblock = 0;
                    }
                    if (isblock == 1) {
                        FacebookSdk.sdkInitialize(getActivity());
                        Toast.makeText(getContext(), getResources().getString(R.string.block_toast), Toast.LENGTH_SHORT).show();
                        sharedPrefManager.createUserCredentialSession(null, null, null);
                        LoginManager.getInstance().logOut();
                        /*Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                                new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(Status status) {
                                        //updateUI(false);
                                    }
                                });*/

                        sharedPrefManager.set_notification_status("ON");
                        Intent loginintent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(loginintent);
                    } else {
                        if (collabResponseStatus.getStatus() == 1) {
                            Toast.makeText(getContext(), collabResponseStatus.getSuccessMsg(), Toast.LENGTH_SHORT).show();

                            //delete channel from sendbird. Concat with GRP for Group and CLB for Collaboration
                            String channelName = receivedCname + " - CLB" + collabid;
                            filterGroupChannel(channelName); //fetch data based on given club name
                            callDeleteGrpChannels(fetchedChannelUrl); //will delete channels based on certain url of grp

                            // move to collaboration list fragment
                            Bundle bundle = new Bundle();
                            bundle.putString("tab", "tab2");  // tab2 for collaborations
                            GroupCollabFrag groupCollabFrag = new GroupCollabFrag();
                            groupCollabFrag.setArguments(bundle);
                            fragmgr.beginTransaction().replace(R.id.content_frame, groupCollabFrag).commit();
                        } else if (collabResponseStatus.getStatus() == 0) {
                            Toast.makeText(getContext(), collabResponseStatus.getErrorMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    mDialog.dismiss();
                    Log.d("responsecollab", "" + e.getMessage());
                    StringWriter writer = new StringWriter();
                    e.printStackTrace(new PrintWriter(writer));
                    Bugreport bg = new Bugreport();
                    bg.sendbug(writer.toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                mDialog.dismiss();
                Log.d("responsecollab", "" + t.getMessage());
                ToastPopUp.show(myContext, getString(R.string.server_response_error));
            }
        });
    }

    //---------------------exit group only for group member or group admin-----------------------------------------------
    public void exitCollab(final String collabid, final String user_id) {
        mDialog.setConfiguration(new ArcConfiguration(getContext()));
        mDialog.show();
        mDialog.setCancelable(false);
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MAIN_API_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        RemoveCollabMemberInterface service = retrofit.create(RemoveCollabMemberInterface.class);
        Call<CollabResponseStatus> call = service.sendData(collabid, user_id);
        Log.d("input_exit", collabid + ":" + user_id);
        call.enqueue(new Callback<CollabResponseStatus>() {
            @Override
            public void onResponse(Response<CollabResponseStatus> response, Retrofit retrofit) {
                mDialog.dismiss();
                Log.d("exit_responsegroup", "" + response.body());
                try {
                    CollabResponseStatus collabResponseStatus = response.body();
                    int isblock = 0;
                    try {
                        isblock = collabResponseStatus.getIsBlocked();
                    } catch (Exception e) {
                        isblock = 0;
                    }
                    if (isblock == 1) {
                        FacebookSdk.sdkInitialize(getActivity());
                        Toast.makeText(getContext(), getResources().getString(R.string.block_toast), Toast.LENGTH_SHORT).show();
                        sharedPrefManager.createUserCredentialSession(null, null, null);
                        LoginManager.getInstance().logOut();
                        /*Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                                new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(Status status) {
                                        //updateUI(false);
                                    }
                                });*/

                        sharedPrefManager.set_notification_status("ON");
                        Intent loginintent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(loginintent);
                    } else {
                        if (collabResponseStatus.getStatus() == 1) {
                            Toast.makeText(getContext(), getResources().getString(R.string.exit_group_msg), Toast.LENGTH_SHORT).show();

                            //remove member from sendbird channel. Concat with GRP for Group and CLB for Collaboration
                            String channel_name = receivedCname + " - CLB" + collabid;
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
                            Bundle bundle = new Bundle();
                            bundle.putString("tab", "tab2");  // tab2 for collaborations
                            GroupCollabFrag groupCollabFrag = new GroupCollabFrag();
                            groupCollabFrag.setArguments(bundle);
                            fragmgr.beginTransaction().replace(R.id.content_frame, groupCollabFrag).commit();
                        } else if (collabResponseStatus.getStatus() == 0) {
                            Toast.makeText(getContext(), collabResponseStatus.getErrorMsg(), Toast.LENGTH_SHORT).show();
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
                    Bundle bundle = new Bundle();
                    bundle.putString("tab", "tab2");  // tab2 for collaborations
                    GroupCollabFrag groupCollabFrag = new GroupCollabFrag();
                    groupCollabFrag.setArguments(bundle);
                    fragmgr.beginTransaction().replace(R.id.content_frame, groupCollabFrag).commit();
                    return true;
                }
                return false;
            }
        });
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
        Log.d("remove_sb_member_params", "" + urlOfChannel + ":" + model_obj);
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
