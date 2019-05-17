package giftadeed.kshantechsoft.com.giftadeed.Group;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.squareup.okhttp.OkHttpClient;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import giftadeed.kshantechsoft.com.giftadeed.Bug.Bugreport;
import giftadeed.kshantechsoft.com.giftadeed.Login.LoginActivity;
import giftadeed.kshantechsoft.com.giftadeed.R;
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

public class AddGroupMemberFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener {
    View rootview;
    FragmentActivity myContext;
    static FragmentManager fragmgr;
    SimpleArcDialog mDialog;
    SessionManager sessionManager;
    String strUser_ID;
    String receivedGid = "", receivedGName = "", strClubName;
    EditText editsearch;
    Button btnSearch, btnAddSearchedMember;
    TextView searchedRecordResult, searchedUsername, searchedEmail, tvAlreadyAdded;
    LinearLayout layoutSearchedRecord;
    private GoogleApiClient mGoogleApiClient;
    ArrayList<MemberDetails> userList;
    String searchedMemberId;
    private List<GroupListInfo> lstGetChannelsList = new ArrayList<>();
    private String mChannelUrl = "";

    public static AddGroupMemberFragment newInstance(int sectionNumber) {
        AddGroupMemberFragment fragment = new AddGroupMemberFragment();
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
        rootview = inflater.inflate(R.layout.add_group_member_layout, container, false);
        sessionManager = new SessionManager(getActivity());
        TaggedneedsActivity.updateTitle(getResources().getString(R.string.add_member));
        TaggedneedsActivity.fragname = AddGroupMemberFragment.newInstance(0);
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
        receivedGName = group.get(sessionManager.GROUP_NAME);
        // Sendbird channel. Concat with GRP for Group and CLB for Collaboration
        strClubName = receivedGName + " - GRP" + receivedGid;
        try {
            getChannelsDetails();
        } catch (Exception e) {
            e.printStackTrace();
        }

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((editsearch.getText().length() == 0) || (!Validation.isValidEmailAddress(editsearch.getText().toString().trim()))) {
                    editsearch.requestFocus();
                    editsearch.setFocusable(true);
                    editsearch.setError("Enter valid email");
                } else {
                    if (!(Validation.isOnline(getActivity()))) {
                        ToastPopUp.show(getActivity(), getString(R.string.network_validation));
                    } else {
                        searchMember(strUser_ID, editsearch.getText().toString(), receivedGid);
                    }
                }
            }
        });

        btnAddSearchedMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(Validation.isOnline(getActivity()))) {
                    ToastPopUp.show(getActivity(), getString(R.string.network_validation));
                } else {
                    addMemberInGroup(strUser_ID, searchedMemberId, receivedGid);
                }
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
        editsearch = (EditText) rootview.findViewById(R.id.search);
        btnSearch = (Button) rootview.findViewById(R.id.btn_search);
        btnAddSearchedMember = (Button) rootview.findViewById(R.id.btn_add_searched_member);
        searchedRecordResult = (TextView) rootview.findViewById(R.id.tv_searched_record_result);
        searchedUsername = (TextView) rootview.findViewById(R.id.searched_username);
        searchedEmail = (TextView) rootview.findViewById(R.id.searched_email);
        tvAlreadyAdded = (TextView) rootview.findViewById(R.id.tv_already_added);
        layoutSearchedRecord = (LinearLayout) rootview.findViewById(R.id.layout_searched_record);
    }

    //---------------------sending group details to server-----------------------------------------------
    public void searchMember(final String user_id, String email, String groupid) {
        userList = new ArrayList<MemberDetails>();
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        mDialog.setConfiguration(new ArcConfiguration(getContext()));
        mDialog.show();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        SearchMemberInterface service = retrofit.create(SearchMemberInterface.class);
        Call<List<MemberDetails>> call = service.sendData(user_id, email, groupid);
        call.enqueue(new Callback<List<MemberDetails>>() {
            @Override
            public void onResponse(Response<List<MemberDetails>> response, Retrofit retrofit) {
                mDialog.dismiss();
                Log.d("response_search_user", "" + response.body());
                try {
                    List<MemberDetails> memberDetails = response.body();
                    int isblock = 0;
                    try {
                        isblock = memberDetails.get(0).getIsBlocked();
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
                        userList.clear();
                        try {
                            for (int i = 0; i < memberDetails.size(); i++) {
                                MemberDetails groupPOJO = new MemberDetails();
                                groupPOJO.setUserid(memberDetails.get(i).getUserid());
                                groupPOJO.setName(memberDetails.get(i).getName());
                                groupPOJO.setEmail(memberDetails.get(i).getEmail());
                                groupPOJO.setJoined(memberDetails.get(i).getJoined());
                                userList.add(groupPOJO);
                            }
                        } catch (Exception e) {
                            StringWriter writer = new StringWriter();
                            e.printStackTrace(new PrintWriter(writer));
                            Bugreport bg = new Bugreport();
                            bg.sendbug(writer.toString());
                        }

                        if (userList.size() > 0) {
                            searchedRecordResult.setVisibility(View.GONE);
                            layoutSearchedRecord.setVisibility(View.VISIBLE);
                            searchedMemberId = userList.get(0).getUserid();
                            searchedUsername.setText(userList.get(0).getName());
                            searchedEmail.setText(userList.get(0).getEmail());
                            String str = userList.get(0).getJoined();
                            if (str.equals("0")) {
                                tvAlreadyAdded.setVisibility(View.GONE);
                                btnAddSearchedMember.setVisibility(View.VISIBLE);
                            } else {
                                tvAlreadyAdded.setVisibility(View.VISIBLE);
                                btnAddSearchedMember.setVisibility(View.GONE);
                            }
                        } else {
                            searchedRecordResult.setVisibility(View.VISIBLE);
                            searchedRecordResult.setText("No record found");
                            layoutSearchedRecord.setVisibility(View.GONE);
                        }
                    }
                } catch (Exception e) {
                    mDialog.dismiss();
                    Log.d("response_search_user", "" + e.getMessage());
                    StringWriter writer = new StringWriter();
                    e.printStackTrace(new PrintWriter(writer));
                    Bugreport bg = new Bugreport();
                    bg.sendbug(writer.toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                mDialog.dismiss();
                Log.d("response_search_user", "" + t.getMessage());
                ToastPopUp.show(myContext, getString(R.string.server_response_error));
            }
        });
    }

    //---------------------add searched member in group-----------------------------------------------
    public void addMemberInGroup(final String user_id, final String memberid, String groupid) {
        userList = new ArrayList<MemberDetails>();
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        mDialog.setConfiguration(new ArcConfiguration(getContext()));
        mDialog.show();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        AddMemberInterface service = retrofit.create(AddMemberInterface.class);
        Call<GroupResponseStatus> call = service.sendData(user_id, memberid, groupid);
        call.enqueue(new Callback<GroupResponseStatus>() {
            @Override
            public void onResponse(Response<GroupResponseStatus> response, Retrofit retrofit) {
                mDialog.dismiss();
                Log.d("response_add_member", "" + response.body());
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
                            Toast.makeText(getContext(), getResources().getString(R.string.member_added), Toast.LENGTH_SHORT).show();
                            //============add member in sendbird group channel==============
                            if (lstGetChannelsList != null) {
                                for (int i = 0; i < lstGetChannelsList.size(); i++) {
                                    Log.d("channel_name", lstGetChannelsList.get(i).getmChannelName());
                                    if (lstGetChannelsList.get(i).getmChannelName().equals(strClubName)) {
                                        Log.d("channel_url", lstGetChannelsList.get(i).getmUrl());
                                        inviteUser(lstGetChannelsList.get(i).getmUrl().toString(), memberid);
                                    }
                                }
                            }
                            editsearch.setText("");
                            layoutSearchedRecord.setVisibility(View.GONE);
                        } else if (groupResponseStatus.getStatus() == 0) {
                            Toast.makeText(getContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    mDialog.dismiss();
                    Log.d("response_add_member", "" + e.getMessage());
                    StringWriter writer = new StringWriter();
                    e.printStackTrace(new PrintWriter(writer));
                    Bugreport bg = new Bugreport();
                    bg.sendbug(writer.toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                mDialog.dismiss();
                Log.d("response_add_member", "" + t.getMessage());
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

    public void inviteUser(final String url, final String searchedMemberId) {
        SendBird.connect(strUser_ID, new SendBird.ConnectHandler() {
            @Override
            public void onConnected(User user, SendBirdException e) {
                if (e != null) {
                    // Error.
                    return;
                }
                // Get channel instance from URL first.
                GroupChannel.getChannel(url, new GroupChannel.GroupChannelGetHandler() {
                    @Override
                    public void onResult(GroupChannel groupChannel, SendBirdException e) {
                        if (e != null) {
                            // Error!
                            return;
                        }
                        // Then invite the selected members to the channel.
                        groupChannel.inviteWithUserId(searchedMemberId, new GroupChannel.GroupChannelInviteHandler() {
                            @Override
                            public void onResult(SendBirdException e) {
                                if (e != null) {
                                    // Error!
                                    return;
                                }
                            }
                        });
                    }
                });
            }
        });
    }
}
