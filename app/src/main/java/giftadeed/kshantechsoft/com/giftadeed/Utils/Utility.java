/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.Utils;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import android.util.DisplayMetrics;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

/**
 * Created by I-Sys on 14-Feb-18.
 */

public class Utility {
    public static final String avatorDefaultIcon = "https://cdn.pixabay.com/photo/2016/11/08/15/21/user-1808597_960_720.png";

    public static int calculateNoOfColumns(Context context) {
        int noOfColumns = 0;
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        if (dpWidth >= 600) {
            noOfColumns = (int) (dpWidth / 130);
        } else {
            noOfColumns = (int) (dpWidth / 100);
        }
        return noOfColumns;
    }
}
