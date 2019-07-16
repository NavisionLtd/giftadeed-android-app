package giftadeed.kshantechsoft.com.giftadeed.Collaboration;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import giftadeed.kshantechsoft.com.giftadeed.Group.RecyclerViewClickListener;
import giftadeed.kshantechsoft.com.giftadeed.R;

public class CollabListAdapter extends RecyclerView.Adapter<CollabListAdapter.ViewHolder> {
    ArrayList<Colablist> list = new ArrayList<>();
    Context context;

    public CollabListAdapter(ArrayList<Colablist> groups, Context context) {
        this.list = groups;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.colab_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String cname = list.get(position).getColabName();
        if (cname.length() > 50) {
            holder.colabName.setText(list.get(position).getColabName().substring(0, 50) + " ...");
        } else {
            holder.colabName.setText(list.get(position).getColabName());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView colabName;

        public ViewHolder(View view) {
            super(view);
            colabName = (TextView) view.findViewById(R.id.colab_name);
        }
    }
}
