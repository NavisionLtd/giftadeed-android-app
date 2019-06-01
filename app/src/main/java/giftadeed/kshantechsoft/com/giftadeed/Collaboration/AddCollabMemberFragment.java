package giftadeed.kshantechsoft.com.giftadeed.Collaboration;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.squareup.okhttp.OkHttpClient;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import giftadeed.kshantechsoft.com.giftadeed.Bug.Bugreport;
import giftadeed.kshantechsoft.com.giftadeed.Group.RecyclerTouchListener;
import giftadeed.kshantechsoft.com.giftadeed.Group.RecyclerViewClickListener;
import giftadeed.kshantechsoft.com.giftadeed.Login.LoginActivity;
import giftadeed.kshantechsoft.com.giftadeed.R;
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

public class AddCollabMemberFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, GoogleApiClient.OnConnectionFailedListener {
    View rootview;
    FragmentActivity myContext;
    static FragmentManager fragmgr;
    SimpleArcDialog mDialog;
    SessionManager sessionManager;
    String strUser_ID;
    String receivedCid = "";
    EditText editsearch;
    TextView noGroupCreator;
    RecyclerView recyclerView;
    Button btnInviteCreators;
    CollabAddMemberAdapter adapter;
    ArrayList<Creatorslist> groupCreatorsList;
    private AlertDialog alertDialog;
    private GoogleApiClient mGoogleApiClient;
    SwipeRefreshLayout swipeRefreshLayout;
    public static ArrayList<String> selectedCreatorIds = new ArrayList<String>();
    String formattedCreators = ""; // for removing brackets [ and ]

    public static AddCollabMemberFragment newInstance(int sectionNumber) {
        AddCollabMemberFragment fragment = new AddCollabMemberFragment();
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.group_creators_list_layout, container, false);
        sessionManager = new SessionManager(getActivity());
        TaggedneedsActivity.updateTitle(getResources().getString(R.string.invite_creators));
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
        HashMap<String, String> collab = sessionManager.getSelectedColabDetails();
        receivedCid = collab.get(sessionManager.COLAB_ID);

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
                                            recyclerView.setAdapter(null);
                                            getGroupCreatorsList();
                                        }
                                    }
                                }
        );

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(),
                recyclerView, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, final int position) {
                /*ResourceDetailsFrag resourceDetailsFrag = new ResourceDetailsFrag();
                Bundle bundle = new Bundle();
                bundle.putString("str_resid", groupCreatorsList.get(position).getUserId());
                bundle.putString("callingFrom", "list");
                resourceDetailsFrag.setArguments(bundle);
                fragmgr.beginTransaction().replace(R.id.content_frame, resourceDetailsFrag).commit();*/
            }
        }));

        btnInviteCreators.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                formattedCreators = selectedCreatorIds.toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\s+", "");
                Log.d("creators", formattedCreators);
                if (formattedCreators.length() > 0) {
                    inviteGroupCreators();
                } else {
                    ToastPopUp.displayToast(getContext(), "Select group creators");
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
                CollabDetailsFragment collabDetailsFragment = new CollabDetailsFragment();
                fragmgr.beginTransaction().replace(R.id.content_frame, collabDetailsFragment).commit();
            }
        });
        return rootview;
    }

    //--------------------------Initializing the UI variables--------------------------------------------
    private void init() {
        selectedCreatorIds = new ArrayList<String>();
        editsearch = (EditText) rootview.findViewById(R.id.et_search_group);
        noGroupCreator = (TextView) rootview.findViewById(R.id.tv_no_group_creators);
        swipeRefreshLayout = (SwipeRefreshLayout) rootview.findViewById(R.id.creatorlist_swipe_refresh);
        btnInviteCreators = (Button) rootview.findViewById(R.id.btn_invite);
        recyclerView = (RecyclerView) rootview.findViewById(R.id.listview_group_creators);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration itemDecor = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecor);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void inviteGroupCreators() {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        InviteCreatorsInterface service = retrofit.create(InviteCreatorsInterface.class);
        Call<CollabResponseStatus> call = service.sendData(receivedCid, formattedCreators);
        Log.d("invite_inputparams", receivedCid + ":" + formattedCreators);
        call.enqueue(new Callback<CollabResponseStatus>() {
            @Override
            public void onResponse(Response<CollabResponseStatus> response, Retrofit retrofit) {
                Log.d("response_invite", "" + response.body());
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
                        if (collabResponseStatus.getStatus() == 1) {
                            Toast.makeText(getContext(), getResources().getString(R.string.invite_sent), Toast.LENGTH_SHORT).show();
                            CollabDetailsFragment collabDetailsFragment = new CollabDetailsFragment();
                            fragmgr.beginTransaction().replace(R.id.content_frame, collabDetailsFragment).commit();
                        } else if (collabResponseStatus.getStatus() == 0) {
                            Toast.makeText(getContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    Log.d("response_invite", "" + e.getMessage());
                    StringWriter writer = new StringWriter();
                    e.printStackTrace(new PrintWriter(writer));
                    Bugreport bg = new Bugreport();
                    bg.sendbug(writer.toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("response_invite", "" + t.getMessage());
                ToastPopUp.show(myContext, getString(R.string.server_response_error));
            }
        });
    }

    //---------------------get member list for selected collaboration-----------------------------------------------
    public void getGroupCreatorsList() {
        groupCreatorsList = new ArrayList<Creatorslist>();
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        CreatorsListInterface service = retrofit.create(CreatorsListInterface.class);
        Call<CollabPOJO> call = service.sendData(strUser_ID, receivedCid);
        Log.d("input_params", "" + strUser_ID + ":" + receivedCid);
        call.enqueue(new Callback<CollabPOJO>() {
            @Override
            public void onResponse(Response<CollabPOJO> response, Retrofit retrofit) {
                swipeRefreshLayout.setRefreshing(false);
                Log.d("response_memberlist", "" + response.body());
                try {
                    CollabPOJO collabPOJO = response.body();
                    int isblock = 0;
                    try {
                        isblock = collabPOJO.getIsBlocked();
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
                        int size = collabPOJO.getGroupCreatorsList().size();
                        if (size > 0) {
                            for (int i = 0; i < size; i++) {
                                Creatorslist creatorslist = new Creatorslist();
                                creatorslist.setUserId(collabPOJO.getGroupCreatorsList().get(i).getUserId());
                                creatorslist.setFirstName(collabPOJO.getGroupCreatorsList().get(i).getFirstName());
                                creatorslist.setLastName(collabPOJO.getGroupCreatorsList().get(i).getLastName());
                                creatorslist.setGroupNames(collabPOJO.getGroupCreatorsList().get(i).getGroupNames());
                                creatorslist.setSelected(false);
                                groupCreatorsList.add(creatorslist);
                            }

                            if (groupCreatorsList.size() > 0) {
                                noGroupCreator.setText(getResources().getString(R.string.total_members));
                                editsearch.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.VISIBLE);
                                btnInviteCreators.setVisibility(View.VISIBLE);
                                adapter = new CollabAddMemberAdapter(groupCreatorsList, myContext);
                                recyclerView.setAdapter(adapter);
                            } else {
                                recyclerView.setVisibility(View.GONE);
                                editsearch.setVisibility(View.GONE);
                                btnInviteCreators.setVisibility(View.GONE);
                                noGroupCreator.setText(getResources().getString(R.string.no_members));
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
                    CollabDetailsFragment collabDetailsFragment = new CollabDetailsFragment();
                    fragmgr.beginTransaction().replace(R.id.content_frame, collabDetailsFragment).commit();
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
            recyclerView.setAdapter(null);
            getGroupCreatorsList();
        }
    }
}
