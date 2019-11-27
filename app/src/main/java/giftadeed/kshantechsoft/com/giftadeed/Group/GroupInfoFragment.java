package giftadeed.kshantechsoft.com.giftadeed.Group;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
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
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.Picasso;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import giftadeed.kshantechsoft.com.giftadeed.Bug.Bugreport;
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

public class GroupInfoFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener {
    View rootview;
    FragmentActivity myContext;
    static FragmentManager fragmgr;
    SimpleArcDialog mDialog;
    ImageView imageView;
    SessionManager sessionManager;
    TextView groupName, groupDesc, createdBy, createdDate, groupMemHeading, groupMemberCount;
    String strUser_ID, groupid, member_count;
    private AlertDialog alertDialog;
    ListView listView;
    ArrayList<GroupMember> groupMemberList;
    String receivedGid = "", receivedGname = "", receivedGdesc = "";
    private GoogleApiClient mGoogleApiClient;

    public static GroupInfoFragment newInstance(int sectionNumber) {
        GroupInfoFragment fragment = new GroupInfoFragment();
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.group_info_layout, container, false);
        sessionManager = new SessionManager(getActivity());
        HashMap<String, String> user = sessionManager.getUserDetails();
        strUser_ID = user.get(sessionManager.USER_ID);
        TaggedneedsActivity.updateTitle(getResources().getString(R.string.group_info));
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
        mGoogleApiClient = ((TaggedneedsActivity) getActivity()).mGoogleApiClient;

        HashMap<String, String> group = sessionManager.getSelectedGroupDetails();
        receivedGid = group.get(sessionManager.GROUP_ID);
        if (!(Validation.isNetworkAvailable(getActivity()))) {
            ToastPopUp.show(getActivity(), getString(R.string.network_validation));
        } else {
            groupInfo(strUser_ID, receivedGid);
            getMemberListForMember(strUser_ID, receivedGid, "0");
        }
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
        imageView = (ImageView) rootview.findViewById(R.id.groupinfo_profile_image);
        groupName = (TextView) rootview.findViewById(R.id.tv_groupname);
        groupDesc = (TextView) rootview.findViewById(R.id.tv_groupdesc);
        createdBy = (TextView) rootview.findViewById(R.id.tv_created_by);
        createdDate = (TextView) rootview.findViewById(R.id.tv_created_date);
        groupMemHeading = (TextView) rootview.findViewById(R.id.group_member_heading);
        groupMemberCount = (TextView) rootview.findViewById(R.id.group_member_count);
        listView = (ListView) rootview.findViewById(R.id.group_member_list);
    }

    //---------------------getting group info from server-----------------------------------------------
    public void groupInfo(String user_id, String group_id) {
        mDialog.setConfiguration(new ArcConfiguration(getContext()));
        mDialog.show();
        mDialog.setCancelable(false);
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        GroupInfoInterface service = retrofit.create(GroupInfoInterface.class);
        Call<List<GroupInfoPOJO>> call = service.sendData(user_id, group_id);
        call.enqueue(new Callback<List<GroupInfoPOJO>>() {
            @Override
            public void onResponse(Response<List<GroupInfoPOJO>> response, Retrofit retrofit) {
                mDialog.dismiss();
                Log.d("responsegroupinfo", "" + response.body());
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
                        groupName.setText(groupInfoPOJO.get(0).getGroup_name());
                        createdBy.setText("Created by - " + groupInfoPOJO.get(0).getCreator_name());
                        createdDate.setText("Created date - " + groupInfoPOJO.get(0).getCreate_date());
                        if (groupInfoPOJO.get(0).getGroup_desc().length() == 0) {
                            groupDesc.setVisibility(View.GONE);
                        } else {
                            groupDesc.setVisibility(View.VISIBLE);
                            groupDesc.setText("Group description - \n" + groupInfoPOJO.get(0).getGroup_desc());
                        }

                        String strImagepath = WebServices.MAIN_SUB_URL + groupInfoPOJO.get(0).getGroup_image();
                        if (groupInfoPOJO.get(0).getGroup_image().length() > 0) {
                            Picasso.with(getContext()).load(strImagepath).placeholder(R.drawable.group_default_wallpaper).into(imageView);
                        } else {
                            imageView.setImageResource(R.drawable.group_default_wallpaper);
                            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        }
                    }
                } catch (Exception e) {
                    Log.d("responsegroupinfo", "" + e.getMessage());
                    StringWriter writer = new StringWriter();
                    e.printStackTrace(new PrintWriter(writer));
                    Bugreport bg = new Bugreport();
                    bg.sendbug(writer.toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                mDialog.dismiss();
                Log.d("responsegroupinfo", "" + t.getMessage());
                ToastPopUp.show(myContext, getString(R.string.server_response_error));
            }
        });
    }

    //---------------------get member list for admin from server-----------------------------------------------
    public void getMemberListForMember(String user_id, String group_id, String role) {
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
                        FacebookSdk.sdkInitialize(getActivity());
                        Toast.makeText(getContext(), getResources().getString(R.string.block_toast), Toast.LENGTH_SHORT).show();
                        sessionManager.createUserCredentialSession(null, null, null);
                        LoginManager.getInstance().logOut();

                        sessionManager.set_notification_status("ON");

                        /*Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                                new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(Status status) {
                                        //updateUI(false);
                                    }
                                });*/

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
                                groupMemberList.add(member);
                            }

                            if (groupMemberList.size() > 0) {
                                groupMemberCount.setVisibility(View.VISIBLE);
                                groupMemberCount.setText(member_count);
                                groupMemHeading.setText("Total members");
                                listView.setVisibility(View.VISIBLE);
                                listView.setAdapter(new GroupMemberListAdapter(groupMemberList, myContext));
                            } else {
                                listView.setVisibility(View.GONE);
                                groupMemberCount.setVisibility(View.GONE);
                                groupMemHeading.setText("No members found");
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
                    FragmentTransaction fragmentTransaction =
                            getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, groupDetailsFragment);
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
