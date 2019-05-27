package giftadeed.kshantechsoft.com.giftadeed.Group;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import giftadeed.kshantechsoft.com.giftadeed.Collaboration.CollaborationListFragment;
import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsActivity;

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
