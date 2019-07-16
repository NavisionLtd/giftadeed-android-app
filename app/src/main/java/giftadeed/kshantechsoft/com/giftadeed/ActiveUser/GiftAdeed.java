package giftadeed.kshantechsoft.com.giftadeed.ActiveUser;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import androidx.multidex.MultiDex;
import android.util.Log;
import android.widget.Toast;

import com.sendbird.android.SendBird;

import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;

/**
 * Usage:
 * <p>
 * 1. Get the Foreground Singleton, passing a Context or Application object unless you
 * are sure that the Singleton has definitely already been initialised elsewhere.
 * <p>
 * 2.a) Perform a direct, synchronous check: Foreground.isForeground() / .isBackground()
 * <p>
 * or
 * <p>
 * 2.b) Register to be notified (useful in Service or other non-UI components):
 * <p>
 * Foreground.Listener myListener = new Foreground.Listener(){
 * public void onBecameForeground(){
 * // ... whatever you want to do
 * }
 * public void onBecameBackground(){
 * // ... whatever you want to do
 * }
 * }
 * <p>
 * public void onCreate(){
 * super.onCreate();
 * Foreground.get(this).addListener(listener);
 * }
 * <p>
 * public void onDestroy(){
 * super.onCreate();
 * Foreground.get(this).removeListener(listener);
 * }
 */
public class GiftAdeed extends Application {
//    private static final String APP_ID = "A90E8A82-E9CD-477C-829D-A162F478B0E4"; // For live app
//    private static final String APP_ID = "2B2DA376-91B5-4604-9279-C0533F130126"; // For kshandemo development
    public static final String VERSION = "3.0.39";

    @Override
    public void onCreate() {
        super.onCreate();

        Foreground handler = new Foreground();
        registerActivityLifecycleCallbacks(handler);
        registerComponentCallbacks(handler);

        SendBird.init(WebServices.SENDBIRD_APP_ID, getApplicationContext());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}