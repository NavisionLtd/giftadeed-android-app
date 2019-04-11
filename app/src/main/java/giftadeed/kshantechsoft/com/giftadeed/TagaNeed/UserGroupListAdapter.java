package giftadeed.kshantechsoft.com.giftadeed.TagaNeed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import giftadeed.kshantechsoft.com.giftadeed.Group.GroupPOJO;
import giftadeed.kshantechsoft.com.giftadeed.R;

public class UserGroupListAdapter extends BaseAdapter {
    ArrayList<GroupPOJO> list = new ArrayList<>();
    Context context;

    public UserGroupListAdapter(ArrayList<GroupPOJO> subcategories, Context context) {
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
        view = inflater.inflate(R.layout.user_group_name, null);
        TextView tvgroup = (TextView) view.findViewById(R.id.user_group_name);
        tvgroup.setText(list.get(i).getGroup_name());
        notifyDataSetChanged();
        return view;
    }
}
