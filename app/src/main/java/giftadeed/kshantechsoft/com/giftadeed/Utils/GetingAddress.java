package giftadeed.kshantechsoft.com.giftadeed.Utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

import giftadeed.kshantechsoft.com.giftadeed.R;

/**
 * Created by I-Sys on 03-Aug-17.
 */

public class GetingAddress {
    public static String strAdd = "";
    Context context;
    List<Address> addresses;


    public static String countryName;

    public GetingAddress(Context context) {
        this.context = context;
    }

    //from lat to address to get country
    public String getCompleteAddressString(final double LATITUDE, final double LONGITUDE) {
        Thread thread = new Thread() {
            @Override
            public void run() {

                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                try {

                    addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);

                    if (addresses != null && addresses.size() > 0) {
                        Address returnedAddress = addresses.get(0);
                        countryName = addresses.get(0).getCountryName();
                        // Log.d("country", countryName);
                        StringBuilder strReturnedAddress = new StringBuilder("");

                        for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                            strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                        }
                        strAdd = strReturnedAddress.toString();
                        //  Log.d("My Current loction address", "" + strReturnedAddress.toString());
                    } else {
                        Toast.makeText(context, context.getResources().getString(R.string.address_notfound), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    //Log.d("My Current loction address", "Canont get Address!");
                }

            }
        };
        thread.start();
        // while (i <= 5) {
        // str_Geopint = LATITUDE + "," + LONGITUDE;
        // Log.d("Geopoints before change", str_Geopint);

        // }
        return strAdd;
    }


}
