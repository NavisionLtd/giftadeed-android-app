package giftadeed.kshantechsoft.com.giftadeed.Collaboration;

import android.content.Context;
import android.content.Intent;
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
import com.squareup.okhttp.OkHttpClient;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import giftadeed.kshantechsoft.com.giftadeed.Bug.Bugreport;
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
    String userid;
    SimpleArcDialog mDialog;
    SessionManager sessionManager;
    private GoogleApiClient mGoogleApiClient;

    public CollabInvitesAdapter(String userid, ArrayList<Colabrequestlist> colabs, Context context) {
        this.userid = userid;
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
        holder.colabInviteFromDesc.setText(list.get(position).getColabDesc());
        holder.colabInviteFromStartDate.setText(list.get(position).getColabStartDate());

        holder.ivAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // call accept invite api. Send A for accept
                updateInviteRequest(userid, list.get(position).getId(), "A");
            }
        });

        holder.ivReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // call reject invite api. Send R for reject
                updateInviteRequest(userid, list.get(position).getId(), "R");
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
    public void updateInviteRequest(final String user_id, final String collabid, String invite_status) {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        mDialog.setConfiguration(new ArcConfiguration(context));
        mDialog.show();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.Accept_Reject_Request)
                .addConverterFactory(GsonConverterFactory.create()).build();
        ChangeInviteInterface service = retrofit.create(ChangeInviteInterface.class);
        Call<CollabResponseStatus> call = service.sendData(user_id, collabid, invite_status);
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
                            Toast.makeText(context, context.getResources().getString(R.string.accepted), Toast.LENGTH_SHORT).show();
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
}
