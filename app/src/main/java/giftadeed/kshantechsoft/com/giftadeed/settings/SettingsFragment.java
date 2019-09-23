package giftadeed.kshantechsoft.com.giftadeed.settings;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.leo.simplearcloader.SimpleArcDialog;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import giftadeed.kshantechsoft.com.giftadeed.Filter.CategoryPOJO;
import giftadeed.kshantechsoft.com.giftadeed.Group.GroupPOJO;
import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.TagaNeed;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsActivity;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsFrag;
import giftadeed.kshantechsoft.com.giftadeed.Utils.DatabaseAccess;
import giftadeed.kshantechsoft.com.giftadeed.Utils.SessionManager;

public class SettingsFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener {
    View rootview;
    FragmentActivity myContext;
    static FragmentManager fragmgr;
    EditText etSelectLanguage;
    RecyclerView groupRecyclerView, catRecyclerView;
    Button btnSaveSettings;
    Switch switchReceiveNoti;
    DiscreteSeekBar distanceSeekBar;
    double radius;
    TextView txtdist;
    ImageView heart;
    LinearLayout radiusLayout, groupListLayout, catListLayout;
    String strUser_ID;
    SessionManager sessionManager;
    private GoogleApiClient mGoogleApiClient;
    GroupListSettingsAdapter groupListAdapter;
    CatListSettingsAdapter catListSettingsAdapter;
    ArrayList<GroupPOJO> groupArrayList;
    ArrayList<CategoryPOJO> catArrayList;
    SimpleArcDialog simpleArcDialog;
    HashMap<String, String> Notification_status_map;
    String Notification_status = "null";
    DatabaseAccess databaseAccess;
    Locale locale;
    Configuration config;
    String storedLanguage;

    public static SettingsFragment newInstance(int sectionNumber) {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.settings_layout, container, false);
        sessionManager = new SessionManager(getActivity());
        storedLanguage = sessionManager.getLanguage();
        updateLanguage(storedLanguage);
        TaggedneedsActivity.updateTitle(getResources().getString(R.string.settings));
        TaggedneedsActivity.fragname = TagaNeed.newInstance(0);
        FragmentManager fragManager = myContext.getSupportFragmentManager();
        fragmgr = getFragmentManager();
        simpleArcDialog = new SimpleArcDialog(getContext());
        TaggedneedsActivity.imgappbarcamera.setVisibility(View.GONE);
        TaggedneedsActivity.imgappbarsetting.setVisibility(View.GONE);
        TaggedneedsActivity.imgfilter.setVisibility(View.GONE);
        TaggedneedsActivity.imgShare.setVisibility(View.GONE);
        TaggedneedsActivity.editprofile.setVisibility(View.GONE);
        TaggedneedsActivity.saveprofile.setVisibility(View.GONE);
        TaggedneedsActivity.toggle.setDrawerIndicatorEnabled(true);
        TaggedneedsActivity.back.setVisibility(View.GONE);
        TaggedneedsActivity.imgHamburger.setVisibility(View.GONE);
        TaggedneedsActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        init();
        if (storedLanguage.equals("zh")) {
            etSelectLanguage.setText("Chinese");
        } else if (storedLanguage.equals("en")) {
            etSelectLanguage.setText("English");
        } else if (storedLanguage.equals("fr-rFR")) {
            etSelectLanguage.setText("French");
        } else if (storedLanguage.equals("hi")) {
            etSelectLanguage.setText("Hindi");
        } else if (storedLanguage.equals("pt")) {
            etSelectLanguage.setText("Portuguese");
        } else if (storedLanguage.equals("es")) {
            etSelectLanguage.setText("Spanish");
        }
        databaseAccess = DatabaseAccess.getInstance(getContext());
        databaseAccess.open();
        HashMap<String, String> user = sessionManager.getUserDetails();
        strUser_ID = user.get(sessionManager.USER_ID);
        mGoogleApiClient = ((TaggedneedsActivity) getActivity()).mGoogleApiClient;

        Notification_status_map = sessionManager.get_notification_status();
        Notification_status = Notification_status_map.get(sessionManager.KEY_NOTIFICATION);
        radius = sessionManager.getradius();
        double rad = sessionManager.getradius();
        distanceSeekBar.setProgress((int) rad);
        Log.d("validation radius", String.valueOf(rad));
        if (sessionManager.getradius() > 1000) {
            float valueKM = sessionManager.getradius() / 1000;
            txtdist.setText("" + sessionManager.getradius() + " Metres (" + valueKM + " kms)");
        } else {
            txtdist.setText("" + sessionManager.getradius() + " Metres");
        }

        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animate(v);
            }
        });

        etSelectLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.dialog_select_language);
                final RadioButton rbChinese = (RadioButton) dialog.findViewById(R.id.rb_chinese);
                final RadioButton rbEnglish = (RadioButton) dialog.findViewById(R.id.rb_english);
                final RadioButton rbFrench = (RadioButton) dialog.findViewById(R.id.rb_french);
                final RadioButton rbHindi = (RadioButton) dialog.findViewById(R.id.rb_hindi);
                final RadioButton rbPortuguese = (RadioButton) dialog.findViewById(R.id.rb_portuguese);
                final RadioButton rbSpanish = (RadioButton) dialog.findViewById(R.id.rb_spanish);
                RadioGroup rgLanguages = (RadioGroup) dialog.findViewById(R.id.rg_language_group);
                if (storedLanguage.equals("zh")) {
                    rbChinese.setChecked(true);
                } else if (storedLanguage.equals("en")) {
                    rbEnglish.setChecked(true);
                } else if (storedLanguage.equals("fr-rFR")) {
                    rbFrench.setChecked(true);
                } else if (storedLanguage.equals("hi")) {
                    rbHindi.setChecked(true);
                } else if (storedLanguage.equals("pt")) {
                    rbPortuguese.setChecked(true);
                } else if (storedLanguage.equals("es")) {
                    rbSpanish.setChecked(true);
                }
                rgLanguages.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        if (rbChinese.isChecked()) {
                            storedLanguage = "zh";
                            etSelectLanguage.setText("Chinese");
                            dialog.dismiss();
                        } else if (rbEnglish.isChecked()) {
                            storedLanguage = "en";
                            etSelectLanguage.setText("English");
                            dialog.dismiss();
                        } else if (rbFrench.isChecked()) {
                            storedLanguage = "fr-rFR";
                            etSelectLanguage.setText("French");
                            dialog.dismiss();
                        } else if (rbHindi.isChecked()) {
                            storedLanguage = "hi";
                            etSelectLanguage.setText("Hindi");
                            dialog.dismiss();
                        } else if (rbPortuguese.isChecked()) {
                            storedLanguage = "pt";
                            etSelectLanguage.setText("Portuguese");
                            dialog.dismiss();
                        } else if (rbSpanish.isChecked()) {
                            storedLanguage = "es";
                            etSelectLanguage.setText("Spanish");
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();
            }
        });

        distanceSeekBar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                if (value > 1000) {
                    int valueKM = value / 1000;
                    txtdist.setText("" + value + " Metres (" + valueKM + " kms)");
                } else {
                    txtdist.setText("" + value + " Metres");
                }
                distanceSeekBar.setProgress(value);
                radius = value;
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

            }
        });

        try {
            if (Notification_status.equals("ON")) {
                switchReceiveNoti.setChecked(true);
                radiusLayout.setVisibility(View.VISIBLE);
                loadData();
            } else if (Notification_status.equals("OFF")) {
                switchReceiveNoti.setChecked(false);
                radiusLayout.setVisibility(View.GONE);
                groupListLayout.setVisibility(View.GONE);
                groupRecyclerView.setVisibility(View.GONE);
                catListLayout.setVisibility(View.GONE);
                catRecyclerView.setVisibility(View.GONE);
            }
        } catch (Exception e) {

        }

        switchReceiveNoti.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    radiusLayout.setVisibility(View.VISIBLE);
                    loadData();
                } else {
                    radiusLayout.setVisibility(View.GONE);
                    groupListLayout.setVisibility(View.GONE);
                    groupRecyclerView.setVisibility(View.GONE);
                    catListLayout.setVisibility(View.GONE);
                    catRecyclerView.setVisibility(View.GONE);
                }
            }
        });

        btnSaveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchReceiveNoti.isChecked()) {
                    sessionManager.set_notification_status("ON");
                    sessionManager.store_radius((float) radius);
                } else {
                    sessionManager.set_notification_status("OFF");
                }

                if (storedLanguage.equals("zh")) {
                    sessionManager.store_language("zh");
                    updateLanguage("zh");
                } else if (storedLanguage.equals("en")) {
                    sessionManager.store_language("en");
                    updateLanguage("en");
                } else if (storedLanguage.equals("fr-rFR")) {
                    sessionManager.store_language("fr-rFR");
                    updateLanguage("fr-rFR");
                } else if (storedLanguage.equals("hi")) {
                    sessionManager.store_language("hi");
                    updateLanguage("hi");
                } else if (storedLanguage.equals("pt")) {
                    sessionManager.store_language("pt");
                    updateLanguage("pt");
                } else if (storedLanguage.equals("es")) {
                    sessionManager.store_language("es");
                    updateLanguage("es");
                }

                Toast.makeText(getContext(), getResources().getString(R.string.setting_saved), Toast.LENGTH_SHORT).show();
                // move to mapview
                fragmgr = getFragmentManager();
                fragmgr.beginTransaction().replace(R.id.content_frame, TaggedneedsFrag.newInstance(1)).commit();
            }
        });
        return rootview;
    }

    public void init() {
        heart = (ImageView) rootview.findViewById(R.id.animated_heart);
        etSelectLanguage = (EditText) rootview.findViewById(R.id.et_select_language);
        btnSaveSettings = (Button) rootview.findViewById(R.id.btn_save_settings);
        groupRecyclerView = (RecyclerView) rootview.findViewById(R.id.notification_recycler_grouplist);
        catRecyclerView = (RecyclerView) rootview.findViewById(R.id.notification_recycler_catlist);
        switchReceiveNoti = (Switch) rootview.findViewById(R.id.switch_receive_noti);
        distanceSeekBar = (DiscreteSeekBar) rootview.findViewById(R.id.seekBar_notisettings);
        txtdist = (TextView) rootview.findViewById(R.id.txtdistance_settings);
        radiusLayout = (LinearLayout) rootview.findViewById(R.id.layout_radius);
        groupListLayout = (LinearLayout) rootview.findViewById(R.id.layout_noti_group_list);
        catListLayout = (LinearLayout) rootview.findViewById(R.id.layout_noti_cat_list);
        groupRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        groupRecyclerView.setLayoutManager(layoutManager);
        catRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getContext());
        catRecyclerView.setLayoutManager(layoutManager1);
    }

    public void animate(View view) {
        ImageView v = (ImageView) view;
        Drawable d = v.getDrawable();
        if (d instanceof AnimatedVectorDrawable) {
            AnimatedVectorDrawable avd = (AnimatedVectorDrawable) d;
            avd.start();
        } else if (d instanceof AnimatedVectorDrawableCompat) {
            AnimatedVectorDrawableCompat avd = (AnimatedVectorDrawableCompat) d;
            avd.start();
        }
    }

    public void updateLanguage(String language) {
        locale = new Locale(language);
        Locale.setDefault(locale);
        config = new Configuration();
        config.locale = locale;
        getActivity().getBaseContext().getResources().updateConfiguration(config, getActivity().getBaseContext().getResources().getDisplayMetrics());
    }

    private void loadData() {
        groupArrayList = new ArrayList<>();
        groupArrayList = databaseAccess.getAllGroups();
        Log.d("LocalDbGroupSize", "" + groupArrayList.size());
        if (groupArrayList.size() <= 0) {
            groupListLayout.setVisibility(View.GONE);
            groupRecyclerView.setVisibility(View.GONE);
        } else {
            groupListLayout.setVisibility(View.VISIBLE);
            groupRecyclerView.setVisibility(View.VISIBLE);
            groupListAdapter = new GroupListSettingsAdapter(groupArrayList, myContext);
            groupRecyclerView.setAdapter(groupListAdapter);
        }

        catArrayList = new ArrayList<>();
        catArrayList = databaseAccess.getAllCategories();
        Log.d("LocalDbCatSize", "" + catArrayList.size());
        if (catArrayList.size() <= 0) {
            catListLayout.setVisibility(View.GONE);
            catRecyclerView.setVisibility(View.GONE);
        } else {
            catListLayout.setVisibility(View.VISIBLE);
            catRecyclerView.setVisibility(View.VISIBLE);
            catListSettingsAdapter = new CatListSettingsAdapter(catArrayList, myContext);
            catRecyclerView.setAdapter(catListSettingsAdapter);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        mGoogleApiClient.connect();
    }
}
