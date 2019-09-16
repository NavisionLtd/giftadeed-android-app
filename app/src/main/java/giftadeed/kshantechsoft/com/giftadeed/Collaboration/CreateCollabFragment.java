package giftadeed.kshantechsoft.com.giftadeed.Collaboration;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.gson.JsonObject;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.sendbird.android.GroupChannel;
import com.sendbird.android.GroupChannelListQuery;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;
import com.squareup.okhttp.OkHttpClient;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import giftadeed.kshantechsoft.com.giftadeed.Bug.Bugreport;
import giftadeed.kshantechsoft.com.giftadeed.Group.GroupCollabFrag;
import giftadeed.kshantechsoft.com.giftadeed.Group.GroupListInfo;
import giftadeed.kshantechsoft.com.giftadeed.Group.GroupPOJO;
import giftadeed.kshantechsoft.com.giftadeed.Login.LoginActivity;
import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.Resources.OwnedGroupsAdapter;
import giftadeed.kshantechsoft.com.giftadeed.Resources.OwnedGroupsInterface;
import giftadeed.kshantechsoft.com.giftadeed.SendBirdChat.Interfaces.UpdateGrpChannel;
import giftadeed.kshantechsoft.com.giftadeed.SendBirdChat.Pojo.ModalSendBrdUpdate;
import giftadeed.kshantechsoft.com.giftadeed.SendBirdChat.utils.PreferenceUtils;
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

public class CreateCollabFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener {
    View rootview;
    FragmentActivity myContext;
    static FragmentManager fragmgr;
    private SimpleArcDialog mDialog;
    private EditText selectGroup, collabName, collabDesc;
    private Button btnCreateCollab;
    private SessionManager sessionManager;
    private String strUser_ID;
    private String receivedCollabid = "", receivedCollabname = "", receivedCollabdesc = "", callingFrom = "", calling = "screenload";
    private String strGroupmapping_ID, strGroupmapping_Name;
    private GoogleApiClient mGoogleApiClient;
    private List<String> lstusers = new ArrayList<String>();
    private boolean mIsDistinct;
    private String strMessage = "Welcome to GiftADeed chat";
    private List<GroupListInfo> lstGetChannelsList = new ArrayList<>();
    private String fetchedChannelUrl;
    private ArrayList<GroupPOJO> groupArrayList;

    public static CreateCollabFragment newInstance(int sectionNumber) {
        CreateCollabFragment fragment = new CreateCollabFragment();
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.create_collab_layout, container, false);
        sessionManager = new SessionManager(getActivity());
        TaggedneedsActivity.fragname = CreateCollabFragment.newInstance(0);
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
        HashMap<String, String> collab = sessionManager.getSelectedColabDetails();
        callingFrom = collab.get(sessionManager.COLAB_CALL_FROM);
        receivedCollabid = collab.get(sessionManager.COLAB_ID);
        receivedCollabname = collab.get(sessionManager.COLAB_NAME);
        receivedCollabdesc = collab.get(sessionManager.COLAB_DESC);
        strGroupmapping_ID = collab.get(sessionManager.COLAB_FROMGID);
        strGroupmapping_Name = collab.get(sessionManager.COLAB_FROMGNAME);
        if (callingFrom.equals("create")) {
            //from create menu
            TaggedneedsActivity.updateTitle(getResources().getString(R.string.action_create_collab));
            getOwnedGroupList(strUser_ID);
        } else {
            //from edit collab menu
            selectGroup.setText(strGroupmapping_Name);
            collabName.setText(receivedCollabname);
            collabDesc.setText(receivedCollabdesc);
            TaggedneedsActivity.updateTitle(getResources().getString(R.string.edit_collab));
            getChannelsDetails();
        }

        selectGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(Validation.isOnline(getActivity()))) {
                    ToastPopUp.show(getActivity(), getString(R.string.network_validation));
                } else {
                    calling = "group";
                    getOwnedGroupList(strUser_ID);
                }
            }
        });

        btnCreateCollab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectGroup.getText().length() == 0) {
                    ToastPopUp.displayToast(getContext(), getResources().getString(R.string.select_res_group));
                } else if (collabName.getText().length() == 0) {
                    collabName.requestFocus();
                    collabName.setFocusable(true);
                    collabName.setError(getResources().getString(R.string.colab_name_req));
                } else if (collabDesc.getText().length() == 0) {
                    collabDesc.requestFocus();
                    collabDesc.setFocusable(true);
                    collabDesc.setError(getResources().getString(R.string.colab_desc_req));
                } else {
                    if (!(Validation.isOnline(getActivity()))) {
                        ToastPopUp.show(getActivity(), getString(R.string.network_validation));
                    } else {
                        if (callingFrom.equals("create")) {
                            //call create collab api
                            createCollab(strUser_ID, strGroupmapping_ID, collabName.getText().toString(), collabDesc.getText().toString());
                        } else {
                            // call edit collab api
                            editCollab(receivedCollabid, strGroupmapping_ID, collabName.getText().toString(), collabDesc.getText().toString());
                        }
                    }
                }
            }
        });

        TaggedneedsActivity.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callingFrom.equals("create")) {
                    //move to group list
                    Bundle bundle = new Bundle();
                    bundle.putString("tab", "tab1");  // tab2 for collaborations
                    GroupCollabFrag groupCollabFrag = new GroupCollabFrag();
                    groupCollabFrag.setArguments(bundle);
                    fragmgr.beginTransaction().replace(R.id.content_frame, groupCollabFrag).commit();
                } else {
                    CollabDetailsFragment collabDetailsFragment = new CollabDetailsFragment();
                    fragmgr.beginTransaction().replace(R.id.content_frame, collabDetailsFragment).commit();
                }
            }
        });
        return rootview;
    }

    //--------------------------Initilizing the UI variables--------------------------------------------
    private void init() {
        selectGroup = (EditText) rootview.findViewById(R.id.tv_select_collab_group);
        collabName = (EditText) rootview.findViewById(R.id.editText_collab_name);
        collabDesc = (EditText) rootview.findViewById(R.id.editText_collab_desc);
        btnCreateCollab = (Button) rootview.findViewById(R.id.button_create_collab);
    }

    //---------------------sending group details to server for create group-----------------------------------------------
    public void createCollab(String user_id, final String groupid, final String collabname, String collabdesc) {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        ArcConfiguration configuration = new ArcConfiguration(getContext());
        configuration.setText("Creating collaboration..");
        mDialog.setConfiguration(new ArcConfiguration(getContext()));
        mDialog.show();
        mDialog.setCancelable(false);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        CreateCollabInterface service = retrofit.create(CreateCollabInterface.class);
        Call<CollabResponseStatus> call = service.sendData(user_id, groupid, collabname, collabdesc);
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

                        sessionManager.set_notification_status("ON");
                        Intent loginintent = new Intent(getActivity(), LoginActivity.class);
                        loginintent.putExtra("message", "Charity");
                        startActivity(loginintent);
                    } else {
                        if (collabResponseStatus.getStatus() == 1) {
                            Toast.makeText(getContext(), collabResponseStatus.getSuccessMsg(), Toast.LENGTH_SHORT).show();
                            int generatedCollabId = collabResponseStatus.getCollabid();
                            String channelName = "";
                            // Sendbird create channel. Concat with GRP for Group and CLB for Collaboration
                            channelName = collabname + " - CLB" + generatedCollabId;
                            Log.d("channel_name", channelName);
                            //group chat
                            mIsDistinct = PreferenceUtils.getGroupChannelDistinct(myContext);
                            if (strUser_ID != null && channelName != null) {
                                lstusers.add(strUser_ID);
                                mIsDistinct = PreferenceUtils.getGroupChannelDistinct(myContext);
                                createCollabChannel(lstusers, channelName, strMessage, mIsDistinct);
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
                    Log.d("responsegroup", "" + e.getMessage());
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

    //--------------------------getting user owned groups from server------------------------------------------
    public void getOwnedGroupList(String userid) {
        groupArrayList = new ArrayList<>();
        mDialog.setConfiguration(new ArcConfiguration(getContext()));
        mDialog.show();
        mDialog.setCancelable(false);
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
                mDialog.dismiss();
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
                        if (calling.equals("screenload")) {
                            if (groupArrayList.size() == 1) {
                                if (groupArrayList.get(0).getGroup_name().length() > 30) {
                                    selectGroup.setText(groupArrayList.get(0).getGroup_name().substring(0, 29) + "...");
                                } else {
                                    selectGroup.setText(groupArrayList.get(0).getGroup_name());
                                }
                                strGroupmapping_ID = groupArrayList.get(0).getGroup_id();
                                strGroupmapping_Name = groupArrayList.get(0).getGroup_name();
                                selectGroup.setEnabled(false);
                                selectGroup.clearFocus();
                            } else {
                                selectGroup.setEnabled(true);
                                selectGroup.clearFocus();
                            }
                        } else if (calling.equals("group")) {
                            final Dialog dialog = new Dialog(getContext());
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setCancelable(false);
                            dialog.setCanceledOnTouchOutside(false);
                            dialog.setContentView(R.layout.groups_dialog);
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
                                    dialog.dismiss();
                                }
                            });
                            cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });
                            mDialog.dismiss();
                            dialog.show();
                        }
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
                mDialog.dismiss();
                ToastPopUp.show(myContext, getString(R.string.server_response_error));
            }
        });
    }

    //---------------------sending group details to server for edit group-----------------------------------------------
    public void editCollab(final String collabid, String groupid, final String collabname, String collabdesc) {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        ArcConfiguration configuration = new ArcConfiguration(getContext());
        configuration.setText("Editing collaboration..");
        mDialog.setConfiguration(new ArcConfiguration(getContext()));
        mDialog.show();
        mDialog.setCancelable(false);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        EditCollabInterface service = retrofit.create(EditCollabInterface.class);
        Call<CollabResponseStatus> call = service.sendData(collabid, groupid, collabname, collabdesc);
        Log.d("edit_collab_params", collabid + ":" + groupid + ":" + collabname + ":" + collabdesc);
        call.enqueue(new Callback<CollabResponseStatus>() {
            @Override
            public void onResponse(Response<CollabResponseStatus> response, Retrofit retrofit) {
                mDialog.dismiss();
                Log.d("responseclb_onresponse", "" + response.body());
                try {
                    CollabResponseStatus collabResponseStatus = response.body();
                    int isblock = 0;
                    try {
                        isblock = collabResponseStatus.getIsBlocked();
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

                        sessionManager.set_notification_status("ON");
                        Intent loginintent = new Intent(getActivity(), LoginActivity.class);
                        loginintent.putExtra("message", "Charity");
                        startActivity(loginintent);
                    } else {
                        Log.d("edit_collab_status", collabResponseStatus.getStatus().toString());
                        if (collabResponseStatus.getStatus() == 1) {
                            Toast.makeText(getContext(), collabResponseStatus.getSuccessMsg(), Toast.LENGTH_SHORT).show();

                            // update sendbird also
                            String channelName = "";
                            // Sendbird edit channel. Concat with GRP for Group and CLB for Collaboration
                            channelName = collabname + " - CLB" + collabid;
                            filterGroupChannel(receivedCollabname + " - CLB" + receivedCollabid);
                            callUpdateSendBird(fetchedChannelUrl, channelName);

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
                    Log.d("responseclb_onresponse", "" + e.getMessage());
                    StringWriter writer = new StringWriter();
                    e.printStackTrace(new PrintWriter(writer));
                    Bugreport bg = new Bugreport();
                    bg.sendbug(writer.toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                mDialog.dismiss();
                Log.d("responseclb_onresponse", "" + t.getMessage());
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
                    if (callingFrom.equals("create")) {
                        //move to group list
                        Bundle bundle = new Bundle();
                        bundle.putString("tab", "tab1");  // tab2 for collaborations
                        GroupCollabFrag groupCollabFrag = new GroupCollabFrag();
                        groupCollabFrag.setArguments(bundle);
                        fragmgr.beginTransaction().replace(R.id.content_frame, groupCollabFrag).commit();
                    } else {
                        CollabDetailsFragment collabDetailsFragment = new CollabDetailsFragment();
                        fragmgr.beginTransaction().replace(R.id.content_frame, collabDetailsFragment).commit();
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

    private void createCollabChannel(final List<String> userIds, final String clubName, final String message, final boolean distinct) {
        SendBird.connect(strUser_ID, new SendBird.ConnectHandler() {
            @Override
            public void onConnected(User user, SendBirdException e) {
                if (e != null) {
                    // Error.
                    return;
                }
                GroupChannel.createChannelWithUserIds(userIds, distinct, clubName, "", message, new GroupChannel.GroupChannelCreateHandler() {
                    @Override
                    public void onResult(GroupChannel groupChannel, SendBirdException e) {
                        if (e != null) {
                            // Error!
                            return;
                        }
                    }
                });
            }
        });
    }

    //get channels details
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
                                System.out.println("Chnalls: " + list.get(i).getName());
                                /// lstGetChannelsList.add(list.get(i).getName().toString());
                                lstGetChannelsList.add(new GroupListInfo(list.get(i).getData().toString(), list.get(i).getName().toString(), list.get(i).getUrl().toString()));
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

    //==============================================================================================
    public void callUpdateSendBird(String urlOfChannel, final String channelName) {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WebServices.MANI_SENDBRD_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //pass the json obhect
        /*JsonObject postParam = new JsonObject();
        postParam.addProperty("name", channelName);*/

        ModalSendBrdUpdate modalSendBrdUpdate = new ModalSendBrdUpdate();
        modalSendBrdUpdate.setName(channelName);
        modalSendBrdUpdate.setCoverUrl("");

        //call interface
        UpdateGrpChannel service = retrofit.create(UpdateGrpChannel.class);
        Call<ModalSendBrdUpdate> call = service.sendData(urlOfChannel, modalSendBrdUpdate);
        call.enqueue(new Callback<ModalSendBrdUpdate>() {
            @Override
            public void onResponse(Response<ModalSendBrdUpdate> response, Retrofit retrofit) {
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
