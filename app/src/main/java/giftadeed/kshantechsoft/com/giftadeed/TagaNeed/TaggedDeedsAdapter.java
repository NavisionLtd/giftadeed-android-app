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
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;

public class TaggedDeedsAdapter extends BaseAdapter {
    ArrayList<TaggedDeedsPojo> list = new ArrayList<>();
    Context context;
    int qty;

    public TaggedDeedsAdapter(ArrayList<TaggedDeedsPojo> subcategories, Context context) {
        this.list = subcategories;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.already_deeds_item, null);
        ImageView itemicon = (ImageView) view.findViewById(R.id.item_icon);
        TextView tagcatname = (TextView) view.findViewById(R.id.tag_cat_name);
        TextView tagsubtypes = (TextView) view.findViewById(R.id.tag_sub_types);

        tagcatname.setText(list.get(i).getNeedname());
        if (list.get(i).getSubtypes().length() > 0) {
            tagsubtypes.setText("(" + list.get(i).getSubtypes() + ")");
        } else {
            tagsubtypes.setText("");
        }
        String path = WebServices.MAIN_SUB_URL + list.get(i).getIconpath();
        Picasso.with(context).load(path).resize(50, 50).into(itemicon);

        notifyDataSetChanged();
        return view;
    }
}
