package giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import giftadeed.kshantechsoft.com.giftadeed.Bug.Bugreport;
import giftadeed.kshantechsoft.com.giftadeed.Needdetails.NeedDetailsFrag;
import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.Utils.Validation;
import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;

/**
 * Created by I-Sys on 12-May-17.
 */

public class NeedListAdapter extends RecyclerView.Adapter<NeedListAdapter.ViewHolder> {
    List<RowData> item;
    Context context;
    static FragmentManager fragmgr;
    String tab = "";

    public NeedListAdapter(List<RowData> item, Context context, String tab) {
        this.item = item;
        this.context = context;
        this.tab = tab;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.needlistdata, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        try {
            Collections.sort(item, new Comparator<RowData>() {
                @Override
                public int compare(RowData c1, RowData c2) {
                    return Double.compare(c1.getDistance(), c2.getDistance());
                }
            });
            DecimalFormat df2 = new DecimalFormat("#.##");
            // df2.setMaximumFractionDigits(2);
            double dist = item.get(position).getDistance() / 1000;
            df2.format(dist);
            RowData rowData = item.get(position);
            String strdate = item.get(position).getDate();
            // strdate=strdate.substring(0,10);
            String strImagepath = WebServices.MAIN_SUB_URL + item.get(position).getImagepath();
            String strCharpath = WebServices.MAIN_SUB_URL + item.get(position).getCharacterPath();
            holder.title.setText(item.get(position).getTitle());
            holder.endorse.setText(item.get(position).getEndorse());
            holder.views.setText(item.get(position).getViews());
            // holder.date.setText(strdate);
            if (item.get(position).getAddress().length() > 50) {
                holder.address.setText(item.get(position).getAddress().substring(0, 50) + "...");
            } else {
                holder.address.setText(item.get(position).getAddress().trim());
            }
            holder.distance.setText(String.format("%.2f", dist) + " km(s) away");
            if (strImagepath.length() > 57) {
                Picasso.with(context).load(strImagepath).into(holder.imgview);
            } else {
//                holder.imgview.setImageResource(R.drawable.pictu);
//                holder.imgview.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }

            Picasso.with(context).load(strCharpath).into(holder.imgchar);
            String dateString = strdate;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            // use SimpleDateFormat to define how to PARSE the INPUT
            Date date = null;
            try {
                date = sdf.parse(dateString);
            } catch (ParseException e) {
//                e.printStackTrace();
//                StringWriter writer = new StringWriter();
//                e.printStackTrace(new PrintWriter(writer));
//                Bugreport bg = new Bugreport();
//                bg.sendbug(writer.toString());
            }

            sdf = new SimpleDateFormat("dd-MMM-yyyy");
            // System.out.println( sdf.format(date) );
            holder.date.setText(sdf.format(date));


            holder.btngift.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!(Validation.isNetworkAvailable(context))) {
                        Toast.makeText(context, context.getResources().getString(R.string.network_validation), Toast.LENGTH_SHORT).show();
                    } else {
                        int i = 7;
                        TaggedneedsActivity activity = (TaggedneedsActivity) v.getContext();
                        fragmgr = activity.getSupportFragmentManager();
                        Bundle bundle = new Bundle();
                        String str_address = item.get(position).getAddress();
                        String str_tagid = item.get(position).getTaggedID();
                        String str_geopoint = item.get(position).getGeopoint();
                        String str_taggedPhotoPath = item.get(position).getTaggedPhotoPath();
                        String str_description = item.get(position).getDescription();
                        String str_characterPath = item.get(position).getCharacterPath();
                        String str_fname = item.get(position).getFname();
                        String str_lname = item.get(position).getLname();
                        String str_privacy = item.get(position).getPrivacy();
                        String str_userID = item.get(position).getUserID();
                        String str_needName = item.get(position).getNeedName();
                        String str_totalTaggedCreditPoints = item.get(position).getTotalTaggedCreditPoints();
                        String str_endorse = item.get(position).getEndorse();
                        String str_title = item.get(position).getTitle();
                        String str_date = item.get(position).getDate();
                        String str_distance = Double.toString(item.get(position).getDistance());
                        bundle.putString("str_address", str_address);
                        bundle.putString("str_tagid", str_tagid);
                        bundle.putString("str_geopoint", str_geopoint);
                        bundle.putString("str_taggedPhotoPath", str_taggedPhotoPath);
                        bundle.putString("str_description", str_description);
                        bundle.putString("str_characterPath", str_characterPath);
                        // bundle.putString("str_fname", str_fname);
                        //  bundle.putString("str_lname", str_lname);
                        // bundle.putString("str_privacy", str_privacy);
                        bundle.putString("str_userID", str_userID);
                        bundle.putString("str_needName", str_needName);
                        //  bundle.putString("str_totalTaggedCreditPoints", str_totalTaggedCreditPoints);
                        bundle.putString("endorse", str_endorse);
                        bundle.putString("tab", tab);
                        //  bundle.putString("str_title", str_title);
                        bundle.putString("str_date", str_date);
                        bundle.putString("str_distance", str_distance);
                        NeedDetailsFrag fragInfo = new NeedDetailsFrag();
                        fragInfo.setArguments(bundle);
                        fragmgr.beginTransaction().replace(R.id.content_frame, fragInfo).commit();
                    }
/*
                TaggedneedsActivity activity = (TaggedneedsActivity) v.getContext();
                Bundle bundle = new Bundle();
                String str_address = item.get(position).getAddress();;
                bundle.putString("address", str_address);
                FragmentManager fragInfo =   activity.getSupportFragmentManager();
                fragInfo.setArguments(bundle);
                fragmgr.beginTransaction().replace(R.id.content_frame,fragInfo).commit();*/
                }
            });
        } catch (Exception e) {
//            e.printStackTrace();
//            StringWriter writer = new StringWriter();
//            e.printStackTrace(new PrintWriter(writer));
//            Bugreport bg = new Bugreport();
//            bg.sendbug(writer.toString());
        }
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, date, address, distance, views, endorse;
        ImageView imgview;
        CircleImageView imgchar;
        Button btngift;

        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.txtlisttitle);
            date = (TextView) view.findViewById(R.id.txtlistdate);
            address = (TextView) view.findViewById(R.id.txtlistaddress);
            distance = (TextView) view.findViewById(R.id.txtlistdistance);
            imgview = (ImageView) view.findViewById(R.id.imgneedlist);
            btngift = (Button) view.findViewById(R.id.btnlistneeds);
            imgchar = (CircleImageView) view.findViewById(R.id.imgneedlistchar);
            views = (TextView) view.findViewById(R.id.txttab2_view);
            endorse = (TextView) view.findViewById(R.id.txttab2_endorse);
        }
    }
}
