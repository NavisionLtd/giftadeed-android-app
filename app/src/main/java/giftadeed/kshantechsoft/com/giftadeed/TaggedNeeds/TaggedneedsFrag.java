package giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
//import android.app.Fragment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.vignesh_iopex.confirmdialog.Confirm;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.squareup.okhttp.OkHttpClient;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import giftadeed.kshantechsoft.com.giftadeed.Filter.FilterFrag;
import giftadeed.kshantechsoft.com.giftadeed.MyProfile.MyProfilefrag;
import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.GPSTracker;
import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.TagaNeed;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.list_Model.Modeltaglist;

import giftadeed.kshantechsoft.com.giftadeed.Utils.SessionManager;
import giftadeed.kshantechsoft.com.giftadeed.Utils.Validation;
import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

import static giftadeed.kshantechsoft.com.giftadeed.Utils.FontDetails.context;


public class TaggedneedsFrag extends Fragment {
    static android.app.Fragment fragment;
    //static FragmentManager fragmgr;
    private TabLayout tablayout;
    private ViewPager viewpgr;
    View rootview;
    FragmentActivity myContext;
    public static final int RequestPermissionCode = 1;
    String value = "tab1";
    int tab_no;
    String selected_tab;
    //------------------------dialog text
    TextView dialogtext;
    private AlertDialog alertDialogForgot;
    Button btnLogin, dialogconfirm, dialogcancel;

    //--------------------sending list to tab

    SessionManager sessionManager;
    String strUserId;
    ArrayList<String> lat_long = new ArrayList<>();
    ArrayList<String> icon_path = new ArrayList<>();
    ArrayList<String> tag_title = new ArrayList<>();
    double radius_set = 10.00;
    Modeltaglist listData = new Modeltaglist();
    List<RowData> item;
    SimpleArcDialog mDialog;
    static android.support.v4.app.FragmentManager fragmgr;
    String selectedLangugae;
    Locale locale;
    Configuration config;

    public static TaggedneedsFrag newInstance(int sectionNumber) {
        TaggedneedsFrag fragment = new TaggedneedsFrag();
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        TaggedneedsActivity.updateTitle(getResources().getString(R.string.map_tagged_deeds));
        TaggedneedsActivity.imgappbarcamera.setVisibility(View.VISIBLE);
        TaggedneedsActivity.imgappbarsetting.setVisibility(View.VISIBLE);
        TaggedneedsActivity.imgappbarsetting.setImageResource(R.drawable.filter);
        TaggedneedsActivity.imgfilter.setVisibility(View.GONE);
        TaggedneedsActivity.editprofile.setVisibility(View.GONE);
        TaggedneedsActivity.saveprofile.setVisibility(View.GONE);
        TaggedneedsActivity.back.setVisibility(View.GONE);
        TaggedneedsActivity.toggle.setDrawerIndicatorEnabled(true);
        TaggedneedsActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        TaggedneedsActivity.fragname = TaggedneedsFrag.newInstance(0);
        TaggedneedsActivity.imgHamburger.setVisibility(View.GONE);
        mDialog = new SimpleArcDialog(getContext());
        rootview = inflater.inflate(R.layout.fragment_taggedneeds, container, false);
        android.support.v4.app.FragmentManager fragManager = myContext.getSupportFragmentManager();
        fragmgr = getFragmentManager();
        requestgallPermission();
        tablayout = (TabLayout) rootview.findViewById(R.id.tabLayout);
        sessionManager = new SessionManager(getActivity());
        selectedLangugae = sessionManager.getLanguage();
        updateLanguage(selectedLangugae);
        HashMap<String, String> user = sessionManager.getUserDetails();
        strUserId = user.get(sessionManager.USER_ID);
        //get_Tag_data(strUserId);
        viewpgr = (ViewPager) rootview.findViewById(R.id.pager);

        TaggedneedsActivity.fragname = TaggedneedsFrag.newInstance(0);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            value = bundle.getString("tab");
        }

        tablayout.addTab(tablayout.newTab().setText(getResources().getString(R.string.map_map)));
        tablayout.addTab(tablayout.newTab().setText(getResources().getString(R.string.map_list)));
        tablayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tablayout.setupWithViewPager(viewpgr);
        //  tablayout.setupWithViewPager(viewpgr);

        viewpgr.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tablayout));

        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //Toast.makeText(myContext, ""+tab.getPosition(), Toast.LENGTH_SHORT).show();
                tab_no = tab.getPosition();
                if (tab_no == 0) {
                    selected_tab = "tab1";
                } else {
                    selected_tab = "tab2";
                    //  Toast.makeText(myContext, "Only images uploaded during the past 48 hours are displayed", Toast.LENGTH_SHORT).show();
                }
                if (!(Validation.isNetworkAvailable(myContext))) {
                    Toast.makeText(myContext, "OOPS! No INTERNET. Please check your network connection", Toast.LENGTH_SHORT).show();
                    //tab_no = viewpgr.getCurrentItem();


                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewpgr.setAdapter(new Pager(getChildFragmentManager(), tablayout.getTabCount()));
        if (value.equals("tab2")) {
            viewpgr.setCurrentItem(1);
        } else {
            viewpgr.setCurrentItem(0);
        }

        TaggedneedsActivity.imgappbarcamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmgr = getFragmentManager();

                Bundle bundle = new Bundle();
                // int i = 3;
                bundle.putString("tab", selected_tab);
                bundle.putString("page", "taggedneedspage");

                TagaNeed mainHomeFragment = new TagaNeed();
                mainHomeFragment.setArguments(bundle);
                android.support.v4.app.FragmentTransaction fragmentTransaction =
                        getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, mainHomeFragment).addToBackStack(null);
                fragmentTransaction.commit();
                Log.d("tab_selected", selected_tab);

            /*   int i = 2;
                fragmgr.beginTransaction().replace(R.id.content_frame, TagaNeed.newInstance(i)).addToBackStack(null).commit();
                Log.d("tab_selected", String.valueOf(tab_no));*/

            }
        });
        TaggedneedsActivity.imgappbarsetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                // int i = 3;
                bundle.putString("tab", selected_tab);

                FilterFrag mainHomeFragment = new FilterFrag();
                mainHomeFragment.setArguments(bundle);
                android.support.v4.app.FragmentTransaction fragmentTransaction =
                        getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, mainHomeFragment);
                fragmentTransaction.commit();

                /*fragmgr.beginTransaction().replace(R.id.content_frame, FilterFrag.newInstance()).addToBackStack(null).commit();
                Log.d("tab_selected", String.valueOf(tab_no));*/
            }
        });


        rootview.getRootView().setFocusableInTouchMode(true);
        rootview.getRootView().requestFocus();
        rootview.getRootView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
                    // handle back button's click listener
                    //      getActivity().finish();
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.giftneeddialog);
                    //dialog.setTitle("Title...");
                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                    LayoutInflater li = LayoutInflater.from(getContext());
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
                    // if button is clicked, close the custom dialog
                    dialogconfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            myContext.finishAffinity();
                        }
                    });
                    dialogcancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialogForgot.dismiss();
                        }
                    });
                    //  dialog.show();
                    return true;
                } else {
                    return false;
                }
            }
        });
        return rootview;
    }

    @Override
    public void onResume() {
        super.onResume();
        //getView().setFocusableInTouchMode(true);

        /*getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
                    // handle back button's click listener
                    //      getActivity().finish();
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.giftneeddialog);
                    //dialog.setTitle("Title...");
                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                    LayoutInflater li = LayoutInflater.from(getContext());
                    View confirmDialog = li.inflate(R.layout.giftneeddialog, null);
                    dialogconfirm = (Button) confirmDialog.findViewById(R.id.btn_submit_mobileno);
                    dialogcancel = (Button) confirmDialog.findViewById(R.id.btn_Cancel_mobileno);
                    dialogtext = (TextView) confirmDialog.findViewById(R.id.txtgiftneeddialog);

                    dialogtext.setText("Do you really want to exit?");
                    //-------------Adding our dialog box to the view of alert dialog
                    alert.setView(confirmDialog);
                    alert.setCancelable(false);

                    //----------------Creating an alert dialog
                    alertDialogForgot = alert.create();
                    alertDialogForgot.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
                    alertDialogForgot.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    //----------------Displaying the alert dialog
                    alertDialogForgot.show();
                    // if button is clicked, close the custom dialog
                    dialogconfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            myContext.finishAffinity();
                        }
                    });
                    dialogcancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialogForgot.dismiss();
                        }
                    });
                    //  dialog.show();
                    return true;
                } else {
                    return false;
                }
            }
        });*/
    }

    public void updateLanguage(String language) {
        locale = new Locale(language);
        Locale.setDefault(locale);
        config = new Configuration();
        config.locale = locale;
        getActivity().getBaseContext().getResources().updateConfiguration(config, getActivity().getBaseContext().getResources().getDisplayMetrics());
        TaggedneedsActivity.updateTitle(getResources().getString(R.string.map_tagged_deeds));
    }

    private void requestgallPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
            intent.setData(uri);
            startActivityForResult(intent, RequestPermissionCode);
            Toast.makeText(getActivity(), "Permission are denied please enable permissions", Toast.LENGTH_LONG).show();
            //Toast.makeText(getActivity(), "Permission are denied please go to settings and enable", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
       /* if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)){
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
            Toast.makeText(getActivity(), "Permission are denied please go to settings and enable", Toast.LENGTH_LONG).show();

        }*/
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0) {
                    boolean CameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadExternalStoragePermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean WriteExternalStoragePermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean InternetPermission = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                    if (InternetPermission) {
                        //Toast.makeText(getActivity(), "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        //Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }
}
