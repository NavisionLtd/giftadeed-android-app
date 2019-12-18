/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;

/**
 * Created by I-Sys on 03-Apr-17.
 */

public class FontDetails {
    public static Context context;
    public static Typeface fontStandard_spalsh;
    public static Typeface fontStandardForPage;

    public FontDetails(Context context) {
        this.context = context;
        fontStandard_spalsh = Typeface.createFromAsset(context.getAssets(), "EXO350DB.ttf");
        fontStandardForPage = Typeface.createFromAsset(context.getAssets(), "roboto.ttf");
    }
}
