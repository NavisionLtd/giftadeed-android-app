package giftadeed.kshantechsoft.com.giftadeed.Group;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;

public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.ViewHolder> {
    ArrayList<GroupPOJO> list = new ArrayList<>();
    Context context;
    private RecyclerViewClickListener mListener;

    public GroupListAdapter(ArrayList<GroupPOJO> groups, Context context) {
        this.list = groups;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String gname = list.get(position).getGroup_name();
        if (gname.length() > 50) {
            holder.groupName.setText(list.get(position).getGroup_name().substring(0, 50) + " ...");
        } else {
            holder.groupName.setText(list.get(position).getGroup_name());
        }
        String strImagepath = WebServices.MAIN_SUB_URL + list.get(position).getGroup_image();
        if (list.get(position).getGroup_image().length() > 0) {
            Picasso.with(context).load(strImagepath).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE).placeholder(R.drawable.group_default_icon).into(holder.groupImage);
        } else {
            holder.groupImage.setImageResource(R.drawable.group_default_icon);
            holder.groupImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }
//        holder.groupActiveTags.setText(list.get(position).getGroup_tags());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView groupName;
        ImageView groupImage;

        public ViewHolder(View view) {
            super(view);
            groupName = (TextView) view.findViewById(R.id.group_name);
            groupImage = (ImageView) view.findViewById(R.id.iv_group_image);
        }
    }
}
