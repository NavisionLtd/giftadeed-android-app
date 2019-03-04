package giftadeed.kshantechsoft.com.giftadeed.Group;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import giftadeed.kshantechsoft.com.giftadeed.MyProfile.Profile;
import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsActivity;
import giftadeed.kshantechsoft.com.giftadeed.Utils.ToastPopUp;
import giftadeed.kshantechsoft.com.giftadeed.Utils.Validation;

public class FilterGroupMemberAdapter extends BaseAdapter {
    Context mContext;
    LayoutInflater inflater;
    private List<GroupMember> memberList = null;
    private ArrayList<GroupMember> arraylist;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    public static final String DATABASE_PROFILE_PIC_UPLOADS = "users";
    private List<Profile> profileList;

    public FilterGroupMemberAdapter(Context context,
                                    List<GroupMember> list) {
        mContext = context;
        this.memberList = list;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<GroupMember>();
        this.arraylist.addAll(list);
    }

    public class ViewHolder {
        ImageView userImage;
        TextView username;
        TextView useremail;
        TextView role;
        LinearLayout threeDot;
    }

    @Override
    public int getCount() {
        return memberList.size();
    }

    @Override
    public GroupMember getItem(int position) {
        return memberList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.filter_listview_item, null);
            holder.userImage = (ImageView) view.findViewById(R.id.group_member_image);
            holder.username = (TextView) view.findViewById(R.id.group_member_name);
            holder.role = (TextView) view.findViewById(R.id.member_role);
            holder.useremail = (TextView) view.findViewById(R.id.group_member_email);
            holder.threeDot = (LinearLayout) view.findViewById(R.id.three_dot);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.username.setText(memberList.get(position).getMembername());
        holder.useremail.setText(memberList.get(position).getMemberemail());
        if (memberList.get(position).getPrivilege().equals("C")) {
            holder.threeDot.setVisibility(View.GONE);
            holder.role.setVisibility(View.VISIBLE);
            holder.role.setText("Creator");
        } else if (memberList.get(position).getPrivilege().equals("A")) {
            holder.threeDot.setVisibility(View.VISIBLE);
            holder.role.setVisibility(View.VISIBLE);
            holder.role.setText("Admin");
        } else {
            holder.threeDot.setVisibility(View.VISIBLE);
            holder.role.setVisibility(View.GONE);
        }

        if (!(Validation.isOnline(mContext))) {
            ToastPopUp.show(mContext, mContext.getString(R.string.network_validation));
        } else {
            mFirebaseInstance = FirebaseDatabase.getInstance();
            mFirebaseDatabase = mFirebaseInstance.getReference(DATABASE_PROFILE_PIC_UPLOADS);
            profileList = new ArrayList<>();
            DatabaseReference reference = mFirebaseDatabase.child("profile");
            //adding an event listener to fetch values
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    //iterating through all the values in database
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        Profile profile = postSnapshot.getValue(Profile.class);
                        profileList.add(profile);
                    }
                    Log.d("profile_list", "" + profileList.size());
                    if (profileList.size() > 0) {
                        for (Profile profile : profileList) {
                            if (profile.getUserid().equals(memberList.get(position).getMemberid())) {
                                Picasso.with(mContext).load(profile.getPhotourl()).placeholder(R.drawable.group_member).into(holder.userImage);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        memberList.clear();
        if (charText.length() == 0) {
            memberList.addAll(arraylist);
        } else {
            for (GroupMember wp : arraylist) {
                if (wp.getMembername().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    memberList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
