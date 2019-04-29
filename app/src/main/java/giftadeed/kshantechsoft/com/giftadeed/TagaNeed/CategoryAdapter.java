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

/**
 * Created by I-Sys on 21-Jan-17.
 */

public class CategoryAdapter extends BaseAdapter {
    ArrayList<Needtype> categories;
    Context context;
    private ArrayList<Needtype> arraylist;

    public CategoryAdapter(ArrayList<Needtype> categories, Context context) {
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
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view=inflater.inflate(R.layout.category_item,null);
        TextView storename= (TextView) view.findViewById(R.id.store_name);
        storename.setText(categories.get(i).getNeedName());
        notifyDataSetChanged();
        return view;
    }


    /*public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        categories.clear();
        if (charText.length() == 0) {
            categories.addAll(arraylist);
        }
        else
        {
            for (Needtype wp : arraylist)
            {
                if (wp.getNeedName().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    categories.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }*/
}
