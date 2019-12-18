/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.SendBirdChat.openchannel;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sendbird.android.OpenChannel;
import com.sendbird.android.OpenChannelListQuery;
import com.sendbird.android.SendBirdException;

import org.jetbrains.annotations.Nullable;
import java.util.List;

import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.SendBirdChat.main.ConnectionManager;
import giftadeed.kshantechsoft.com.giftadeed.SendBirdChat.utils.PreferenceUtils;

public class OpenChannelListFragment extends Fragment {
    public static final String EXTRA_OPEN_CHANNEL_URL = "OPEN_CHANNEL_URL";
    private static final String LOG_TAG = OpenChannelListFragment.class.getSimpleName();
    static FragmentManager fragmgr;
    private static final int CHANNEL_LIST_LIMIT = 1;
    private static final String CONNECTION_HANDLER_ID = "CONNECTION_HANDLER_OPEN_CHANNEL_LIST";
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private OpenChannelListAdapter mChannelListAdapter;
    private SwipeRefreshLayout mSwipeRefresh;
    private FloatingActionButton mCreateChannelFab;
    private OpenChannelListQuery mChannelListQuery;

    public static OpenChannelListFragment newInstance() {
        OpenChannelListFragment fragment = new OpenChannelListFragment();
        return fragment;
    }

    private OpenChannel channel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_open_channel_list, container, false);
        fragmgr = getFragmentManager();
        setRetainInstance(true);
        setHasOptionsMenu(true);

        //  ((OpenChannelActivity) getActivity()).setActionBarTitle(getResources().getString(R.string.Inspire_Community));

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_open_channel_list);

        //String strOpenChaneel=   channel.getUrl();
        //Log.d("TAGGG",""+strOpenChaneel);

        mChannelListAdapter = new OpenChannelListAdapter(getContext());

        // Set color?
        mSwipeRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_layout_open_channel_list);

        // Swipe down to refresh channel list.
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefresh.setRefreshing(true);
                refresh();
            }
        });

        mCreateChannelFab = (FloatingActionButton) rootView.findViewById(R.id.fab_open_channel_list);
        mCreateChannelFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateOpenChannelActivity.class);
                startActivity(intent);
            }
        });
        switchToOpenChat();

        String userId = PreferenceUtils.getUserId(getActivity());


        return rootView;
    }

    @Override
    public void onDestroyView() {
        ConnectionManager.removeConnectionManagementHandler(CONNECTION_HANDLER_ID);
        super.onDestroyView();
    }

    void setUpRecyclerView() {
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mChannelListAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        // If user scrolls to bottom of the list, loads more channels.
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (mLayoutManager.findLastVisibleItemPosition() == mChannelListAdapter.getItemCount() - 1) {
                    loadNextChannelList();
                }
            }
        });
    }

    // Set touch listeners to RecyclerView items
    private void setUpChannelListAdapter() {
        mChannelListAdapter.setOnItemClickListener(new OpenChannelListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(OpenChannel channel) {
                String channelUrl = channel.getUrl();

                OpenChatFragment fragment = OpenChatFragment.newInstance(channelUrl);
                getFragmentManager().beginTransaction()
                        .replace(R.id.container_open_channel, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        mChannelListAdapter.setOnItemLongClickListener(new OpenChannelListAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongPress(OpenChannel channel) {
            }
        });
    }

    private void refresh() {
        refreshChannelList(CHANNEL_LIST_LIMIT);
    }

    /**
     * Creates a new query to get the list of the user's Open Channels,
     * then replaces the existing dataset.
     *
     * @param numChannels The number of channels to load.
     */
    void refreshChannelList(int numChannels) {
        mChannelListQuery = OpenChannel.createOpenChannelListQuery();
        mChannelListQuery.setLimit(numChannels);
        mChannelListQuery.next(new OpenChannelListQuery.OpenChannelListQueryResultHandler() {
            @Override
            public void onResult(List<OpenChannel> list, SendBirdException e) {
                if (e != null) {
                    e.printStackTrace();
                    return;
                }

                mChannelListAdapter.setOpenChannelList(list);

                if (mSwipeRefresh.isRefreshing()) {
                    mSwipeRefresh.setRefreshing(false);
                }
            }
        });
    }



    /**
     * Loads the next channels from the current query instance.
     */
    void loadNextChannelList() {
        if (mChannelListQuery != null) {
            mChannelListQuery.next(new OpenChannelListQuery.OpenChannelListQueryResultHandler() {
                @Override
                public void onResult(List<OpenChannel> list, SendBirdException e) {
                    if (e != null) {
                        e.printStackTrace();
                        return;
                    }

                    for (OpenChannel channel : list) {
                        mChannelListAdapter.addLast(channel);
                    }
                }
            });
        }
    }

    public void switchToOpenChat() {
        try {
            mChannelListQuery = OpenChannel.createOpenChannelListQuery();
            mChannelListQuery.setLimit(1);
            mChannelListQuery.next(new OpenChannelListQuery.OpenChannelListQueryResultHandler() {
                @Override
                public void onResult(List<OpenChannel> list, SendBirdException e) {
                    if (e != null) {
                        e.printStackTrace();
                        return;
                    }

                    String channelUrl = list.get(0).getUrl();

                    OpenChatFragment fragment = OpenChatFragment.newInstance(channelUrl);
                    getFragmentManager().beginTransaction()
                            .replace(R.id.container_open_channel, fragment)
                            .addToBackStack(null)
                            .commitAllowingStateLoss();

                }


            });
        } catch (Exception e) {
//            FirebaseCrash.report(e);
        }
    }
}
