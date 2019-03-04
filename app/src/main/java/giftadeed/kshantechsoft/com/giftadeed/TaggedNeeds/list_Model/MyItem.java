package giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.list_Model;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by Darshan on 06/28/2017.
 */

public class MyItem implements ClusterItem {
    private final LatLng mPosition;
    private String mTitle = null;
    private String mSnippet=null;
    private  String img_path=null;

    public MyItem(double lat, double lng) {
        mPosition = new LatLng(lat, lng);
    }

    public MyItem(double lat, double lng, String title, String snippet, String mimg_path) {
        mPosition = new LatLng(lat, lng);
        mTitle = title;
        mSnippet = snippet;
        img_path=mimg_path;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    public String getTitle(){
        return mTitle;
    }

    public String getSnippet(){
        return mSnippet;
    }

    public String getimg_path() {
        return img_path;
    }
}
