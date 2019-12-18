/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.taggerfullfiller;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.Utils.FontDetails;

/**
 * Created by Nilesh on 5/11/2017.
 */

public class Adapter_TopTenTagger extends RecyclerView.Adapter<Adapter_TopTenTagger.RecyclerViewHolder> {
    Context context;
    OnItemClickListener mItemClickListener;
    List<RESULT> lstdetail = new ArrayList<>();

    public Adapter_TopTenTagger(Context context, List<RESULT> lstdetail) {
        this.context = context;
        this.lstdetail = lstdetail;

    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        View view = mInflater.inflate(R.layout.card_layout_tagger, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        RESULT result = lstdetail.get(position);
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
        mainHolder.txtTaggerFirstName.setText(output);

        mainHolder.txtPoints.setText(result.getTotalCreditPoint());
        mainHolder.textCounter.setText(String.valueOf(position + 1));


    }

    @Override
    public int getItemCount() {
        return lstdetail.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {


        public TextView txtTaggerFirstName, txtPoints, textCounter, txt_tagged_last_name;


        public RecyclerViewHolder(View view) {
            super(view);
            txtTaggerFirstName = (TextView) view.findViewById(R.id.txt_tagged_name);
            txtPoints = (TextView) view.findViewById(R.id.txt_tagged_points);
            txt_tagged_last_name = view.findViewById(R.id.txt_tagged_last_name);
            textCounter = view.findViewById(R.id.textCounter);
            txt_tagged_last_name.setTypeface(new FontDetails(context).fontStandardForPage);
            txtTaggerFirstName.setTypeface(new FontDetails(context).fontStandardForPage);
            txtPoints.setTypeface(new FontDetails(context).fontStandardForPage);

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
