package giftadeed.kshantechsoft.com.giftadeed.Notifications;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import com.squareup.okhttp.OkHttpClient;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import giftadeed.kshantechsoft.com.giftadeed.Bug.Bugreport;
import giftadeed.kshantechsoft.com.giftadeed.Group.GroupResponseStatus;
import giftadeed.kshantechsoft.com.giftadeed.Login.LoginActivity;
import giftadeed.kshantechsoft.com.giftadeed.Needdetails.DeeddeletedInterface;
import giftadeed.kshantechsoft.com.giftadeed.Needdetails.DeeddeletedModel;
import giftadeed.kshantechsoft.com.giftadeed.Needdetails.NeedDetailsFrag;
import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.Resources.ResourceDetailsInterface;
import giftadeed.kshantechsoft.com.giftadeed.Resources.ResourcePOJO;
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

/**
 * Created by I-Sys on 30-Aug-17.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> implements GoogleApiClient.OnConnectionFailedListener {
    List<Notification> item;
    Context context;
    Fragment fragment;
    SessionManager sessionManager;
    static FragmentManager fragmgr;
    String strfreepaid, adress_show;
    SimpleArcDialog mDialog;
    private GoogleApiClient mGoogleApiClient;

    public NotificationAdapter(List<Notification> item, Context context) {
        this.item = item;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        mGoogleApiClient = ((TaggedneedsActivity) context).mGoogleApiClient;
        fragmgr = ((TaggedneedsActivity) context).getSupportFragmentManager();
        //String imgpath=item.get(position).get
        //adress_show = new GetingAddress(context).getCompleteAddressString(lat,longi);
        if (item.get(position).getNtType().equals("new")) {
            holder.title.setText("There was a new tag for " + item.get(position).getNeedName() + " near you");
            holder.title.setTextColor(Color.parseColor("#f087d2"));
            // holder.message.setText(item.get(position).getNtType());#7dcb87
        } else {
            holder.title.setText("Your tag for " + item.get(position).getNeedName() + " was fulfilled");
            holder.title.setTextColor(Color.parseColor("#7dcb87"));
        }
//--------------------------------data
        String dateString = item.get(position).getDate();
        String dateString_arr[] = dateString.split(" ");
        // holder.txt_date.setText(dateString_arr[0]);
//        holder.txt_time.setText(dateString_arr[1]+" "+dateString_arr[2]);

        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkActiveDeed(item.get(position).getTagid());
            }
        });
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        mGoogleApiClient.connect();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, message, txt_date, txt_time, txtfreepaid;
        ImageView imgview, imgchar;
        Button btngift;
        LinearLayout llayout;

        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.txt_title);
            /*message = (TextView) view.findViewById(R.id.txt_message);
            txt_date = (TextView) view.findViewById(R.id.txt_date);
            txt_time = (TextView) view.findViewById(R.id.txt_time);*/
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void checkActiveDeed(final String tagid) {
        mDialog = new SimpleArcDialog(context);
        mDialog.setConfiguration(new ArcConfiguration(context));
        mDialog.show();
        mDialog.setCancelable(false);
        sessionManager = new SessionManager(context);
        HashMap<String, String> user = sessionManager.getUserDetails();
        String strUser_ID = user.get(sessionManager.USER_ID);
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        DeeddeletedInterface service = retrofit.create(DeeddeletedInterface.class);
        Call<DeeddeletedModel> call = service.fetchData(tagid);
        call.enqueue(new Callback<DeeddeletedModel>() {
            @Override
            public void onResponse(Response<DeeddeletedModel> response, Retrofit retrofit) {
                Log.d("response_tagactive", "" + response.body());
                try {
                    DeeddeletedModel groupResponseStatus = response.body();
                    int isblock = 0;
                    try {
                        isblock = groupResponseStatus.getIsBlocked();
                    } catch (Exception e) {
                        isblock = 0;
                    }
                    if (isblock == 1) {
                        mDialog.dismiss();
                        FacebookSdk.sdkInitialize(context);
                        Toast.makeText(context, "You have been blocked", Toast.LENGTH_SHORT).show();
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
                        DeeddeletedModel statusModel = response.body();
                        int strstatus = statusModel.getIsDeleted();
                        if (strstatus == 0) {
                        mDialog.dismiss();
                        // show tag details
                            Bundle bundle = new Bundle();
                            bundle.putString("str_tagid", tagid);
                            bundle.putString("tab", "notification");
                            NeedDetailsFrag fragInfo = new NeedDetailsFrag();
                            fragInfo.setArguments(bundle);
                            fragmgr.beginTransaction().replace(R.id.content_frame, fragInfo).commit();
                        } else {
                            mDialog.dismiss();
                            Toast.makeText(context, "This deed does not exist anymore", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    Log.d("response_tagactive", "" + e.getMessage());
                    mDialog.dismiss();
//                    StringWriter writer = new StringWriter();
//                    e.printStackTrace(new PrintWriter(writer));
//                    Bugreport bg = new Bugreport();
//                    bg.sendbug(writer.toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("response_tagactive", "" + t.getMessage());
                mDialog.dismiss();
                ToastPopUp.show(context, context.getString(R.string.server_response_error));
            }
        });
    }
}
