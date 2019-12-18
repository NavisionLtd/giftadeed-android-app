/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.Mytags;

import android.content.Context;
import android.graphics.Color;

import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import giftadeed.kshantechsoft.com.giftadeed.Bug.Bugreport;
import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.list_Model.Taggedlist;
import giftadeed.kshantechsoft.com.giftadeed.Utils.FontDetails;
import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import giftadeed.kshantechsoft.com.giftadeed.taggerfullfiller.AdapterToptenFullfiller;
import giftadeed.kshantechsoft.com.giftadeed.taggerfullfiller.RESULTFFILLER;

import static giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices.MANI_URL;
import static giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices.SUB_URL;

/**
 * Created by Nilesh on 5/16/2017.
 */

public class AdapterMyTags extends RecyclerView.Adapter<AdapterMyTags.RecyclerViewHolder> {
    Context context;
    OnItemClickListener mItemClickListener;
    List<Taggedlist> lstdetail = new ArrayList<Taggedlist>();

    public AdapterMyTags(Context context, List<Taggedlist> lstdetail) {

        this.context = context;
        this.lstdetail = lstdetail;


    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        View view = mInflater.inflate(R.layout.cardlayout_mytags, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        Taggedlist model = lstdetail.get(position);
        RecyclerViewHolder mainHolder = (RecyclerViewHolder) holder;// holder
        // mainHolder.txtMyTagsTitle.setTypeface(FontDetails.fontStandardForPage);
        mainHolder.txtMyTagsTitle.setText(model.getTaggedTitle());
        mainHolder.txtMytagsAddress.setTypeface(FontDetails.fontStandardForPage);
        mainHolder.txtstatus.setTypeface(FontDetails.fontStandardForPage);
        if (model.getTagStatus().equals("No")) {
            mainHolder.txtstatus.setText("Fulfilled");
            mainHolder.txtstatus.setTextColor(Color.parseColor("#73c67b"));
        } else {
            mainHolder.txtstatus.setText("Unfulfilled");
            mainHolder.txtstatus.setTextColor(Color.parseColor("#f84048"));
        }
        mainHolder.mytagsviews.setText(model.getViews().toString());
        mainHolder.mytagsendorse.setText(model.getEndorse().toString());
        mainHolder.txtMytagsAddress.setText(model.getAddress().trim());
        Picasso.with(context).load(MANI_URL + SUB_URL + model.getCharacterPath()).resize(50, 50).into(mainHolder.imgchar);
        mainHolder.txtMytagsDate.setTypeface(FontDetails.fontStandardForPage);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(model.getTaggedDatetime());
        } catch (ParseException e) {
            e.printStackTrace();
            StringWriter writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));
            Bugreport bg = new Bugreport();
            bg.sendbug(writer.toString());
        }

        sdf = new SimpleDateFormat("dd-MMM-yyyy");
        System.out.println(sdf.format(date));
        holder.txtMytagsDate.setText(sdf.format(date));
        String strImagepath = MANI_URL + SUB_URL + model.getTaggedPhotoPath();
        // mainHolder.txtMytagsDate.setText(model.getTaggedDatetime());
        if (strImagepath.length() > 57) {

            try {

                Picasso.with(context).load(MANI_URL + SUB_URL + model.getTaggedPhotoPath()).placeholder(R.drawable.pictu).into(mainHolder.imgMytags);
            } catch (Exception e) {
                // Picasso.with(context).load(R.drawable.pictu).resize(100, 100).into(mainHolder.imgMytags);
                StringWriter writer = new StringWriter();
                e.printStackTrace(new PrintWriter(writer));
                Bugreport bg = new Bugreport();
                bg.sendbug(writer.toString());
                //mainHolder.imgMytags.setImageResource(R.drawable.pictu);
                //mainHolder.imgMytags.setScaleType(ImageView.ScaleType.FIT_XY);
            }
        } else {
            // Picasso.with(context).load(R.drawable.pictu).resize(100, 100).into(mainHolder.imgMytags);
            mainHolder.imgMytags.setImageResource(R.drawable.pictu);
            mainHolder.imgMytags.setScaleType(ImageView.ScaleType.FIT_XY);
        }
    }

    @Override
    public int getItemCount() {
        return lstdetail.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {


        public TextView txtMyTagsTitle, txtMytagsAddress, txtMytagsDate, txtstatus, mytagsviews, mytagsendorse;
        public ImageView imgMytags, imgchar;


        public RecyclerViewHolder(View view) {
            super(view);
            txtMyTagsTitle = (TextView) view.findViewById(R.id.txtmytagTitle);
            txtMytagsAddress = (TextView) view.findViewById(R.id.txtmytagsAddress);
            txtMytagsDate = (TextView) view.findViewById(R.id.txtmytagsdatentime);
            imgMytags = (ImageView) view.findViewById(R.id.imgMytagsList);
            imgchar = (ImageView) view.findViewById(R.id.imgmytagschar);
            txtstatus = (TextView) view.findViewById(R.id.txtmytagsstatus);
            mytagsviews = view.findViewById(R.id.mytagsviews);
            mytagsendorse = view.findViewById(R.id.mytagsendorse);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClick(v, getPosition());
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}
