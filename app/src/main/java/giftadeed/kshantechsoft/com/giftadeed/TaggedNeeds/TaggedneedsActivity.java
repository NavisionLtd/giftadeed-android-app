package giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds;

import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.squareup.okhttp.OkHttpClient;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import giftadeed.kshantechsoft.com.giftadeed.Bug.Bugreport;
import giftadeed.kshantechsoft.com.giftadeed.Filter.FilterFrag;
import giftadeed.kshantechsoft.com.giftadeed.GridMenu.MenuGrid;
import giftadeed.kshantechsoft.com.giftadeed.Group.GroupPOJO;
import giftadeed.kshantechsoft.com.giftadeed.Group.GroupsListFragment;
import giftadeed.kshantechsoft.com.giftadeed.Login.LoginActivity;
import giftadeed.kshantechsoft.com.giftadeed.MyFullFillTag.MyFullFillTags;
import giftadeed.kshantechsoft.com.giftadeed.MyProfile.MyProfilefrag;
import giftadeed.kshantechsoft.com.giftadeed.MyProfile.Profile;
import giftadeed.kshantechsoft.com.giftadeed.Mytags.MyTagsList;
import giftadeed.kshantechsoft.com.giftadeed.Notifications.Notificationfrag;
import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.Resources.CreateResourceFragment;
import giftadeed.kshantechsoft.com.giftadeed.Resources.OwnedGroupsAdapter;
import giftadeed.kshantechsoft.com.giftadeed.Resources.OwnedGroupsInterface;
import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.GPSTracker;
import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.TagaNeed;
import giftadeed.kshantechsoft.com.giftadeed.Utils.DBGAD;
import giftadeed.kshantechsoft.com.giftadeed.Utils.GetingAddress;
import giftadeed.kshantechsoft.com.giftadeed.Utils.SessionManager;
import giftadeed.kshantechsoft.com.giftadeed.Utils.ToastPopUp;
import giftadeed.kshantechsoft.com.giftadeed.Utils.Validation;
import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import giftadeed.kshantechsoft.com.giftadeed.settings.SettingsFragment;
import giftadeed.kshantechsoft.com.giftadeed.taggerfullfiller.TaggerList;
import giftadeed.kshantechsoft.com.giftadeed.taggerfullfiller.TopTenFullfillerList;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class TaggedneedsActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
        NavigationView.OnNavigationItemSelectedListener, DrawerLayout.DrawerListener {
    String selectedLangugae;
    Locale locale;
    Configuration config;
    static Fragment frag;
    static android.support.v4.app.FragmentManager fragmgr;
    static FragmentTransaction fragTrans;
    public static TextView headingtext, editprofile, saveprofile;
    ImageView profilePic;
    public static ImageView imgappbarcamera, imgfilter, imgappbarsetting, back;
    NavigationView navigationView;
    public static DrawerLayout drawer;
    TextView profiletxtview, txtProfileName, txtOrgName;
    Toolbar toolbar;
    public static ActionBarDrawerToggle toggle;
    SessionManager sharedPreferences;
    String message, strUserId, strUserName, strOrgName;
    public String str_Geopint, adress_show, selectedAccount;
    String latitude_source, longitude_source;
    Context myContext;
    public static ImageView imgHamburger;
    String drawervalue;
    HashMap<String, String> draerwe_status;
    public static Fragment fragname;
    //------------------------dialog text
    TextView dialogtext;
    private AlertDialog alertDialogForgot;
    Button btnLogin, dialogconfirm, dialogcancel;
    public GoogleApiClient mGoogleApiClient;
    SimpleArcDialog mDialog;
    private List<Profile> profileList;
    public static final String DATABASE_PROFILE_PIC_UPLOADS = "users";
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taggedneeds);
        myContext = TaggedneedsActivity.this;
        init();

        // add NavigationItemSelectedListener to check the navigation clicks
        navigationView.setNavigationItemSelectedListener(this);
        sharedPreferences = new SessionManager(getApplicationContext());
        selectedLangugae = sharedPreferences.getLanguage();
        setSupportActionBar(toolbar);
        GPSTracker gps = new GPSTracker(TaggedneedsActivity.this);
        toolbar.setTitle("");
        headingtext.setText("Tagged Needs");
        imgHamburger = (ImageView) toolbar.findViewById(R.id.imgHamburger);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        mDialog = new SimpleArcDialog(TaggedneedsActivity.this);
        updateLanguage(selectedLangugae);
        HashMap<String, String> user = sharedPreferences.getUserDetails();
        strUserId = user.get(sharedPreferences.USER_ID);
        strUserName = user.get(sharedPreferences.USER_NAME);
        sharedPreferences.set_drawer_status("close");
        if (!gps.isGPSEnabled) {
            gps.showSettingsAlert();
        }
        drawer.setDrawerListener(this);
        Bundle bundle = getIntent().getExtras();
        message = bundle.getString("message");
        fragmgr = getSupportFragmentManager();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(TaggedneedsActivity.this)
                .enableAutoManage(TaggedneedsActivity.this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        Menu menu = navigationView.getMenu();
        MenuItem nav_home = menu.findItem(R.id.nav_home);
        nav_home.setTitle(getResources().getString(R.string.drawer_home));
        MenuItem nav_groups = menu.findItem(R.id.nav_groups);
        nav_groups.setTitle(getResources().getString(R.string.drawer_groups));
        MenuItem nav_taganeed = menu.findItem(R.id.nav_taganeed);
        nav_taganeed.setTitle(getResources().getString(R.string.drawer_tag_deed));
        MenuItem nav_resources = menu.findItem(R.id.nav_resources);
        nav_resources.setTitle(getResources().getString(R.string.drawer_resources));
        MenuItem aboutapp = menu.findItem(R.id.aboutapp);
        aboutapp.setTitle(getResources().getString(R.string.drawer_about_app));
        MenuItem notifications = menu.findItem(R.id.notifications);
        notifications.setTitle(getResources().getString(R.string.drawer_notification));
        MenuItem settings = menu.findItem(R.id.settings);
        settings.setTitle(getResources().getString(R.string.drawer_settings));
        MenuItem logout = menu.findItem(R.id.logout);
        logout.setTitle(getResources().getString(R.string.drawer_logout));
        navigationView.setNavigationItemSelectedListener(this);
        View hView = navigationView.getHeaderView(0);
        profilePic = (ImageView) hView.findViewById(R.id.imageView_profile_pic);
        txtProfileName = (TextView) hView.findViewById(R.id.txtProfilename);
        profiletxtview = (TextView) hView.findViewById(R.id.txtviewprofile);
        txtProfileName.setText(strUserName);
        profiletxtview.setText(getResources().getString(R.string.view_profile));

        if (!(Validation.isOnline(TaggedneedsActivity.this))) {
            ToastPopUp.show(TaggedneedsActivity.this, getString(R.string.network_validation));
        } else {
            mFirebaseInstance = FirebaseDatabase.getInstance();
            mFirebaseDatabase = mFirebaseInstance.getReference(DATABASE_PROFILE_PIC_UPLOADS);
            profileList = new ArrayList<>();
            DatabaseReference reference = mFirebaseDatabase.child("profile");
            //adding an event listener to fetch values
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    //iterating through all the values in database
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        Profile profile = postSnapshot.getValue(Profile.class);
                        profileList.add(profile);
                    }
                    Log.d("profile_list", "" + profileList.size());
                    if (profileList.size() > 0) {
                        for (Profile profile : profileList) {
                            if (profile.getUserid().equals(strUserId)) {
                                Glide.with(getApplicationContext()).load(profile.getPhotourl()).into(profilePic);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        profiletxtview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(Validation.isOnline(TaggedneedsActivity.this))) {
                    ToastPopUp.show(TaggedneedsActivity.this, getString(R.string.network_validation));
                } else {
                    selectfragment(0);
                    //Toast.makeText(getApplicationContext(), "hello", Toast.LENGTH_SHORT).show();
                    drawer.closeDrawer(GravityCompat.START);
                }
            }
        });

        adress_show = new GetingAddress(TaggedneedsActivity.this).getCompleteAddressString(new GPSTracker(TaggedneedsActivity.this).getLatitude(), new GPSTracker(TaggedneedsActivity.this).getLatitude());
        Log.d("myadd", adress_show);
//---------------------------------clicking hamburger
        /*imgHamburger.setOnClickListener(new View.OnClickListener() {
            int i = 0;

            @Override
            public void onClick(View v) {
                draerwe_status = sharedPreferences.get_drawer_status();
                drawervalue = draerwe_status.get(sharedPreferences.DRAWER_STATUS);

                if (drawervalue.equals("close")) {
                    mDialog = new SimpleArcDialog(TaggedneedsActivity.this);
                    mDialog.setConfiguration(new ArcConfiguration(TaggedneedsActivity.this));
                    mDialog.show();
                    getNotificationCount();

                } else {
                    sharedPreferences.set_drawer_status("close");
                    android.support.v4.app.FragmentTransaction transaction = fragmgr.beginTransaction();
                    transaction.setCustomAnimations(R.anim.slide_right, R.anim.slide_left);
                    transaction.replace(R.id.content_frame, fragname);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }

            }
        });*/

        if (savedInstanceState == null) {
            selectfragment(1);
        }
    }

    public void dialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(TaggedneedsActivity.this);
        LayoutInflater li = LayoutInflater.from(TaggedneedsActivity.this);
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

        //----------------Displaying the alert dialog
        alertDialogForgot.show();
        dialogconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //alertDialogForgot.dismiss();
                alertDialogForgot.dismiss();
                //   myContext.finishAffinity();
            }
        });
        dialogcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogForgot.dismiss();
            }
        });
    }

    public void updateLanguage(String language) {
        locale = new Locale(language);
        Locale.setDefault(locale);
        config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        TaggedneedsActivity.updateTitle(getResources().getString(R.string.map_tagged_deeds));
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle bottom_navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            selectfragment(1);
        } else if (id == R.id.nav_groups) {
            //open groups fragment
            selectfragment(9);
        } else if (id == R.id.nav_mytags) {
            selectfragment(7);
        } else if (id == R.id.nav_myfulfilledtags) {
            selectfragment(3);
        } else if (id == R.id.nav_taganeed) {
            selectfragment(2);
        } else if (id == R.id.nav_resources) {
            if (!(Validation.isOnline(TaggedneedsActivity.this))) {
                ToastPopUp.show(TaggedneedsActivity.this, getString(R.string.network_validation));
            } else {
                getOwnedGroupList(strUserId);
            }
        } else if (id == R.id.toptenaggers) {
            selectfragment(4);  //nilesh      11/5/2017
        } else if (id == R.id.topfullfillers) {
            selectfragment(5);
        } else if (id == R.id.aboutapp) {
            selectfragment(8);
        } else if (id == R.id.notifications) {
            selectfragment(6);
        } else if (id == R.id.settings) {
            selectfragment(10);
        } else if (id == R.id.logout) {
            /*Intent needdtls = new Intent(TaggedneedsActivity.this, NeedDetailsActivity.class);
            startActivity(needdtls);*/
            FacebookSdk.sdkInitialize(getApplicationContext());
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            LayoutInflater li = LayoutInflater.from(this);
            View confirmDialog = li.inflate(R.layout.giftneeddialog, null);
            dialogconfirm = (Button) confirmDialog.findViewById(R.id.btn_submit_mobileno);
            dialogcancel = (Button) confirmDialog.findViewById(R.id.btn_Cancel_mobileno);
            dialogtext = (TextView) confirmDialog.findViewById(R.id.txtgiftneeddialog);

            dialogtext.setText(getResources().getString(R.string.logout_msg));
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
                    LoginManager.getInstance().logOut();
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                            new ResultCallback<Status>() {
                                @Override
                                public void onResult(Status status) {
                                    //updateUI(false);
                                }
                            });
                    sharedPreferences.createUserCredentialSession(null, null, null);
                    Intent loginintent = new Intent(TaggedneedsActivity.this, LoginActivity.class);
                    loginintent.putExtra("message", "Charity");
                    startActivity(loginintent);
                }
            });
            dialogcancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialogForgot.dismiss();
                }
            });
        }
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //---------------------------------selecting a fragment to display----------------------------------
    public static void selectfragment(int i) {
        frag = null;
        switch (i) {
            case 0:
                fragmgr.beginTransaction().replace(R.id.content_frame, MyProfilefrag.newInstance(i)).addToBackStack(null).commit();
                break;
            case 1:
                fragmgr.beginTransaction().replace(R.id.content_frame, TaggedneedsFrag.newInstance(i)).commit();
                break;
            case 2:
                fragmgr.beginTransaction().replace(R.id.content_frame, TagaNeed.newInstance(i)).addToBackStack(null).commit();
                break;
            case 3:
                fragmgr.beginTransaction().replace(R.id.content_frame, MyFullFillTags.newInstance(i)).addToBackStack(null).commit();
                break;
            case 4:
                fragmgr.beginTransaction().replace(R.id.content_frame, TaggerList.newInstance(i)).addToBackStack(null).commit();
                break;
            case 5:
                fragmgr.beginTransaction().replace(R.id.content_frame, TopTenFullfillerList.newInstance(i)).addToBackStack(null).commit();
                break;
            case 6:
                fragmgr.beginTransaction().replace(R.id.content_frame, Notificationfrag.newInstance(i)).addToBackStack(null).commit();
                break;
            case 7:
                fragmgr.beginTransaction().replace(R.id.content_frame, MyTagsList.newInstance(i)).addToBackStack(null).commit();
                break;
            case 8:
//                fragmgr.beginTransaction().replace(R.id.content_frame, AboutApp.newInstance(i)).addToBackStack(null).commit();
                android.support.v4.app.FragmentTransaction transaction = fragmgr.beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_right, R.anim.slide_left);
                transaction.replace(R.id.content_frame, MenuGrid.newInstance("0"));
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case 9:
                android.support.v4.app.FragmentTransaction transaction1 = fragmgr.beginTransaction();
                transaction1.setCustomAnimations(R.anim.slide_right, R.anim.slide_left);
                transaction1.replace(R.id.content_frame, GroupsListFragment.newInstance(i));
                transaction1.addToBackStack(null);
                transaction1.commit();
//                fragmgr.beginTransaction().replace(R.id.content_frame, GroupsListFragment.newInstance(i)).addToBackStack(null).commit();
                break;
            case 10:
                fragmgr.beginTransaction().replace(R.id.content_frame, SettingsFragment.newInstance(i)).addToBackStack(null).commit();
                break;
            case 11:
                fragmgr.beginTransaction().replace(R.id.content_frame, CreateResourceFragment.newInstance(i)).addToBackStack(null).commit();
                break;
        }
    }

    public static void updateTitle(String title) {
        if (headingtext != null) {
            headingtext.setText(title);
        }
    }

    public void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        headingtext = (TextView) toolbar.findViewById(R.id.txtheading);
        editprofile = (TextView) toolbar.findViewById(R.id.appbar_editprofile);
        saveprofile = (TextView) toolbar.findViewById(R.id.appbar_editprofilesave);
        back = (ImageView) toolbar.findViewById(R.id.backbutton);
        imgappbarcamera = (ImageView) toolbar.findViewById(R.id.imgappbarcamera);
        imgappbarsetting = (ImageView) toolbar.findViewById(R.id.imgappbarsetting);
        imgfilter = (ImageView) toolbar.findViewById(R.id.img_filter);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(View drawerView) {
        // Toast.makeText(TaggedneedsActivity.this, "changed", Toast.LENGTH_LONG).show();
        sharedPreferences = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sharedPreferences.getUserDetails();
        strUserId = user.get(sharedPreferences.USER_ID);
        strUserName = user.get(sharedPreferences.USER_NAME);
        txtProfileName.setText(strUserName);
//        mDialog = new SimpleArcDialog(TaggedneedsActivity.this);
//        mDialog.setConfiguration(new ArcConfiguration(TaggedneedsActivity.this));
//        mDialog.show();
//        mDialog.setCancelable(false);
        if (!(Validation.isOnline(TaggedneedsActivity.this))) {
            ToastPopUp.show(TaggedneedsActivity.this, getString(R.string.network_validation));
        } else {
            getNotificationCount();
        }
    }

    @Override
    public void onDrawerClosed(View drawerView) {

    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }


    /*@Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View view = getCurrentFocus();
        if (view != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) && view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            view.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + view.getLeft() - scrcoords[0];
            float y = ev.getRawY() + view.getTop() - scrcoords[1];
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom())
                ((InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
        }
        return super.dispatchTouchEvent(ev);
    }*/

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        mGoogleApiClient.connect();
    }

    public void getNotificationCount() {
        // mDialog.setCancelable(false);
        // item = new ArrayList<>();

        String Latitude = String.valueOf(new GPSTracker(TaggedneedsActivity.this).getLatitude());
        String Longitude = String.valueOf(new GPSTracker(TaggedneedsActivity.this).getLongitude());
        String lat_long = Latitude + "," + Longitude;

        //final RowData rowData = new RowData();
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        NotificationCountInterface service = retrofit.create(NotificationCountInterface.class);
        Call<NotificationCountModel> call = service.fetchData(strUserId, lat_long);
        call.enqueue(new Callback<NotificationCountModel>() {
            @Override
            public void onResponse(Response<NotificationCountModel> response, Retrofit retrofit) {
                try {
//                    mDialog.dismiss();
                    NotificationCountModel notificationCountModel = response.body();
                    String count = notificationCountModel.getNtCount();
                    setMenuCounter(R.id.notifications, Integer.parseInt(count));

//                    drawer.openDrawer(GravityCompat.START);
//                    sharedPreferences.set_drawer_status("open");
//                    android.support.v4.app.FragmentTransaction transaction = fragmgr.beginTransaction();
//                    transaction.setCustomAnimations(R.anim.slide_right, R.anim.slide_left);
//                    transaction.replace(R.id.content_frame, MenuGrid.newInstance(count));
//                    transaction.addToBackStack(null);
//                    transaction.commit();
                } catch (Exception e) {
//                    mDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Throwable t) {
//                mDialog.dismiss();
                ToastPopUp.show(TaggedneedsActivity.this, getString(R.string.server_response_error));
            }
        });
    }

    private void setMenuCounter(@IdRes int itemId, int count) {
        TextView view = (TextView) navigationView.getMenu().findItem(itemId).getActionView();
        if (count > 0) {
            view.setVisibility(View.VISIBLE);
            view.setText(String.valueOf(count));
        } else {
            view.setVisibility(View.GONE);
        }
//        view.setText(count > 0 ? String.valueOf(count) : null);
    }

    //--------------------------getting user owned groups from server------------------------------------------
    public void getOwnedGroupList(String userid) {
        mDialog = new SimpleArcDialog(TaggedneedsActivity.this);
        mDialog.setConfiguration(new ArcConfiguration(TaggedneedsActivity.this));
        mDialog.show();
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        OwnedGroupsInterface service = retrofit.create(OwnedGroupsInterface.class);
        Call<List<GroupPOJO>> call = service.sendData(userid);
        call.enqueue(new Callback<List<GroupPOJO>>() {
            @Override
            public void onResponse(Response<List<GroupPOJO>> response, Retrofit retrofit) {
                mDialog.dismiss();
                Log.d("response_ownedgrouplist", "" + response.body());
                try {
                    List<GroupPOJO> res = response.body();
                    int isblock = 0;
                    try {
                        isblock = res.get(0).getIsBlocked();
                    } catch (Exception e) {
                        isblock = 0;
                    }
                    if (isblock == 1) {
                        FacebookSdk.sdkInitialize(getApplicationContext());
                        Toast.makeText(getApplicationContext(), "You have been blocked", Toast.LENGTH_SHORT).show();
                        sharedPreferences.createUserCredentialSession(null, null, null);
                        LoginManager.getInstance().logOut();
                        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                                new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(Status status) {
                                        //updateUI(false);
                                    }
                                });
                        int i = new DBGAD(getApplicationContext()).delete_row_message();
                        sharedPreferences.set_notification_status("ON");
                        Intent loginintent = new Intent(getApplicationContext(), LoginActivity.class);
                        loginintent.putExtra("message", "Charity");
                        startActivity(loginintent);
                    } else {
                        List<GroupPOJO> groupPOJOS = response.body();
                        if (groupPOJOS.size() > 0) {
                            selectfragment(11);
                        } else {
                            Toast.makeText(getApplicationContext(), "You don't own groups to create the resource", Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (Exception e) {
                    mDialog.dismiss();
                    Log.d("response_ownedgrouplist", "" + e.getMessage());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                mDialog.dismiss();
                ToastPopUp.show(myContext, getString(R.string.server_response_error));
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        // dialog();
    }
}
