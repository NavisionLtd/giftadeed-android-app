/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.taggerfullfiller;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.Utils.FontDetails;

import static giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices.MAIN_API_URL;
import static giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices.API_SUB_URL;

/**
 * Created by Nilesh on 5/13/2017.
 */

public class AdapterToptenFullfiller extends RecyclerView.Adapter<AdapterToptenFullfiller.RecyclerViewHolder> {

    Context context;
  OnItemClickListener mItemClickListener;
    List<RESULTFFILLER> lstdetail = new ArrayList<RESULTFFILLER>();

    public AdapterToptenFullfiller(Context context, List<RESULTFFILLER> lstdetail) {
        this.context = context;
        this.lstdetail = lstdetail;

    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        View view = mInflater.inflate(R.layout.card_layout_toptenfullfiller, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        RESULTFFILLER result = lstdetail.get(position);
       RecyclerViewHolder mainHolder = (RecyclerViewHolder) holder;// holder

        String fullName = result.getFirstName();
        String lastName = "";
        String output = "";
        if (result.getLastName().length() > 0) {
            // mainHolder.txt_tagged_last_name.setVisibility(View.VISIBLE);
            // mainHolder.txt_tagged_last_name.setText(result.getLastName());
            output = fullName.substring(0, 1).toUpperCase() + fullName.substring(1)+"."+result.getLastName().toUpperCase().charAt(0);
        }else {
            output = fullName.substring(0, 1).toUpperCase() + fullName.substring(1);
        }


        mainHolder.txttopTenFullfillerName.setText(output);
        mainHolder.txttoptenfullfillerrnkname.setText(result.getFullFillerRank());
        mainHolder.txtTopTenfullfillerpoints.setText(result.getTotalFullfillerPoints());
        mainHolder.txttoptenfullfillneeds.setText("Fulfilled Score");
        mainHolder.count_number.setText(String.valueOf(position+1));
        if (MAIN_API_URL + API_SUB_URL +result.getUrlFullfillerRank() != "") {
            try {
                System.out.print("image url" + MAIN_API_URL + API_SUB_URL +result.getUrlFullfillerRank());
                Log.d("image url", MAIN_API_URL + API_SUB_URL +result.getUrlFullfillerRank());
                Picasso.with(context).load(MAIN_API_URL + API_SUB_URL +result.getUrlFullfillerRank()).resize(60, 60).into(mainHolder.imgtopFullfiller);
            } catch (Exception es) {
                Picasso.with(context).load(MAIN_API_URL + API_SUB_URL +result.getUrlFullfillerRank()).resize(60, 60).into(mainHolder.imgtopFullfiller);
            }
        } else {
            Picasso.with(context).load(MAIN_API_URL + API_SUB_URL +result.getUrlFullfillerRank()).resize(60, 60).into(mainHolder.imgtopFullfiller);
        }
    }

    @Override
    public int getItemCount() {
        return lstdetail.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {


        public TextView txttopTenFullfillerName, txtTopTenfullfillerpoints, txttoptenfullfillerrnkname, txttoptenfullfillneeds,count_number;
        public ImageView imgtopFullfiller;


        public RecyclerViewHolder(View view) {
            super(view);
            txttopTenFullfillerName = (TextView) view.findViewById(R.id.txttopfullfillername);
            txtTopTenfullfillerpoints = (TextView) view.findViewById(R.id.txttopfullfillerpoints);
            txttoptenfullfillerrnkname = (TextView) view.findViewById(R.id.txttopfullfillrankname);
            imgtopFullfiller = (ImageView) view.findViewById(R.id.imgtopfullfiller);
            txttoptenfullfillneeds = (TextView) view.findViewById(R.id.txttopfullfillneeds);
            count_number=view.findViewById(R.id.count_number);
            txttopTenFullfillerName.setTypeface(new FontDetails(context).fontStandardForPage);
            txtTopTenfullfillerpoints.setTypeface(new FontDetails(context).fontStandardForPage);
            txttoptenfullfillerrnkname.setTypeface(new FontDetails(context).fontStandardForPage);
            txttoptenfullfillneeds.setTypeface(new FontDetails(context).fontStandardForPage);

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
