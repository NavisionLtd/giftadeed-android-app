package giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.leo.simplearcloader.SimpleArcDialog;

import java.util.HashMap;
import java.util.Locale;

import giftadeed.kshantechsoft.com.giftadeed.Filter.FilterFrag;
import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.TagaNeed;
import giftadeed.kshantechsoft.com.giftadeed.Utils.SessionManager;
import giftadeed.kshantechsoft.com.giftadeed.Utils.Validation;

public class TaggedneedsFrag extends Fragment {
    private TabLayout tablayout;
    private ViewPager viewpgr;
    View rootview;
    FragmentActivity myContext;
    public static final int RequestPermissionCode = 1;
    String value = "tab1";
    int tab_no;
    String selected_tab;
    TextView dialogtext;
    private AlertDialog alertDialogForgot;
    Button dialogconfirm, dialogcancel;
    SessionManager sessionManager;
    String strUserId;
    SimpleArcDialog mDialog;
    static FragmentManager fragmgr;
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
        TaggedneedsActivity.imgShare.setVisibility(View.GONE);
        TaggedneedsActivity.editprofile.setVisibility(View.GONE);
        TaggedneedsActivity.saveprofile.setVisibility(View.GONE);
        TaggedneedsActivity.back.setVisibility(View.GONE);
        TaggedneedsActivity.toggle.setDrawerIndicatorEnabled(true);
        TaggedneedsActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        TaggedneedsActivity.fragname = TaggedneedsFrag.newInstance(0);
        TaggedneedsActivity.imgHamburger.setVisibility(View.GONE);
        mDialog = new SimpleArcDialog(getContext());
        rootview = inflater.inflate(R.layout.fragment_taggedneeds, container, false);
        fragmgr = getFragmentManager();
        requestgallPermission();
        tablayout = (TabLayout) rootview.findViewById(R.id.tabLayout);
        sessionManager = new SessionManager(getActivity());
        selectedLangugae = sessionManager.getLanguage();
        updateLanguage(selectedLangugae);
        HashMap<String, String> user = sessionManager.getUserDetails();
        strUserId = user.get(sessionManager.USER_ID);
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

        viewpgr.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tablayout));

        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab_no = tab.getPosition();
                if (tab_no == 0) {
                    selected_tab = "tab1";
                } else {
                    selected_tab = "tab2";
                }
                if (!(Validation.isNetworkAvailable(myContext))) {
                    Toast.makeText(myContext, getResources().getString(R.string.network_validation), Toast.LENGTH_SHORT).show();
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
                bundle.putString("tab", selected_tab);
                bundle.putString("page", "taggedneedspage");
                TagaNeed mainHomeFragment = new TagaNeed();
                mainHomeFragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction =
                        getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, mainHomeFragment).addToBackStack(null);
                fragmentTransaction.commit();
                Log.d("tab_selected", selected_tab);
            }
        });

        TaggedneedsActivity.imgappbarsetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("tab", selected_tab);
                FilterFrag mainHomeFragment = new FilterFrag();
                mainHomeFragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction =
                        getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, mainHomeFragment);
                fragmentTransaction.commit();
            }
        });

        rootview.getRootView().setFocusableInTouchMode(true);
        rootview.getRootView().requestFocus();
        rootview.getRootView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.giftneeddialog);
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
