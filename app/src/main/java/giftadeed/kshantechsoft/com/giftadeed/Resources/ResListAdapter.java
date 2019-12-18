/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.Resources;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import giftadeed.kshantechsoft.com.giftadeed.R;

public class ResListAdapter extends RecyclerView.Adapter<ResListAdapter.ViewHolder> {
    ArrayList<ResourcePOJO> list = new ArrayList<>();
    Context context;

    public ResListAdapter(ArrayList<ResourcePOJO> resourcePOJOS, Context context) {
        this.list = resourcePOJOS;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.res_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String resname = list.get(position).getResName();
        if (resname.length() > 50) {
            holder.resName.setText(list.get(position).getResName().substring(0, 50) + " ...");
        } else {
            holder.resName.setText(list.get(position).getResName());
        }
        holder.resNeeds.setText(list.get(position).getNeedName());
        holder.resGroup.setText(list.get(position).getGroup_name());
        holder.resDate.setText(list.get(position).getCreated_date());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView resName, resNeeds, resGroup, resDate;

        public ViewHolder(View view) {
            super(view);
            resName = (TextView) view.findViewById(R.id.res_list_name);
            resNeeds = (TextView) view.findViewById(R.id.res_list_needs);
            resGroup = (TextView) view.findViewById(R.id.res_list_addedby);
            resDate = (TextView) view.findViewById(R.id.res_list_created_date);
        }
    }
}
