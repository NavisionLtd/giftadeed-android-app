package giftadeed.kshantechsoft.com.giftadeed.Resources;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.ArrayList;

import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.Needtype;

public class ResourceMultiCatAdapter extends RecyclerView.Adapter<ResourceMultiCatAdapter.ViewHolder> {
    ArrayList<Needtype> list = new ArrayList<>();
    Context context;

    public ResourceMultiCatAdapter(ArrayList<Needtype> categoryPOJOS, Context context) {
        this.list = categoryPOJOS;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.resource_cat_item, parent, false);
        return new ResourceMultiCatAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ResourceMultiCatAdapter.ViewHolder holder, final int position) {
        holder.mCheckedTextView.setText(list.get(position).getNeedName());

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
            mCheckedTextView = (CheckBox) itemView.findViewById(R.id.chk_catname);
        }
    }
}
