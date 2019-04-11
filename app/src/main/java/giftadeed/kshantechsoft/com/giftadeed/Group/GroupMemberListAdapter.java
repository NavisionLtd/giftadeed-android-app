package giftadeed.kshantechsoft.com.giftadeed.Group;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import giftadeed.kshantechsoft.com.giftadeed.MyProfile.Profile;
import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.Utils.Validation;
import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;

public class GroupMemberListAdapter extends BaseAdapter {
    ArrayList<GroupMember> list = new ArrayList<>();
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private List<Profile> profileList;
    Context context;

    public GroupMemberListAdapter(ArrayList<GroupMember> groupMembers, Context context) {
        this.list = groupMembers;
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
    public View getView(final int position, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.group_member_list_item, null);
        final ImageView memberImage = (ImageView) view.findViewById(R.id.iv_member_image);
        TextView memberName = (TextView) view.findViewById(R.id.group_member_name);
        TextView memberRole = (TextView) view.findViewById(R.id.member_role);
        memberName.setText(list.get(position).getMembername());
        if (list.get(position).getPrivilege().equals("C")) {
            memberRole.setVisibility(View.VISIBLE);
            memberRole.setText("Creator");
        } else if (list.get(position).getPrivilege().equals("A")) {
            memberRole.setVisibility(View.VISIBLE);
            memberRole.setText("Admin");
        } else {
            memberRole.setVisibility(View.GONE);
        }

        if (!(Validation.isNetworkAvailable(context))) {
            Toast.makeText(context, context.getResources().getString(R.string.network_validation), Toast.LENGTH_SHORT).show();
        } else {
            mFirebaseInstance = FirebaseDatabase.getInstance();
            mFirebaseDatabase = mFirebaseInstance.getReference(WebServices.DATABASE_PROFILE_PIC_UPLOADS);
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
                            if (profile.getUserid().equals(list.get(position).getMemberid())) {
                                Picasso.with(context).load(profile.getPhotourl()).placeholder(R.drawable.group_member).into(memberImage);
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
}
