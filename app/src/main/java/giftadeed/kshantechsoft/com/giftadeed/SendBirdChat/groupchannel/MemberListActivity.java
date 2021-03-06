/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.SendBirdChat.groupchannel;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;

import com.sendbird.android.GroupChannel;
import com.sendbird.android.Member;
import com.sendbird.android.SendBirdException;

import org.jetbrains.annotations.Nullable;

import java.util.List;

import giftadeed.kshantechsoft.com.giftadeed.R;

public class MemberListActivity extends AppCompatActivity {
    private UserListAdapter mListAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private String mChannelUrl;
    private GroupChannel mChannel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_list);
        mChannelUrl = getIntent().getStringExtra(GroupChatFragment.EXTRA_CHANNEL_URL);
        if (mChannelUrl == null) {
            // Theoretically shouldn't happen
            finish();
        }

        mChannelUrl = getIntent().getStringExtra(GroupChatFragment.EXTRA_CHANNEL_URL);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_member_list);
        mListAdapter = new UserListAdapter(this, true);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_member_list);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_left_white_24_dp);
        }

        setUpRecyclerView();
        getChannelFromUrl(mChannelUrl);
    }

    private void setUpRecyclerView() {
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mListAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
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

    private void getChannelFromUrl(String url) {
        GroupChannel.getChannel(url, new GroupChannel.GroupChannelGetHandler() {
            @Override
            public void onResult(GroupChannel groupChannel, SendBirdException e) {
                if (e != null) {
                    // Error!
                    return;
                }
                mChannel = groupChannel;
                refreshChannel();
            }
        });
    }

    private void refreshChannel() {
        mChannel.refresh(new GroupChannel.GroupChannelRefreshHandler() {
            @Override
            public void onResult(SendBirdException e) {
                if (e != null) {
                    // Error!
                    return;
                }
                setUserList(mChannel.getMembers());
            }
        });
    }

    private void setUserList(List<Member> userList) {
        mListAdapter.setUserList(userList);
    }
}
