package giftadeed.kshantechsoft.com.giftadeed.Resources;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.Needtype;

public class ResourceMultiCatAdapter extends RecyclerView.Adapter<ResourceMultiCatAdapter.ViewHolder> {
    ArrayList<Needtype> list = new ArrayList<>();

    public ResourceMultiCatAdapter(ArrayList<Needtype> categoryPOJOS) {
        this.list = categoryPOJOS;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.resource_cat_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.bindData(list.get(position));
        //in some cases, it will prevent unwanted situations
        holder.mCheckedTextView.setOnCheckedChangeListener(null);
        //if true, your checkbox will be selected, else unselected
        holder.mCheckedTextView.setChecked(list.get(position).getChecked());

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
            mCheckedTextView = (CheckBox) itemView.findViewById(R.id.chk_catname);
        }

        public void bindData(Needtype needtype) {
            mCheckedTextView.setText(needtype.getNeedName());
        }
    }
}
