/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.Resources;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import giftadeed.kshantechsoft.com.giftadeed.Group.GroupPOJO;
import giftadeed.kshantechsoft.com.giftadeed.R;

/**
 * Created by I-Sys on 21-Jan-17.
 */

public class OwnedGroupsAdapter extends BaseAdapter {
    ArrayList<GroupPOJO> groups;
    Context context;
    private ArrayList<GroupPOJO> arraylist;

    public OwnedGroupsAdapter(ArrayList<GroupPOJO> groups, Context context) {
        this.groups = groups;
        this.context = context;
        this.arraylist = new ArrayList<GroupPOJO>();
        this.arraylist.addAll(groups);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return groups.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.user_group_item, null);
        TextView groupname = (TextView) view.findViewById(R.id.user_group_name);
        notifyDataSetChanged();
        groupname.setText(groups.get(i).getGroup_name());
        return view;
    }


    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        groups.clear();
        if (charText.length() == 0) {
            groups.addAll(arraylist);
        } else {
            for (GroupPOJO wp : arraylist) {
                if (wp.getGroup_name().toLowerCase(Locale.getDefault()).contains(charText)) {
                    groups.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
