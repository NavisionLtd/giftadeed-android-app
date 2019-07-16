package giftadeed.kshantechsoft.com.giftadeed.Notifications;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.List;

import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsActivity;

/**
 * Created by I-Sys on 30-Aug-17.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> implements GoogleApiClient.OnConnectionFailedListener {
    List<Notification> item;
    Context context;
    static FragmentManager fragmgr;
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
        if (item.get(position).getSeen().equals("0")) {
            holder.title.setTypeface(Typeface.DEFAULT_BOLD);
        } else {
            holder.title.setTypeface(Typeface.DEFAULT);
        }
        if (item.get(position).getNtType().equals("new")) {
            holder.title.setText("There was a new tag for " + item.get(position).getNeedName() + " near you");
//            holder.title.setTextColor(Color.parseColor("#f087d2"));
        } else {
            holder.title.setText("Your tag for " + item.get(position).getNeedName() + " was fulfilled");
//            holder.title.setTextColor(Color.parseColor("#7dcb87"));
        }

        holder.date.setText(item.get(position).getDate());
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
        TextView title, date;

        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.txt_title);
            date = (TextView) view.findViewById(R.id.txt_date);
        }
    }
}
