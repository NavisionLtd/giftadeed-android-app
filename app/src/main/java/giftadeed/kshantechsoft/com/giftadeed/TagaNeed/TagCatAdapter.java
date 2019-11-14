package giftadeed.kshantechsoft.com.giftadeed.TagaNeed;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import giftadeed.kshantechsoft.com.giftadeed.R;

public class TagCatAdapter extends RecyclerView.Adapter<TagCatAdapter.ViewHolder> {
    ArrayList<Needtype> list = new ArrayList<>();

    public TagCatAdapter(ArrayList<Needtype> categoryPOJOS) {
        this.list = categoryPOJOS;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.bindData(list.get(position));
    }

    @Override
    public int getItemCount() {
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCatName;

        public ViewHolder(View view) {
            super(view);
            tvCatName = (TextView) itemView.findViewById(R.id.store_name);
        }

        public void bindData(Needtype needtype) {
            tvCatName.setText(needtype.getNeedName());
        }
    }
}
