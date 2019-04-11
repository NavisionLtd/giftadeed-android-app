package giftadeed.kshantechsoft.com.giftadeed.Resources;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.leo.simplearcloader.SimpleArcDialog;

import java.util.ArrayList;
import java.util.HashMap;

import giftadeed.kshantechsoft.com.giftadeed.Group.RecyclerTouchListener;
import giftadeed.kshantechsoft.com.giftadeed.Group.RecyclerViewClickListener;
import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.TagaNeed;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsActivity;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsFrag;
import giftadeed.kshantechsoft.com.giftadeed.Utils.SessionManager;
import giftadeed.kshantechsoft.com.giftadeed.Utils.ToastPopUp;
import giftadeed.kshantechsoft.com.giftadeed.Utils.Validation;

public class ResourceListFragment extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener, GoogleApiClient.OnConnectionFailedListener {
    View rootview;
    FragmentActivity myContext;
    static FragmentManager fragmgr;
    SimpleArcDialog mDialog;
    RecyclerView recyclerView;
    ArrayList<ResourcePOJO> resArrayList;
    TextView noResText;
    Button btnCreateRes;
    SwipeRefreshLayout swipeRefreshLayout;
    String strUser_ID;
    SessionManager sessionManager;
    private GoogleApiClient mGoogleApiClient;
    ResListAdapter resListAdapter;

    public static ResourceListFragment newInstance(int sectionNumber) {
        ResourceListFragment fragment = new ResourceListFragment();
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
        rootview = inflater.inflate(R.layout.fragment_res_layout, container, false);
        sessionManager = new SessionManager(getActivity());
        TaggedneedsActivity.updateTitle(getResources().getString(R.string.drawer_resources));
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
//                                            getResList(strUser_ID);
                                        }
                                    }
                                }
        );

        btnCreateRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateResourceFragment createResourceFragment = new CreateResourceFragment();
                sessionManager.createResourceDetails("create");
                fragmgr.beginTransaction().replace(R.id.content_frame, createResourceFragment).commit();
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(),
                recyclerView, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, final int position) {
                /*ResourceDetailsFrag resourceDetailsFrag = new ResourceDetailsFrag();
                Log.d("infogrp", "" + resArrayList.get(position).getGroup_id() + resArrayList.get(position).getGroup_name());
                sessionManager.createGroupDetails("", resArrayList.get(position).getGroup_id(), resArrayList.get(position).getGroup_name(), resArrayList.get(position).getGroup_desc(), resArrayList.get(position).getGroup_image());
                fragmgr.beginTransaction().replace(R.id.content_frame, resourceDetailsFrag).commit();*/
            }
        }));
        return rootview;
    }

    //--------------------------Initializing the UI variables--------------------------------------------
    private void init() {
        recyclerView = (RecyclerView) rootview.findViewById(R.id.list_resources);
        noResText = (TextView) rootview.findViewById(R.id.no_res_text);
        btnCreateRes = (Button) rootview.findViewById(R.id.btn_create_res);
        swipeRefreshLayout = (SwipeRefreshLayout) rootview.findViewById(R.id.swipe_refresh_res_layout);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.resource_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_create_resource:
                CreateResourceFragment createGroupFragment = new CreateResourceFragment();
                sessionManager.createResourceDetails("create");
                fragmgr.beginTransaction().replace(R.id.content_frame, createGroupFragment).commit();
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
//            getResList(strUser_ID);
        }
    }

    //---------------------get the list of joined and created groups from server-----------------------------------------------
   /* public void getResList(String user_id) {
        resArrayList = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        ResourcelistInterface service = retrofit.create(ResourcelistInterface.class);
        Call<Modelresourcelist> call = service.fetchData(user_id);
        call.enqueue(new Callback<Modelresourcelist>() {
            @Override
            public void onResponse(Response<Modelresourcelist> response, Retrofit retrofit) {
                swipeRefreshLayout.setRefreshing(false);
                Log.d("response_grouplist", "" + response.body());
                try {
                    Modelresourcelist res = response.body();
                    int isblock = 0;
                    try {
                        isblock = res.getReslist();
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
                        List<ResourcePOJO> resourcePOJOList = response.body();
                        resArrayList.clear();
                        try {
                            for (int i = 0; i < resourcePOJOList.size(); i++) {
                                ResourcePOJO resourcePOJO = new ResourcePOJO();
                                resourcePOJO.setResName(resourcePOJOList.get(i).getResName());
                                resArrayList.add(resourcePOJO);
                            }
                        } catch (Exception e) {

                        }

                        if (resArrayList.size() <= 0) {
//            swipeRefreshLayout.setRefreshing(false);
                            noGroupsText.setVisibility(View.VISIBLE);
                            btnCreateGroup.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        } else {
//            swipeRefreshLayout.setRefreshing(false);
                            noGroupsText.setVisibility(View.GONE);
                            btnCreateGroup.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            resListAdapter = new ResListAdapter(resArrayList, myContext);
                            recyclerView.setAdapter(resListAdapter);
                        }
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
    }*/

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
