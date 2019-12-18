/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.TagaNeed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.Signup.SignupPOJO;

/**
 * Created by I-Sys on 21-Jan-17.
 */

public class CustomCatAdapter extends RecyclerView.Adapter<CustomCatAdapter.ViewHolder> {
    ArrayList<CustomNeedtype> list = new ArrayList<>();

    public CustomCatAdapter(ArrayList<CustomNeedtype> categoryPOJOS) {
        this.list = categoryPOJOS;
    }

    @Override
    public CustomCatAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        return new CustomCatAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CustomCatAdapter.ViewHolder holder, final int position) {
        holder.bindData(list.get(position));
    }

    @Override
    public int getItemCount() {
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCatName;

        public ViewHolder(View view) {
            super(view);
            tvCatName = (TextView) itemView.findViewById(R.id.store_name);
        }

        public void bindData(CustomNeedtype customNeedtype) {
            tvCatName.setText(customNeedtype.getNeedName());
        }
    }
}