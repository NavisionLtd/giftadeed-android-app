package giftadeed.kshantechsoft.com.giftadeed.Landing;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import giftadeed.kshantechsoft.com.giftadeed.Login.LoginActivity;
import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.subscription.Subscription_Activity;
import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.GPSTracker;
import giftadeed.kshantechsoft.com.giftadeed.Utils.FontDetails;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {
    Button btncharity, btnsubscription;
    TextView dialogtext, mainactivitytext;
    private AlertDialog alertDialogForgot;
    Button btnLogin, dialogconfirm, dialogcancel;
    String messagecharity, messagesubscription;
    public static final int RequestPermissionCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        GPSTracker gps = new GPSTracker(MainActivity.this);
        if (!gps.isGPSEnabled) {
            gps.showSettingsAlert();
            //showSettingsAlert();
        }
        if (checkPermission()) {
            // Toast.makeText(MainActivity.this, "All Permissions Granted Successfully", Toast.LENGTH_LONG).show();
        } else {
            requestPermission();
        }
//--------------------------------clicking charety button-------------------------------------------
        btncharity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messagecharity = "Charity";
                Intent log = new Intent(getApplicationContext(), LoginActivity.class);
                log.putExtra("message", messagecharity);
                startActivity(log);
            }
        });
//-------------------Clicking subscription----------------------------------------------------------
        btnsubscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messagesubscription = "Subscription";
                Intent subscribe = new Intent(getApplicationContext(), Subscription_Activity.class);
                startActivity(subscribe);
                // Toast.makeText(MainActivity.this, "Coming soon.........", Toast.LENGTH_LONG).show();
                /*Intent log = new Intent(getApplicationContext(), SendBirdLoginActivity.class);
                log.putExtra("message", messagesubscription);
                startActivity(log);*/
            }
        });
    }


    //-----------------------initializing the UI elements-----------------------
    private void init() {
        btncharity = (Button) findViewById(R.id.charity);
        btnsubscription = (Button) findViewById(R.id.subscription);
        mainactivitytext = (TextView) findViewById(R.id.mainactivitytext);
        mainactivitytext.setTypeface(new FontDetails(MainActivity.this).fontStandardForPage);
        btncharity.setTypeface(new FontDetails(MainActivity.this).fontStandardForPage);
        btnsubscription.setTypeface(new FontDetails(MainActivity.this).fontStandardForPage);
    }


    //--------------------clicking the back button--------------------
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            submitdialog();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    //----------------------------------forgot password dialog----------------------------------
    private void submitdialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        LayoutInflater li = LayoutInflater.from(this);
        View confirmDialog = li.inflate(R.layout.giftneeddialog, null);
        dialogconfirm = (Button) confirmDialog.findViewById(R.id.btn_submit_mobileno);
        dialogcancel = (Button) confirmDialog.findViewById(R.id.btn_Cancel_mobileno);
        dialogtext = (TextView) confirmDialog.findViewById(R.id.txtgiftneeddialog);

        dialogtext.setText(getResources().getString(R.string.exit_msg));
        //-------------Adding our dialog box to the view of alert dialog
        alert.setView(confirmDialog);
        alert.setCancelable(false);

        //----------------Creating an alert dialog
        alertDialogForgot = alert.create();
        alertDialogForgot.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        alertDialogForgot.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //----------------Displaying the alert dialog
        alertDialogForgot.show();
        dialogconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //alertDialogForgot.dismiss();
                alertDialogForgot.dismiss();
                finishAffinity();
            }
        });
        dialogcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogForgot.dismiss();
            }
        });
    }

    //---------------------request permissions method---------------------------------------------------
    private void requestPermission() {
        ActivityCompat.requestPermissions(MainActivity.this, new String[]
                {
                        CAMERA,
                        READ_EXTERNAL_STORAGE,
                        WRITE_EXTERNAL_STORAGE,
                        ACCESS_FINE_LOCATION
                }, RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0) {
                    boolean CameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadExternalStoragePermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean WriteExternalStoragePermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean InternetPermission = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                    if (CameraPermission && ReadExternalStoragePermission && WriteExternalStoragePermission && InternetPermission) {
                        //  Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        // Toast.makeText(MainActivity.this,"Permission Denied",Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    //---------------------------------check permissions method-------------------------------------
    public boolean checkPermission() {
        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int ThirdPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int FourthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);

        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ThirdPermissionResult == PackageManager.PERMISSION_GRANTED &&
                FourthPermissionResult == PackageManager.PERMISSION_GRANTED;
    }


    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog;
        alertDialog = new AlertDialog.Builder(MainActivity.this, R.style.AppCompatAlertDialogStyle);
        // Setting Dialog Title
        alertDialog.setTitle(R.string.GPSsettings);

        // Setting Dialog Message
        alertDialog.setMessage(R.string.GPSsettingsmessage);

        // On pressing Settings button
        alertDialog.setPositiveButton(R.string.settings, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

                if (!provider.contains("gps")) { //if gps is disabled
                    final Intent poke = new Intent();
                    poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
                    poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
                    poke.setData(Uri.parse("3"));
                    sendBroadcast(poke);
                }


                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);


            }
        });
        alertDialog.show();


    }


    private void turnGPSOn() {
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if (!provider.contains("gps")) { //if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            sendBroadcast(poke);
        }
    }


}
