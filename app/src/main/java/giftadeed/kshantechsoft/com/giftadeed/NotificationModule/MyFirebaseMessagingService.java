package giftadeed.kshantechsoft.com.giftadeed.NotificationModule;

/**
 * Created by Darshan on 05/08/2017.
 */

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import giftadeed.kshantechsoft.com.giftadeed.Bug.Bugreport;
import giftadeed.kshantechsoft.com.giftadeed.EmergencyPositioning.SOSDetailsActivity;
import giftadeed.kshantechsoft.com.giftadeed.Group.GroupPOJO;
import giftadeed.kshantechsoft.com.giftadeed.Landing.MainActivity;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsActivity;
import giftadeed.kshantechsoft.com.giftadeed.Utils.DatabaseAccess;
import giftadeed.kshantechsoft.com.giftadeed.Utils.SessionManager;

/**
 * Created by Belal on 03/11/16.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "MyFirebaseMsgService";
    SessionManager sessionManager;
    String strUserId = null;
    String id = "", type = "", groupids = "";
    //location
    //   private static final String TAG = "BOOMBOOMTESTGPS";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 10f;

    private GoogleApiClient mGoogleApiClient;
    public static Location mLocation;
    public static Location updated_loc;
    String str_geo_point;
    public String title;
    public String message;
    DatabaseAccess databaseAccess;
    ArrayList<GroupPOJO> savedGroupList;
    int checkedGroups;
    int selectedCheckedCount;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
        databaseAccess.open();
        savedGroupList = new ArrayList<>();
        savedGroupList = databaseAccess.getAllGroups();
        checkedGroups = databaseAccess.getGroupCheckedCount();
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + "" + remoteMessage.getNotification().getBody());
        }
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                LocationManager lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                sendPushNotification(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    //this method will display the notification
    //We are passing the JSONObject that is received from
    //firebase cloud messaging
    private void sendPushNotification(JSONObject json) {
        //optionally we can display the json into log
        Log.e(TAG, "Notificationfrag JSON " + json.toString());
        try {
            //getting the json data
            JSONObject data = json.getJSONObject("data");
            //parsing json data
            title = data.getString("title");
            message = data.getString("message");
            String imageUrl = data.getString("latlong");
            String user_one = data.getString("user");
            id = data.getString("id");
            type = data.getString("type");
            groupids = data.getString("grp_ids");
            selectedCheckedCount = databaseAccess.getSelectedCheckedCount(groupids);
            Log.d("db_res_notification", "savedGroupList : " + savedGroupList.size() + ", checkedGroups : " + checkedGroups + ", selectedCheckedCount : " + selectedCheckedCount);
            System.out.print("#######" + imageUrl);
            //creating MyNotificationManager object
            //creating an intent for the notification
            sessionManager = new SessionManager(getApplicationContext());
            HashMap<String, String> user = sessionManager.getUserDetails();
            strUserId = user.get(sessionManager.USER_ID);
            String Notification_status = "";
            HashMap<String, String> Notification_status_map;
            sessionManager = new SessionManager(getApplicationContext());
            Notification_status_map = sessionManager.get_notification_status();
            Notification_status = Notification_status_map.get(sessionManager.KEY_NOTIFICATION);

            //------------------------------------------------------
            if (Notification_status != "") {
                if (user_one.equals("single")) {
                    MyNotificationManager mNotificationManager = new MyNotificationManager(getApplicationContext());
                    Intent intent;
                    if (strUserId == null) {
                        intent = new Intent(getApplicationContext(), MainActivity.class);
                        //pass value
                        intent.putExtra("tag_id", "1001");
                    } else {
                        if (type.equals("1")) {
                            // move to sos details
                            intent = new Intent(getApplicationContext(), SOSDetailsActivity.class);
                            intent.putExtra("sos_id", id);
                        } else {
                            intent = new Intent(getApplicationContext(), TaggedneedsActivity.class);
                            //pass value
                            //intent.putExtra("message", "Charity");
                            intent.putExtra("tag_id", "1001");
                        }
                    }
                    if (Notification_status.equals("ON")) {
                        mNotificationManager.showSmallNotification(title, message, intent);
                    } else if (Notification_status.equals("OFF")) {

                    } else {
                        mNotificationManager.showSmallNotification(title, message, intent);
                    }
                }
            } else {
                if (user_one.equals("single")) {
                    MyNotificationManager mNotificationManager = new MyNotificationManager(getApplicationContext());
                    Intent intent;
                    if (strUserId == null) {
                        intent = new Intent(getApplicationContext(), MainActivity.class);
                        //pass value
                        intent.putExtra("tag_id", "1001");
                    } else {
                        if (type.equals("1")) {
                            // move to sos details
                            intent = new Intent(getApplicationContext(), SOSDetailsActivity.class);
                            intent.putExtra("sos_id", id);
                        } else {
                            intent = new Intent(getApplicationContext(), TaggedneedsActivity.class);
                            //pass value
                            //intent.putExtra("message", "Charity");
                            intent.putExtra("tag_id", "1001");
                        }
                    }
                    mNotificationManager.showSmallNotification(title, message, intent);
                }
            }
            //creating an intent for the notification
            //checking sesion

            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();

            mGoogleApiClient.connect();
            updated_loc = mLocation;
            // code for showing notification
//--------------taking tag location---------------------
            str_geo_point = imageUrl;
            //------------------------------------------------------
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    @Override
    public void onCreate() {

    }


    @Override
    public void onLocationChanged(Location location) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if (mLocation != null) {
                if (str_geo_point != null) {
                    if (!(str_geo_point.equals(""))) {
                        String[] words = str_geo_point.split(",");
                        Location tagLocation2 = new Location("tag Location");
                        tagLocation2.setLatitude(Double.parseDouble(words[0]));
                        tagLocation2.setLongitude(Double.parseDouble(words[1]));

//---------------taking current location-----------------------
                        DecimalFormat df2 = new DecimalFormat("#.##");
                        double radi = sessionManager.getradius();
                        if (radi == 0.0f) {
                            radi = 10.0f;
                        }
                        double dist1 = mLocation.distanceTo(tagLocation2);

                        if (dist1 < radi) {
                            MyNotificationManager mNotificationManager = new MyNotificationManager(getApplicationContext());
                            Intent intent;
                            //------------------------recive notification
                            String Notification_status = "";
                            HashMap<String, String> Notification_status_map;
                            sessionManager = new SessionManager(getApplicationContext());
                            Notification_status_map = sessionManager.get_notification_status();
                            Notification_status = Notification_status_map.get(sessionManager.KEY_NOTIFICATION);

                            if (strUserId == null) {
                                if (type.equals("1")) {
                                    // move to sos details
                                    intent = new Intent(getApplicationContext(), SOSDetailsActivity.class);
                                    intent.putExtra("sos_id", id);
                                } else {
                                    intent = new Intent(getApplicationContext(), MainActivity.class);
                                    //pass value
                                    intent.putExtra("tag_id", "1001");
                                }
                            } else {
                                if (type.equals("1")) {
                                    // move to sos details
                                    intent = new Intent(getApplicationContext(), SOSDetailsActivity.class);
                                    intent.putExtra("sos_id", id);
                                } else {
                                    intent = new Intent(getApplicationContext(), TaggedneedsActivity.class);
                                    intent.putExtra("tag_id", "1001");
                                }
                            }

                            if (Notification_status.equals("ON")) {
                                if (groupids.equals("")) {
                                    if (savedGroupList.size() == 0) {
                                        //tag audience : Individual user
                                        mNotificationManager.showSmallNotification(title, message, intent);
                                    } else {
                                        if (checkedGroups > 0) {
                                            //tag audience : All groups
                                            mNotificationManager.showSmallNotification(title, message, intent);
                                        }
                                    }
                                } else {
                                    if (selectedCheckedCount > 0) {
                                        //tag audience : selected groups
                                        mNotificationManager.showSmallNotification(title, message, intent);
                                    } else {
                                        if (savedGroupList.size() == 0) {
                                            //tag audience : Individual user
                                            mNotificationManager.showSmallNotification(title, message, intent);
                                        }
                                    }
                                }
                            } else if (Notification_status.equals("OFF")) {

                            } else {
                                mNotificationManager.showSmallNotification(title, message, intent);
                            }
                        }
                    }
                } else {
                    // Toast.makeText(this, "Location not Detected", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Toast.makeText(this, "Location not Detected", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
//            StringWriter writer = new StringWriter();
//            e.printStackTrace(new PrintWriter(writer));
//            Bugreport bg = new Bugreport();
//            bg.sendbug(writer.toString());
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection Suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed. Error: " + connectionResult.getErrorCode());
    }
}