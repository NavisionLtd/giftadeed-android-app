package giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.sendbird.android.GroupChannel;
import com.sendbird.android.GroupChannelListQuery;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;
import com.squareup.okhttp.OkHttpClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import giftadeed.kshantechsoft.com.giftadeed.EmergencyPositioning.SOSEmergencyNumbers;
import giftadeed.kshantechsoft.com.giftadeed.GridMenu.MenuGrid;
import giftadeed.kshantechsoft.com.giftadeed.Group.GroupCollabFrag;
import giftadeed.kshantechsoft.com.giftadeed.Group.GroupPOJO;
import giftadeed.kshantechsoft.com.giftadeed.Login.LoginActivity;
import giftadeed.kshantechsoft.com.giftadeed.MyFullFillTag.MyFullFillTags;
import giftadeed.kshantechsoft.com.giftadeed.MyProfile.MyProfilefrag;
import giftadeed.kshantechsoft.com.giftadeed.MyProfile.Profile;
import giftadeed.kshantechsoft.com.giftadeed.Mytags.MyTagsList;
import giftadeed.kshantechsoft.com.giftadeed.Notifications.Notificationfrag;
import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.Resources.OwnedGroupsInterface;
import giftadeed.kshantechsoft.com.giftadeed.Resources.ResourceListFragment;
import giftadeed.kshantechsoft.com.giftadeed.SendBirdChat.main.ConnectionManager;
import giftadeed.kshantechsoft.com.giftadeed.SendBirdChat.openchannel.OpenChannelActivity;
import giftadeed.kshantechsoft.com.giftadeed.SendBirdChat.utils.PreferenceUtils;
import giftadeed.kshantechsoft.com.giftadeed.SendBirdChat.utils.PushUtils;
import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.GPSTracker;
import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.TagaNeed;
import giftadeed.kshantechsoft.com.giftadeed.Utils.DBGAD;
import giftadeed.kshantechsoft.com.giftadeed.Utils.DatabaseAccess;
import giftadeed.kshantechsoft.com.giftadeed.Utils.GetingAddress;
import giftadeed.kshantechsoft.com.giftadeed.Utils.SessionManager;
import giftadeed.kshantechsoft.com.giftadeed.Utils.ToastPopUp;
import giftadeed.kshantechsoft.com.giftadeed.Utils.Utility;
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
    DatabaseAccess databaseAccess;
    Locale locale;
    Configuration config;
    static Fragment frag;
    String path = "";
    static FragmentManager fragmgr;
    public static TextView headingtext, editprofile, saveprofile;
    CircleImageView profilePic;
    public static ImageView imgHamburger, imgappbarcamera, imgfilter, imgappbarsetting, back, imgShare;
    NavigationView navigationView;
    public static DrawerLayout drawer;
    TextView profiletxtview, txtProfileName;
    Toolbar toolbar;
    public static ActionBarDrawerToggle toggle;
    SessionManager sharedPreferences;
    String message, strUserId, strUserName;
    public String adress_show;
    Context myContext;
    public static Fragment fragname;
    TextView dialogtext;
    private AlertDialog alertDialogForgot;
    Button dialogconfirm, dialogcancel;
    public GoogleApiClient mGoogleApiClient;
    SimpleArcDialog mDialog;
    private List<Profile> profileList;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    public static String userClubCount;
    String selectedOption = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taggedneeds);
        myContext = TaggedneedsActivity.this;
        init();
        databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
        databaseAccess.open();
        sharedPreferences = new SessionManager(getApplicationContext());
        selectedLangugae = sharedPreferences.getLanguage();
        setSupportActionBar(toolbar);
        GPSTracker gps = new GPSTracker(TaggedneedsActivity.this);
        toolbar.setTitle("");
        headingtext.setText(getResources().getString(R.string.map_tagged_deeds));
        imgHamburger = (ImageView) toolbar.findViewById(R.id.imgHamburger);
        imgShare = (ImageView) toolbar.findViewById(R.id.img_share);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        mDialog = new SimpleArcDialog(TaggedneedsActivity.this);
        updateLanguage(selectedLangugae);
        HashMap<String, String> user = sharedPreferences.getUserDetails();
        strUserId = user.get(sharedPreferences.USER_ID);
        strUserName = user.get(sharedPreferences.USER_NAME);

        String image_path = sharedPreferences.getProfileImagePath();
        if (image_path != null) {
            loginWithSendbirdchat(strUserId, strUserName, image_path);
        } else {
            loginWithSendbirdchat(strUserId, strUserName, "");
        }

        getChannelsDetails();
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

        navigationView.setNavigationItemSelectedListener(this);
        View hView = navigationView.getHeaderView(0);
        profilePic = (CircleImageView) hView.findViewById(R.id.imageView_profile_pic);
        txtProfileName = (TextView) hView.findViewById(R.id.txtProfilename);
        profiletxtview = (TextView) hView.findViewById(R.id.txtviewprofile);
//        txtProfileName.setText(strUserName);
//        profiletxtview.setText(getResources().getString(R.string.view_profile));

        if (!(Validation.isOnline(TaggedneedsActivity.this))) {
            ToastPopUp.show(TaggedneedsActivity.this, getString(R.string.network_validation));
        } else {
            mFirebaseInstance = FirebaseDatabase.getInstance();
            mFirebaseDatabase = mFirebaseInstance.getReference(WebServices.DATABASE_PROFILE_PIC_UPLOADS);
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
                                path = profile.getPhotourl();
//                                Glide.with(getApplicationContext()).load(profile.getPhotourl()).into(profilePic);
                            }
                        }
                        if (path.length() > 0) {
                            Glide.with(getApplicationContext()).load(path).into(profilePic);
                        } else {
                            Glide.with(getApplicationContext()).load(R.drawable.ic_default_profile_pic).into(profilePic);
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

        selectedOption = getIntent().getStringExtra("selected_option");
        Log.d("selected_option", "" + selectedOption);
        if (selectedOption != null) {
            Log.d("selected_option_inside", "" + selectedOption);
            if (selectedOption.equals("tagdeed")) {
                // open tag deed if calling from app shortcut tag deed
                selectfragment(2);
            }
        } else {
            if (savedInstanceState == null) {
                selectfragment(1);
            }
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
        } else if (id == R.id.nav_inspire_community) {
            selectfragment(13);
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
        } else if (id == R.id.emergency_contacts) {
            selectfragment(12);
        } else if (id == R.id.logout) {
            FacebookSdk.sdkInitialize(getApplicationContext());
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            LayoutInflater li = LayoutInflater.from(this);
            View confirmDialog = li.inflate(R.layout.giftneeddialog, null);
            dialogconfirm = (Button) confirmDialog.findViewById(R.id.btn_submit_mobileno);
            dialogcancel = (Button) confirmDialog.findViewById(R.id.btn_Cancel_mobileno);
            dialogtext = (TextView) confirmDialog.findViewById(R.id.txtgiftneeddialog);
            dialogtext.setText(getResources().getString(R.string.logout_msg));
            alert.setView(confirmDialog);
            alert.setCancelable(false);
            alertDialogForgot = alert.create();
            alertDialogForgot.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
            alertDialogForgot.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialogForgot.show();
            dialogconfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoginManager.getInstance().logOut();
                    if (mGoogleApiClient.isConnected()) {
                        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                                new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(Status status) {
                                        //updateUI(false);
                                    }
                                });
                    }
                    sharedPreferences.createUserCredentialSession(null, null, null);
                    DisconnectSendbirdCall();
                    sharedPreferences.set_notification_status("ON");
                    Log.d("LocalDbGroupSize_before", "" + databaseAccess.getAllGroups().size());
                    databaseAccess.deleteAllGroups();
                    Log.d("LocalDbGroupSize_after", "" + databaseAccess.getAllGroups().size());
                    Log.d("LocalDbCategoriesBefore", "" + databaseAccess.getAllCategories().size());
                    databaseAccess.deleteAllCategory();
                    Log.d("LocalDbCategoriesAfter", "" + databaseAccess.getAllCategories().size());
                    Intent loginintent = new Intent(TaggedneedsActivity.this, LoginActivity.class);
                    loginintent.putExtra("message", "Charity");
                    startActivity(loginintent);
                    TaggedneedsActivity.this.finish();
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
    public void selectfragment(int i) {
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
                FragmentTransaction transaction = fragmgr.beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_right, R.anim.slide_left);
                transaction.replace(R.id.content_frame, MenuGrid.newInstance("0"));
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case 9:
                fragmgr.beginTransaction().replace(R.id.content_frame, GroupCollabFrag.newInstance(i)).commit();
                /*android.support.v4.app.FragmentTransaction transaction1 = fragmgr.beginTransaction();
                transaction1.setCustomAnimations(R.anim.slide_right, R.anim.slide_left);
                transaction1.replace(R.id.content_frame, GroupsListFragment.newInstance(i));
                transaction1.addToBackStack(null);
                transaction1.commit();*/
                break;
            case 10:
                fragmgr.beginTransaction().replace(R.id.content_frame, SettingsFragment.newInstance(i)).addToBackStack(null).commit();
                break;
            case 11:
//                fragmgr.beginTransaction().replace(R.id.content_frame, CreateResourceFragment.newInstance(i)).addToBackStack(null).commit();
                fragmgr.beginTransaction().replace(R.id.content_frame, ResourceListFragment.newInstance()).addToBackStack(null).commit();
                break;
            case 12:
                Intent intent = new Intent(TaggedneedsActivity.this, SOSEmergencyNumbers.class);
                intent.putExtra("callingfrom", "app");
                startActivity(intent);
                break;
            case 13:
                //jump to sendbird chat
               /* SendBirdLoginActivity loginActivity = new SendBirdLoginActivity();
                Bundle bl = new Bundle();
                bl.putString("CHATPAGE", "SHARECOMMUNITY");
                loginActivity.setArguments(bl);
                fragmgr.beginTransaction().replace(R.id.content_frame, loginActivity).addToBackStack(null).commit();*/

                // global chat functionality
                Intent global_intent = new Intent(TaggedneedsActivity.this, OpenChannelActivity.class);
                startActivity(global_intent);
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
//        mDialog = new SimpleArcDialog(TaggedneedsActivity.this);
//        mDialog.setConfiguration(new ArcConfiguration(TaggedneedsActivity.this));
//        mDialog.show();
//        mDialog.setCancelable(false);
        if (!(Validation.isOnline(TaggedneedsActivity.this))) {
            ToastPopUp.show(TaggedneedsActivity.this, getString(R.string.network_validation));
        } else {
            getNotificationCount();
        }
        Menu menu = navigationView.getMenu();
        MenuItem nav_home = menu.findItem(R.id.nav_home);
        nav_home.setTitle(getResources().getString(R.string.drawer_home));
        MenuItem nav_groups = menu.findItem(R.id.nav_groups);
        nav_groups.setTitle(getResources().getString(R.string.drawer_groups));
        MenuItem nav_inspire = menu.findItem(R.id.nav_inspire_community);
        nav_inspire.setTitle(getResources().getString(R.string.drawer_inspire));
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
        MenuItem contact = menu.findItem(R.id.emergency_contacts);
        contact.setTitle(getResources().getString(R.string.drawer_emergency_contacts));
        MenuItem logout = menu.findItem(R.id.logout);
        logout.setTitle(getResources().getString(R.string.drawer_logout));
        profiletxtview.setText(getResources().getString(R.string.view_profile));
        HashMap<String, String> user = sharedPreferences.getUserDetails();
        strUserName = user.get(sharedPreferences.USER_NAME);
        txtProfileName.setText(strUserName);
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
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.block_toast), Toast.LENGTH_SHORT).show();
                        sharedPreferences.createUserCredentialSession(null, null, null);
                        LoginManager.getInstance().logOut();
                        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                                new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(Status status) {
                                        //updateUI(false);
                                    }
                                });
                        sharedPreferences.set_notification_status("ON");
                        Intent loginintent = new Intent(getApplicationContext(), LoginActivity.class);
                        loginintent.putExtra("message", "Charity");
                        startActivity(loginintent);
                    } else {
                        List<GroupPOJO> groupPOJOS = response.body();
                        if (groupPOJOS.size() > 0) {
                            selectfragment(11);
                        } else {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.resource_error), Toast.LENGTH_LONG).show();
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

    //==============================================Send Bird Chat===================================================

    /**
     * it connects the user to sendbird environment where it takes input like userid,username,photo of google login
     *
     * @param strUserId
     * @param strUserName
     * @param strPhotoPath
     */
    public void loginWithSendbirdchat(String strUserId, String strUserName, String strPhotoPath) {
        if (strUserId != null && strUserName != null) {
            strUserId = strUserId.replaceAll("\\s", "");
            // String userNickname = mUserNicknameEditText.getText().toString();
            PreferenceUtils.setUserId(TaggedneedsActivity.this, strUserId);
            PreferenceUtils.setNickname(TaggedneedsActivity.this, strUserName);

            connectToSendBird(strUserId, strUserName, strPhotoPath);
        } else {
//            Toast.makeText(TaggedneedsActivity.this, "UnAuthorized Access", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Attempts to connect a user to SendBird.
     *
     * @param userId       The unique ID of the user.
     * @param userNickname The user's nickname, which will be displayed in chats.
     */
    private void connectToSendBird(final String userId, final String userNickname, final String strPhotoUrl) {
        // Show the loading indicator
        // showProgressBar(true);
        // mConnectButton.setEnabled(false);
        ConnectionManager.login(userId, new SendBird.ConnectHandler() {
            @Override
            public void onConnected(User user, SendBirdException e) {
                // Callback received; hide the progress bar.
                if (e != null) {
                    // Error!
                    Log.d("sendbird connect error", "" + e.getCode() + ": " + e.getMessage());
                    PreferenceUtils.setConnected(TaggedneedsActivity.this, false);
                    return;
                }
                Log.d("PHOTOSNADBIRD", "vv  " + user.getProfileUrl());
                PreferenceUtils.setUserId(TaggedneedsActivity.this, user.getUserId());
                PreferenceUtils.setNickname(TaggedneedsActivity.this, user.getNickname());
                PreferenceUtils.setProfileUrl(TaggedneedsActivity.this, user.getProfileUrl());
                PreferenceUtils.setConnected(TaggedneedsActivity.this, true);
                PreferenceUtils.setGroupChannelDistinct(TaggedneedsActivity.this, false);   //set the group chat false to allow create many group by single users
                // Update the user's nickname
                updateCurrentUserInfo(userNickname, strPhotoUrl);
                updateCurrentUserPushToken();
            }
        });
    }

    /**
     * Update the user's push token.
     */
    private void updateCurrentUserPushToken() {
        PushUtils.registerPushTokenForCurrentUser(TaggedneedsActivity.this, null);
    }

    /**
     * Updates the user's nickname.
     *
     * @param userNickname The new nickname of the user.
     */
    private void updateCurrentUserInfo(final String userNickname, String photoPath) {
        Log.d("PHOTOSNADBIRD", "uner method :  " + photoPath);
        if (photoPath.equalsIgnoreCase("")) {
            SendBird.updateCurrentUserInfo(userNickname, Utility.avatorDefaultIcon, new SendBird.UserInfoUpdateHandler() {
                @Override
                public void onUpdated(SendBirdException e) {
                    if (e != null) {
                        // Error!
                        Toast.makeText(
                                TaggedneedsActivity.this, "" + e.getCode() + ":" + e.getMessage(),
                                Toast.LENGTH_SHORT)
                                .show();
                        // Show update failed snackbar
//                        ToastPopUp.displayToast(TaggedneedsActivity.this, "Update user nickname failed");
                        return;
                    }
                    Log.d("TAG", "NickName189" + userNickname);
                    PreferenceUtils.setNickname(TaggedneedsActivity.this, userNickname);
                }
            });
        } else {
            SendBird.updateCurrentUserInfo(userNickname, photoPath, new SendBird.UserInfoUpdateHandler() {
                @Override
                public void onUpdated(SendBirdException e) {
                    if (e != null) {
                        // Error!
                        Toast.makeText(
                                TaggedneedsActivity.this, "" + e.getCode() + ":" + e.getMessage(),
                                Toast.LENGTH_SHORT)
                                .show();
                        // Show update failed snackbar
//                        ToastPopUp.displayToast(TaggedneedsActivity.this, "Update user nickname failed");
                        return;
                    }
                    Log.d("TAG", "NickName189" + userNickname);
                    PreferenceUtils.setNickname(TaggedneedsActivity.this, userNickname);
                }
            });
        }
    }

    public void DisconnectSendbirdCall() {
        SendBird.unregisterPushTokenAllForCurrentUser(new SendBird.UnregisterPushTokenHandler() {
            @Override
            public void onUnregistered(SendBirdException e) {
                if (e != null) {
                    // Error!
                    e.printStackTrace();
                    // Don't return because we still need to disconnect.
                } else {
//                    Toast.makeText(MainActivity.this, "All push tokens unregistered.", Toast.LENGTH_SHORT).show();
                    ConnectionManager.logout(new SendBird.DisconnectHandler() {
                        @Override
                        public void onDisconnected() {
                            PreferenceUtils.setConnected(TaggedneedsActivity.this, false);
                            //  finish();
                        }
                    });
                }
            }
        });
    }

    /**
     * to get he user club to identify they have any channel or not
     */
    public void getChannelsDetails() {
        //always use connect() along with any method of chat #phase 2 requirement 27 feb 2018 Nilesh
        SendBird.connect(strUserId, new SendBird.ConnectHandler() {
            @Override
            public void onConnected(User user, SendBirdException e) {
                if (e != null) {
                    // Error.
                    return;
                }
                GroupChannelListQuery channelListQuery = GroupChannel.createMyGroupChannelListQuery();
                channelListQuery.setIncludeEmpty(true);
                channelListQuery.next(new GroupChannelListQuery.GroupChannelListQueryResultHandler() {
                    @Override
                    public void onResult(List<GroupChannel> list, SendBirdException e) {
                        if (e != null) {
                            // Error.
                            return;
                        }
                        if (list != null) {
                            if (list.size() != 0) {
                                userClubCount = "Yes";
                            } else {
                                userClubCount = "No";
                            }
                        }
                    }
                });
            }
        });
    }
}
