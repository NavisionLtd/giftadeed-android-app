package giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds;

import android.content.Context;
import android.location.Location;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.leo.simplearcloader.ArcConfiguration;
import com.squareup.okhttp.OkHttpClient;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.GPSTracker;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.list_Model.Modeltaglist;

import giftadeed.kshantechsoft.com.giftadeed.Utils.SessionManager;
import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

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
    List<RowData> item;

    public Pager(FragmentManager fm, int tabcount) {
        super(fm);
        this.item = item;
        this.tabcount = tabcount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                TabNew tab1 = new TabNew();
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
