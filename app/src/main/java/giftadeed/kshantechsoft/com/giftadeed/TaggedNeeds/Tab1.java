package giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.github.florent37.viewtooltip.ViewTooltip;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.Picasso;
import com.twotoasters.clusterkraf.Clusterkraf;
import com.twotoasters.clusterkraf.InputPoint;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import giftadeed.kshantechsoft.com.giftadeed.Bug.Bugreport;
import giftadeed.kshantechsoft.com.giftadeed.Filter.FilterFrag;
import giftadeed.kshantechsoft.com.giftadeed.Login.LoginActivity;
import giftadeed.kshantechsoft.com.giftadeed.Needdetails.NeedDetailsFrag;
import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.GPSTracker;
import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.TagaNeed;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.list_Model.JobRenderer;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.list_Model.Modeltaglist;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.list_Model.MyItem;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.list_Model.Taggedlist;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.list_Model.YourMapPointModel;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.list_Model.taglistInterface;
import giftadeed.kshantechsoft.com.giftadeed.Utils.DBGAD;
import giftadeed.kshantechsoft.com.giftadeed.Utils.SessionManager;
import giftadeed.kshantechsoft.com.giftadeed.Utils.ToastPopUp;
import giftadeed.kshantechsoft.com.giftadeed.Utils.Validation;
import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.text.TextUtils.TruncateAt.START;
import static android.view.Gravity.CENTER;
import static com.github.florent37.viewtooltip.ViewTooltip.Position.RIGHT;

//import com.basgeekball.awesomevalidation.AwesomeValidation;

/**
 * Created by pranali on 30-Nov-16.
 */

////////////////////////////////////////////////////////////////////
//                                                               //
//     Displays deeds on map                                    //
/////////////////////////////////////////////////////////////////

public class Tab1 extends android.support.v4.app.Fragment implements OnMapReadyCallback, ClusterManager.OnClusterItemClickListener<MyItem>,ClusterManager.OnClusterItemInfoWindowClickListener<MyItem>, GoogleApiClient.OnConnectionFailedListener {
    //Initialisation for google map
    //-----------------------------setting on map -----------------------------------------
    // private GoogleMap googleMap;
    private MapView mapView;
    private boolean mapsSupported = true;
    //------------------------------------------------------------------
//    ImageView imgInfoIcon;
    //------------------------------------------------------------------ui -----------------------
    View rootview;
    FragmentActivity myContext;
    List<Taggedlist> taggedlists = new ArrayList<>();
    //------------------------------------------------------------------------------------------------
    MyItem clickedClusterItem;
    //--------------------------------api call variable--------------------------------------
    SessionManager sessionManager;
    //float radius_set = (float) Validation.radius;
    String strUserId;
    ArrayList<String> lat_long = new ArrayList<>();
    ArrayList<String> icon_path = new ArrayList<>();
    ArrayList<String> tag_title = new ArrayList<>();
    SimpleArcDialog mDialog;
    private GoogleApiClient mGoogleApiClient;
    //-------------------------------------------------------------------------------
    String type_name = Validation.FILTER_CATEGORY;
    MapView mMapView;
    private GoogleMap mGoogleMap;
    static android.support.v4.app.FragmentManager fragmgr;

    public static final int RequestPermissionCode = 1;
    public static final int STORAGE_PERMISSION_CODE = 23;
    public static List<RowData> item;

    double longitude_gps;
    double latitude_gps;
    //----------------------new code pranali

    private ClusterManager<MyItem> mClusterManager;

    //YourMapPointModel[] yourMapPointModels = new YourMapPointModel[] { new YourMapPointModel(new LatLng(0d, 1d) /* etc */ ) };
    ArrayList<InputPoint> inputPoints = new ArrayList<InputPoint>();
    Clusterkraf clusterkraf;
    MarkerOptions mts = new MarkerOptions();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab1, container, false);
        mDialog = new SimpleArcDialog(getContext());
        fragmgr = getFragmentManager();
        TaggedneedsActivity.imgappbarcamera.setVisibility(View.VISIBLE);
        TaggedneedsActivity.imgappbarsetting.setVisibility(View.VISIBLE);
        TaggedneedsActivity.imgfilter.setVisibility(View.GONE);
        TaggedneedsActivity.imgShare.setVisibility(View.GONE);
        mMapView = (MapView) v.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this); //this is important
        //---------------------------network on main thread

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        mGoogleApiClient = ((TaggedneedsActivity) getActivity()).mGoogleApiClient;
        //---------------------------------------------

        /*imgInfoIcon = (ImageView) v.findViewById(R.id.imgInfoicon);
        imgInfoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewTooltip
                        .on(imgInfoIcon)
                        .corner(30)
                        .position(ViewTooltip.Position.RIGHT)
                        .text("Distance displayed may not be accurate")
                        .autoHide(true, 3000)
                        .clickToHide(true)
                        .textColor(Color.BLACK)
                        .color(Color.WHITE)
                        .show();
            }
        });*/



       /* TaggedneedsActivity.imgappbarcamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmgr = getFragmentManager();
                int i=2;
                fragmgr.beginTransaction().replace(R.id.content_frame, TagaNeed.newInstance(i)).addToBackStack(null).commit();
            }
        });
        TaggedneedsActivity.imgappbarsetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmgr.beginTransaction().replace(R.id.content_frame, FilterFrag.newInstance()).addToBackStack(null).commit();
            }
        });*/

        //-----------------------------

        sessionManager = new SessionManager(getActivity());
        HashMap<String, String> user = sessionManager.getUserDetails();
        strUserId = user.get(sessionManager.USER_ID);
        //--------------------------------------------------------------

        //fetching radius details
        sessionManager = new SessionManager(getActivity());
      /*  HashMap<String, String> radius_user = sessionManager.getUserDetails();
        radius_set = Float.parseFloat(radius_user.get(sessionManager.KEY_radius));*/
        //radius_set=10.0f;
        //--------------------------------------------------------------

        if (!(Validation.isNetworkAvailable(myContext))) {
            Toast.makeText(myContext, getString(R.string.network_validation), Toast.LENGTH_SHORT).show();
        } else {

           /* mDialog.setConfiguration(new ArcConfiguration(getContext()));
            mDialog.show();*/
//            final Thread thread = new Thread() {
//                @Override
//
//                public void run() {
//                    try {
//                        sleep(5000);
//
//                        // mDialog.dismiss();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            };
//
//            thread.start();


            //  get_Taglist_data(strUserId);
        }
        //-------------------------------------------------------------------


        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
//
        // Getting longitude of the current location
        latitude_gps = new GPSTracker(myContext).getLatitude();
        longitude_gps = new GPSTracker(myContext).getLongitude();
        LatLng sydney = new LatLng(latitude_gps, longitude_gps);
        googleMap.getUiSettings().setCompassEnabled(true);
           /* Bitmap bmp_img=getBitmapFromURL("http://kshandemo.co.in/GiftAdeed/image/icon/shelter/shelter.png");
            int height = 30;
            int width = 30;
            Bitmap small_bmp= Bitmap.createScaledBitmap(bmp_img, width, height, false);*/
        // googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney").icon(BitmapDescriptorFactory.fromPath("http://kshandemo.co.in/GiftAdeed/logo.png")));
        mGoogleMap.addMarker(new MarkerOptions().position(sydney));

        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        float zoomLevel = (float) 16.0; //This goes up to 21 ///16

        // mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoomLevel));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(sydney)// Sets the center of the map to Mountain View
                .zoom(17)                   // Sets the zoom
                // Sets the orientation of the camera to east
                .tilt(50)                   // Sets the tilt of the camera to 30 degrees
                .build();
        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        // mGoogleMap.addMarker(new MarkerOptions().position(/*some location*/));
        // mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(/*some location*/, 10));
        //setUpClusterer();
        mClusterManager = new ClusterManager<>(myContext, mGoogleMap);
        mDialog.setConfiguration(new ArcConfiguration(getContext()));
        mDialog.show();
        if (!(Validation.isOnline(getActivity()))) {
            ToastPopUp.show(getActivity(), getString(R.string.network_validation));
        } else {
            get_Taglist_data(strUserId);
        }

//        mClusterManager.setRenderer(new JobRenderer(myContext, mGoogleMap, mClusterManager));
        mGoogleMap.setOnCameraIdleListener(mClusterManager);
        mGoogleMap.setInfoWindowAdapter(mClusterManager.getMarkerManager());
        mGoogleMap.setOnInfoWindowClickListener(mClusterManager); //added
        mGoogleMap.setOnMarkerClickListener(mClusterManager);

//        mClusterManager.setOnClusterClickListener(this);
//        mClusterManager.setOnClusterInfoWindowClickListener(this);
        mClusterManager.setOnClusterItemClickListener(this);
        mClusterManager.setOnClusterItemInfoWindowClickListener(this);

        /*mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MyItem>() {
            @Override
            public boolean onClusterItemClick(MyItem item) {
                clickedClusterItem = item;
                return false;
            }
        });*/

        mClusterManager.getMarkerCollection().setOnInfoWindowAdapter(new PopupAdapter(clickedClusterItem, getLayoutInflater()));
        // mGoogleMap.setOnMarkerClickListener(mClusterManager);
        // mGoogleMap.setOnInfoWindowClickListener(mClusterManager);
        /*mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Log.d("Snippet", marker.getSnippet());

                String str_address = "", str_tagid = "", str_geopoint = "", str_taggedPhotoPath = "", str_description = "", str_characterPath = "",
                        str_fname = "", str_lname = "", str_privacy = "", str_userID = "", str_needName = "", str_totalTaggedCreditPoints = "",
                        str_totalFulfilledCreditPoints = "", str_title = "", str_date = "", str_distance = "";

                Location myLocation = new Location("My Location");
                myLocation.setLatitude(latitude_gps);
                myLocation.setLongitude(longitude_gps);
                for (int m = 0; m < taggedlists.size(); m++) {
                    if (marker.getSnippet().equals(taggedlists.get(m).getTaggedID())) {
                        str_address = taggedlists.get(m).getAddress();
                        str_tagid = taggedlists.get(m).getTaggedID();
                        str_geopoint = taggedlists.get(m).getGeopoint();
                        str_taggedPhotoPath = taggedlists.get(m).getTaggedPhotoPath();
                        str_description = taggedlists.get(m).getDescription();
                        str_characterPath = taggedlists.get(m).getCharacterPath();
                        str_fname = taggedlists.get(m).getFname();
                        str_lname = taggedlists.get(m).getLname();
                        str_privacy = taggedlists.get(m).getPrivacy();
                        str_userID = taggedlists.get(m).getUserID();
                        str_needName = taggedlists.get(m).getNeedName();
                        str_totalTaggedCreditPoints = taggedlists.get(m).getTotalTaggedCreditPoints();
                        str_totalFulfilledCreditPoints = taggedlists.get(m).getTotalFulfilledCreditPoints();
                        str_title = taggedlists.get(m).getTaggedTitle();
                        str_date = taggedlists.get(m).getTaggedDatetime();
                        str_description = taggedlists.get(m).getDescription();


                        String str_geo_point = taggedlists.get(m).getGeopoint();
                        String[] words = str_geo_point.split(",");
                        Location tagLocation2 = new Location("tag Location");
                        tagLocation2.setLatitude(Double.parseDouble(words[0]));
                        tagLocation2.setLongitude(Double.parseDouble(words[1]));

                        float dist1 = myLocation.distanceTo(tagLocation2) / 1000;
                        str_distance = String.valueOf(dist1);

                    }
                }


                android.support.v4.app.FragmentManager newfrag;
                newfrag = getActivity().getSupportFragmentManager();
                //  str_distance = Double.toString(item.get(position).getDistance());
                Bundle bundle = new Bundle();
                bundle.putString("str_address", str_address);
                bundle.putString("str_tagid", str_tagid);
                bundle.putString("str_geopoint", str_geopoint);
                bundle.putString("str_taggedPhotoPath", str_taggedPhotoPath);
                bundle.putString("str_description", str_description);
                bundle.putString("str_characterPath", str_characterPath);
                bundle.putString("str_fname", str_fname);
                bundle.putString("str_lname", str_lname);
                bundle.putString("str_privacy", str_privacy);
                bundle.putString("str_userID", str_userID);
                bundle.putString("str_needName", str_needName);
                bundle.putString("str_totalTaggedCreditPoints", str_totalTaggedCreditPoints);
                bundle.putString("str_totalFulfilledCreditPoints", str_totalFulfilledCreditPoints);

                bundle.putString("str_title", str_title);
                bundle.putString("str_date", str_date);
                bundle.putString("str_distance", str_distance);
                bundle.putString("tab", "tab1");
                *//*NeedDetailsFrag fragInfo = new NeedDetailsFrag();
                fragInfo.setArguments(bundle);
                fragmgr.beginTransaction().replace(R.id.Myprofile_frame, fragInfo).commit();*//*
                NeedDetailsFrag fragInfo = new NeedDetailsFrag();
                fragInfo.setArguments(bundle);
                newfrag.beginTransaction().replace(R.id.content_main, fragInfo).commit();


            }
        });*/
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }


    //----------------------------------------------------------api call for tagger list

    public void get_Taglist_data(String user_id) {

      /*  final ProgressDialog progressDialog = new ProgressDialog(myContext);
        progressDialog.show();*/
        //setUpClusterer();


        mDialog.setCancelable(false);
        item = new ArrayList<>();
        final RowData rowData = new RowData();
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        taglistInterface service = retrofit.create(taglistInterface.class);

        Call<Modeltaglist> call = service.fetchData(user_id);
        call.enqueue(new Callback<Modeltaglist>() {
            @Override
            public void onResponse(Response<Modeltaglist> response, Retrofit retrofit) {
                Modeltaglist model = new Modeltaglist();
                try {
                    model = response.body();
                    int isblock = 0;
                    try {
                        isblock = model.getIsBlocked();
                    } catch (Exception e) {
                        isblock = 0;
                    }
                    if (isblock == 1) {
                        mDialog.dismiss();
                        FacebookSdk.sdkInitialize(getActivity());
                        Toast.makeText(getContext(), getResources().getString(R.string.block_toast), Toast.LENGTH_SHORT).show();
                        sessionManager.createUserCredentialSession(null, null, null);
                        LoginManager.getInstance().logOut();
                        int i = new DBGAD(getContext()).delete_row_message();
                        sessionManager.set_notification_status("ON");

                        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                                new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(Status status) {
                                        //updateUI(false);
                                    }
                                });

                        Intent loginintent = new Intent(getActivity(), LoginActivity.class);
                        loginintent.putExtra("message", "Charity");
                        startActivity(loginintent);
                    } else {

                        taggedlists = response.body().getTaggedlist();
                        double current_latitude = new GPSTracker(myContext).getLatitude();
//
//                // Getting longitude of the current location
                        double current_longitude = new GPSTracker(myContext).getLongitude();
                        Location myLocation = new Location("My Location");
                        myLocation.setLatitude(current_latitude);
                        myLocation.setLongitude(current_longitude);

//---------------------adding data\
                        if (model != null) {
                            if (model.getTaggedlist().size() > 0) {
                                Log.d("taglist_Size", String.valueOf(model.getTaggedlist().size()));
                                for (int j = 0; j < model.getTaggedlist().size(); j++) {

                                    String str_geo_point = model.getTaggedlist().get(j).getGeopoint();
                                    String[] words = str_geo_point.split(",");
                                    if (words.length > 1) {
                                        Location tagLocation2 = new Location("tag Location");
                                        tagLocation2.setLatitude(Double.parseDouble(words[0]));
                                        tagLocation2.setLongitude(Double.parseDouble(words[1]));
                                        double radi = sessionManager.getradius();
                                        DecimalFormat df2 = new DecimalFormat("#.##");
                                        double dist1 = myLocation.distanceTo(tagLocation2);
                                        Log.d("radius", String.valueOf(dist1));
                                        if (dist1 < radi) {
                                            //  Log.d("val", "typenamet"+type_name);
                                            if (type_name.equals(model.getTaggedlist().get(j).getNeedName())) {

                                                // System.out.print(model.getTaggedlist().get(j).getIconPath());
                                                lat_long.add(model.getTaggedlist().get(j).getGeopoint());
                                                icon_path.add(model.getTaggedlist().get(j).getIconPath());
                                                tag_title.add(model.getTaggedlist().get(j).getNeedName());

                                                //---------------------for tab2

                                                rowData.setTitle(model.getTaggedlist().get(j).getNeedName());
                                                rowData.setAddress(model.getTaggedlist().get(j).getAddress());
                                                rowData.setDate(model.getTaggedlist().get(j).getTaggedDatetime());
                                                rowData.setImagepath(model.getTaggedlist().get(j).getTaggedPhotoPath());
                                                rowData.setDistance(dist1);
                                                rowData.setCharacterPath(model.getTaggedlist().get(j).getCharacterPath());
                                                rowData.setFname(model.getTaggedlist().get(j).getFname());
                                                rowData.setLname(model.getTaggedlist().get(j).getLname());
                                                rowData.setPrivacy(model.getTaggedlist().get(j).getPrivacy());
                                                rowData.setNeedName(model.getTaggedlist().get(j).getNeedName());
                                                rowData.setTotalTaggedCreditPoints(model.getTaggedlist().get(j).getTotalTaggedCreditPoints());
                                                rowData.setTotalFulfilledCreditPoints(model.getTaggedlist().get(j).getTotalFulfilledCreditPoints());
                                                rowData.setUserID(model.getTaggedlist().get(j).getUserID());
                                                rowData.setTaggedID(model.getTaggedlist().get(j).getTaggedID());
                                                rowData.setGeopoint(model.getTaggedlist().get(j).getGeopoint());
                                                rowData.setTaggedPhotoPath(model.getTaggedlist().get(j).getTaggedPhotoPath());
                                                rowData.setDescription(model.getTaggedlist().get(j).getDescription());
                                                rowData.setViews(model.getTaggedlist().get(j).getViews());
                                                rowData.setEndorse(model.getTaggedlist().get(j).getEndorse());

                                                item.add(rowData);
                                                // new Tab2().get_Tag_data();


                                                String str_lati_logi = model.getTaggedlist().get(j).getGeopoint();
                                                String[] words_new = str_lati_logi.split(",");

                                                String icon_path_str_new = WebServices.MANI_URL + WebServices.SUB_URL + model.getTaggedlist().get(j).getCharacterPath();
                                                Log.d("imagepath", icon_path_str_new);
                                                MyItem offsetItem = new MyItem(Double.parseDouble(words_new[0]), Double.parseDouble(words_new[1]), model.getTaggedlist().get(j).getTaggedTitle(), model.getTaggedlist().get(j).getTaggedID(), icon_path_str_new);
                                                mClusterManager.addItem(offsetItem);
                                                mClusterManager.setRenderer(new JobRenderer(getContext(), mGoogleMap, mClusterManager));

                                            } else if (type_name.equals("All")) {

                                                lat_long.add(model.getTaggedlist().get(j).getGeopoint());
                                                icon_path.add(model.getTaggedlist().get(j).getIconPath());
                                                tag_title.add(model.getTaggedlist().get(j).getNeedName());


                                      /*  RowData rowData = new RowData();
                                        rowData.setTitle(model.getTaggedlist().get(j).getTaggedTitle());
                                        rowData.setAddress(model.getTaggedlist().get(j).getAddress());
                                        rowData.setDate(model.getTaggedlist().get(j).getTaggedDatetime());
                                        rowData.setImagepath(model.getTaggedlist().get(j).getTaggedPhotoPath());
                                        rowData.setDistance(dist1);
                                        rowData.setDescription(model.getTaggedlist().get(j).getDescription());
                                        rowData.setEndorse(model.getTaggedlist().get(j).getEndorse());
                                        rowData.setViews(model.getTaggedlist().get(j).getViews());

                                        item.add(rowData);*/

                                                //-----------new


                                                //---------------------for tab2

                                                rowData.setTitle(model.getTaggedlist().get(j).getNeedName());
                                                rowData.setAddress(model.getTaggedlist().get(j).getAddress());
                                                rowData.setDate(model.getTaggedlist().get(j).getTaggedDatetime());
                                                rowData.setImagepath(model.getTaggedlist().get(j).getTaggedPhotoPath());
                                                rowData.setDistance(dist1);
                                                rowData.setCharacterPath(model.getTaggedlist().get(j).getCharacterPath());
                                                rowData.setFname(model.getTaggedlist().get(j).getFname());
                                                rowData.setLname(model.getTaggedlist().get(j).getLname());
                                                rowData.setPrivacy(model.getTaggedlist().get(j).getPrivacy());
                                                rowData.setNeedName(model.getTaggedlist().get(j).getNeedName());
                                                rowData.setTotalTaggedCreditPoints(model.getTaggedlist().get(j).getTotalTaggedCreditPoints());
                                                rowData.setTotalFulfilledCreditPoints(model.getTaggedlist().get(j).getTotalFulfilledCreditPoints());
                                                rowData.setUserID(model.getTaggedlist().get(j).getUserID());
                                                rowData.setTaggedID(model.getTaggedlist().get(j).getTaggedID());
                                                rowData.setGeopoint(model.getTaggedlist().get(j).getGeopoint());
                                                rowData.setTaggedPhotoPath(model.getTaggedlist().get(j).getTaggedPhotoPath());
                                                rowData.setDescription(model.getTaggedlist().get(j).getDescription());
                                                rowData.setViews(model.getTaggedlist().get(j).getViews());
                                                rowData.setEndorse(model.getTaggedlist().get(j).getEndorse());

                                                item.add(rowData);


                                                String str_lati_logi = model.getTaggedlist().get(j).getGeopoint();
                                                String[] words_new = str_lati_logi.split(",");
                                                String marker_title = model.getTaggedlist().get(j).getNeedName();
                                                String marker_id = model.getTaggedlist().get(j).getTaggedID();
                                                String icon_path_str_new = WebServices.MANI_URL + WebServices.SUB_URL + model.getTaggedlist().get(j).getCharacterPath();
                                                Log.d("imagepath2", icon_path_str_new);
                                                MyItem offsetItem = new MyItem(Double.parseDouble(words_new[0]), Double.parseDouble(words_new[1]), marker_title, marker_id, icon_path_str_new);
                                                mClusterManager.addItem(offsetItem);
                                                mClusterManager.setRenderer(new JobRenderer(getContext(), mGoogleMap, mClusterManager));

                                            }

                                            //for  tab 2


                                   /* RowData rowData = new RowData();
                                    rowData.setTitle(model.getTaggedlist().get(j).getTaggedTitle());
                                    rowData.setAddress(model.getTaggedlist().get(j).getAddress());
                                    rowData.setDate(model.getTaggedlist().get(j).getTaggedDatetime());
                                    rowData.setImagepath(model.getTaggedlist().get(j).getTaggedPhotoPath());
                                    rowData.setDistance(dist1);
                                    item.add(rowData);*/
//
//

                                        }
                                    }

                                }

                                mDialog.dismiss();
                                //new Tab2().get_Tag_data();
                            }
                        }




/*
                com.twotoasters.clusterkraf.Options options = new com.twotoasters.clusterkraf.Options();
                // customize the options before you construct a Clusterkraf instance




                clusterkraf = new Clusterkraf(mGoogleMap, options, inputPoints);*/

                        mClusterManager.cluster();

                        mDialog.dismiss();
                    }
                } catch (Exception e) {
//                    StringWriter writer = new StringWriter();
//                    e.printStackTrace(new PrintWriter(writer));
//                    Bugreport bg = new Bugreport();
//                    bg.sendbug(writer.toString());
                }

               /* inputPoints = new ArrayList<InputPoint>(lat_long.size());
                for (int i=0;i>lat_long.size();i++) {

                    String str_geo_point = lat_long.get(i);
                    String[] words = str_geo_point.split(",");

                    LatLng tagLocation2 = new LatLng(Double.parseDouble(words[0]), Double.parseDouble(words[1]));
                    inputPoints.add(new InputPoint(tagLocation2, model));
                }*/
//--------------sorting

               /* for (int j = 0; j < lat_long.size(); j++) {
                   // mDialog.show();

                    String str_geo_point = lat_long.get(j);
                    String[] words = str_geo_point.split(",");

                    LatLng tagLocation2 = new LatLng(Double.parseDouble(words[0]), Double.parseDouble(words[1]));
                    String icon_path_str_new= WebServices.MANI_URL+ WebServices.SUB_URL+icon_path.get(j);

                   // System.out.print(icon_path_str_new);
                    Bitmap bmp_img=getBitmapFromURL(icon_path_str_new);
                    int height = 70;
                    int width = 70;
                    if (bmp_img!=null) {
                        Bitmap small_bmp = Bitmap.createScaledBitmap(bmp_img, width, height, false);
                        // googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney").icon(BitmapDescriptorFactory.fromPath("http://kshandemo.co.in/GiftAdeed/logo.png")));

                       // mGoogleMap.addMarker(new MarkerOptions().position(tagLocation2).title(tag_title.get(j)).icon(BitmapDescriptorFactory.fromBitmap(small_bmp)));
                        // mapView.invalidate();
                       // mMapView.invalidate();
                    }//mDialog.dismiss();

                }*/
                //--------------------------------


            }

            @Override
            public void onFailure(Throwable t) {

                ToastPopUp.show(myContext, getString(R.string.server_response_error));
                mDialog.dismiss();
            }
        });
        // mDialog.dismiss();
    }

    public boolean checkLocationPermission() {

        //int FirstPermissionResult = ContextCompat.checkSelfPermission(getContext(), CAMERA);
        int ThirdPermissionResult = ContextCompat.checkSelfPermission(getContext(), WRITE_EXTERNAL_STORAGE);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getContext(), READ_EXTERNAL_STORAGE);

        //int FourthPermissionResult = ContextCompat.checkSelfPermission(getContext(), ACCESS_FINE_LOCATION);

        //FirstPermissionResult == PackageManager.PERMISSION_GRANTED&&
        return ThirdPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED;

        // FourthPermissionResult == PackageManager.PERMISSION_GRANTED;*/
    }

    private void requestgallPermission() {
        try {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                //If the user has denied the permission previously your code will come to this block
                //Here you can explain why you need this permission
                //Explain here why you need this permission

                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, RequestPermissionCode);
                Toast.makeText(getActivity(), "Permission are denied please enable permissions", Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            // SendMail smail=new SendMail("giftadeed2017@gmail.com",getResources().getString(R.string.error),e.toString());
//            StringWriter writer = new StringWriter();
//            e.printStackTrace(new PrintWriter(writer));
//            Bugreport bg = new Bugreport();
//            bg.sendbug(writer.toString());
        }
        /*ActivityCompat.requestPermissions(getActivity(), new String[]
                {

                        READ_EXTERNAL_STORAGE,
                        WRITE_EXTERNAL_STORAGE
                       *//* ACCESS_FINE_LOCATION*//*
                }, RequestPermissionCode);*/


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

                    if (CameraPermission && WriteExternalStoragePermission && ReadExternalStoragePermission) {

                        //Toast.makeText(getActivity(), "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        // Toast.makeText(getActivity(),"Permission Denied",Toast.LENGTH_LONG).show();

                    }
                }

                break;

            case STORAGE_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //Displaying a toast
                    Toast.makeText(getContext(), "Permission granted now you can access location", Toast.LENGTH_LONG).show();
                } else {
                    //Displaying another toast if permission is not granted
                    Toast.makeText(getContext(), "Oops you just denied the permission", Toast.LENGTH_LONG).show();
                }
                break;

        }
    }
    //---------------add marker

    private void setUpClusterer() {
        // Position the map.
        //getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.503186, -0.126446), 10));

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<MyItem>(getContext(), mGoogleMap);

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        mGoogleMap.setOnCameraIdleListener(mClusterManager);
        mGoogleMap.setOnMarkerClickListener(mClusterManager);

        // Add cluster items (markers) to the cluster manager.

    }


    @Override
    public void onClusterItemInfoWindowClick(MyItem myItem) {
        String str_address = "", str_tagid = "", str_geopoint = "", str_taggedPhotoPath = "", str_description = "", str_characterPath = "",
                str_fname = "", str_lname = "", str_privacy = "", str_userID = "", str_needName = "", str_totalTaggedCreditPoints = "",
                str_totalFulfilledCreditPoints = "", str_title = "", str_date = "", str_distance = "";

        Location myLocation = new Location("My Location");
        myLocation.setLatitude(latitude_gps);
        myLocation.setLongitude(longitude_gps);
        for (int m = 0; m < taggedlists.size(); m++) {
            if (myItem.getSnippet().equals(taggedlists.get(m).getTaggedID())) {
                str_address = taggedlists.get(m).getAddress();
                str_tagid = taggedlists.get(m).getTaggedID();
                str_geopoint = taggedlists.get(m).getGeopoint();
                str_taggedPhotoPath = taggedlists.get(m).getTaggedPhotoPath();
                str_description = taggedlists.get(m).getDescription();
                str_characterPath = taggedlists.get(m).getCharacterPath();
                str_fname = taggedlists.get(m).getFname();
                str_lname = taggedlists.get(m).getLname();
                str_privacy = taggedlists.get(m).getPrivacy();
                str_userID = taggedlists.get(m).getUserID();
                str_needName = taggedlists.get(m).getNeedName();
                str_totalTaggedCreditPoints = taggedlists.get(m).getTotalTaggedCreditPoints();
                str_totalFulfilledCreditPoints = taggedlists.get(m).getTotalFulfilledCreditPoints();
                str_title = taggedlists.get(m).getTaggedTitle();
                str_date = taggedlists.get(m).getTaggedDatetime();
                str_description = taggedlists.get(m).getDescription();


                String str_geo_point = taggedlists.get(m).getGeopoint();
                String[] words = str_geo_point.split(",");
                Location tagLocation2 = new Location("tag Location");
                tagLocation2.setLatitude(Double.parseDouble(words[0]));
                tagLocation2.setLongitude(Double.parseDouble(words[1]));

                float dist1 = myLocation.distanceTo(tagLocation2);
                str_distance = String.valueOf(dist1);

            }
        }


        android.support.v4.app.FragmentManager newfrag;
        newfrag = getActivity().getSupportFragmentManager();
        //  str_distance = Double.toString(item.get(position).getDistance());
        Bundle bundle = new Bundle();
        bundle.putString("str_address", str_address);
        bundle.putString("str_tagid", str_tagid);
        bundle.putString("str_geopoint", str_geopoint);
        bundle.putString("str_taggedPhotoPath", str_taggedPhotoPath);
        bundle.putString("str_description", str_description);
        bundle.putString("str_characterPath", str_characterPath);
        bundle.putString("str_fname", str_fname);
        bundle.putString("str_lname", str_lname);
        bundle.putString("str_privacy", str_privacy);
        bundle.putString("str_userID", str_userID);
        bundle.putString("str_needName", str_needName);
        bundle.putString("str_totalTaggedCreditPoints", str_totalTaggedCreditPoints);
        bundle.putString("str_totalFulfilledCreditPoints", str_totalFulfilledCreditPoints);

        bundle.putString("str_title", str_title);
        bundle.putString("str_date", str_date);
        bundle.putString("str_distance", str_distance);
        bundle.putString("tab", "tab1");
                /*NeedDetailsFrag fragInfo = new NeedDetailsFrag();
                fragInfo.setArguments(bundle);
                fragmgr.beginTransaction().replace(R.id.Myprofile_frame, fragInfo).commit();*/
        NeedDetailsFrag fragInfo = new NeedDetailsFrag();
        fragInfo.setArguments(bundle);
        newfrag.beginTransaction().replace(R.id.content_frame, fragInfo).commit();


    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        mGoogleApiClient.connect();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed()) { // fragment is visible and have created
            Log.d("UserVisible","visible");
//            mClusterManager.setRenderer(new JobRenderer(getContext(), mGoogleMap, mClusterManager));
        }
    }

    @Override
    public boolean onClusterItemClick(MyItem myItem) {
        return false;
    }
}