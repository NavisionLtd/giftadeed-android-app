/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.TagaNeed;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import giftadeed.kshantechsoft.com.giftadeed.R;

public class SubCatAdapter extends BaseAdapter {
    ArrayList<SubCategories> list = new ArrayList<>();
    Context context;
//    int qty = 0;

    public SubCatAdapter(ArrayList<SubCategories> subcategories, Context context) {
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
        view = inflater.inflate(R.layout.sub_cat_item, null);
        TextView subCatname = (TextView) view.findViewById(R.id.sub_cat_name);
        ImageView imgPlus = (ImageView) view.findViewById(R.id.iv_plus);
        ImageView imgMinus = (ImageView) view.findViewById(R.id.iv_minus);
        final EditText subCatQty = (EditText) view.findViewById(R.id.tv_sub_cat_qty);
        subCatname.setText(list.get(i).getSubCatName());
        subCatQty.setText("" + list.get(i).getQty());

        imgPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* qty = Integer.parseInt(subCatQty.getText().toString());
                if ((qty >= 0) && (qty != 99999)) {
                    qty = qty + 1;
                    subCatQty.setText("" + qty);
                    list.get(i).setQty(qty);
                }*/
            }
        });

        imgMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*qty = Integer.parseInt(subCatQty.getText().toString());
                if (qty > 0) {
                    qty = qty - 1;
                    subCatQty.setText("" + qty);
                    list.get(i).setQty(qty);
                }*/
            }
        });

        subCatQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("text_watcher", "before");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                qty = Integer.parseInt(subCatQty.getText().toString());
//                list.get(i).setQty(qty);
                Log.d("text_watcher", "ontextchange");
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("text_watcher", "after");
            }
        });

        notifyDataSetChanged();
        return view;
    }
}
