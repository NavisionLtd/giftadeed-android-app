/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.Notifications;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.squareup.okhttp.OkHttpClient;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import giftadeed.kshantechsoft.com.giftadeed.Filter.CategoriesAdaptor;
import giftadeed.kshantechsoft.com.giftadeed.Group.RecyclerTouchListener;
import giftadeed.kshantechsoft.com.giftadeed.Group.RecyclerViewClickListener;
import giftadeed.kshantechsoft.com.giftadeed.Login.LoginActivity;
import giftadeed.kshantechsoft.com.giftadeed.Needdetails.DeeddeletedInterface;
import giftadeed.kshantechsoft.com.giftadeed.Needdetails.DeeddeletedModel;
import giftadeed.kshantechsoft.com.giftadeed.Needdetails.NeedDetailsFrag;
import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.CategoryInterface;
import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.CategoryType;
import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.GPSTracker;
import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.Needtype;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsActivity;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsFrag;
import giftadeed.kshantechsoft.com.giftadeed.Utils.DBGAD;
import giftadeed.kshantechsoft.com.giftadeed.Utils.SharedPrefManager;
import giftadeed.kshantechsoft.com.giftadeed.Utils.ToastPopUp;
import giftadeed.kshantechsoft.com.giftadeed.Utils.Validation;
import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

////////////////////////////////////////////////////////////////////
//     Shows list of notification of last seven days            //
/////////////////////////////////////////////////////////////////
public class Notificationfrag extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    NotificationAdapter notificationAdapter;
    LinearLayout layout_filternotification;
    Button searchnotification;
    String strUserId;
    static FragmentManager fragmgr;
    View rootview;
    ArrayList<Notification> notiArrayList;
    SharedPrefManager sharedPreferences;
    LinearLayout layout_data_not_found;
    float notification_radius = 10.0f;
    int days = 7;
    List<Notification> db_noti_list;
    EditText ednotificationfiltercategory;
    DiscreteSeekBar notificationProgressbar_distance, notificationProgressbar_time;
    private ArrayList<Needtype> categories;
    SimpleArcDialog mDialog;
    String strNeedmapping_ID, strFiltertype = "All";
    private AlertDialog alertDialogreturn;
    TextView txtfilterrmaxadius, txtfilterrmaxtime;

    private double LATITUDE, LONGITUDE;
    private String str_geopoint = "";
    DBGAD db_gad;

    public static Notificationfrag newInstance(int sectionNumber) {
        Notificationfrag fragment = new Notificationfrag();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_notification, container, false);
        TaggedneedsActivity.fragname = Notificationfrag.newInstance(0);
        TaggedneedsActivity.updateTitle(getResources().getString(R.string.noti_history));
        TaggedneedsActivity.imgappbarcamera.setVisibility(View.GONE);
        TaggedneedsActivity.imgappbarsetting.setVisibility(View.GONE);
        TaggedneedsActivity.imgfilter.setVisibility(View.VISIBLE);
        TaggedneedsActivity.imgShare.setVisibility(View.GONE);
        TaggedneedsActivity.editprofile.setVisibility(View.GONE);
        TaggedneedsActivity.saveprofile.setVisibility(View.GONE);
        TaggedneedsActivity.toggle.setDrawerIndicatorEnabled(true);
        TaggedneedsActivity.back.setVisibility(View.GONE);
        TaggedneedsActivity.imgHamburger.setVisibility(View.GONE);
        TaggedneedsActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        mDialog = new SimpleArcDialog(getContext());
        fragmgr = getFragmentManager();
        //------------------------------get data from sharedpreferences------------------
        sharedPreferences = new SharedPrefManager(getContext());
        HashMap<String, String> user = sharedPreferences.getUserDetails();
        strUserId = user.get(sharedPreferences.USER_ID);
        LATITUDE = new GPSTracker(getContext()).latitude;
        LONGITUDE = new GPSTracker(getContext()).longitude;
        str_geopoint = LATITUDE + "," + LONGITUDE;
        init();
        mDialog = new SimpleArcDialog(getContext());
        db_gad = new DBGAD(getContext());
        swipeRefreshLayout.setOnRefreshListener(this);
        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (!(Validation.isNetworkAvailable(getActivity()))) {
                                            swipeRefreshLayout.setRefreshing(false);
                                            ToastPopUp.show(getActivity(), getString(R.string.network_validation));
                                        } else {
                                            swipeRefreshLayout.setRefreshing(true);
                                            recyclerView.setAdapter(null);
                                            getNotifications();
                                        }
                                    }
                                }
        );

        TaggedneedsActivity.imgfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterNotificationDialog();
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(),
                recyclerView, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, final int position) {
                checkActiveDeed(notiArrayList.get(position).getTagid());
            }
        }));
        return rootview;
    }

    public void init() {
        layout_data_not_found = rootview.findViewById(R.id.layout_data_not_found);
        layout_filternotification = rootview.findViewById(R.id.layout_filternotification);
        swipeRefreshLayout = (SwipeRefreshLayout) rootview.findViewById(R.id.swipe_refresh_notification);
        recyclerView = rootview.findViewById(R.id.list_notifications);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
    }

    public void getNotifications() {
        notiArrayList = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        AllNotificationInterface service = retrofit.create(AllNotificationInterface.class);
        Log.d("response_notilist", "" + strUserId + "," + str_geopoint);
        Call<AllNotificationModel> call = service.fetchData(strUserId, str_geopoint);
        call.enqueue(new Callback<AllNotificationModel>() {
            @Override
            public void onResponse(Response<AllNotificationModel> response, Retrofit retrofit) {
                swipeRefreshLayout.setRefreshing(false);
                Log.d("response_notilist", "" + response.body());
                try {
                    AllNotificationModel allNotificationModel = response.body();
                    int isblock = 0;
                    try {
                        isblock = allNotificationModel.getIsBlocked();
                    } catch (Exception e) {
                        isblock = 0;
                    }
                    if (isblock == 1) {
                        swipeRefreshLayout.setRefreshing(false);
                        FacebookSdk.sdkInitialize(getActivity());
                        Toast.makeText(getContext(), getResources().getString(R.string.block_toast), Toast.LENGTH_SHORT).show();
                        sharedPreferences.createUserCredentialSession(null, null, null);
                        LoginManager.getInstance().logOut();
                        /*Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                                new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(Status status) {
                                        //updateUI(false);
                                    }
                                });*/

                        sharedPreferences.set_notification_status("ON");
                        Intent loginintent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(loginintent);
                    } else {
                        AllNotificationModel model = response.body();
                        notiArrayList.clear();
                        try {
                            for (int i = 0; i < model.getNotifications().size(); i++) {
                                Notification notification = new Notification();
                                notification.setDate(model.getNotifications().get(i).getDate());
                                notification.setTime(model.getNotifications().get(i).getTime());
                                notification.setNtType(model.getNotifications().get(i).getNtType());
                                notification.setTagType(model.getNotifications().get(i).getTagType());
                                notification.setGeopoint(model.getNotifications().get(i).getGeopoint());
                                notification.setNeedName(model.getNotifications().get(i).getNeedName());
                                notification.setTagid(model.getNotifications().get(i).getTagid());
                                notification.setSeen(model.getNotifications().get(i).getSeen());
                                notification.setDistanceInKms(model.getNotifications().get(i).getDistanceInKms());
                                notiArrayList.add(notification);
                            }
                        } catch (Exception e) {

                        }

                        if (notiArrayList.size() <= 0) {
                            recyclerView.setVisibility(View.GONE);
                            layout_data_not_found.setVisibility(View.VISIBLE);
                        } else {
                            layout_data_not_found.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            notificationAdapter = new NotificationAdapter(notiArrayList, getContext());
                            recyclerView.setAdapter(notificationAdapter);
                        }

                        db_gad.delete_All_messages();
                        for (int i = 0; i < notiArrayList.size(); i++) {
                            db_gad.insert_msg(notiArrayList.get(i).getDate(), notiArrayList.get(i).getTime(), notiArrayList.get(i).getNtType(), notiArrayList.get(i).getTagType(), notiArrayList.get(i).getGeopoint(), notiArrayList.get(i).getNeedName(), notiArrayList.get(i).getTagid(), notiArrayList.get(i).getSeen(), notiArrayList.get(i).getDistanceInKms());
                        }

                        // api list size
                        Log.d("api_notilist", "" + notiArrayList.size());

                        // local database records size
                        int dbRecordsCount = db_gad.getMessagesCount();
                        Log.d("db_notilist", "" + dbRecordsCount);
                    }
                } catch (Exception e) {
                    Log.d("response_notilist", "" + e.getMessage());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Log.d("response_notilist", "" + t.getMessage());
                ToastPopUp.show(getContext(), getString(R.string.server_response_error));
            }
        });
    }

    public void filterNotification() {
        mDialog.show();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Date newDate = calendar.getTime();
        String enddate = sdf.format(newDate);
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date);
        calendar1.add(Calendar.DAY_OF_YEAR, -days);
        Date newDate1 = calendar1.getTime();
        String fromdate = sdf.format(newDate1);
        db_noti_list = new ArrayList<>();
        db_noti_list.clear();
        Log.d("from_end_date", fromdate + "," + enddate);
        db_noti_list = db_gad.getDateRecords(fromdate, enddate);
        Log.d("notilist_size_db", "" + db_noti_list.size());

        notiArrayList.clear();
        try {
            for (int i = 0; i < db_noti_list.size(); i++) {
                if (strFiltertype.equals("All") && Float.valueOf(db_noti_list.get(i).getDistanceInKms()) < notification_radius) {
                    // add all records within selected radius
                    Notification notification = new Notification();
                    notification.setDate(db_noti_list.get(i).getDate());
                    notification.setTime(db_noti_list.get(i).getTime());
                    notification.setNtType(db_noti_list.get(i).getNtType());
                    notification.setTagType(db_noti_list.get(i).getTagType());
                    notification.setGeopoint(db_noti_list.get(i).getGeopoint());
                    notification.setNeedName(db_noti_list.get(i).getNeedName());
                    notification.setTagid(db_noti_list.get(i).getTagid());
                    notification.setSeen(db_noti_list.get(i).getSeen());
                    notification.setDistanceInKms(db_noti_list.get(i).getDistanceInKms());
                    notiArrayList.add(notification);
                } else {
                    // add selected category matched records within selected radius
                    if (db_noti_list.get(i).getNeedName().equals(strFiltertype) && Float.valueOf(db_noti_list.get(i).getDistanceInKms()) < notification_radius) {
                        Notification notification = new Notification();
                        notification.setDate(db_noti_list.get(i).getDate());
                        notification.setTime(db_noti_list.get(i).getTime());
                        notification.setNtType(db_noti_list.get(i).getNtType());
                        notification.setTagType(db_noti_list.get(i).getTagType());
                        notification.setGeopoint(db_noti_list.get(i).getGeopoint());
                        notification.setNeedName(db_noti_list.get(i).getNeedName());
                        notification.setTagid(db_noti_list.get(i).getTagid());
                        notification.setSeen(db_noti_list.get(i).getSeen());
                        notification.setDistanceInKms(db_noti_list.get(i).getDistanceInKms());
                        notiArrayList.add(notification);
                    }
                }
            }
        } catch (Exception e) {

        }

        if (notiArrayList.size() <= 0) {
            recyclerView.setVisibility(View.GONE);
            layout_data_not_found.setVisibility(View.VISIBLE);
        } else {
            layout_data_not_found.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            notificationAdapter = new NotificationAdapter(notiArrayList, getContext());
            recyclerView.setAdapter(notificationAdapter);
        }
        mDialog.dismiss();
    }


    public void getCategory() {
        categories = new ArrayList<>();
        mDialog.setConfiguration(new ArcConfiguration(getContext()));
        mDialog.show();
        mDialog.setCancelable(false);
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        CategoryInterface service = retrofit.create(CategoryInterface.class);
        Call<CategoryType> call = service.sendData("");
        call.enqueue(new Callback<CategoryType>() {
            @Override
            public void onResponse(Response<CategoryType> response, Retrofit retrofit) {
                CategoryType categoryType = response.body();
                categories.clear();
                Needtype needtype = new Needtype();
                needtype.setNeedMappingID("0");
                needtype.setNeedName("All");
                needtype.setCharacterPath("");
                categories.add(needtype);
                if (categoryType.getNeedtype().size() > 0) {
                    for (int i = 0; i < categoryType.getNeedtype().size(); i++) {
                        Needtype needtype1 = new Needtype();
                        needtype1.setNeedMappingID(categoryType.getNeedtype().get(i).getNeedMappingID().toString());
                        needtype1.setNeedName(categoryType.getNeedtype().get(i).getNeedName());
                        needtype1.setCharacterPath(categoryType.getNeedtype().get(i).getCharacterPath());
                        categories.add(needtype1);
                    }
                }
                try {
                    final Dialog dialog = new Dialog(getContext());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(false);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setContentView(R.layout.category_dialog);
                    EditText edsearch = (EditText) dialog.findViewById(R.id.search_from_list);
                    edsearch.setVisibility(View.GONE);
                    ListView categorylist = (ListView) dialog.findViewById(R.id.category_list);
                    Button cancel = (Button) dialog.findViewById(R.id.category_cancel);
                    categorylist.setAdapter(new CategoriesAdaptor(categories, getContext()));
//                    setDynamicHeight(categorylist);
                    categorylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            ednotificationfiltercategory.setText(categories.get(i).getNeedName());
                            // strNeed_Name = edselectcategory.getText().toString();
                            strNeedmapping_ID = categories.get(i).getNeedMappingID();
                            dialog.dismiss();
                        }
                    });
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                    mDialog.dismiss();
                } catch (Exception e) {
//                    StringWriter writer = new StringWriter();
//                    e.printStackTrace(new PrintWriter(writer));
//                    Bugreport bg = new Bugreport();
//                    bg.sendbug(writer.toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                ToastPopUp.show(getContext(), getString(R.string.server_response_error));
                mDialog.dismiss();
            }
        });
    }

    //----------------------------------------filter popup
    private void filterNotificationDialog() {
        AlertDialog.Builder alertdialog = new AlertDialog.Builder(getActivity());
        LayoutInflater li = LayoutInflater.from(getActivity());
        View confirmDialog = li.inflate(R.layout.filter_notification_dialouge, null);
        searchnotification = (Button) confirmDialog.findViewById(R.id.searchnotification);

        txtfilterrmaxadius = confirmDialog.findViewById(R.id.txtfilterrmaxadius);
        txtfilterrmaxtime = confirmDialog.findViewById(R.id.txtfilterrmaxtime);
        notificationProgressbar_distance = confirmDialog.findViewById(R.id.notificationProgressbar_distance);
        notificationProgressbar_time = confirmDialog.findViewById(R.id.notificationProgressbar_time);
        ednotificationfiltercategory = confirmDialog.findViewById(R.id.ednotificationfiltercategory);

        //-------------Adding dialog box to the view of alert dialog
        alertdialog.setView(confirmDialog);
        alertdialog.setCancelable(true);

        //----------------Creating an alert dialog
        alertDialogreturn = alertdialog.create();
        //----------------Displaying the alert dialog
        alertDialogreturn.show();

        notificationProgressbar_distance.setMin(1);
        notificationProgressbar_distance.setProgress((int) notification_radius);
        txtfilterrmaxadius.setText(String.valueOf((int) notification_radius) + " km(s)");
        notificationProgressbar_distance.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                txtfilterrmaxadius.setText("" + value + " km(s)");
                notification_radius = value;
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

            }
        });

        notificationProgressbar_time.setMin(1);
        notificationProgressbar_time.setProgress(days);
        txtfilterrmaxtime.setText(days + " day(s)");
        notificationProgressbar_time.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                txtfilterrmaxtime.setText("" + value + " day(s)");
                days = value;
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

            }
        });

        ednotificationfiltercategory.setText(strFiltertype);
        ednotificationfiltercategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCategory();
            }
        });


        searchnotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogreturn.dismiss();
                try {
                    strFiltertype = ednotificationfiltercategory.getText().toString();
                    filterNotification();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void checkActiveDeed(final String tagid) {
        mDialog = new SimpleArcDialog(getContext());
        mDialog.setConfiguration(new ArcConfiguration(getContext()));
        mDialog.show();
        mDialog.setCancelable(false);
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        DeeddeletedInterface service = retrofit.create(DeeddeletedInterface.class);
        Call<DeeddeletedModel> call = service.fetchData(tagid);
        call.enqueue(new Callback<DeeddeletedModel>() {
            @Override
            public void onResponse(Response<DeeddeletedModel> response, Retrofit retrofit) {
                Log.d("response_tagactive", "" + response.body());
                try {
                    DeeddeletedModel groupResponseStatus = response.body();
                    int isblock = 0;
                    try {
                        isblock = groupResponseStatus.getIsBlocked();
                    } catch (Exception e) {
                        isblock = 0;
                    }
                    if (isblock == 1) {
                        mDialog.dismiss();
                        FacebookSdk.sdkInitialize(getContext());
                        Toast.makeText(getContext(), getContext().getResources().getString(R.string.block_toast), Toast.LENGTH_SHORT).show();
                        sharedPreferences.createUserCredentialSession(null, null, null);
                        LoginManager.getInstance().logOut();
                        /*Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                                new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(Status status) {
                                        //updateUI(false);
                                    }
                                });*/

                        sharedPreferences.set_notification_status("ON");
                        Intent loginintent = new Intent(getContext(), LoginActivity.class);
                        getContext().startActivity(loginintent);
                    } else {
                        DeeddeletedModel statusModel = response.body();
                        int strstatus = statusModel.getIsDeleted();
                        if (strstatus == 0) {
                            mDialog.dismiss();
                            // show tag details
                            Bundle bundle = new Bundle();
                            bundle.putString("str_tagid", tagid);
                            bundle.putString("tab", "notification");
                            NeedDetailsFrag fragInfo = new NeedDetailsFrag();
                            fragInfo.setArguments(bundle);
                            fragmgr.beginTransaction().replace(R.id.content_frame, fragInfo).commit();
                        } else {
                            mDialog.dismiss();
                            Toast.makeText(getContext(), "This deed does not exist anymore", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    Log.d("response_tagactive", "" + e.getMessage());
                    mDialog.dismiss();
//                    StringWriter writer = new StringWriter();
//                    e.printStackTrace(new PrintWriter(writer));
//                    Bugreport bg = new Bugreport();
//                    bg.sendbug(writer.toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("response_tagactive", "" + t.getMessage());
                mDialog.dismiss();
                ToastPopUp.show(getContext(), getContext().getString(R.string.server_response_error));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                    Bundle bundle = new Bundle();
                    int i = 3;
                    bundle.putString("tab", "tab1");
                    TaggedneedsFrag mainHomeFragment = new TaggedneedsFrag();
                    mainHomeFragment.setArguments(bundle);
                    FragmentTransaction fragmentTransaction =
                            getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, mainHomeFragment);
                    fragmentTransaction.commit();
                    return true;
                }
                return false;
            }
        });
    }



    @Override
    public void onRefresh() {
        if (!(Validation.isNetworkAvailable(getActivity()))) {
            swipeRefreshLayout.setRefreshing(false);
            ToastPopUp.show(getActivity(), getString(R.string.network_validation));
        } else {
            swipeRefreshLayout.setRefreshing(true);
            recyclerView.setAdapter(null);
            getNotifications();
        }
    }
}
