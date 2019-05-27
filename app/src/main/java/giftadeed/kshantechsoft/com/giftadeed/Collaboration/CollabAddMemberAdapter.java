package giftadeed.kshantechsoft.com.giftadeed.Collaboration;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import static giftadeed.kshantechsoft.com.giftadeed.Collaboration.AddCollabMemberFragment.selectedCreatorIds;

import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.Utils.ToastPopUp;

public class CollabAddMemberAdapter extends RecyclerView.Adapter<CollabAddMemberAdapter.ViewHolder> {
    ArrayList<Creatorslist> creatorlist = new ArrayList<>();
    private ArrayList<Creatorslist> arraylist;
    Context context;

    public CollabAddMemberAdapter(ArrayList<Creatorslist> list, Context context) {
        this.creatorlist = list;
        this.context = context;
        this.arraylist = new ArrayList<Creatorslist>();
        this.arraylist.addAll(list);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_creators_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.cName.setText(creatorlist.get(position).getFirstName() + " " + creatorlist.get(position).getLastName());
        holder.cGroups.setText(creatorlist.get(position).getGroupNames());

        if (creatorlist.get(position).isSelected()) {
            holder.chkCreator.setChecked(true);
        } else {
            holder.chkCreator.setChecked(false);
        }

        holder.chkCreator.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    selectedCreatorIds.add(creatorlist.get(position).getUserId());
                    creatorlist.get(position).setSelected(true);
//                    ToastPopUp.displayToast(context, "Added " + creatorlist.get(position).getUserId());
                } else {
                    selectedCreatorIds.remove(creatorlist.get(position).getUserId());
                    creatorlist.get(position).setSelected(true);
//                    ToastPopUp.displayToast(context, "Removed " + creatorlist.get(position).getUserId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return creatorlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView cName, cGroups;
        CheckBox chkCreator;

        public ViewHolder(View view) {
            super(view);
            cName = (TextView) view.findViewById(R.id.tv_creator_name);
            cGroups = (TextView) view.findViewById(R.id.tv_creator_groups);
            chkCreator = (CheckBox) view.findViewById(R.id.chk_creator);
        }
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        creatorlist.clear();
        if (charText.length() == 0) {
            creatorlist.addAll(arraylist);
        } else {
            for (Creatorslist wp : arraylist) {
                if (wp.getGroupNames().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    creatorlist.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
