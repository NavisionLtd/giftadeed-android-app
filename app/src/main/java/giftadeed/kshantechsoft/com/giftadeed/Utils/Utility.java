package giftadeed.kshantechsoft.com.giftadeed.Utils;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

/**
 * Created by I-Sys on 14-Feb-18.
 */

public class Utility{

    public static int calculateNoOfColumns(Context context) {
        int noOfColumns=0;
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        if (dpWidth>=600) {
            noOfColumns = (int) (dpWidth / 130);
        }else{
            noOfColumns = (int) (dpWidth / 100);
        }
        return noOfColumns;
    }
}
