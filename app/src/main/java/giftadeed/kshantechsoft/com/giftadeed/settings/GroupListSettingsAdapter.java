/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.settings;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import giftadeed.kshantechsoft.com.giftadeed.Group.GroupPOJO;
import giftadeed.kshantechsoft.com.giftadeed.Group.RecyclerViewClickListener;
import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.Utils.DatabaseAccess;
import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;

public class GroupListSettingsAdapter extends RecyclerView.Adapter<GroupListSettingsAdapter.ViewHolder> {
    ArrayList<GroupPOJO> list = new ArrayList<>();
    Context context;
    private RecyclerViewClickListener mListener;
    DatabaseAccess databaseAccess;

    public GroupListSettingsAdapter(ArrayList<GroupPOJO> groups, Context context) {
        this.list = groups;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        databaseAccess = DatabaseAccess.getInstance(context);
        databaseAccess.open();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.settings_group_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.groupName.setText(list.get(position).getGroup_name());

        if (list.get(position).isChecked()) {
            holder.groupName.setChecked(true);
        } else {
            holder.groupName.setChecked(false);
        }

        holder.groupName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    list.get(position).setChecked(true);
                    databaseAccess.Update_Group_Details(list.get(position).getGroup_id(),"true");
                } else {
                    list.get(position).setChecked(false);
                    databaseAccess.Update_Group_Details(list.get(position).getGroup_id(),"false");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Switch groupName;

        public ViewHolder(View view) {
            super(view);
            groupName = (Switch) view.findViewById(R.id.switch_group_name);
        }
    }
}
