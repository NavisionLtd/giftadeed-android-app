package giftadeed.kshantechsoft.com.giftadeed.Collaboration;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
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
import java.util.List;
import java.util.concurrent.TimeUnit;

import giftadeed.kshantechsoft.com.giftadeed.Bug.Bugreport;
import giftadeed.kshantechsoft.com.giftadeed.Group.GroupListInfo;
import giftadeed.kshantechsoft.com.giftadeed.Login.LoginActivity;
import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsActivity;
import giftadeed.kshantechsoft.com.giftadeed.Utils.DBGAD;
import giftadeed.kshantechsoft.com.giftadeed.Utils.SessionManager;
import giftadeed.kshantechsoft.com.giftadeed.Utils.ToastPopUp;
import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class CollabInvitesAdapter extends RecyclerView.Adapter<CollabInvitesAdapter.ViewHolder> {
    ArrayList<Colabrequestlist> list = new ArrayList<>();
    Context context;
    String strUser_ID;
    String collabCreatorID = "";
    SimpleArcDialog mDialog;
    SessionManager sessionManager;
    private GoogleApiClient mGoogleApiClient;
    private Fragment fragment;
    private List<GroupListInfo> lstGetChannelsList = new ArrayList<>();
    private String strClubName = "";

    public CollabInvitesAdapter(Fragment fragment, String userid, ArrayList<Colabrequestlist> colabs, Context context) {
        this.fragment = fragment;
        this.strUser_ID = userid;
        this.list = colabs;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.colab_invites_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        sessionManager = new SessionManager(context);
        mGoogleApiClient = ((TaggedneedsActivity) context).mGoogleApiClient;
        holder.colabInviteFromName.setText(list.get(position).getColabName());
        holder.colabInviteFromDesc.setText("Details : " + list.get(position).getColabDesc());
        holder.colabInviteFromStartDate.setText("Started on : " + list.get(position).getColabStartDate());

        holder.ivAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collabCreatorID = list.get(position).getCreator_id();
                try {
                    getChannelsDetails();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // Sendbird channel. Concat with GRP for Group and CLB for Collaboration
                strClubName = list.get(position).getColabName() + " - CLB" + list.get(position).getId();
                Log.d("club_channel_name", strClubName);
                // call accept invite api. Send A for accept
                updateInviteRequest(strUser_ID, list.get(position).getId(), "A");
            }
        });

        holder.ivReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // call reject invite api. Send R for reject
                updateInviteRequest(strUser_ID, list.get(position).getId(), "R");
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView colabInviteFromName, colabInviteFromDesc, colabInviteFromStartDate;
        ImageView ivAccept, ivReject;

        public ViewHolder(View view) {
            super(view);
            colabInviteFromName = (TextView) view.findViewById(R.id.invite_from_colab_name);
            colabInviteFromDesc = (TextView) view.findViewById(R.id.invite_from_colab_desc);
            colabInviteFromStartDate = (TextView) view.findViewById(R.id.invite_from_colab_startdate);
            ivAccept = (ImageView) view.findViewById(R.id.iv_accept_invite);
            ivReject = (ImageView) view.findViewById(R.id.iv_reject_invite);
        }
    }

    //---------------------call accept/reject invite status api-----------------------------------------------
    public void updateInviteRequest(final String user_id, final String collabid, final String invite_status) {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        mDialog = new SimpleArcDialog(context);
        mDialog.setConfiguration(new ArcConfiguration(context));
        mDialog.show();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        ChangeInviteInterface service = retrofit.create(ChangeInviteInterface.class);
        Call<CollabResponseStatus> call = service.sendData(user_id, collabid, invite_status);
        Log.d("input_accept_reject", user_id + ":" + collabid + ":" + invite_status);
        call.enqueue(new Callback<CollabResponseStatus>() {
            @Override
            public void onResponse(Response<CollabResponseStatus> response, Retrofit retrofit) {
                mDialog.dismiss();
                Log.d("response_invite_request", "" + response.body());
                try {
                    CollabResponseStatus collabResponseStatus = response.body();
                    int isblock = 0;
                    try {
                        isblock = collabResponseStatus.getIsBlocked();
                    } catch (Exception e) {
                        isblock = 0;
                    }
                    if (isblock == 1) {
                        FacebookSdk.sdkInitialize(context);
                        Toast.makeText(context, context.getResources().getString(R.string.block_toast), Toast.LENGTH_SHORT).show();
                        sessionManager.createUserCredentialSession(null, null, null);
                        LoginManager.getInstance().logOut();
                        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                                new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(Status status) {
                                        //updateUI(false);
                                    }
                                });
                        int i = new DBGAD(context).delete_row_message();
                        sessionManager.set_notification_status("ON");
                        Intent loginintent = new Intent(context, LoginActivity.class);
                        loginintent.putExtra("message", "Charity");
                        context.startActivity(loginintent);
                    } else {
                        if (collabResponseStatus.getStatus() == 1) {
                            Toast.makeText(context, "Status changed", Toast.LENGTH_SHORT).show();

                            ((CollabPendingInvitesFragment) fragment).getPendingInviteList();

                            //============add member in sendbird group channel==============
                            if (invite_status.equals("A")) {
                                if (lstGetChannelsList != null) {
                                    for (int i = 0; i < lstGetChannelsList.size(); i++) {
                                        Log.d("channel_name", lstGetChannelsList.get(i).getmChannelName());
                                        if (lstGetChannelsList.get(i).getmChannelName().equals(strClubName)) {
                                            Log.d("channel_url", lstGetChannelsList.get(i).getmUrl());
                                            inviteUser(lstGetChannelsList.get(i).getmUrl().toString(), strUser_ID);
                                        }
                                    }
                                }
                            }
                        } else if (collabResponseStatus.getStatus() == 0) {
                            Toast.makeText(context, context.getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    mDialog.dismiss();
                    Log.d("response_invite_request", "" + e.getMessage());
                    StringWriter writer = new StringWriter();
                    e.printStackTrace(new PrintWriter(writer));
                    Bugreport bg = new Bugreport();
                    bg.sendbug(writer.toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                mDialog.dismiss();
                Log.d("response_invite_request", "" + t.getMessage());
                ToastPopUp.show(context, context.getString(R.string.server_response_error));
            }
        });
    }

    public void getChannelsDetails() {
        //always use connect() along with any method of chat
        SendBird.connect(collabCreatorID, new SendBird.ConnectHandler() {
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
        SendBird.connect(collabCreatorID, new SendBird.ConnectHandler() {
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
