package giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.list_Model.MyItem;

/**
 * Created by I-Sys on 23-Feb-18.
 */

public class PopupAdapter implements GoogleMap.InfoWindowAdapter {
    private View popup = null;
    private LayoutInflater inflater = null;
    MyItem clickedClusterItem;

    PopupAdapter(MyItem clickedClusterItem,LayoutInflater inflater) {

        this.clickedClusterItem=clickedClusterItem;
        this.inflater=inflater;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {

        if (popup == null) {
            popup = inflater.inflate(R.layout.windowlayout, null);
        }

        TextView tv = (TextView) popup.findViewById(R.id.tv_lat);

        tv.setText(marker.getTitle());

        tv = (TextView) popup.findViewById(R.id.tv_lng);
        tv.setText(marker.getSnippet());
        return popup;
    }
}
