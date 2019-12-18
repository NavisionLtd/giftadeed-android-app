/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.Resources;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import giftadeed.kshantechsoft.com.giftadeed.Group.GroupPOJO;
import giftadeed.kshantechsoft.com.giftadeed.R;

public class UserGroupListAdapter extends RecyclerView.Adapter<UserGroupListAdapter.ViewHolder> {
    ArrayList<GroupPOJO> list = new ArrayList<>();

    public UserGroupListAdapter(ArrayList<GroupPOJO> subcategories) {
        this.list = subcategories;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_org_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.bindData(list.get(position));
        //in some cases, it will prevent unwanted situations
        holder.mCheckedTextView.setOnCheckedChangeListener(null);
        //if true, your checkbox will be selected, else unselected
        holder.mCheckedTextView.setChecked(list.get(position).isChecked());

        holder.mCheckedTextView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                list.get(holder.getAdapterPosition()).setChecked(isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox mCheckedTextView;

        public ViewHolder(View view) {
            super(view);
            mCheckedTextView = (CheckBox) itemView.findViewById(R.id.chkbox);
        }

        public void bindData(GroupPOJO groupPOJO) {
            mCheckedTextView.setText(groupPOJO.getGroup_name());
        }
    }

    /*@Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.user_org_item, null);
        CheckBox chk = (CheckBox) view.findViewById(R.id.chkbox);
        chk.setText(list.get(i).getGroup_name());

        if (selectedGroups.contains(list.get(i).getGroup_id())) {
            chk.setChecked(true);
        }
        chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    selectedGroups.add(list.get(i).getGroup_id());
                    selectedGrpNames.add(list.get(i).getGroup_name());
                } else {
                    selectedGroups.remove(list.get(i).getGroup_id());
                    selectedGrpNames.remove(list.get(i).getGroup_name());
                }
            }
        });

        notifyDataSetChanged();
        return view;
    }*/
}
