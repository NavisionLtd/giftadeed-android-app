package giftadeed.kshantechsoft.com.giftadeed.Collaboration;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

import giftadeed.kshantechsoft.com.giftadeed.R;

import static giftadeed.kshantechsoft.com.giftadeed.Collaboration.AddCollabMemberFragment.selectedCreatorIds;

public class CollabAddMemberAdapter extends RecyclerView.Adapter<CollabAddMemberAdapter.ViewHolder> {
    ArrayList<Creatorslist> creatorlist = new ArrayList<>();
    private ArrayList<Creatorslist> arraylist;
    Context myContext;

    public CollabAddMemberAdapter(ArrayList<Creatorslist> list, Context context) {
        this.creatorlist = list;
        this.myContext = context;
        this.arraylist = new ArrayList<Creatorslist>();
        this.arraylist.addAll(list);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_creators_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.bindData(creatorlist.get(position));
        //in some cases, it will prevent unwanted situations
        holder.chkCreator.setOnCheckedChangeListener(null);
        //if true, your checkbox will be selected, else unselected
//        holder.chkCreator.setChecked(creatorlist.get(position).isSelected());

        if (creatorlist.get(position).getInvitedAlready().equals("YES")) {
            //if true, your checkbox will be selected, else unselected
            holder.chkCreator.setChecked(creatorlist.get(position).isSelected());
            holder.chkCreator.setEnabled(false);
            holder.cName.setTextColor(myContext.getResources().getColor(R.color.grey));
            holder.tvAlreadyInvited.setVisibility(View.VISIBLE);
        } else {
            //if true, your checkbox will be selected, else unselected
            holder.chkCreator.setChecked(creatorlist.get(position).isSelected());
            holder.chkCreator.setEnabled(true);
            holder.cName.setTextColor(myContext.getResources().getColor(R.color.colorPrimary));
            holder.tvAlreadyInvited.setVisibility(View.GONE);
        }

        holder.chkCreator.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                creatorlist.get(holder.getAdapterPosition()).setSelected(isChecked);
                if (isChecked) {
                    selectedCreatorIds.add(creatorlist.get(position).getUserId());
                } else {
                    selectedCreatorIds.remove(creatorlist.get(position).getUserId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return creatorlist.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView cName, cGroups, tvAlreadyInvited;
        CheckBox chkCreator;

        public ViewHolder(View view) {
            super(view);
            tvAlreadyInvited = (TextView) view.findViewById(R.id.tv_already_invited);
            cName = (TextView) view.findViewById(R.id.tv_creator_name);
            cGroups = (TextView) view.findViewById(R.id.tv_creator_groups);
            chkCreator = (CheckBox) view.findViewById(R.id.chk_creator);
        }

        public void bindData(Creatorslist creatorslist) {
            cName.setText(creatorslist.getFirstName() + " " + creatorslist.getLastName());
            cGroups.setText(creatorslist.getGroupNames());
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
