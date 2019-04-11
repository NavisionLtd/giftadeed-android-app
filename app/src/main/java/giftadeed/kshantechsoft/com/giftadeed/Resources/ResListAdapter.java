package giftadeed.kshantechsoft.com.giftadeed.Resources;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import giftadeed.kshantechsoft.com.giftadeed.Group.RecyclerViewClickListener;
import giftadeed.kshantechsoft.com.giftadeed.R;

public class ResListAdapter extends RecyclerView.Adapter<ResListAdapter.ViewHolder> {
    ArrayList<ResourcePOJO> list = new ArrayList<>();
    Context context;
    private RecyclerViewClickListener mListener;

    public ResListAdapter(ArrayList<ResourcePOJO> resourcePOJOS, Context context) {
        this.list = resourcePOJOS;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.res_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String resname = list.get(position).getGroup_name();
        if (resname.length() > 50) {
            holder.resName.setText(list.get(position).getResName().substring(0, 50) + " ...");
        } else {
            holder.resName.setText(list.get(position).getResName());
        }
        holder.resAddress.setText(list.get(position).getAddress());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView resName, resAddress;

        public ViewHolder(View view) {
            super(view);
            resName = (TextView) view.findViewById(R.id.res_list_name);
            resAddress = (TextView) view.findViewById(R.id.res_list_address);
        }
    }
}
