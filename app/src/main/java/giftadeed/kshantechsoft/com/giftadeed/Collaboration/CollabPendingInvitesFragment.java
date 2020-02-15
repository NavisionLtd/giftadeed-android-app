/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.Collaboration;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.leo.simplearcloader.SimpleArcDialog;
import com.squareup.okhttp.OkHttpClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import giftadeed.kshantechsoft.com.giftadeed.Group.GroupCollabFrag;
import giftadeed.kshantechsoft.com.giftadeed.Group.RecyclerTouchListener;
import giftadeed.kshantechsoft.com.giftadeed.Group.RecyclerViewClickListener;
import giftadeed.kshantechsoft.com.giftadeed.Login.LoginActivity;
import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.TagaNeed;
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

public class CollabPendingInvitesFragment extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener {
    View rootview;
    FragmentActivity myContext;
    static FragmentManager fragmgr;
    SimpleArcDialog mDialog;
    RecyclerView recyclerView;
    ArrayList<Colabrequestlist> colabArrayList;
    TextView noPendingInvites;
    SwipeRefreshLayout swipeRefreshLayout;
    String strUser_ID;
    SharedPrefManager sharedPrefManager;
    CollabInvitesAdapter collabListAdapter;

    public static CollabPendingInvitesFragment newInstance(int sectionNumber) {
        CollabPendingInvitesFragment fragment = new CollabPendingInvitesFragment();
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
        rootview = inflater.inflate(R.layout.fragment_pending_invites_layout, container, false);
        sharedPrefManager = new SharedPrefManager(getActivity());
        TaggedneedsActivity.updateTitle(getResources().getString(R.string.action_collab_invitation));
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
        HashMap<String, String> user = sharedPrefManager.getUserDetails();
        strUser_ID = user.get(sharedPrefManager.USER_ID);
        swipeRefreshLayout.setOnRefreshListener(this);
        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (!(Validation.isNetworkAvailable(getActivity()))) {
                                            swipeRefreshLayout.setRefreshing(false);
                                            ToastPopUp.show(getActivity(), getString(R.string.network_validation));
                                        } else {
                                            swipeRefreshLayout.setRefreshing(true);
                                            recyclerView.setAdapter(null);
                                            getPendingInviteList();
                                        }
                                    }
                                }
        );

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(),
                recyclerView, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, final int position) {
                /*FragmentManager newfrag;
                newfrag = getActivity().getSupportFragmentManager();
                Log.d("infogrp", "" + colabArrayList.get(position).getId() + colabArrayList.get(position).getColabName());
                sharedPrefManager.createColabDetails("", colabArrayList.get(position).getId(), colabArrayList.get(position).getColabName(), colabArrayList.get(position).getUserRole());
                GroupDetailsFragment fragment = new GroupDetailsFragment();
                newfrag.beginTransaction().replace(R.id.content_frame, fragment).commit();*/
            }
        }));

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
        recyclerView = (RecyclerView) rootview.findViewById(R.id.list_pending_invites);
        noPendingInvites = (TextView) rootview.findViewById(R.id.no_pending_invites_text);
        swipeRefreshLayout = (SwipeRefreshLayout) rootview.findViewById(R.id.swipe_refresh_layout_invites);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration itemDecor = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecor);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onRefresh() {
        if (!(Validation.isNetworkAvailable(getActivity()))) {
            swipeRefreshLayout.setRefreshing(false);
            ToastPopUp.show(getActivity(), getString(R.string.network_validation));
        } else {
            swipeRefreshLayout.setRefreshing(true);
            recyclerView.setAdapter(null);
            getPendingInviteList();
        }
    }

    //---------------------get the list of group creators-----------------------------------------------
    public void getPendingInviteList() {
        colabArrayList = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        ColabRequestListInterface service = retrofit.create(ColabRequestListInterface.class);
        Call<CollabPOJO> call = service.sendData(strUser_ID);
        call.enqueue(new Callback<CollabPOJO>() {
            @Override
            public void onResponse(Response<CollabPOJO> response, Retrofit retrofit) {
                swipeRefreshLayout.setRefreshing(false);
                Log.d("response_colabreqlist", "" + response.body());
                try {
                    CollabPOJO res = response.body();
                    int isblock = 0;
                    try {
                        isblock = res.getIsBlocked();
                    } catch (Exception e) {
                        isblock = 0;
                    }
                    if (isblock == 1) {
                        swipeRefreshLayout.setRefreshing(false);
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
                        colabArrayList.clear();
                        if (res.getStatus() == 1) {
                            for (int i = 0; i < res.getColab_requestlist().size(); i++) {
                                Colabrequestlist colabrequestlist = new Colabrequestlist();
                                colabrequestlist.setCollabId(res.getColab_requestlist().get(i).getCollabId());
                                colabrequestlist.setCreator_id(res.getColab_requestlist().get(i).getCreator_id());
                                colabrequestlist.setColabName(res.getColab_requestlist().get(i).getColabName());
                                colabrequestlist.setColabDesc(res.getColab_requestlist().get(i).getColabDesc());
                                colabrequestlist.setColabStartDate(res.getColab_requestlist().get(i).getColabStartDate());
                                colabrequestlist.setInviteStatus(res.getColab_requestlist().get(i).getInviteStatus());
                                colabArrayList.add(colabrequestlist);
                            }

                            if (colabArrayList.size() <= 0) {
                                noPendingInvites.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            } else {
                                noPendingInvites.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                                collabListAdapter = new CollabInvitesAdapter(CollabPendingInvitesFragment.this, strUser_ID, colabArrayList, myContext);
                                recyclerView.setAdapter(collabListAdapter);
                            }
                        } else if (res.getStatus() == 0) {
//                            Toast.makeText(getContext(), res.getErrorMsg(), Toast.LENGTH_SHORT).show();
                            noPendingInvites.setVisibility(View.VISIBLE);
                            noPendingInvites.setText("" + res.getErrorMsg());
                            recyclerView.setVisibility(View.GONE);
                        }
                    }
                } catch (Exception e) {
                    Log.d("response_colabreqlist", "" + e.getMessage());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Log.d("response_colabreqlist", "" + t.getMessage());
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
                    GroupCollabFrag groupCollabFrag = new GroupCollabFrag();
                    fragmgr.beginTransaction().replace(R.id.content_frame, groupCollabFrag).commit();
                    return true;
                }
                return false;
            }
        });
    }
}
