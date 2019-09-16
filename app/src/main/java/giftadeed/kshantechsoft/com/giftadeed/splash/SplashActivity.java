package giftadeed.kshantechsoft.com.giftadeed.splash;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;

import giftadeed.kshantechsoft.com.giftadeed.Animation.FadeInActivity;
import giftadeed.kshantechsoft.com.giftadeed.Login.LoginActivity;
import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.GPSTracker;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsActivity;
import giftadeed.kshantechsoft.com.giftadeed.Utils.DatabaseAccess;
import giftadeed.kshantechsoft.com.giftadeed.Utils.FontDetails;
import giftadeed.kshantechsoft.com.giftadeed.Utils.GetingAddress;
import giftadeed.kshantechsoft.com.giftadeed.Utils.SessionManager;
import giftadeed.kshantechsoft.com.giftadeed.Utils.ToastPopUp;
import giftadeed.kshantechsoft.com.giftadeed.Utils.Validation;

public class SplashActivity extends AppCompatActivity {
    TextView txt_title;
    TextView txt_title2, txt_app_version;
    private boolean isBackPressed = false;
    SessionManager sessionManager;
    String strUserId = null, message, address_show;
    String currentVersion = "", newVersion;
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        init();
        try {
            currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            if (currentVersion.length() > 0) {
//                txt_app_version.setText("App Version " + currentVersion);
                txt_app_version.setText(getResources().getString(R.string.app_name));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (isInternetOn()) {
            new GetVersionCode().execute();
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setMessage(getString(R.string.network_validation));
            alertDialogBuilder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            finish();
                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    public void init() {
        //-----------------------title txt---------------------------------------------------------
        txt_title = (TextView) findViewById(R.id.text_splash_title);
        txt_title2 = (TextView) findViewById(R.id.text_splash_title2);
        txt_app_version = (TextView) findViewById(R.id.tv_app_version);
        txt_title.setShadowLayer(30, 0, 0, Color.BLACK);
        //------------------Font setting------------------------------------------------------------
        txt_title.setTypeface(new FontDetails(SplashActivity.this).fontStandard_spalsh);
        txt_title2.setTypeface(new FontDetails(SplashActivity.this).fontStandard_spalsh);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            isBackPressed = true;
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void showForceUpdateDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SplashActivity.this);
        alertDialogBuilder.setTitle(getApplicationContext().getString(R.string.youAreNotUpdatedTitle));
        alertDialogBuilder.setMessage(getApplicationContext().getString(R.string.youAreNotUpdatedMessage));
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent i = new Intent(android.content.Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://play.google.com/store/apps/details?id=giftadeed.kshantechsoft.com.giftadeed&hl=en_IN"));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
//                getApplicationContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=giftadeed.kshantechsoft.com.giftadeed&hl=en_IN")));
                dialog.dismiss();
                SplashActivity.this.finish();
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                SplashActivity.this.finish();
//                proceedToApp();
            }
        });
        alertDialogBuilder.show();
    }

    public void proceedToApp() {
        address_show = new GetingAddress(SplashActivity.this).getCompleteAddressString(new GPSTracker(SplashActivity.this).getLatitude(), new GPSTracker(SplashActivity.this).getLatitude());
        Log.d("address", address_show);
        new Handler().postDelayed(new Runnable() {
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                if (!isBackPressed) {
                    sessionManager = new SessionManager(getApplicationContext());
                    HashMap<String, String> user = sessionManager.getUserDetails();
                    strUserId = user.get(sessionManager.USER_ID);
                    if (strUserId == null) {
                        //messagecharity = "Charity";
                        Intent log = new Intent(getApplicationContext(), LoginActivity.class);
                        log.putExtra("message", "Charity");
                        startActivity(log);
                    } else {
                        message = "Charity";
                        Intent in = new Intent(SplashActivity.this, TaggedneedsActivity.class);
                        in.putExtra("message", message);
                        startActivity(in);
                    }
                }
                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    class GetVersionCode extends AsyncTask<Void, String, String> {
        @Override
        protected String doInBackground(Void... voids) {
            try {
                Document document = Jsoup.connect("https://play.google.com/store/apps/details?id=giftadeed.kshantechsoft.com.giftadeed&hl=en_IN")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get();
                if (document != null) {
                    Elements element = document.getElementsContainingOwnText("Current Version");
                    for (Element ele : element) {
                        if (ele.siblingElements() != null) {
                            Elements sibElemets = ele.siblingElements();
                            for (Element sibElemet : sibElemets) {
                                newVersion = sibElemet.text();
                            }
                        }
                    }
                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SplashActivity.this);
                    alertDialogBuilder.setCancelable(false);
                    alertDialogBuilder.setMessage(getString(R.string.network_validation));
                    alertDialogBuilder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    finish();
                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("appinfodetails", "" + newVersion);
            return newVersion;
        }

        @Override
        protected void onPostExecute(String onlineVersion) {
            super.onPostExecute(onlineVersion);
            /*if (onlineVersion != null && !onlineVersion.isEmpty()) {
                if (Float.valueOf(currentVersion) < Float.valueOf(onlineVersion)) {
                    //show update dialog
                    showForceUpdateDialog();
                } else {
            proceedToApp();
            // Animation trial
//            Intent log = new Intent(getApplicationContext(), FadeInActivity.class);
//            startActivity(log);
                }
            }*/
            proceedToApp();
            Log.d("appinfodetails", "Current version " + currentVersion + "playstore version " + onlineVersion);
        }
    }

    public final boolean isInternetOn() {
        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
        // Check for network connections
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {
            // if connected with internet
            return true;
        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
            return false;
        }
        return false;
    }
}

