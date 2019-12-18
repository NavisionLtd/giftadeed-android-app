/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.Filter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.Signup.SignupPOJO;
import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.Needtype;
import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;

/**
 * Created by I-Sys on 27-Mar-18.
 */

public class CategoriesAdaptor extends BaseAdapter {
    ArrayList<Needtype> categories;
    Context context;
    private ArrayList<Needtype> arraylist;

    public CategoriesAdaptor(ArrayList<Needtype> categories, Context context) {
        this.categories = categories;
        this.context = context;
        this.arraylist = new ArrayList<Needtype>();
        this.arraylist.addAll(categories);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return categories.size();
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
        view = inflater.inflate(R.layout.select_category_item, null);
        TextView storename = (TextView) view.findViewById(R.id.category_name);
        ImageView cat_img = view.findViewById(R.id.categoryimg);
        notifyDataSetChanged();
        storename.setText(categories.get(i).getNeedName());
        Log.d("cat_names", "" + categories.get(i).getNeedName());
        Picasso.with(context).load(WebServices.MAIN_SUB_URL + categories.get(i).getCharacterPath()).into(cat_img);
        return view;
    }


    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        categories.clear();
        if (charText.length() == 0) {
            categories.addAll(arraylist);
        } else {
            for (Needtype wp : arraylist) {
                if (wp.getNeedName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    categories.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
