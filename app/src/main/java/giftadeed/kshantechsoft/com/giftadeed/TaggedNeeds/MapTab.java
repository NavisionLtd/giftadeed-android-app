package giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.squareup.okhttp.OkHttpClient;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import giftadeed.kshantechsoft.com.giftadeed.BottomNavigation.BottomNavigationViewHelper;
import giftadeed.kshantechsoft.com.giftadeed.EmergencyPositioning.SOSDetailsFrag;
import giftadeed.kshantechsoft.com.giftadeed.EmergencyPositioning.SOSOptionActivity;
import giftadeed.kshantechsoft.com.giftadeed.Group.GroupResponseStatus;
import giftadeed.kshantechsoft.com.giftadeed.Login.LoginActivity;
import giftadeed.kshantechsoft.com.giftadeed.MyFullFillTag.MyFullFillTags;
import giftadeed.kshantechsoft.com.giftadeed.Needdetails.NeedDetailsFrag;
import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.Resources.ResourceDetailsFrag;
import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.GPSTracker;
import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.TagaNeed;
import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.TaggedDeedsAdapter;
import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.TaggedDeedsPojo;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.list_Model.Modelresourcelist;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.list_Model.Modelsoslist;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.list_Model.Modeltaglist;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.list_Model.Resourcelist;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.list_Model.ResourcelistInterface;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.list_Model.SOSlist;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.list_Model.SOSlistInterface;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.list_Model.Taggedlist;
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

import static com.facebook.FacebookSdk.getApplicationContext;

public class MapTab extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, Animation.AnimationListener {
    ArrayList<TaggedDeedsPojo> taggeddeeds = new ArrayList<TaggedDeedsPojo>();
    FragmentActivity myContext;
    List<Taggedlist> taggedlists = new ArrayList<>();
    List<SOSlist> soslists = new ArrayList<>();
    List<Resourcelist> resourcelists = new ArrayList<>();
    SessionManager sessionManager;
    String strUserId, str_tagid = "", str_geopoint = "", str_address = "";
    ArrayList<String> lat_long = new ArrayList<>();
    ArrayList<String> icon_path = new ArrayList<>();
    ArrayList<String> tag_title = new ArrayList<>();
    SimpleArcDialog mDialog;
    private GoogleApiClient mGoogleApiClient;
    String filter_category = Validation.FILTER_CATEGORY;
    String filter_groups = Validation.FILTER_GROUP_IDS;
    String permanent_marker_path;
    MapView mMapView;
    private GoogleMap mGoogleMap;
    static FragmentManager fragmgr;
    public static final int RequestPermissionCode = 1;
    public static final int STORAGE_PERMISSION_CODE = 23;
    public static List<RowData> item;
    double longitude_gps;
    double latitude_gps;
    ImageView mMarkerImageView;
    View customView;
    String selectedLangugae;
    Locale locale;
    Configuration config;
    Animation animFadein;
    ImageView imageView;
    MediaPlayer mp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_map_tab, container, false);
        mDialog = new SimpleArcDialog(getContext());
        fragmgr = getFragmentManager();
        TaggedneedsActivity.imgappbarcamera.setVisibility(View.VISIBLE);
        TaggedneedsActivity.imgappbarsetting.setVisibility(View.VISIBLE);
        TaggedneedsActivity.imgfilter.setVisibility(View.GONE);
        TaggedneedsActivity.imgShare.setVisibility(View.GONE);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) v.findViewById(R.id.bottom_navigation_bar);
        bottomNavigationView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mMapView = (MapView) v.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this); //this is important

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        mGoogleApiClient = ((TaggedneedsActivity) getActivity()).mGoogleApiClient;

        sessionManager = new SessionManager(getActivity());
        selectedLangugae = sessionManager.getLanguage();
        updateLanguage(selectedLangugae);
        HashMap<String, String> user = sessionManager.getUserDetails();
        strUserId = user.get(sessionManager.USER_ID);
        HashMap<String, String> notification = sessionManager.get_notification_status();
        String Notification_status = notification.get(sessionManager.KEY_NOTIFICATION);
        Log.d("Notification_status", "" + Notification_status);
        strUserId = user.get(sessionManager.USER_ID);
        sessionManager = new SessionManager(getActivity());
        if (!(Validation.isNetworkAvailable(myContext))) {
            Toast.makeText(myContext, getString(R.string.network_validation), Toast.LENGTH_SHORT).show();
        } else {

        }
        customView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.view_custom_marker, null);
        mMarkerImageView = (ImageView) customView.findViewById(R.id.profile_image);
        return v;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager newfrag;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    newfrag = getActivity().getSupportFragmentManager();
                    TaggedneedsFrag fragInfo = new TaggedneedsFrag();
                    newfrag.beginTransaction().replace(R.id.content_frame, fragInfo).commit();
                    return true;
                case R.id.navigation_tag_deed:
                    newfrag = getActivity().getSupportFragmentManager();
                    TagaNeed tagaNeed = new TagaNeed();
                    newfrag.beginTransaction().replace(R.id.content_frame, tagaNeed).commit();
                    return true;
                case R.id.navigation_fullfill:
                    newfrag = getActivity().getSupportFragmentManager();
                    Bundle bundle = new Bundle();
                    bundle.putString("tab", "tabbar");
                    MyFullFillTags myFullFillTags = new MyFullFillTags();
                    myFullFillTags.setArguments(bundle);
                    newfrag.beginTransaction().replace(R.id.content_frame, myFullFillTags).commit();
                    return true;
                case R.id.navigation_sos:
                    Intent i = new Intent(getApplicationContext(), SOSOptionActivity.class);
                    i.putExtra("callingfrom", "app");
                    sessionManager.store_sos_option1_clicked("no");
                    sessionManager.store_sos_option2_clicked("no");
                    sessionManager.store_sos_option3_clicked("no");
                    startActivity(i);

//                    endorseDialog();
                    return true;
            }
            return false;
        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        mGoogleMap.getUiSettings().setMapToolbarEnabled(true);

        if (!(Validation.isOnline(getActivity()))) {
            ToastPopUp.show(getActivity(), getString(R.string.network_validation));
        } else {
            mDialog.setConfiguration(new ArcConfiguration(getContext()));
            mDialog.show();
            get_Taglist_data(strUserId); // get normal deeds and permanent deeds
            get_SOSlist_data(strUserId); // get sos list tags
            get_Resourcelist_data(strUserId); // get resource list tags
        }

        latitude_gps = new GPSTracker(myContext).getLatitude();
        longitude_gps = new GPSTracker(myContext).getLongitude();
        LatLng sydney = new LatLng(latitude_gps, longitude_gps);
        googleMap.getUiSettings().setCompassEnabled(true);
//        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.addMarker(new MarkerOptions().position(sydney).title("My location"));
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(sydney)// Sets the center of the map to Mountain View
                .zoom(17)                   // Sets the zoom
                .tilt(0)                   // Sets the tilt of the camera to 30 degrees
                .build();
        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String markertag = String.valueOf(marker.getTag());
                if (markertag != null) {
                    if (markertag.equals("null")) {
                        marker.setTitle("Current Location");
                    } else {
                        marker.setTitle(markertag);
                    }
                } else {
                    marker.setTitle("Current Location");
                }
                return false;
            }
        });

        mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Log.d("marker_clicked", "" + marker.getSnippet() + "," + marker.getTag() + "," + marker.getId() + "," + marker.getTitle());
                String str_taggedPhotoPath = "", str_description = "", str_characterPath = "",
                        str_fname = "", str_lname = "", str_privacy = "", str_userID = "", str_needName = "", str_totalTaggedCreditPoints = "",
                        str_totalFulfilledCreditPoints = "", str_title = "", str_date = "", str_distance = "", str_permanent = "";

                if (marker.getTitle().equals("Current Location")) {

                } else {
                    Location myLocation = new Location("My Location");
                    myLocation.setLatitude(latitude_gps);
                    myLocation.setLongitude(longitude_gps);
                    Log.d("taggedlistsize", "" + taggedlists.size());
                    for (int m = 0; m < taggedlists.size(); m++) {
                        if (marker.getTag().equals("Permanent")) {
                            if (marker.getSnippet().equals(taggedlists.get(m).getTaggedID())) {
                                str_tagid = taggedlists.get(m).getTaggedID();
                                str_geopoint = taggedlists.get(m).getGeopoint();
                                str_address = taggedlists.get(m).getAddress();
                                if (!(Validation.isOnline(getActivity()))) {
                                    ToastPopUp.show(getActivity(), getString(R.string.network_validation));
                                } else {
                                    getPermanentDeedList(str_tagid, str_geopoint, str_address);
                                }
                            }
                        } else if (marker.getTag().equals(taggedlists.get(m).getNeedName())) {
                            if (marker.getSnippet().equals(taggedlists.get(m).getTaggedID())) {
                                if (!(Validation.isOnline(getActivity()))) {
                                    ToastPopUp.show(getActivity(), getString(R.string.network_validation));
                                } else {
                                    Log.d("taggedlistsize", "in condition");
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
                                    str_permanent = taggedlists.get(m).getPermanent();
                                    String str_geo_point = taggedlists.get(m).getGeopoint();
                                    String[] words = str_geo_point.split(",");
                                    if (words.length > 1) {
                                        Location tagLocation2 = new Location("tag Location");
                                        tagLocation2.setLatitude(Double.parseDouble(words[0]));
                                        tagLocation2.setLongitude(Double.parseDouble(words[1]));
                                        float dist1 = myLocation.distanceTo(tagLocation2);
                                        str_distance = String.valueOf(dist1);

                                        FragmentManager newfrag;
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
                                        bundle.putString("str_permanent", str_permanent);
                                        bundle.putString("str_title", str_title);
                                        bundle.putString("str_date", str_date);
                                        bundle.putString("str_distance", str_distance);
                                        bundle.putString("tab", "tab1");
                                        NeedDetailsFrag fragInfo = new NeedDetailsFrag();
                                        fragInfo.setArguments(bundle);
                                        newfrag.beginTransaction().replace(R.id.content_frame, fragInfo).commit();
                                    }
                                }
                            }
                        }
                    }

                    Log.d("soslistsize", "" + soslists.size());
                    for (int m = 0; m < soslists.size(); m++) {
                        if (marker.getTag().equals("SOS")) {
                            if (marker.getSnippet().equals(soslists.get(m).getId())) {
                                if (!(Validation.isOnline(getActivity()))) {
                                    ToastPopUp.show(getActivity(), getString(R.string.network_validation));
                                } else {
                                    Log.d("soslistsize", "in condition");
                                    String str_sosid = soslists.get(m).getId();
                                    FragmentManager newfrag;
                                    newfrag = getActivity().getSupportFragmentManager();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("str_sosid", str_sosid);
                                    SOSDetailsFrag fragInfo = new SOSDetailsFrag();
                                    fragInfo.setArguments(bundle);
                                    newfrag.beginTransaction().replace(R.id.content_frame, fragInfo).commit();
                                }
                            }
                        }
                    }

                    for (int m = 0; m < resourcelists.size(); m++) {
                        if (marker.getTag().equals("Resource")) {
                            if (marker.getSnippet().equals(resourcelists.get(m).getId())) {
                                if (!(Validation.isOnline(getActivity()))) {
                                    ToastPopUp.show(getActivity(), getString(R.string.network_validation));
                                } else {
                                    Log.d("reslistsize", "in condition");
                                    String str_resid = resourcelists.get(m).getId();
                                    FragmentManager newfrag;
                                    newfrag = getActivity().getSupportFragmentManager();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("str_resid", str_resid);
                                    bundle.putString("callingFrom", "map");
                                    ResourceDetailsFrag fragInfo = new ResourceDetailsFrag();
                                    fragInfo.setArguments(bundle);
                                    newfrag.beginTransaction().replace(R.id.content_frame, fragInfo).commit();
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    //----------------------------------------
    private void endorseDialog() {
        final Dialog dialog = new Dialog(getActivity());
        mp = MediaPlayer.create(getActivity(), R.raw.endorsed_sound);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.endorse_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.show();
        imageView = (ImageView) dialog.findViewById(R.id.image_endorsed);
        Glide.with(getActivity())
                .load(R.drawable.ic_endorsed_stamp)
                .into(imageView);
        // load the animation
        animFadein = AnimationUtils.loadAnimation(getContext(),
                R.anim.zoom_in);
        // set animation listener
        animFadein.setAnimationListener(this);
        // start the animation
        imageView.startAnimation(animFadein);
        mp.start();
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (dialog.isShowing()) {
                    dialog.dismiss();

                }
            }
        };
        handler.postDelayed(runnable, 5000);
    }

    private Bitmap getMarkerBitmapFromView(View view, Bitmap bitmap) {
        mMarkerImageView.setImageBitmap(bitmap);
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = view.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        view.draw(canvas);
        return returnedBitmap;
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

    public void get_Taglist_data(String user_id) {
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
        Log.d("input_parameters_tags", user_id);
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
                        permanent_marker_path = response.body().getPermanent_path();
                        double current_latitude = new GPSTracker(myContext).getLatitude();
                        double current_longitude = new GPSTracker(myContext).getLongitude();
                        Location myLocation = new Location("My Location");
                        myLocation.setLatitude(current_latitude);
                        myLocation.setLongitude(current_longitude);
                        if (model != null) {
                            if (model.getTaggedlist().size() > 0) {
                                Log.d("taglist_Size", String.valueOf(model.getTaggedlist().size()));
                                for (int j = 0; j < model.getTaggedlist().size(); j++) {
                                    String audience_all_groups = model.getTaggedlist().get(j).getAll_groups();  // Y or N
                                    String audience_selected_groups = model.getTaggedlist().get(j).getUserGrpIds();  // group ids
                                    String str_geo_point = model.getTaggedlist().get(j).getGeopoint();
                                    String[] words = str_geo_point.split(",");
                                    Log.d("words_size", String.valueOf(words.length));
                                    if (words.length > 1) {
                                        Location tagLocation2 = new Location("tag Location");
                                        tagLocation2.setLatitude(Double.parseDouble(words[0]));
                                        tagLocation2.setLongitude(Double.parseDouble(words[1]));
                                        double radi = sessionManager.getradius();
                                        DecimalFormat df2 = new DecimalFormat("#.##");
                                        double dist1 = myLocation.distanceTo(tagLocation2);
                                        Log.d("radius", String.valueOf(dist1));
                                        if (dist1 < radi) {
                                            if ((filter_category.equals(model.getTaggedlist().get(j).getNeedName()) && filter_groups.equals("All")) || (filter_category.equals("All") && filter_groups.equals("All"))) {
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
                                                rowData.setGetIconPath(model.getTaggedlist().get(j).getIconPath());
                                                rowData.setFname(model.getTaggedlist().get(j).getFname());
                                                rowData.setLname(model.getTaggedlist().get(j).getLname());
                                                rowData.setPrivacy(model.getTaggedlist().get(j).getPrivacy());
                                                rowData.setNeedName(model.getTaggedlist().get(j).getNeedName());
                                                rowData.setTotalTaggedCreditPoints(model.getTaggedlist().get(j).getTotalTaggedCreditPoints());
                                                rowData.setTotalFulfilledCreditPoints(model.getTaggedlist().get(j).getTotalFulfilledCreditPoints());
                                                rowData.setUserID(model.getTaggedlist().get(j).getUserID());
                                                rowData.setCatType(model.getTaggedlist().get(j).getCatType());
                                                rowData.setTaggedID(model.getTaggedlist().get(j).getTaggedID());
                                                rowData.setGeopoint(model.getTaggedlist().get(j).getGeopoint());
                                                rowData.setTaggedPhotoPath(model.getTaggedlist().get(j).getTaggedPhotoPath());
                                                rowData.setDescription(model.getTaggedlist().get(j).getDescription());
                                                rowData.setViews(model.getTaggedlist().get(j).getViews());
                                                rowData.setEndorse(model.getTaggedlist().get(j).getEndorse());
                                                rowData.setPermanent(model.getTaggedlist().get(j).getPermanent());
                                                rowData.setAllGroups(model.getTaggedlist().get(j).getAll_groups());
                                                rowData.setUser_group_ids(model.getTaggedlist().get(j).getUserGrpIds());
                                                item.add(rowData);
                                                // new Tab2().get_Tag_data();


                                                String str_lati_logi = model.getTaggedlist().get(j).getGeopoint();
                                                String[] words_new = str_lati_logi.split(",");
                                                if (words_new.length > 1) {
                                                    final String id = model.getTaggedlist().get(j).getTaggedID();
                                                    final String permanent = model.getTaggedlist().get(j).getPermanent();
                                                    final String catType = model.getTaggedlist().get(j).getCatType();
                                                    final String markerTitle, icon_path_str_new;
                                                    if (permanent.equals("Y")) {
                                                        markerTitle = "Permanent";
                                                        icon_path_str_new = WebServices.MANI_URL + WebServices.SUB_URL + permanent_marker_path;
                                                    } else {
                                                        if (catType.equals("C")) {
                                                            markerTitle = model.getTaggedlist().get(j).getTaggedTitle();
                                                            icon_path_str_new = WebServices.CUSTOM_CATEGORY_IMAGE_URL + model.getTaggedlist().get(j).getIconPath();
                                                        } else {
                                                            markerTitle = model.getTaggedlist().get(j).getTaggedTitle();
                                                            icon_path_str_new = WebServices.MANI_URL + WebServices.SUB_URL + model.getTaggedlist().get(j).getIconPath();
                                                        }
                                                    }

                                                    Log.d("imagepath", icon_path_str_new);
                                                    Double maplat = Double.parseDouble(words_new[0]);
                                                    Double maplong = Double.parseDouble(words_new[1]);
                                                    final LatLng point = new LatLng(maplat, maplong);//
                                                    Glide.with(getApplicationContext())
                                                            .asBitmap()
                                                            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                                                            .load(icon_path_str_new)
                                                            .into(new SimpleTarget<Bitmap>() {
                                                                @Override
                                                                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                                                    Marker marker = mGoogleMap.addMarker(new MarkerOptions().position(point)
                                                                            .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(customView, resource))).title(markerTitle).anchor(0.5f, 0.907f));
                                                                    marker.setTag(markerTitle);
                                                                    marker.setSnippet(id);
//                                                            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 17f));
                                                                }
                                                            });
                                                }
                                            } else if ((filter_category.equals(model.getTaggedlist().get(j).getNeedName()) && (!filter_groups.equals("All"))) || (filter_category.equals("All") && (!filter_groups.equals("All")))) {
                                                String[] split_filter_groups = filter_groups.split(",");       // filter selected groupids
                                                ArrayList<String> filterGroupList = new ArrayList<String>(Arrays.asList(split_filter_groups)); // arraylist for filter selected groupids

                                                String[] split_audience_groupids = audience_selected_groups.split(","); // server audience groupids
                                                ArrayList<String> audienceGroupList = new ArrayList<String>(Arrays.asList(split_audience_groupids)); // arraylist server audience groupids
                                                if (model.getTaggedlist().get(j).getFromGroupID() != null) {  // check for "from groupid"... if null means tagger is individual user
                                                    if (filterGroupList.contains(model.getTaggedlist().get(j).getFromGroupID())) {  // check for filter settings having tagger's group id
                                                        if (audience_all_groups.equals("N")) { // check for tagger selected all groups audience
                                                            if (audienceGroupList.contains(model.getTaggedlist().get(j).getFromGroupID())) {
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
                                                                rowData.setGetIconPath(model.getTaggedlist().get(j).getIconPath());
                                                                rowData.setFname(model.getTaggedlist().get(j).getFname());
                                                                rowData.setLname(model.getTaggedlist().get(j).getLname());
                                                                rowData.setPrivacy(model.getTaggedlist().get(j).getPrivacy());
                                                                rowData.setNeedName(model.getTaggedlist().get(j).getNeedName());
                                                                rowData.setTotalTaggedCreditPoints(model.getTaggedlist().get(j).getTotalTaggedCreditPoints());
                                                                rowData.setTotalFulfilledCreditPoints(model.getTaggedlist().get(j).getTotalFulfilledCreditPoints());
                                                                rowData.setUserID(model.getTaggedlist().get(j).getUserID());
                                                                rowData.setTaggedID(model.getTaggedlist().get(j).getTaggedID());
                                                                rowData.setCatType(model.getTaggedlist().get(j).getCatType());
                                                                rowData.setGeopoint(model.getTaggedlist().get(j).getGeopoint());
                                                                rowData.setTaggedPhotoPath(model.getTaggedlist().get(j).getTaggedPhotoPath());
                                                                rowData.setDescription(model.getTaggedlist().get(j).getDescription());
                                                                rowData.setViews(model.getTaggedlist().get(j).getViews());
                                                                rowData.setEndorse(model.getTaggedlist().get(j).getEndorse());
                                                                rowData.setPermanent(model.getTaggedlist().get(j).getPermanent());
                                                                rowData.setAllGroups(model.getTaggedlist().get(j).getAll_groups());
                                                                rowData.setUser_group_ids(model.getTaggedlist().get(j).getUserGrpIds());
                                                                item.add(rowData);

                                                                String str_lati_logi = model.getTaggedlist().get(j).getGeopoint();
                                                                String[] words_new = str_lati_logi.split(",");
                                                                if (words_new.length > 1) {
                                                                    final String marker_id = model.getTaggedlist().get(j).getTaggedID();
                                                                    final String catType = model.getTaggedlist().get(j).getCatType();
                                                                    final String permanent = model.getTaggedlist().get(j).getPermanent();
                                                                    final String markerTitle, icon_path_str_new;
                                                                    if (permanent.equals("Y")) {
                                                                        markerTitle = "Permanent";
                                                                        icon_path_str_new = WebServices.MANI_URL + WebServices.SUB_URL + permanent_marker_path;
                                                                    } else {
                                                                        if (catType.equals("C")) {
                                                                            markerTitle = model.getTaggedlist().get(j).getTaggedTitle();
                                                                            icon_path_str_new = WebServices.CUSTOM_CATEGORY_IMAGE_URL + model.getTaggedlist().get(j).getIconPath();
                                                                        } else {
                                                                            markerTitle = model.getTaggedlist().get(j).getTaggedTitle();
                                                                            icon_path_str_new = WebServices.MANI_URL + WebServices.SUB_URL + model.getTaggedlist().get(j).getIconPath();
                                                                        }
                                                                    }

                                                                    Log.d("imagepath2", icon_path_str_new);
                                                                    Double maplat = Double.parseDouble(words_new[0]);
                                                                    Double maplong = Double.parseDouble(words_new[1]);
                                                                    final LatLng point = new LatLng(maplat, maplong);//
                                                                    Glide.with(getApplicationContext())
                                                                            .asBitmap()
                                                                            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                                                                            .load(icon_path_str_new)
                                                                            .into(new SimpleTarget<Bitmap>() {
                                                                                @Override
                                                                                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                                                                    Marker marker = mGoogleMap.addMarker(new MarkerOptions().position(point)
                                                                                            .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(customView, resource))).title(markerTitle).anchor(0.5f, 0.907f));
                                                                                    marker.setTag(markerTitle);
                                                                                    marker.setSnippet(marker_id);
//                                                            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 17f));
                                                                                }
                                                                            });
                                                                }
                                                            }
                                                        } else {
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
                                                            rowData.setGetIconPath(model.getTaggedlist().get(j).getIconPath());
                                                            rowData.setFname(model.getTaggedlist().get(j).getFname());
                                                            rowData.setLname(model.getTaggedlist().get(j).getLname());
                                                            rowData.setPrivacy(model.getTaggedlist().get(j).getPrivacy());
                                                            rowData.setNeedName(model.getTaggedlist().get(j).getNeedName());
                                                            rowData.setTotalTaggedCreditPoints(model.getTaggedlist().get(j).getTotalTaggedCreditPoints());
                                                            rowData.setTotalFulfilledCreditPoints(model.getTaggedlist().get(j).getTotalFulfilledCreditPoints());
                                                            rowData.setUserID(model.getTaggedlist().get(j).getUserID());
                                                            rowData.setTaggedID(model.getTaggedlist().get(j).getTaggedID());
                                                            rowData.setCatType(model.getTaggedlist().get(j).getCatType());
                                                            rowData.setGeopoint(model.getTaggedlist().get(j).getGeopoint());
                                                            rowData.setTaggedPhotoPath(model.getTaggedlist().get(j).getTaggedPhotoPath());
                                                            rowData.setDescription(model.getTaggedlist().get(j).getDescription());
                                                            rowData.setViews(model.getTaggedlist().get(j).getViews());
                                                            rowData.setEndorse(model.getTaggedlist().get(j).getEndorse());
                                                            rowData.setPermanent(model.getTaggedlist().get(j).getPermanent());
                                                            rowData.setAllGroups(model.getTaggedlist().get(j).getAll_groups());
                                                            rowData.setUser_group_ids(model.getTaggedlist().get(j).getUserGrpIds());
                                                            item.add(rowData);

                                                            String str_lati_logi = model.getTaggedlist().get(j).getGeopoint();
                                                            String[] words_new = str_lati_logi.split(",");
                                                            if (words_new.length > 1) {
                                                                final String marker_id = model.getTaggedlist().get(j).getTaggedID();
                                                                final String catType = model.getTaggedlist().get(j).getCatType();
                                                                final String permanent = model.getTaggedlist().get(j).getPermanent();
                                                                final String markerTitle, icon_path_str_new;
                                                                if (permanent.equals("Y")) {
                                                                    markerTitle = "Permanent";
                                                                    icon_path_str_new = WebServices.MANI_URL + WebServices.SUB_URL + permanent_marker_path;
                                                                } else {
                                                                    if (catType.equals("C")) {
                                                                        markerTitle = model.getTaggedlist().get(j).getTaggedTitle();
                                                                        icon_path_str_new = WebServices.CUSTOM_CATEGORY_IMAGE_URL + model.getTaggedlist().get(j).getIconPath();
                                                                    } else {
                                                                        markerTitle = model.getTaggedlist().get(j).getTaggedTitle();
                                                                        icon_path_str_new = WebServices.MANI_URL + WebServices.SUB_URL + model.getTaggedlist().get(j).getIconPath();
                                                                    }
                                                                }

                                                                Log.d("imagepath2", icon_path_str_new);
                                                                Double maplat = Double.parseDouble(words_new[0]);
                                                                Double maplong = Double.parseDouble(words_new[1]);
                                                                final LatLng point = new LatLng(maplat, maplong);//
                                                                Glide.with(getApplicationContext())
                                                                        .asBitmap()
                                                                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                                                                        .load(icon_path_str_new)
                                                                        .into(new SimpleTarget<Bitmap>() {
                                                                            @Override
                                                                            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                                                                Marker marker = mGoogleMap.addMarker(new MarkerOptions().position(point)
                                                                                        .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(customView, resource))).title(markerTitle).anchor(0.5f, 0.907f));
                                                                                marker.setTag(markerTitle);
                                                                                marker.setSnippet(marker_id);
//                                                            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 17f));
                                                                            }
                                                                        });
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                mDialog.dismiss();
                            }
                        }
                        mDialog.dismiss();
                    }
                } catch (Exception e) {
//                    StringWriter writer = new StringWriter();
//                    e.printStackTrace(new PrintWriter(writer));
//                    Bugreport bg = new Bugreport();
//                    bg.sendbug(writer.toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                ToastPopUp.show(myContext, getString(R.string.server_response_error));
                mDialog.dismiss();
            }
        });
    }

    public void get_SOSlist_data(String user_id) {
        item = new ArrayList<>();
        final RowData rowData = new RowData();
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        SOSlistInterface service = retrofit.create(SOSlistInterface.class);

        Call<Modelsoslist> call = service.fetchData(user_id);
        Log.d("input_parameters_tags", user_id);
        call.enqueue(new Callback<Modelsoslist>() {
            @Override
            public void onResponse(Response<Modelsoslist> response, Retrofit retrofit) {
                Modelsoslist model = new Modelsoslist();
                try {
                    model = response.body();
                    int isblock = 0;
                    try {
                        isblock = model.getIsBlocked();
                    } catch (Exception e) {
                        isblock = 0;
                    }
                    if (isblock == 1) {
                        FacebookSdk.sdkInitialize(getActivity());
                        Toast.makeText(getContext(), "You have been blocked", Toast.LENGTH_SHORT).show();
                        sessionManager.createUserCredentialSession(null, null, null);
                        LoginManager.getInstance().logOut();

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
                        soslists = response.body().getSoslist();
                        double current_latitude = new GPSTracker(myContext).getLatitude();
                        double current_longitude = new GPSTracker(myContext).getLongitude();
                        Location myLocation = new Location("My Location");
                        myLocation.setLatitude(current_latitude);
                        myLocation.setLongitude(current_longitude);
                        if (model != null) {
                            if (model.getSoslist().size() > 0) {
                                Log.d("soslist_Size", String.valueOf(model.getSoslist().size()));
                                for (int j = 0; j < model.getSoslist().size(); j++) {
                                    String str_geo_point = model.getSoslist().get(j).getGeopoints();
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
                                            rowData.setTitle(model.getSoslist().get(j).getId());
                                            rowData.setAddress(model.getSoslist().get(j).getGeopoints());
                                            item.add(rowData);

                                            String str_lati_logi = model.getSoslist().get(j).getGeopoints();
                                            String[] words_new = str_lati_logi.split(",");
                                            if (words_new.length > 1) {
                                                final String marker_id = model.getSoslist().get(j).getId();
                                                String icon_path_str_new = WebServices.MANI_URL + WebServices.SUB_URL + response.body().getMarker_path();
                                                Log.d("sosiconpath", icon_path_str_new);
                                                Double maplat = Double.parseDouble(words_new[0]);
                                                Double maplong = Double.parseDouble(words_new[1]);
                                                final LatLng point = new LatLng(maplat, maplong);//
                                                Glide.with(getApplicationContext())
                                                        .asBitmap()
                                                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                                                        .load(icon_path_str_new)
                                                        .into(new SimpleTarget<Bitmap>() {
                                                            @Override
                                                            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                                                Marker marker = mGoogleMap.addMarker(new MarkerOptions().position(point)
                                                                        .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(customView, resource))).title(marker_id).anchor(0.5f, 0.907f));
                                                                marker.setTag("SOS");
                                                                marker.setSnippet(marker_id);
//                                                        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 17f));
                                                            }
                                                        });
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
//                    StringWriter writer = new StringWriter();
//                    e.printStackTrace(new PrintWriter(writer));
//                    Bugreport bg = new Bugreport();
//                    bg.sendbug(writer.toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                ToastPopUp.show(myContext, getString(R.string.server_response_error));
            }
        });
    }

    public void get_Resourcelist_data(String user_id) {
        item = new ArrayList<>();
        final RowData rowData = new RowData();
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        ResourcelistInterface service = retrofit.create(ResourcelistInterface.class);

        Call<Modelresourcelist> call = service.fetchData(user_id);
        Log.d("input_parameters_tags", user_id);
        call.enqueue(new Callback<Modelresourcelist>() {
            @Override
            public void onResponse(Response<Modelresourcelist> response, Retrofit retrofit) {
                Modelresourcelist model = new Modelresourcelist();
                try {
                    model = response.body();
                    int isblock = 0;
                    try {
                        isblock = model.getIsBlocked();
                    } catch (Exception e) {
                        isblock = 0;
                    }
                    if (isblock == 1) {
                        FacebookSdk.sdkInitialize(getActivity());
                        Toast.makeText(getContext(), getResources().getString(R.string.block_toast), Toast.LENGTH_SHORT).show();
                        sessionManager.createUserCredentialSession(null, null, null);
                        LoginManager.getInstance().logOut();

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
                        resourcelists = response.body().getReslist();
                        double current_latitude = new GPSTracker(myContext).getLatitude();
                        double current_longitude = new GPSTracker(myContext).getLongitude();
                        Location myLocation = new Location("My Location");
                        myLocation.setLatitude(current_latitude);
                        myLocation.setLongitude(current_longitude);
                        if (model != null) {
                            if (model.getReslist().size() > 0) {
                                Log.d("reslist_Size", String.valueOf(model.getReslist().size()));
                                for (int j = 0; j < model.getReslist().size(); j++) {
                                    String str_geo_point = model.getReslist().get(j).getGeopoints();
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
                                            rowData.setId(model.getReslist().get(j).getId());
                                            rowData.setTitle(model.getReslist().get(j).getResname());
                                            rowData.setAddress(model.getReslist().get(j).getGeopoints());
                                            item.add(rowData);

                                            String str_lati_logi = model.getReslist().get(j).getGeopoints();
                                            String[] words_new = str_lati_logi.split(",");
                                            if (words_new.length > 1) {
                                                final String marker_id = model.getReslist().get(j).getId();
                                                String icon_path_str_new = WebServices.MANI_URL + WebServices.SUB_URL + response.body().getMarker_path();
                                                Log.d("resiconpath", icon_path_str_new);
                                                Double maplat = Double.parseDouble(words_new[0]);
                                                Double maplong = Double.parseDouble(words_new[1]);
                                                final LatLng point = new LatLng(maplat, maplong);//
                                                Glide.with(getApplicationContext())
                                                        .asBitmap()
                                                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                                                        .load(icon_path_str_new)
                                                        .into(new SimpleTarget<Bitmap>() {
                                                            @Override
                                                            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                                                Marker marker = mGoogleMap.addMarker(new MarkerOptions().position(point)
                                                                        .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(customView, resource))).title(marker_id).anchor(0.5f, 0.907f));
                                                                marker.setTag("Resource");
                                                                marker.setSnippet(marker_id);
//                                                        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 17f));
                                                            }
                                                        });
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
//                    StringWriter writer = new StringWriter();
//                    e.printStackTrace(new PrintWriter(writer));
//                    Bugreport bg = new Bugreport();
//                    bg.sendbug(writer.toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                ToastPopUp.show(myContext, getString(R.string.server_response_error));
            }
        });
    }

    public void updateLanguage(String language) {
        locale = new Locale(language);
        Locale.setDefault(locale);
        config = new Configuration();
        config.locale = locale;
        getActivity().getBaseContext().getResources().updateConfiguration(config, getActivity().getBaseContext().getResources().getDisplayMetrics());
//        loginheading.setText(getResources().getString(R.string.login_string));
    }

    public void getPermanentDeedList(final String tagid, final String geopoints, final String str_address) {
        taggeddeeds = new ArrayList<>();
        mDialog.setConfiguration(new ArcConfiguration(getContext()));
        mDialog.show();
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        PermanentTaggedDeedsInterface service = retrofit.create(PermanentTaggedDeedsInterface.class);
        Log.d("input_params", strUserId + "," + geopoints);
        Call<List<TaggedDeedsPojo>> call = service.sendData(strUserId, geopoints);
        call.enqueue(new Callback<List<TaggedDeedsPojo>>() {
            @Override
            public void onResponse(Response<List<TaggedDeedsPojo>> response, Retrofit retrofit) {
                List<TaggedDeedsPojo> taggedDeedsPojoList = response.body();
                Log.d("response_checkdeedlist", "" + response.body());
                int isblock = 0;
                try {
                    isblock = taggedDeedsPojoList.get(0).getIsBlocked();
                } catch (Exception e) {
                    isblock = 0;
                }
                if (isblock == 1) {
                    mDialog.dismiss();
                    FacebookSdk.sdkInitialize(getActivity());
                    Toast.makeText(getContext(), getResources().getString(R.string.block_toast), Toast.LENGTH_SHORT).show();
                    sessionManager.createUserCredentialSession(null, null, null);
                    LoginManager.getInstance().logOut();
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                            new ResultCallback<Status>() {
                                @Override
                                public void onResult(Status status) {
                                    //updateUI(false);
                                }
                            });

                    sessionManager.set_notification_status("ON");
                    Intent loginintent = new Intent(getActivity(), LoginActivity.class);
                    loginintent.putExtra("message", "Charity");
                    startActivity(loginintent);
                } else {
                    mDialog.dismiss();
                    taggeddeeds.clear();
                    try {
                        for (int i = 0; i < taggedDeedsPojoList.size(); i++) {
                            TaggedDeedsPojo taggedDeedsPojo = new TaggedDeedsPojo();
                            taggedDeedsPojo.setTagid(taggedDeedsPojoList.get(i).getTagid());
                            taggedDeedsPojo.setNeedname(taggedDeedsPojoList.get(i).getNeedname());
                            taggedDeedsPojo.setSubtypes(taggedDeedsPojoList.get(i).getSubtypes());
                            taggedDeedsPojo.setIconpath(taggedDeedsPojoList.get(i).getIconpath());
                            taggeddeeds.add(taggedDeedsPojo);
                        }
                    } catch (Exception e) {
//                        StringWriter writer = new StringWriter();
//                        e.printStackTrace(new PrintWriter(writer));
//                        Bugreport bg = new Bugreport();
//                        bg.sendbug(writer.toString());
                    }

                    if (taggeddeeds.size() > 0) {
                        final Dialog dialog = new Dialog(getContext());
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.permanent_map_dialog);
                        TextView tv_address = (TextView) dialog.findViewById(R.id.permanent_address);
                        final ListView permanent_deedslist = (ListView) dialog.findViewById(R.id.permanent_deeds_list);
                        Button btnNewTag = (Button) dialog.findViewById(R.id.permanent_btn_tag_deed);
                        Button btnRemoveLocation = (Button) dialog.findViewById(R.id.permanent_btn_remove_location);
                        permanent_deedslist.setVisibility(View.VISIBLE);
                        btnNewTag.setVisibility(View.VISIBLE);
                        btnRemoveLocation.setVisibility(View.GONE);
                        Log.d("taggeddeeds_size", "" + taggeddeeds.size());
                        if (str_address.length() > 40) {
                            tv_address.setText(str_address.substring(0, 40) + "...");
                        } else {
                            tv_address.setText(str_address);
                        }
                        permanent_deedslist.setAdapter(new TaggedDeedsAdapter(taggeddeeds, getContext()));
                        permanent_deedslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                if (!(Validation.isNetworkAvailable(getContext()))) {
                                    Toast.makeText(getContext(), getResources().getString(R.string.network_validation), Toast.LENGTH_SHORT).show();
                                } else {
                                    FragmentManager newfrag;
                                    newfrag = getActivity().getSupportFragmentManager();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("str_tagid", taggeddeeds.get(position).getTagid());
                                    Log.d("permanent_tagid", taggeddeeds.get(position).getTagid());
                                    bundle.putString("tab", "tab1");
                                    dialog.dismiss();
                                    NeedDetailsFrag fragInfo = new NeedDetailsFrag();
                                    fragInfo.setArguments(bundle);
                                    newfrag.beginTransaction().replace(R.id.content_frame, fragInfo).commit();
                                }
                            }
                        });
                        btnNewTag.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                FragmentManager newfrag;
                                newfrag = getActivity().getSupportFragmentManager();
                                Bundle bundle = new Bundle();
                                bundle.putString("page", "map_permanent_dialog");
                                bundle.putString("tab", "tab1");
                                bundle.putString("permanent_address", str_address);
                                bundle.putString("permanent_geopoints", geopoints);
                                dialog.dismiss();
                                TagaNeed tagaNeed = new TagaNeed();
                                tagaNeed.setArguments(bundle);
                                newfrag.beginTransaction().replace(R.id.content_frame, tagaNeed).commit();
                            }
                        });
                        dialog.show();
                    } else {
                        final Dialog dialog = new Dialog(getContext());
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.permanent_map_dialog);
                        TextView tv_address = (TextView) dialog.findViewById(R.id.permanent_address);
                        final ListView permanent_deedslist = (ListView) dialog.findViewById(R.id.permanent_deeds_list);
                        Button btnNewTag = (Button) dialog.findViewById(R.id.permanent_btn_tag_deed);
                        Button btnRemoveLocation = (Button) dialog.findViewById(R.id.permanent_btn_remove_location);
                        btnRemoveLocation.setVisibility(View.GONE);
                        Log.d("taggeddeeds_size", "" + taggeddeeds.size());
                        if (str_address.length() > 40) {
                            tv_address.setText(str_address.substring(0, 40) + "...");
                        } else {
                            tv_address.setText(str_address);
                        }
                        permanent_deedslist.setVisibility(View.GONE);
                        btnNewTag.setVisibility(View.VISIBLE);
                        btnRemoveLocation.setVisibility(View.VISIBLE);
                        btnNewTag.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                FragmentManager newfrag;
                                newfrag = getActivity().getSupportFragmentManager();
                                Bundle bundle = new Bundle();
                                bundle.putString("page", "map_permanent_dialog");
                                bundle.putString("tab", "tab1");
                                bundle.putString("permanent_address", str_address);
                                bundle.putString("permanent_geopoints", geopoints);
                                dialog.dismiss();
                                TagaNeed tagaNeed = new TagaNeed();
                                tagaNeed.setArguments(bundle);
                                newfrag.beginTransaction().replace(R.id.content_frame, tagaNeed).commit();
                            }
                        });
                        btnRemoveLocation.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                if (!(Validation.isOnline(getActivity()))) {
                                    ToastPopUp.show(getActivity(), getString(R.string.network_validation));
                                } else {
                                    removeLocation(tagid);
                                }
                            }
                        });
                        dialog.show();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                mDialog.dismiss();
                Log.d("response_checkdeedlist", t.getMessage());
                ToastPopUp.show(myContext, getString(R.string.server_response_error));
            }
        });
    }

    private void removeLocation(String tagid) {
        mDialog.setConfiguration(new ArcConfiguration(getContext()));
        mDialog.show();
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        RemoveLocationInterface service = retrofit.create(RemoveLocationInterface.class);
        Call<GroupResponseStatus> call = service.sendData(strUserId, tagid);
        Log.d("remove_location_params", strUserId + "," + tagid);
        call.enqueue(new Callback<GroupResponseStatus>() {
            @Override
            public void onResponse(Response<GroupResponseStatus> response, Retrofit retrofit) {
                mDialog.dismiss();
                Log.d("response_removelocation", "" + response.body());
                try {
                    GroupResponseStatus groupResponseStatus = response.body();
                    int isblock = 0;
                    try {
                        isblock = groupResponseStatus.getIsBlocked();
                    } catch (Exception e) {
                        isblock = 0;
                    }
                    if (isblock == 1) {
                        FacebookSdk.sdkInitialize(getActivity());
                        Toast.makeText(getContext(), getResources().getString(R.string.block_toast), Toast.LENGTH_SHORT).show();
                        sessionManager.createUserCredentialSession(null, null, null);
                        LoginManager.getInstance().logOut();
                        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                                new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(Status status) {
                                        //updateUI(false);
                                    }
                                });

                        sessionManager.set_notification_status("ON");
                        Intent loginintent = new Intent(getActivity(), LoginActivity.class);
                        loginintent.putExtra("message", "Charity");
                        startActivity(loginintent);
                    } else {
                        if (groupResponseStatus.getStatus() == 1) {
                            Toast.makeText(getContext(), "Location removed successfully", Toast.LENGTH_SHORT).show();
                        } else if (groupResponseStatus.getStatus() == 0) {
                            Toast.makeText(getContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    mDialog.dismiss();
                    Log.d("response_removelocation", "" + e.getMessage());
//                    StringWriter writer = new StringWriter();
//                    e.printStackTrace(new PrintWriter(writer));
//                    Bugreport bg = new Bugreport();
//                    bg.sendbug(writer.toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                mDialog.dismiss();
                Log.d("response_removelocation", "" + t.getMessage());
                ToastPopUp.show(myContext, getString(R.string.server_response_error));
            }
        });
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        mGoogleApiClient.connect();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed()) { // fragment is visible and have created
            Log.d("UserVisible", "visible");
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            Log.d("map", "ON connected");

        } else
            try {
                LocationServices.FusedLocationApi.removeLocationUpdates(
                        mGoogleApiClient, this);

            } catch (Exception e) {
                e.printStackTrace();
            }
        try {
            LocationRequest mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(10000);
            mLocationRequest.setFastestInterval(5000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("map", "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            if (location != null)
                LocationServices.FusedLocationApi.removeLocationUpdates(
                        mGoogleApiClient, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}