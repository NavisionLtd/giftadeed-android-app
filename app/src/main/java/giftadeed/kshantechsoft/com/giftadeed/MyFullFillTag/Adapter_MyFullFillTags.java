/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.MyFullFillTag;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.list_Model.Taggedlist;
import giftadeed.kshantechsoft.com.giftadeed.Utils.FontDetails;

import static giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices.MANI_URL;
import static giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices.SUB_URL;

/**
 * Created by Nilesh on 5/22/2017.
 */

public class Adapter_MyFullFillTags extends RecyclerView.Adapter<Adapter_MyFullFillTags.RecyclerViewHolder> {
    Context context;
    List<Taggedlist> lstdetail = new ArrayList<Taggedlist>();
    OnItemClickListener mItemClickListener;

    Adapter_MyFullFillTags(Context context, List<Taggedlist> lstDetail) {
        this.context = context;
        this.lstdetail = lstDetail;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        View view = mInflater.inflate(R.layout.card_myfullfilltags_lyt, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        Taggedlist model = lstdetail.get(position);
        RecyclerViewHolder mainHolder = (RecyclerViewHolder) holder;// holder
        //  mainHolder.txtmyFullflltagTitle.setTypeface(FontDetails.fontStandardForPage);
        mainHolder.txtmyFulllfilltagsAddress.setTypeface(FontDetails.fontStandardForPage);
        mainHolder.txtmyFullfilltagsdatentime.setTypeface(FontDetails.fontStandardForPage);
//        mainHolder.txtmyFullfilltagpoints.setTypeface(FontDetails.fontStandardForPage);
        //--------------------------------add data----------------------------------------
        mainHolder.txtmyFullflltagTitle.setText(model.getTaggedTitle());
        mainHolder.txtmyFulllfilltagsAddress.setText(model.getAddress().trim());
        mainHolder.txtmyfulfilviews.setText(model.getViews());
        mainHolder.txtmyfulfilendorse.setText(model.getEndorse());
        Picasso.with(context).load(MANI_URL + SUB_URL + model.getCharacterPath()).resize(50, 50).into(mainHolder.imgmyfullfilltagschar);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(model.getFullFilledDatetime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sdf = new SimpleDateFormat("dd-MMM-yyyy");
        System.out.println(sdf.format(date));
        holder.txtmyFullfilltagsdatentime.setText(sdf.format(date));

        // mainHolder.txtmyFullfilltagsdatentime.setText(model.getFullFilledDatetime());
        //mainHolder.txtmyFullfilltagpoints.setText(model.getFullFilledPoints());


        String strImagepath = MANI_URL + SUB_URL + model.getFullFilledPhotoPath();
        // mainHolder.txtMytagsDate.setText(model.getTaggedDatetime());
        if (strImagepath.length() > 57) {
            try {
                Picasso.with(context).load(MANI_URL + SUB_URL + model.getFullFilledPhotoPath()).placeholder(R.drawable.pictu).into(mainHolder.imgMyFullfilltags);
            } catch (Exception es) {
                // Picasso.with(context).load(R.drawable.pictu).resize(100, 100).into(mainHolder.imgMyFullfilltags);
                mainHolder.imgMyFullfilltags.setImageResource(R.drawable.pictu);
//                mainHolder.imgMyFullfilltags.setScaleType(ImageView.ScaleType.FIT_XY);
            }
        } else {
            // Picasso.with(context).load(R.drawable.pictu).resize(100, 100).into(mainHolder.imgMyFullfilltags);
            mainHolder.imgMyFullfilltags.setImageResource(R.drawable.pictu);
//            mainHolder.imgMyFullfilltags.setScaleType(ImageView.ScaleType.FIT_XY);
        }
    }

    @Override
    public int getItemCount() {
        return lstdetail.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {


        public TextView txtmyFullflltagTitle, txtmyFulllfilltagsAddress, txtmyFullfilltagsdatentime, txtmyFullfilltagpoints, txtmyfulfilendorse, txtmyfulfilviews;
        public ImageView imgMyFullfilltags, imgmyfullfilltagschar;


        public RecyclerViewHolder(View view) {
            super(view);
            txtmyFullflltagTitle = (TextView) view.findViewById(R.id.txtmyFullflltagTitle);
            txtmyFulllfilltagsAddress = (TextView) view.findViewById(R.id.txtmyFulllfilltagsAddress);
            txtmyFullfilltagsdatentime = (TextView) view.findViewById(R.id.txtmyFullfilltagsdatentime);
            // txtmyFullfilltagpoints = (TextView) view.findViewById(R.id.txtmyFullfilltagpoints);
            imgMyFullfilltags = (ImageView) view.findViewById(R.id.imgMyFullFilltags);
            imgmyfullfilltagschar = (ImageView) view.findViewById(R.id.imgmyfullfilltagschar);
            txtmyfulfilendorse = view.findViewById(R.id.txtmyfulfilendorse);
            txtmyfulfilviews = view.findViewById(R.id.txtmyfulfilviews);

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
