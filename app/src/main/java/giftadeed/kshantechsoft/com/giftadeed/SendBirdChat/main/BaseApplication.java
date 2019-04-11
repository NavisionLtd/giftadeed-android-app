package giftadeed.kshantechsoft.com.giftadeed.SendBirdChat.main;


import android.app.Application;

import com.sendbird.android.SendBird;

public class BaseApplication extends Application {
    //    private static final String APP_ID = "A90E8A82-E9CD-477C-829D-A162F478B0E4"; // For live app
    private static final String APP_ID = "2B2DA376-91B5-4604-9279-C0533F130126"; // For kshandemo development
    public static final String VERSION = "3.0.39";

    @Override
    public void onCreate() {
        super.onCreate();
        SendBird.init(APP_ID, getApplicationContext());
    }
}
