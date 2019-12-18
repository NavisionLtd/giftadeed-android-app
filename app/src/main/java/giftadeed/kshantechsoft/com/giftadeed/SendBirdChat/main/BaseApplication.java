/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.SendBirdChat.main;


import android.app.Application;

import com.sendbird.android.SendBird;

import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;

public class BaseApplication extends Application {
    public static final String VERSION = "3.0.39";

    @Override
    public void onCreate() {
        super.onCreate();
        SendBird.init(WebServices.SENDBIRD_APP_ID, getApplicationContext());
    }
}
