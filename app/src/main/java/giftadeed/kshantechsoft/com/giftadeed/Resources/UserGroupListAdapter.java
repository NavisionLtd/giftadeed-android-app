package giftadeed.kshantechsoft.com.giftadeed.Resources;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.ArrayList;

import giftadeed.kshantechsoft.com.giftadeed.Group.GroupPOJO;
import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.Utils.SessionManager;

import static giftadeed.kshantechsoft.com.giftadeed.Resources.CreateResourceFragment.selectedGroups;
import static giftadeed.kshantechsoft.com.giftadeed.Resources.CreateResourceFragment.selectedGrpNames;

public class UserGroupListAdapter extends BaseAdapter {
    ArrayList<GroupPOJO> list = new ArrayList<>();
    Context context;
    SessionManager sessionManager;

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
        view = inflater.inflate(R.layout.user_org_item, null);
        CheckBox chk = (CheckBox) view.findViewById(R.id.chkbox);
        chk.setText(list.get(i).getGroup_name());
        sessionManager = new SessionManager(context);

        if (selectedGroups.contains(list.get(i).getGroup_id())) {
            chk.setChecked(true);
        }
        chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    selectedGroups.add(list.get(i).getGroup_id());
                    selectedGrpNames.add(list.get(i).getGroup_name());
                } else {
                    selectedGroups.remove(list.get(i).getGroup_id());
                    selectedGrpNames.remove(list.get(i).getGroup_name());
                }
            }
        });

        notifyDataSetChanged();
        return view;
    }
}
