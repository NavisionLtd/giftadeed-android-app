/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.Group;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import giftadeed.kshantechsoft.com.giftadeed.Collaboration.CollaborationListFragment;

/**
 * Created by I-Sys on 30-Nov-16.
 */

public class GroupsPager extends FragmentPagerAdapter {
    int tabcount;
    private String tabTitles[] = new String[]{"Groups", "Collaborations"};

    public GroupsPager(FragmentManager fm, int tabcount) {
        super(fm);
        this.tabcount = tabcount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                GroupsListFragment groupsListFragment = new GroupsListFragment();
                return groupsListFragment;
            case 1:
                CollaborationListFragment collaborationListFragment = new CollaborationListFragment();
                return collaborationListFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabcount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
