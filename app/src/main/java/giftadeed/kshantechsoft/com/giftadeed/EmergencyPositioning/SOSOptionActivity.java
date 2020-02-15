/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.EmergencyPositioning;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.squareup.okhttp.OkHttpClient;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import giftadeed.kshantechsoft.com.giftadeed.Bug.Bugreport;
import giftadeed.kshantechsoft.com.giftadeed.Login.LoginActivity;
import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.Utils.DatabaseAccess;
import giftadeed.kshantechsoft.com.giftadeed.Utils.GpsUtils;
import giftadeed.kshantechsoft.com.giftadeed.Utils.SharedPrefManager;
import giftadeed.kshantechsoft.com.giftadeed.Utils.ToastPopUp;
import giftadeed.kshantechsoft.com.giftadeed.Utils.Validation;
import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class SOSOptionActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    SimpleArcDialog mDialog;
    GoogleApiClient googleApiClient;
    LinearLayout layoutCall, layoutSMS, layoutShareLocation;
    TextView tvClose, noContactFoundMSG, sosOption1, sosOption2, sosOption3;
    Button btnSetContact;
    String strLatitude = "", strLongitude = "", message1, message2, country_sos_number = "911";
    public static final int RequestPermissionCode = 1;
    DatabaseAccess databaseAccess;
    int contactsCount;
    ArrayList<Contact> contactArrayList;
    String msgNumber1 = "", msgNumber2 = "";
    ImageView callSign, smsSign, locationSign, correctSign1, correctSign2, correctSign3;
    SharedPrefManager sharedPrefManager;
    FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private boolean isContinue = false;
    private boolean isGPS = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sos_options_layout);

        layoutCall = (LinearLayout) findViewById(R.id.layout_call);
        layoutSMS = (LinearLayout) findViewById(R.id.layout_sms);
        layoutShareLocation = (LinearLayout) findViewById(R.id.layout_share_location);
        tvClose = (TextView) findViewById(R.id.tv_close);
        noContactFoundMSG = (TextView) findViewById(R.id.no_contacts_found_msg);
        btnSetContact = (Button) findViewById(R.id.btn_set_contacts);
        callSign = (ImageView) findViewById(R.id.call_sign);
        smsSign = (ImageView) findViewById(R.id.sms_sign);
        locationSign = (ImageView) findViewById(R.id.location_sign);
        sosOption1 = (TextView) findViewById(R.id.sos_option_1);
        sosOption2 = (TextView) findViewById(R.id.sos_option_2);
        sosOption3 = (TextView) findViewById(R.id.sos_option_3);
        correctSign1 = (ImageView) findViewById(R.id.correct_sign_1);
        correctSign2 = (ImageView) findViewById(R.id.correct_sign_2);
        correctSign3 = (ImageView) findViewById(R.id.correct_sign_3);
        setUpGClient();

        sharedPrefManager = new SharedPrefManager(SOSOptionActivity.this);
        mDialog = new SimpleArcDialog(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10 * 1000); // 10 seconds
        locationRequest.setFastestInterval(5 * 1000); // 5 seconds

        new GpsUtils(this).turnGPSOn(new GpsUtils.onGpsListener() {
            @Override
            public void gpsStatus(boolean isGPSEnable) {
                // turn on GPS
                isGPS = isGPSEnable;
            }
        });

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        strLatitude = String.valueOf(location.getLatitude());
                        strLongitude = String.valueOf(location.getLongitude());
                        if (!isContinue) {
                            Log.d("SOSOptions", strLatitude + "," + strLongitude);
                        }
                        if (!isContinue && fusedLocationProviderClient != null) {
                            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
                        }
                    }
                }
            }
        };

        if (sharedPrefManager.getSosOption1Clicked().equals("yes")) {  // check if CALL option performed already
            callSign.setVisibility(View.GONE);
            correctSign1.setVisibility(View.VISIBLE);
            sosOption1.setTextColor(getResources().getColor(R.color.colorPurple));
        } else {
            callSign.setVisibility(View.VISIBLE);
            correctSign1.setVisibility(View.GONE);
            sosOption1.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
        if (sharedPrefManager.getSosOption2Clicked().equals("yes")) {  // check if SMS option performed already
            smsSign.setVisibility(View.GONE);
            correctSign2.setVisibility(View.VISIBLE);
            sosOption2.setTextColor(getResources().getColor(R.color.colorPurple));
        } else {
            smsSign.setVisibility(View.VISIBLE);
            correctSign2.setVisibility(View.GONE);
            sosOption2.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
        if (sharedPrefManager.getSosOption3Clicked().equals("yes")) {  // check if SHARE LOCATION option performed already
            locationSign.setVisibility(View.GONE);
            correctSign3.setVisibility(View.VISIBLE);
            sosOption3.setTextColor(getResources().getColor(R.color.colorPurple));
        } else {
            locationSign.setVisibility(View.VISIBLE);
            correctSign3.setVisibility(View.GONE);
            sosOption3.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
        databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        contactsCount = databaseAccess.getContactsCount();
        Log.d("contacts_count", "" + contactsCount);

        if (contactsCount > 0) {
            contactArrayList = new ArrayList<>();
            contactArrayList = databaseAccess.getAllContacts();
            for (Contact contact : contactArrayList) {
                msgNumber1 = contact.getContact1();
                msgNumber2 = contact.getContact2();
            }

            if (!TextUtils.isEmpty(msgNumber1) || !TextUtils.isEmpty(msgNumber2)) {
                noContactFoundMSG.setVisibility(View.GONE);
                btnSetContact.setVisibility(View.GONE);
            } else {
                noContactFoundMSG.setVisibility(View.VISIBLE);
                btnSetContact.setVisibility(View.VISIBLE);
            }
        }

        layoutCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(Validation.isNetworkAvailable(SOSOptionActivity.this))) {
                    ToastPopUp.show(SOSOptionActivity.this, getString(R.string.network_validation));
                    country_sos_number = sharedPrefManager.getCountrySOSCode();
                    String dial = "tel:" + country_sos_number;
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse(dial));
                    callSign.setVisibility(View.GONE);
                    correctSign1.setVisibility(View.VISIBLE);
                    sosOption1.setTextColor(getResources().getColor(R.color.colorPurple));
                    sharedPrefManager.store_sos_option1_clicked("yes");
                    startActivity(intent);
                } else {
                    if (!isGPS) {
                        new GpsUtils(SOSOptionActivity.this).turnGPSOn(new GpsUtils.onGpsListener() {
                            @Override
                            public void gpsStatus(boolean isGPSEnable) {
                                // turn on GPS
                                isGPS = isGPSEnable;
                            }
                        });
                        isContinue = false;
                        getLocation();
                    } else {
                        if (strLatitude.length() > 0 && strLongitude.length() > 0) {
                            //------- latitude and longitude available
                            //----------------getting country emergency number based on latitude,longitude from server-----------------------
                            getCountryEmergencyCode();
                        }
                    }
                }
            }
        });

        layoutSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isGPS) {
                    new GpsUtils(SOSOptionActivity.this).turnGPSOn(new GpsUtils.onGpsListener() {
                        @Override
                        public void gpsStatus(boolean isGPSEnable) {
                            // turn on GPS
                            isGPS = isGPSEnable;
                        }
                    });
                    isContinue = false;
                    getLocation();
                } else {
                    if (contactsCount > 0) {
                        if (strLatitude.length() > 0 && strLongitude.length() > 0) {
                            String numbers = "";
                            if (!TextUtils.isEmpty(msgNumber1) && !TextUtils.isEmpty(msgNumber2)) {
                                numbers = msgNumber1 + ";" + msgNumber2;
                            } else if (!TextUtils.isEmpty(msgNumber1) && TextUtils.isEmpty(msgNumber2)) {
                                numbers = msgNumber1;
                            } else if (TextUtils.isEmpty(msgNumber1) && !TextUtils.isEmpty(msgNumber2)) {
                                numbers = msgNumber2;
                            }

                            if (numbers.length() > 0) {
                                Uri uri = Uri.parse("smsto:" + numbers);
                                Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                                it.putExtra("sms_body", message1 + message2);
                                smsSign.setVisibility(View.GONE);
                                correctSign2.setVisibility(View.VISIBLE);
                                sosOption2.setTextColor(getResources().getColor(R.color.colorPurple));
                                sharedPrefManager.store_sos_option2_clicked("yes");
                                startActivity(it);
                            }
                        }
                    }
                }
            }
        });

        layoutShareLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isGPS) {
                    new GpsUtils(SOSOptionActivity.this).turnGPSOn(new GpsUtils.onGpsListener() {
                        @Override
                        public void gpsStatus(boolean isGPSEnable) {
                            // turn on GPS
                            isGPS = isGPSEnable;
                        }
                    });
                    isContinue = false;
                    getLocation();
                } else {
                    if (strLatitude.length() > 0 && strLongitude.length() > 0) {
                        Intent intent = new Intent(SOSOptionActivity.this, EmergencyStageTwo.class);
                        intent.putExtra("latitude", strLatitude);
                        intent.putExtra("longitude", strLongitude);
                        intent.putExtra("callingfrom", "app");
                        locationSign.setVisibility(View.GONE);
                        correctSign3.setVisibility(View.VISIBLE);
                        sosOption3.setTextColor(getResources().getColor(R.color.colorPurple));
                        sharedPrefManager.store_sos_option3_clicked("yes");
                        startActivity(intent);
                    }
                }
            }
        });

        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSetContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    // All Permissions Granted
                    Intent intent = new Intent(SOSOptionActivity.this, SOSEmergencyNumbers.class);
                    intent.putExtra("callingfrom", "set");
                    startActivity(intent);
                    SOSOptionActivity.this.finish();
                } else {
                    requestPermission();
                    Toast.makeText(SOSOptionActivity.this, "Please allow app permissions", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void getLocation() {
        if (checkPermission()) {
            // All Permissions Granted
            if (isContinue) {
                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
            } else {
                fusedLocationProviderClient.getLastLocation().addOnSuccessListener(SOSOptionActivity.this, location -> {
                    if (location != null) {
                        strLatitude = String.valueOf(location.getLatitude());
                        strLongitude = String.valueOf(location.getLongitude());
                        Log.d("SOSOptions", strLatitude + "," + strLongitude);
                    } else {
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
                    }
                });
            }
        } else {
            requestPermission();
            Toast.makeText(SOSOptionActivity.this, "Please allow app permissions", Toast.LENGTH_LONG).show();
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (isContinue) {
                    fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
                } else {
                    fusedLocationProviderClient.getLastLocation().addOnSuccessListener(SOSOptionActivity.this, location -> {
                        if (location != null) {
                            strLatitude = String.valueOf(location.getLatitude());
                            strLongitude = String.valueOf(location.getLongitude());
                            Log.d("SOSOptions", strLatitude + "," + strLongitude);
                        } else {
                            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
                        }
                    });
                }
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //---------------------getting country emergency number based on latitude,longitude from server-----------------------
    public void getCountryEmergencyCode() {
        mDialog.setConfiguration(new ArcConfiguration(SOSOptionActivity.this));
        mDialog.show();
        mDialog.setCancelable(false);
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        SOSNumberInfoInterface service = retrofit.create(SOSNumberInfoInterface.class);
        Call<SOSNumberInfoPOJO> call = service.sendData(strLatitude + "," + strLongitude);
        Log.d("sosinfo_input_params", strLatitude + "," + strLongitude);
        call.enqueue(new Callback<SOSNumberInfoPOJO>() {
            @Override
            public void onResponse(Response<SOSNumberInfoPOJO> response, Retrofit retrofit) {
                mDialog.dismiss();
                Log.d("response_sosinfo", "" + response.body());
                try {
                    SOSNumberInfoPOJO sosNumberInfoPOJO = response.body();
                    int isblock = 0;
                    try {
                        isblock = sosNumberInfoPOJO.getIsBlocked();
                    } catch (Exception e) {
                        isblock = 0;
                    }
                    if (isblock == 1) {
                        mDialog.dismiss();
                        FacebookSdk.sdkInitialize(SOSOptionActivity.this);
                        Toast.makeText(SOSOptionActivity.this, getResources().getString(R.string.block_toast), Toast.LENGTH_SHORT).show();
                        sharedPrefManager.createUserCredentialSession(null, null, null);
                        LoginManager.getInstance().logOut();
                        // TODO: 01-Feb-20  Google signout code
                        sharedPrefManager.set_notification_status("ON");
                        Intent loginintent = new Intent(SOSOptionActivity.this, LoginActivity.class);
                        startActivity(loginintent);
                    } else {
                        String status = sosNumberInfoPOJO.getStatus();
                        if (status.equals("1")) {
                            country_sos_number = sosNumberInfoPOJO.getContactNo();
                            Log.d("country_sos_number", "" + country_sos_number);
                            sharedPrefManager.store_country_sos_code(country_sos_number);
                            String dial = "tel:" + country_sos_number;
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse(dial));
                            callSign.setVisibility(View.GONE);
                            correctSign1.setVisibility(View.VISIBLE);
                            sosOption1.setTextColor(getResources().getColor(R.color.colorPurple));
                            sharedPrefManager.store_sos_option1_clicked("yes");
                            startActivity(intent);
                        } else {
                            ToastPopUp.show(SOSOptionActivity.this, "" + sosNumberInfoPOJO.getErrorMsg());
                        }
                    }
                } catch (Exception e) {
                    mDialog.dismiss();
                    Log.d("response_sosinfo", "" + e.getMessage());
                    StringWriter writer = new StringWriter();
                    e.printStackTrace(new PrintWriter(writer));
                    Bugreport bg = new Bugreport();
                    bg.sendbug(writer.toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                mDialog.dismiss();
                Log.d("response_sosinfo", "" + t.getMessage());
                ToastPopUp.show(SOSOptionActivity.this, getString(R.string.server_response_error));
            }
        });
    }

    private synchronized void setUpGClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            strLatitude = String.valueOf(location.getLatitude());
            strLongitude = String.valueOf(location.getLongitude());
            message1 = "Your friend is in emergency situation \n";
            message2 = "http://maps.google.com/maps?saddr=" + strLatitude + "," + strLongitude;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SharedPrefManager.GPS_REQUEST) {
                isGPS = true; // flag maintain before get location
            }
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        getLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        //You can display a message here
    }

    //-------------------requests all permissions-------------------------------------------------------
    private void requestPermission() {
        ActivityCompat.requestPermissions(SOSOptionActivity.this, new String[]
                {
                        CAMERA,
                        READ_EXTERNAL_STORAGE,
                        WRITE_EXTERNAL_STORAGE,
                        ACCESS_FINE_LOCATION,
                        READ_CONTACTS
                }, RequestPermissionCode);
    }

    //---------------------------------check permissions method-------------------------------------
    public boolean checkPermission() {
        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int ThirdPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int FourthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int FifthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_CONTACTS);

        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ThirdPermissionResult == PackageManager.PERMISSION_GRANTED &&
                FourthPermissionResult == PackageManager.PERMISSION_GRANTED &&
                FifthPermissionResult == PackageManager.PERMISSION_GRANTED;
    }
}
