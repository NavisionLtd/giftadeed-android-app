package giftadeed.kshantechsoft.com.giftadeed.TagaNeed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.Signup.SignupPOJO;

/**
 * Created by I-Sys on 21-Jan-17.
 */

public class CustomCatAdapter extends BaseAdapter {
    ArrayList<CustomNeedtype> customCategories;
    Context context;
    private ArrayList<CustomNeedtype> arraylist;

    public CustomCatAdapter(ArrayList<CustomNeedtype> customCategories, Context context) {
        this.customCategories = customCategories;
        this.context = context;
        this.arraylist = new ArrayList<CustomNeedtype>();
        this.arraylist.addAll(customCategories);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return customCategories.size();
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
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view=inflater.inflate(R.layout.category_item,null);
        TextView storename= (TextView) view.findViewById(R.id.store_name);
        notifyDataSetChanged();
        storename.setText(customCategories.get(i).getNeedName());

        return view;
    }


    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        customCategories.clear();
        if (charText.length() == 0) {
            customCategories.addAll(arraylist);
        }
        else
        {
            for (CustomNeedtype wp : arraylist)
            {
                if (wp.getNeedName().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    customCategories.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
