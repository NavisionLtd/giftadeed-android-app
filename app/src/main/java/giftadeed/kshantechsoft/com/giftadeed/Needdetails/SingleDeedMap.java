package giftadeed.kshantechsoft.com.giftadeed.Needdetails;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;

//import net.alexandroid.gps.GpsStatusDetector;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import giftadeed.kshantechsoft.com.giftadeed.R;
//import billionyogis.kshantechsoft.com.billionyogis.Utils.GPSTracker;
import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.GPSTracker;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsActivity;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.list_Model.JobRenderer;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.list_Model.MyItem;
import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;

import static android.view.View.GONE;
import static com.facebook.FacebookSdk.getApplicationContext;


public class SingleDeedMap extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMarkerDragListener
        , GoogleMap.OnMapLongClickListener {
    private GoogleMap mMap;
    View rootview;
    static android.support.v4.app.FragmentManager fragmgr;
    // private GpsStatusDetector mGpsStatusDetector;
    double LATITUDE, LONGITUDE;
    private double longitude;
    private double latitude;
    GPSTracker gpsTracker;
    String strgeopoints, str_address, strFragmentName, str_distance, str_tagid, str_taggedPhotoPath, str_characterPath, strNeed_name, tab, str_title, str_date;
    LatLng latLngf;
    Bitmap bmp;
    ImageView mMarkerImageView;
    View customView;
//    private ClusterManager<MyItem> mClusterManager;

    public static SingleDeedMap newInstance(int sectionNumber) {
        SingleDeedMap fragment = new SingleDeedMap();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_single_deed_map, container, false);
        fragmgr = getFragmentManager();
        ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.singlemap)).getMapAsync(this);
        TaggedneedsActivity.updateTitle(getResources().getString(R.string.title_activity_maps));
        TaggedneedsActivity.imgHamburger.setVisibility(GONE);
        TaggedneedsActivity.back.setVisibility(View.VISIBLE);
        TaggedneedsActivity.imgShare.setVisibility(View.GONE);
        TaggedneedsActivity.toggle.setDrawerIndicatorEnabled(false);
        TaggedneedsActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        // mGpsStatusDetector = new GpsStatusDetector(this);
        //mGpsStatusDetector.checkLocationSettingStatus();
        gpsTracker = new GPSTracker(getContext());
        //  mGpsStatusDetector.checkGpsStatus();
        Bundle bundle = new Bundle();
        strFragmentName = this.getArguments().getString("fragmentName");

        str_address = this.getArguments().getString("str_address");
        str_tagid = this.getArguments().getString("str_tagid");
        strgeopoints = this.getArguments().getString("str_geopoint");
        str_taggedPhotoPath = this.getArguments().getString("str_taggedPhotoPath");
        // str_description = this.getArguments().getString("str_description");
        str_characterPath = this.getArguments().getString("str_characterPath");
        //  str_lname = this.getArguments().getString("str_lname");
        //  str_privacy = this.getArguments().getString("str_privacy");
        //  str_userID = this.getArguments().getString("str_userID");
        strNeed_name = this.getArguments().getString("str_needName");
        tab = this.getArguments().getString("tab");
        //str_totalTaggedCreditPoints = this.getArguments().getString("str_totalTaggedCreditPoints");
        //str_endorse = this.getArguments().getString("endorse");
        str_title = this.getArguments().getString("str_title");
        str_date = this.getArguments().getString("str_date");
        str_distance = this.getArguments().getString("str_distance");


        String[] words = strgeopoints.split(",");
        // Location tagLocation2 = new Location("tag Location");
        double lat = Double.parseDouble(words[0]);
        double longi = Double.parseDouble(words[1]);
        latLngf = new LatLng(lat, longi);


        TaggedneedsActivity.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();


                bundle.putString("str_address", str_address);
                bundle.putString("str_tagid", str_tagid);
                bundle.putString("str_geopoint", strgeopoints);
                bundle.putString("str_taggedPhotoPath", str_taggedPhotoPath);
                //  bundle.putString("str_description", str_description);
                bundle.putString("str_characterPath", str_characterPath);
                // bundle.putString("str_fname", str_fname);
                //  bundle.putString("str_lname", str_lname);
                // bundle.putString("str_privacy", str_privacy);
                // bundle.putString("str_userID", str_userID);
                bundle.putString("str_needName", strNeed_name);
                //  bundle.putString("str_totalTaggedCreditPoints", str_totalTaggedCreditPoints);
                //  bundle.putString("endorse", str_endorse);
                bundle.putString("tab", "tab2");
                //  bundle.putString("str_title", str_title);
                bundle.putString("str_date", str_date);
                bundle.putString("str_distance", str_distance);
                NeedDetailsFrag fragInfo = new NeedDetailsFrag();
                fragInfo.setArguments(bundle);
                fragmgr.beginTransaction().replace(R.id.content_frame, fragInfo).commit();

                //fragmgr.beginTransaction().replace(R.id.content_frame, MapviewFragment.newInstance(i)).addToBackStack(null).commit();
            }
        });

        customView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.view_custom_marker, null);
        mMarkerImageView = (ImageView) customView.findViewById(R.id.profile_image);
        return rootview;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        double latitude = gpsTracker.getLatitude();
        double longitude = gpsTracker.getLongitude();
        String msg = latitude + "," + longitude;
        LatLng latLng = new LatLng(latitude, longitude);
        Log.d("LatLong", msg);
        mMap.addMarker(new MarkerOptions().position(latLng));
        String str_lati_logi = strgeopoints;
        String[] words_new = str_lati_logi.split(",");

        String icon_path_str_new = WebServices.MAIN_SUB_URL + str_characterPath;
        Log.d("single_deed_icon", icon_path_str_new);
//        MyItem offsetItem = new MyItem(Double.parseDouble(words_new[0]), Double.parseDouble(words_new[1]), str_title, str_tagid, icon_path_str_new);
//        mClusterManager.addItem(offsetItem);
//        mClusterManager.setRenderer(new JobRenderer(getContext(), mMap, mClusterManager));
        Double maplat = Double.parseDouble(words_new[0]);
        Double maplong = Double.parseDouble(words_new[1]);
        final LatLng point = new LatLng(maplat, maplong);
        Glide.with(getApplicationContext())
                .asBitmap()
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                .load(icon_path_str_new)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        mMap.addMarker(new MarkerOptions().position(point)
                                .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(customView, resource))).anchor(0.5f, 0.907f));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 17f));
                    }
                });

//        mMap.setOnCameraIdleListener(mClusterManager);
//        mMap.setOnMarkerClickListener(mClusterManager);
//        mMap.setOnInfoWindowClickListener(mClusterManager);


        // mMap.addMarker(new MarkerOptions().position(latLngf).title(strNeed_name).icon(BitmapDescriptorFactory.fromBitmap(bmp)));


//        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLngf));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(18));

        // Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
    }

    public Bitmap mergeBitmap(Bitmap bitmap) {
        Bitmap bmp1 = BitmapFactory.decodeResource(getResources(), R.drawable.pin);
        Bitmap bmp2 = Bitmap.createScaledBitmap(bitmap, 70, 70, true);
        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
//        int centreX = (bmp1.getWidth() - bmp2.getWidth()) / 2;
//        int centreY = (bmp1.getHeight() - bmp2.getHeight()) / 2;
        canvas.drawBitmap(bmp1, new Matrix(), null);
        canvas.drawBitmap(bmp2, 5, 5, null);
        return bmOverlay;
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


    private void moveMap(double latitude, double longitude) {
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(latLng).draggable(true));

    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        latitude = marker.getPosition().latitude;
        longitude = marker.getPosition().longitude;
        moveMap(latitude, longitude);
    }


    @Override
    public void onResume() {
        super.onResume();
        //getView().setFocusableInTouchMode(true);
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
//------------------back press of mobile------------------------------------------------------------
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    Bundle bundle = new Bundle();


                    bundle.putString("str_address", str_address);
                    bundle.putString("str_tagid", str_tagid);
                    bundle.putString("str_geopoint", strgeopoints);
                    bundle.putString("str_taggedPhotoPath", str_taggedPhotoPath);
                    //  bundle.putString("str_description", str_description);
                    bundle.putString("str_characterPath", str_characterPath);
                    // bundle.putString("str_fname", str_fname);
                    //  bundle.putString("str_lname", str_lname);
                    // bundle.putString("str_privacy", str_privacy);
                    // bundle.putString("str_userID", str_userID);
                    bundle.putString("str_needName", strNeed_name);
                    //  bundle.putString("str_totalTaggedCreditPoints", str_totalTaggedCreditPoints);
                    //  bundle.putString("endorse", str_endorse);
                    bundle.putString("tab", "tab2");
                    //  bundle.putString("str_title", str_title);
                    bundle.putString("str_date", str_date);
                    bundle.putString("str_distance", str_distance);
                    NeedDetailsFrag fragInfo = new NeedDetailsFrag();
                    fragInfo.setArguments(bundle);
                    fragmgr.beginTransaction().replace(R.id.content_frame, fragInfo).commit();

                    return true;
                }
                return false;
            }
        });


    }


}
