package giftadeed.kshantechsoft.com.giftadeed.GridMenu;

import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.HashMap;

import giftadeed.kshantechsoft.com.giftadeed.AboutApp.AboutApp;
import giftadeed.kshantechsoft.com.giftadeed.ContactUs.Contactus;
import giftadeed.kshantechsoft.com.giftadeed.CookiesPolicy.CookiesPolicy;
import giftadeed.kshantechsoft.com.giftadeed.Dashboard.Dashboard;
import giftadeed.kshantechsoft.com.giftadeed.Disclaimer.Disclaimer;
import giftadeed.kshantechsoft.com.giftadeed.EnduserAggrement.EnduserAggrement;
import giftadeed.kshantechsoft.com.giftadeed.Help.Help;
import giftadeed.kshantechsoft.com.giftadeed.MyFullFillTag.MyFullFillTags;
import giftadeed.kshantechsoft.com.giftadeed.Mytags.MyTagsList;
import giftadeed.kshantechsoft.com.giftadeed.PrivacyPolicy.PrivacyPolicy;
import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.GPSTracker;
import giftadeed.kshantechsoft.com.giftadeed.Tagcounter.Tagcounter;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsActivity;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsFrag;
import giftadeed.kshantechsoft.com.giftadeed.Utils.SessionManager;
import giftadeed.kshantechsoft.com.giftadeed.Utils.Utility;
import giftadeed.kshantechsoft.com.giftadeed.taggerfullfiller.TaggerList;
import giftadeed.kshantechsoft.com.giftadeed.taggerfullfiller.TopTenFullfillerList;
import giftadeed.kshantechsoft.com.giftadeed.termsandconditions.Terms_Condition;

////////////////////////////////////////////////////////////////////
//     Shows menu on clicking about app from drawer              //
/////////////////////////////////////////////////////////////////
public class MenuGrid extends Fragment implements GoogleApiClient.OnConnectionFailedListener {
    static FragmentManager fragmgr;
    SessionManager sharedPreferences;
    String strUserId, strUserName;
    static String NotificationCountnum;
    private GoogleApiClient mGoogleApiClient;
    static int color_pos = 0;
    TextView txt_app_version;
    String currentVersion = "";
    View rootview;
    GridadapterRecycler adptr;
    double longitude_gps;
    double latitude_gps;
    public int[] mThumbIds = {
            R.drawable.my_tags_icon, R.drawable.myfulltilltags_icon,
            R.drawable.top10taggers_icon, R.drawable.top10tagfullfillers_icon,
            R.drawable.tagcounter_icon, R.drawable.dashboard_icon,
            R.drawable.aboutapp_icon, R.drawable.tc_icon,
            R.drawable.privacypolicy_icon, R.drawable.cookiespolicy_icon,
            R.drawable.enduseragreement_icon, R.drawable.disclimer_icon,
            R.drawable.help_icon, R.drawable.contact_icon
    };

    public String[] texts = {"MY TAGS", "MY FULFILLED TAGS",
            "TOP 10 TAGGERS", "TOP 10 TAG FULFILLERS", "TAG COUNTER", "DASHBOARD", "ABOUT US", "TERMS AND CONDITIONS",
            "PRIVACY POLICY", "COOKIES POLICY", "END-USER AGREEMENT", "DISCLAIMER", "FAQs", "CONTACT US"};

    public static MenuGrid newInstance(String sectionNumber) {
        NotificationCountnum = sectionNumber;
        MenuGrid fragment = new MenuGrid();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_menu_grid, container, false);
        // GridView gridView = (GridView) rootview.findViewById(R.id.gridview);
        TaggedneedsActivity.updateTitle(getResources().getString(R.string.drawer_about_app));
        TaggedneedsActivity.toggle.setDrawerIndicatorEnabled(true);
        TaggedneedsActivity.back.setVisibility(View.GONE);
        TaggedneedsActivity.imgappbarcamera.setVisibility(View.GONE);
        TaggedneedsActivity.imgappbarsetting.setVisibility(View.GONE);
        TaggedneedsActivity.imgfilter.setVisibility(View.GONE);
        TaggedneedsActivity.imgShare.setVisibility(View.GONE);
        TaggedneedsActivity.editprofile.setVisibility(View.GONE);
        TaggedneedsActivity.saveprofile.setVisibility(View.GONE);
        sharedPreferences = new SessionManager(getContext());
        HashMap<String, String> user = sharedPreferences.getUserDetails();
        strUserId = user.get(sharedPreferences.USER_ID);
        strUserName = user.get(sharedPreferences.USER_NAME);
        fragmgr = getFragmentManager();
        int mNoOfColumns = Utility.calculateNoOfColumns(getContext());
        latitude_gps = new GPSTracker(getContext()).getLatitude();
        longitude_gps = new GPSTracker(getContext()).getLongitude();
        //----------------for google logout
        mGoogleApiClient = ((TaggedneedsActivity) getActivity()).mGoogleApiClient;
        RecyclerView recyclerView = (RecyclerView) rootview.findViewById(R.id.recycler_view);
        txt_app_version = rootview.findViewById(R.id.tv_about_app_version);
        try {
            currentVersion = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0).versionName;
            if (currentVersion.length() > 0) {
                txt_app_version.setVisibility(View.VISIBLE);
                txt_app_version.setText("App Version " + currentVersion);
            } else {
                txt_app_version.setVisibility(View.GONE);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String user_Name;
        try {
            if (strUserName.trim().contains(" ")) {
                String name_user[] = strUserName.split(" ");
                String output = name_user[0].substring(0, 1).toUpperCase() + name_user[0].substring(1);
                user_Name = output + "." + name_user[1].toUpperCase().charAt(0);
            } else {
                user_Name = strUserName;
            }
        } catch (Exception e) {

        }
        recyclerView.setHasFixedSize(true);

        /**
         Simple GridLayoutManager that spans two columns
         **/
        GridLayoutManager manager = new GridLayoutManager(getContext(), 3, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        final ArrayList<DataModel> androidversions = new ArrayList<>();

        for (int i = 0; i < texts.length; i++) {
            DataModel model = new DataModel();
            model.setVersionName(texts[i]);
            model.setImageurl(mThumbIds[i]);
            androidversions.add(model);
            adptr = new GridadapterRecycler(androidversions, getContext(), color_pos, NotificationCountnum, new GridadapterRecycler.OnItemClickListener() {

                @Override
                public void onItemClick(View view, int i) {
                    color_pos = i;
                    if (i == 0) {
                        // my tags
                        sharedPreferences.set_drawer_status("close");
                        int k = 0;
                        FragmentTransaction transaction = fragmgr.beginTransaction();
                        transaction.setCustomAnimations(R.anim.slide_right, R.anim.slide_left);
                        transaction.replace(R.id.content_frame, MyTagsList.newInstance(k));
                        transaction.addToBackStack(null);
                        transaction.commit();
                    } else if (i == 1) {
                        // my fulfilled tags
                        sharedPreferences.set_drawer_status("close");
                        int k = 0;
                        Bundle bundle = new Bundle();
                        bundle.putString("tab", "menu");
                        MyFullFillTags myFullFillTags = new MyFullFillTags();
                        myFullFillTags.setArguments(bundle);
                        FragmentTransaction transaction = fragmgr.beginTransaction();
                        transaction.setCustomAnimations(R.anim.slide_right, R.anim.slide_left);
                        transaction.replace(R.id.content_frame, myFullFillTags);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    } else if (i == 2) {
                        // top 10 taggers
                        sharedPreferences.set_drawer_status("close");
                        int k = 0;
                        FragmentTransaction transaction = fragmgr.beginTransaction();
                        transaction.setCustomAnimations(R.anim.slide_right, R.anim.slide_left);
                        transaction.replace(R.id.content_frame, TaggerList.newInstance(k));
                        transaction.addToBackStack(null);
                        transaction.commit();
                    } else if (i == 3) {
                        // top 10 fulfillers
                        sharedPreferences.set_drawer_status("close");
                        int k = 0;
                        FragmentTransaction transaction = fragmgr.beginTransaction();
                        transaction.setCustomAnimations(R.anim.slide_right, R.anim.slide_left);
                        transaction.replace(R.id.content_frame, TopTenFullfillerList.newInstance(k));
                        transaction.addToBackStack(null);
                        transaction.commit();
                    } else if (i == 4) {
                        // tag counter
                        sharedPreferences.set_drawer_status("close");
                        int k = 0;
                        FragmentTransaction transaction = fragmgr.beginTransaction();
                        transaction.setCustomAnimations(R.anim.slide_right, R.anim.slide_left);
                        transaction.replace(R.id.content_frame, Tagcounter.newInstance(k));
                        transaction.addToBackStack(null);
                        transaction.commit();
                    } else if (i == 5) {
                        // dashboard
                        sharedPreferences.set_drawer_status("close");
                        int k = 0;
                        FragmentTransaction transaction = fragmgr.beginTransaction();
                        transaction.setCustomAnimations(R.anim.slide_right, R.anim.slide_left);
                        transaction.replace(R.id.content_frame, Dashboard.newInstance(k));
                        transaction.addToBackStack(null);
                        transaction.commit();
                    } else if (i == 6) {
                        // about us
                        sharedPreferences.set_drawer_status("close");
                        int k = 0;
                        FragmentTransaction transaction = fragmgr.beginTransaction();
                        transaction.setCustomAnimations(R.anim.slide_right, R.anim.slide_left);
                        transaction.replace(R.id.content_frame, AboutApp.newInstance(k));
                        transaction.addToBackStack(null);
                        transaction.commit();
                    } else if (i == 7) {
                        // terms and conditions
                        sharedPreferences.set_drawer_status("close");
                        int k = 0;
                        FragmentTransaction transaction = fragmgr.beginTransaction();
                        transaction.setCustomAnimations(R.anim.slide_right, R.anim.slide_left);
                        transaction.replace(R.id.content_frame, Terms_Condition.newInstance(k));
                        transaction.addToBackStack(null);
                        transaction.commit();
                    } else if (i == 8) {
                        // privacy policy
                        sharedPreferences.set_drawer_status("close");
                        int k = 0;
                        FragmentTransaction transaction = fragmgr.beginTransaction();
                        transaction.setCustomAnimations(R.anim.slide_right, R.anim.slide_left);
                        transaction.replace(R.id.content_frame, PrivacyPolicy.newInstance(k));
                        transaction.addToBackStack(null);
                        transaction.commit();
                    } else if (i == 9) {
                        // cookies policy
                        sharedPreferences.set_drawer_status("close");
                        int k = 0;
                        FragmentTransaction transaction = fragmgr.beginTransaction();
                        transaction.setCustomAnimations(R.anim.slide_right, R.anim.slide_left);
                        transaction.replace(R.id.content_frame, CookiesPolicy.newInstance(k));
                        transaction.addToBackStack(null);
                        transaction.commit();
                    } else if (i == 10) {
                        // end user agreement
                        sharedPreferences.set_drawer_status("close");
                        int k = 0;
                        FragmentTransaction transaction = fragmgr.beginTransaction();
                        transaction.setCustomAnimations(R.anim.slide_right, R.anim.slide_left);
                        transaction.replace(R.id.content_frame, EnduserAggrement.newInstance(k));
                        transaction.addToBackStack(null);
                        transaction.commit();
                    } else if (i == 11) {
                        // disclaimer
                        sharedPreferences.set_drawer_status("close");
                        int k = 0;
                        FragmentTransaction transaction = fragmgr.beginTransaction();
                        transaction.setCustomAnimations(R.anim.slide_right, R.anim.slide_left);
                        transaction.replace(R.id.content_frame, Disclaimer.newInstance(k));
                        transaction.addToBackStack(null);
                        transaction.commit();
                    } else if (i == 12) {
                        // help
                        sharedPreferences.set_drawer_status("close");
                        int k = 0;
                        FragmentTransaction transaction = fragmgr.beginTransaction();
                        transaction.setCustomAnimations(R.anim.slide_right, R.anim.slide_left);
                        transaction.replace(R.id.content_frame, Help.newInstance(k));
                        transaction.addToBackStack(null);
                        transaction.commit();
                    } else if (i == 13) {
                        // contact us
                        sharedPreferences.set_drawer_status("close");
                        int k = 0;
                        FragmentTransaction transaction = fragmgr.beginTransaction();
                        transaction.setCustomAnimations(R.anim.slide_right, R.anim.slide_left);
                        transaction.replace(R.id.content_frame, Contactus.newInstance(k));
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }

                    /* sharedPreferences.set_drawer_status("close");
                    int k = 0;
                    FragmentTransaction transaction = fragmgr.beginTransaction();
                    transaction.setCustomAnimations(R.anim.slide_right, R.anim.slide_left);
                    transaction.replace(R.id.content_frame, Notificationfrag.newInstance(k));
                    transaction.addToBackStack(null);
                    transaction.commit();*/

                    /* else if (i == 9) {     // chnage request done 3 june 2018 under supervisison of vaibhav
                        sharedPreferences.set_drawer_status("close");
                        int k = 0;
                        FragmentTransaction transaction = fragmgr.beginTransaction();
                        transaction.setCustomAnimations(R.anim.slide_right, R.anim.slide_left);
                        transaction.replace(R.id.content_main, AdvisoryBoard.newInstance(k));
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }*/

                    /*else if (i == 17) {
                        sharedPreferences.set_drawer_status("close");
                        FacebookSdk.sdkInitialize(getActivity());
                        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                        LayoutInflater li = LayoutInflater.from(getContext());
                        View confirmDialog = li.inflate(R.layout.giftneeddialog, null);
                        dialogconfirm = (Button) confirmDialog.findViewById(R.id.btn_submit_mobileno);
                        dialogcancel = (Button) confirmDialog.findViewById(R.id.btn_Cancel_mobileno);
                        dialogtext = (TextView) confirmDialog.findViewById(R.id.txtgiftneeddialog);

                        dialogtext.setText("Do you really want to logout?");
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
                                sharedPreferences.createUserCredentialSession(null, null, null);
                                LoginManager.getInstance().logOut();
                                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                                new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(Status status) {
                                        //updateUI(false);
                                    }
                                });

                                sharedPreferences.set_notification_status("OFF");
                                Intent loginintent = new Intent(getActivity(), SendBirdLoginActivity.class);
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
                    }*/
                }
            });
        }

        //  GridadapterRecycler adptr = new GridadapterRecycler(getContext(), texts, mThumbIds);
        recyclerView.setAdapter(adptr);
        return rootview;
    }

    @Override
    public void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
//                    draerwe_status = sharedPreferences.get_drawer_status();
//                    drawervalue = draerwe_status.get(sharedPreferences.DRAWER_STATUS);
//                    if (drawervalue.equals("open")) {
                    sharedPreferences.set_drawer_status("close");
                    Bundle bundle = new Bundle();
                    int i = 3;
                    bundle.putString("tab", "tab1");
                    TaggedneedsFrag mainHomeFragment = new TaggedneedsFrag();
                    mainHomeFragment.setArguments(bundle);
                    FragmentTransaction fragmentTransaction =
                            getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, mainHomeFragment);
                    fragmentTransaction.commit();
//                    }
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onPause() {
        super.onPause();
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
    }
}
