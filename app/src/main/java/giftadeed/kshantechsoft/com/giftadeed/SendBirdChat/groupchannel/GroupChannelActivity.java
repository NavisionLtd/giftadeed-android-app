/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.SendBirdChat.groupchannel;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sendbird.android.GroupChannel;
import com.sendbird.android.SendBirdException;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.SendBirdChat.utils.PreferenceUtils;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsActivity;
import giftadeed.kshantechsoft.com.giftadeed.Utils.SessionManager;

public class GroupChannelActivity extends AppCompatActivity {
    public SessionManager sessionManager;
    public Context mContext;
    private String strUserID, strUsername, strClubName;
    List<String> lstusers = new ArrayList<>();
    private boolean mIsDistinct = true;
    public static int count = 0;
    ImageView imgbackbtn;
    static TextView txtgrpheading;
    private String channelUrl = "";
    private String strRedirectionPage = "";
    public static String strStaicRedirectionPage;
    static FragmentManager fragmgr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_channel);
        mContext = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_group_channel);
        imgbackbtn = (ImageView) findViewById(R.id.backbuttongrpchannel);
        txtgrpheading = (TextView) findViewById(R.id.txtgrpheading);
        setSupportActionBar(toolbar);
        fragmgr = getSupportFragmentManager();
       /* if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_left_white_24_dp);
        }*/

        //====================================================================================
        //get data from shared prereference to get the data of userid,usrname,clubname , clubname is sued for creting grouap name  made by nilesh feb 2018
        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        strUserID = user.get(sessionManager.USER_ID);
        strUsername = user.get(sessionManager.USER_NAME);
        mIsDistinct = PreferenceUtils.getGroupChannelDistinct(GroupChannelActivity.this);
        strClubName = sessionManager.getGroupName();


        //     Log.d("ZZZ", "userid" + strUserID);
        //     Log.d("ZZZ", "username" + strUsername);
        //     Log.d("ZZZ", "clubname" + strClubName);
        //     Log.d("ZZZ", "ISDistinct" + mIsDistinct);
       /* if (strUserID != null && strClubName != null) {
            lstusers.add(strUserID);

            createGroupChannel(lstusers, strClubName, mIsDistinct);

        }
*/
        //geting the value from login activity
        /*channelurl : used  for getting the url name of group from owned club adapter and joined club
         * redirectionPage: used for abck to  certain page whlle user tried to back to specific page */
        channelUrl = getIntent().getStringExtra("groupChannelUrl");
        strRedirectionPage = getIntent().getStringExtra("REDIRECTPAGE");
        strStaicRedirectionPage = getIntent().getStringExtra("REDIRECTPAGE");
        if (channelUrl == null || channelUrl.equals("")) {
            if (savedInstanceState == null) {
                // Load list of Group Channels
                Fragment fragment = GroupChannelListFragment.newInstance();
                FragmentManager manager = getSupportFragmentManager();
                manager.popBackStack();
                manager.beginTransaction()
                        .replace(R.id.container_group_channel, fragment)
                        .commit();
            }

        } else {
            if (channelUrl != null) {
                // If started from notification
                Fragment fragment = GroupChatFragment.newInstance(channelUrl);
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.container_group_channel, fragment)
                        .addToBackStack(null)
                        .commitAllowingStateLoss();
            }
        }
        imgbackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), TaggedneedsActivity.class);
                i.putExtra("FRAGMENT", strRedirectionPage);
                startActivity(i);
            }
        });
    }

    interface onBackPressedListener {
        boolean onBack();
    }

    private onBackPressedListener mOnBackPressedListener;

    public void setOnBackPressedListener(onBackPressedListener listener) {
        mOnBackPressedListener = listener;
    }

    @Override
    public void onBackPressed() {
        if (mOnBackPressedListener != null && mOnBackPressedListener.onBack()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void setActionBarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    /**
     * Creates a new Group Channel.
     * <p>
     * Note that if you have not included empty channels in your GroupChannelListQuery,
     * the channel will not be shown in the user's channel list until at least one message
     * has been sent inside.
     *
     * @param userId   The users to be members of the new channel.
     * @param distinct Whether the channel is unique for the selected members.
     *                 If you attempt to create another Distinct channel with the same members,
     *                 the existing channel instance will be returned.
     */
    private void createGroupChannel(List<String> userId, String GroupName, boolean distinct) {
        Log.d("ZZZ", "" + distinct);
        GroupChannel.createChannelWithUserIds(userId, distinct, GroupName, "", "hello", new GroupChannel.GroupChannelCreateHandler() {
            @Override
            public void onResult(GroupChannel groupChannel, SendBirdException e) {
                if (e != null) {
                    // Error!
                    return;
                }

                // Intent intent = new Intent();
                //  intent.putExtra(EXTRA_NEW_CHANNEL_URL, groupChannel.getUrl());
                //  setResult(RESULT_OK, intent);
                //  finish();
            }
        });
    }


}
