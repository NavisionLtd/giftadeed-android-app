/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import java.util.ArrayList;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.list_Model.Modeltaglist;
import giftadeed.kshantechsoft.com.giftadeed.Utils.SessionManager;

/**
 * Created by I-Sys on 30-Nov-16.
 */

public class Pager extends FragmentPagerAdapter {
    int tabcount;
    private String tabTitles[] = new String[]{"Map", "List"};
    Modeltaglist listData = new Modeltaglist();
    //List<RowData> item;
    FragmentActivity myContext;
    double radius_set = 10.00;
    SessionManager sessionManager;
    ArrayList<String> lat_long = new ArrayList<>();
    ArrayList<String> icon_path = new ArrayList<>();
    ArrayList<String> tag_title = new ArrayList<>();
    String strUser_ID;
    Context context;

    public Pager(FragmentManager fm, int tabcount) {
        super(fm);
        this.tabcount = tabcount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                MapTab tab1 = new MapTab();
                return tab1;
            case 1:
                Tab2 tab2 = new Tab2();
                return tab2;
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
