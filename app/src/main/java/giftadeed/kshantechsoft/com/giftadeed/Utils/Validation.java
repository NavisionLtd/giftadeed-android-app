/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.ArrayList;

/**
 * Created by nilesh on 2/22/2016.
 */
public class Validation {
    public static String FILTER_CATEGORY = "All";
    public static String FILTER_GROUP_IDS = "All";
    public static String FILTER_GROUP_NAMES = "All";
    //    public static float inital_radius=10.0f;
    public static float inital_radius = 10000.0f;

    public static boolean isStringNullOrBlank(String str) {
        try {
            if (str == null) {
                return true;
            } else if (str.equals("null") || str.equals("") || (str != null && str.isEmpty())) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
