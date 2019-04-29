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
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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
import com.squareup.okhttp.OkHttpClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import giftadeed.kshantechsoft.com.giftadeed.Login.LoginActivity;
import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.SendBirdChat.groupchannel.GroupChannelListFragment;
import giftadeed.kshantechsoft.com.giftadeed.SendBirdChat.main.SendBirdLoginActivity;
import giftadeed.kshantechsoft.com.giftadeed.SendBirdChat.utils.PreferenceUtils;
import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.TagaNeed;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsActivity;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsFrag;
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

import static giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsActivity.userClubCount;

public class GroupsListFragment extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener, GoogleApiClient.OnConnectionFailedListener {
    View rootview;
    FragmentActivity myContext;
    static FragmentManager fragmgr;
    SimpleArcDialog mDialog;
    RecyclerView recyclerView;
    ArrayList<GroupPOJO> groupArrayList;
    TextView noGroupsText;
    Button btnCreateGroup;
    SwipeRefreshLayout swipeRefreshLayout;
    String strUser_ID;
    SessionManager sessionManager;
    private GoogleApiClient mGoogleApiClient;
    GroupListAdapter groupListAdapter;
    DatabaseAccess databaseAccess;
    private List<GroupListInfo> lstGetChannelsList = new ArrayList<>();
    private List<String> channelList = new ArrayList<String>();
    private boolean mIsDistinct;
    private String strMessage = "Welcome to GiftADeed Chat";
    private List<String> lstusers = new ArrayList<String>();

    public static GroupsListFragment newInstance(int sectionNumber) {
        GroupsListFragment fragment = new GroupsListFragment();
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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_groups_layout, container, false);
        sessionManager = new SessionManager(getActivity());
        TaggedneedsActivity.updateTitle(getResources().getString(R.string.drawer_groups));
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
        TaggedneedsActivity.toggle.setDrawerIndicatorEnabled(true);
        TaggedneedsActivity.back.setVisibility(View.GONE);
        TaggedneedsActivity.imgHamburger.setVisibility(View.GONE);
        TaggedneedsActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        init();
        databaseAccess = DatabaseAccess.getInstance(getContext());
        databaseAccess.open();
        mGoogleApiClient = ((TaggedneedsActivity) getActivity()).mGoogleApiClient;
        HashMap<String, String> user = sessionManager.getUserDetails();
        strUser_ID = user.get(sessionManager.USER_ID);
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
                                            getGroupList(strUser_ID);
                                            getChannelsDetails();
                                        }
                                    }
                                }
        );

        btnCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateGroupFragment createGroupFragment = new CreateGroupFragment();
                sessionManager.createGroupDetails("create", "", "", "", "");
                fragmgr.beginTransaction().replace(R.id.content_frame, createGroupFragment).commit();
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(),
                recyclerView, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, final int position) {
                GroupDetailsFragment groupDetailsFragment = new GroupDetailsFragment();
                Log.d("infogrp", "" + groupArrayList.get(position).getGroup_id() + groupArrayList.get(position).getGroup_name());
                sessionManager.createGroupDetails("", groupArrayList.get(position).getGroup_id(), groupArrayList.get(position).getGroup_name(), groupArrayList.get(position).getGroup_desc(), groupArrayList.get(position).getGroup_image());
                fragmgr.beginTransaction().replace(R.id.content_frame, groupDetailsFragment).commit();
            }
        }));
        return rootview;
    }

    //--------------------------Initializing the UI variables--------------------------------------------
    private void init() {
        recyclerView = (RecyclerView) rootview.findViewById(R.id.list_groups);
        noGroupsText = (TextView) rootview.findViewById(R.id.no_groups_text);
        btnCreateGroup = (Button) rootview.findViewById(R.id.btn_create_group);
        swipeRefreshLayout = (SwipeRefreshLayout) rootview.findViewById(R.id.swipe_refresh_layout);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration itemDecor = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecor);
        recyclerView.setLayoutManager(layoutManager);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.groups_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_create_group:
                CreateGroupFragment createGroupFragment = new CreateGroupFragment();
                sessionManager.createGroupDetails("create", "", "", "", "");
                fragmgr.beginTransaction().replace(R.id.content_frame, createGroupFragment).commit();
                return true;
            case R.id.action_group_messages:
                if (userClubCount != null) {
                    if (userClubCount.equals("Yes")) {
                        int i = 0;
                        /*SendBirdLoginActivity sendBirdLoginActivity = new SendBirdLoginActivity();
                        Bundle bundle = new Bundle();
                        bundle.putString("CHATPAGE", "MSGPAGE");
                        bundle.putString("PAGE", "MESSAGES");
                        sendBirdLoginActivity.setArguments(bundle);
                        fragmgr.beginTransaction().replace(R.id.content_frame, sendBirdLoginActivity).commit();*/

                        // Load list of Group Channels
                        Fragment fragment = GroupChannelListFragment.newInstance();
                        fragmgr.beginTransaction()
                                .replace(R.id.content_frame, fragment)
                                .commit();
                    } else if (userClubCount.equals("No")) {
                        Toast.makeText(getContext(), getResources().getString(R.string.no_channel), Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRefresh() {
        if (!(Validation.isOnline(getActivity()))) {
            swipeRefreshLayout.setRefreshing(false);
            ToastPopUp.show(getActivity(), getString(R.string.network_validation));
        } else {
            swipeRefreshLayout.setRefreshing(true);
            getGroupList(strUser_ID);
            getChannelsDetails();
        }
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
                swipeRefreshLayout.setRefreshing(false);
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
                        swipeRefreshLayout.setRefreshing(false);
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
//            swipeRefreshLayout.setRefreshing(false);
                            noGroupsText.setVisibility(View.VISIBLE);
                            btnCreateGroup.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                            databaseAccess.deleteAllGroups();
                        } else {
//            swipeRefreshLayout.setRefreshing(false);
                            noGroupsText.setVisibility(View.GONE);
                            btnCreateGroup.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            groupListAdapter = new GroupListAdapter(groupArrayList, myContext);
                            recyclerView.setAdapter(groupListAdapter);

                            for (int i = 0; i < groupArrayList.size(); i++) {
                                if (!databaseAccess.groupidExist(groupArrayList.get(i).getGroup_id())) {
                                    databaseAccess.Create_Group(groupArrayList.get(i).getGroup_id(), groupArrayList.get(i).getGroup_name(), groupArrayList.get(i).getGroup_image(), "true");
                                }

//                                String channelName = "";
//                                channelName = groupArrayList.get(i).getGroup_name() + " - " + groupArrayList.get(i).getGroup_id();
//                                Log.d("group_name", "" + channelName);
//                                filterGroupChannel(channelName);
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
                swipeRefreshLayout.setRefreshing(false);
                Log.d("response_grouplist", "" + t.getMessage());
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

    public void getChannelsDetails() {
        channelList = new ArrayList<String>();
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
                            if (list.size() != 0) {
                                userClubCount = "Yes";
                                for (int i = 0; i < list.size(); i++) {
                                    System.out.println("Chnalls: " + list.get(i).getName());
                                    lstGetChannelsList.add(new GroupListInfo(list.get(i).getData().toString(), list.get(i).getName().toString(), list.get(i).getUrl().toString()));
                                    channelList.add(list.get(i).getName().toString());
                                }
                            } else {
                                userClubCount = "No";
                            }
                        }
                    }
                });
            }
        });
    }

    public void filterGroupChannel(String clubname) {
        System.out.println("clbname line n0 3950        " + clubname);
        /*if (lstGetChannelsList != null && lstGetChannelsList.size() != 0) {
            for (int i = 0; i < lstGetChannelsList.size(); i++) {
                if (lstGetChannelsList.get(i).getmChannelName().equals(clubname)) {

                } else {
                    mIsDistinct = PreferenceUtils.getGroupChannelDistinct(myContext);
                    if (strUser_ID != null && clubname != null) {
                        lstusers.add(strUser_ID);
                        mIsDistinct = PreferenceUtils.getGroupChannelDistinct(myContext);
                        createGroupChannel(lstusers, clubname, strMessage, mIsDistinct);
                    }
                }
            }
        } else {
            mIsDistinct = PreferenceUtils.getGroupChannelDistinct(myContext);
            if (strUser_ID != null && clubname != null) {
                lstusers.add(strUser_ID);
                mIsDistinct = PreferenceUtils.getGroupChannelDistinct(myContext);
                createGroupChannel(lstusers, clubname, strMessage, mIsDistinct);
            }
        }*/

        if (channelList != null && channelList.size() != 0) {
            for (int i = 0; i < channelList.size(); i++) {
                if (channelList.contains(clubname)) {

                } else {
                    mIsDistinct = PreferenceUtils.getGroupChannelDistinct(myContext);
                    if (strUser_ID != null && clubname != null) {
                        lstusers.add(strUser_ID);
                        mIsDistinct = PreferenceUtils.getGroupChannelDistinct(myContext);
                        createGroupChannel(lstusers, clubname, strMessage, mIsDistinct);
                    }
                }
            }
        } else {
            mIsDistinct = PreferenceUtils.getGroupChannelDistinct(myContext);
            if (strUser_ID != null && clubname != null) {
                lstusers.add(strUser_ID);
                mIsDistinct = PreferenceUtils.getGroupChannelDistinct(myContext);
                createGroupChannel(lstusers, clubname, strMessage, mIsDistinct);
            }
        }
    }

    private void createGroupChannel(final List<String> userIds, final String clubName, final String message, final boolean distinct) {
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
}
