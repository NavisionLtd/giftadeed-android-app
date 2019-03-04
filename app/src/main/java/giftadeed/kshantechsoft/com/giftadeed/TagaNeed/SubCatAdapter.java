package giftadeed.kshantechsoft.com.giftadeed.TagaNeed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import giftadeed.kshantechsoft.com.giftadeed.R;

public class SubCatAdapter extends BaseAdapter {
    ArrayList<SubCategories> list = new ArrayList<>();
    Context context;
    int qty;

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
        final TextView subCatQty = (TextView) view.findViewById(R.id.tv_sub_cat_qty);
        subCatname.setText(list.get(i).getSubCatName());
        subCatQty.setText(""+list.get(i).getQty());

        imgPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qty = Integer.parseInt(subCatQty.getText().toString());
                if (qty >= 0) {
                    qty = qty + 1;
                    subCatQty.setText("" + qty);
                    list.get(i).setQty(qty);
                }
            }
        });

        imgMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qty = Integer.parseInt(subCatQty.getText().toString());
                if (qty > 0) {
                    qty = qty - 1;
                    subCatQty.setText("" + qty);
                    list.get(i).setQty(qty);
                }
            }
        });

        notifyDataSetChanged();
        return view;
    }
}
