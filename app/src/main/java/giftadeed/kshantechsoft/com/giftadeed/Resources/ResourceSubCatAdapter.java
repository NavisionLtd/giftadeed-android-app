package giftadeed.kshantechsoft.com.giftadeed.Resources;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.ArrayList;

import giftadeed.kshantechsoft.com.giftadeed.R;

public class ResourceSubCatAdapter extends RecyclerView.Adapter<ResourceSubCatAdapter.ViewHolder> {
    ArrayList<MultiSubCategories> list = new ArrayList<>();
    Context context;

    public ResourceSubCatAdapter(ArrayList<MultiSubCategories> subcategories, Context context) {
        this.list = subcategories;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.resource_sub_cat_item, parent, false);
        return new ResourceSubCatAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ResourceSubCatAdapter.ViewHolder holder, final int position) {
        holder.mCheckedTextView.setText(String.valueOf(list.get(position).getNeedname()) + " : " +String.valueOf(list.get(position).getSubCatName()));

        if (list.get(position).getChecked()) {
            holder.mCheckedTextView.setChecked(true);
        } else {
            holder.mCheckedTextView.setChecked(false);
        }

        holder.mCheckedTextView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    list.get(position).setChecked(true);
                } else {
                    list.get(position).setChecked(false);
                }
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox mCheckedTextView;

        public ViewHolder(View view) {
            super(view);
            mCheckedTextView = (CheckBox) itemView.findViewById(R.id.chk_subcatname);
        }
    }
}
