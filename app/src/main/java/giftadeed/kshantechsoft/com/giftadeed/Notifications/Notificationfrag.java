package giftadeed.kshantechsoft.com.giftadeed.Notifications;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.squareup.okhttp.OkHttpClient;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import giftadeed.kshantechsoft.com.giftadeed.Bug.Bugreport;
import giftadeed.kshantechsoft.com.giftadeed.Filter.CategoriesAdaptor;
import giftadeed.kshantechsoft.com.giftadeed.Filter.CategoryPOJO;
import giftadeed.kshantechsoft.com.giftadeed.GridMenu.MenuGrid;
import giftadeed.kshantechsoft.com.giftadeed.Login.LoginActivity;
import giftadeed.kshantechsoft.com.giftadeed.Needdetails.DeeddeletedModel;
import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.Signup.CountryAdapter;
import giftadeed.kshantechsoft.com.giftadeed.Signup.SignupPOJO;
import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.CategoryInterface;
import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.CategoryType;
import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.GPSTracker;
import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.Needtype;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.NotificationCountInterface;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.NotificationCountModel;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsActivity;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsFrag;
import giftadeed.kshantechsoft.com.giftadeed.Utils.DBGAD;
import giftadeed.kshantechsoft.com.giftadeed.Utils.SessionManager;
import giftadeed.kshantechsoft.com.giftadeed.Utils.ToastPopUp;
import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

////////////////////////////////////////////////////////////////////
//     Shows list of notification of last seven days            //
/////////////////////////////////////////////////////////////////
public class Notificationfrag extends Fragment implements GoogleApiClient.OnConnectionFailedListener {
    LinearLayout layout_filternotification;
    Button btnfilternotification, searchnotification;
    String strUserId;
    static android.support.v4.app.FragmentManager fragmgr;
    View rootview;
    List<Notification> notilist;
    SessionManager sharedPreferences;
    LinearLayout layout_message, layout_message_two, layout_data_not_found;
    float notification_radius = 10.0f;
    int days = 7;
    List<Notification> notification_list;
    EditText ednotificationfiltercategory;
    DiscreteSeekBar notificationProgressbar_distance, notificationProgressbar_time;
    private ArrayList<Needtype> categories;
    SimpleArcDialog mDialog;
    String strNeedmapping_ID, strFiltertype = "All";
    HashMap<String, String> Notification_status_map;
    String Notification_status = "null";
    private AlertDialog alertDialogForgot, alertDialogreturn;
    TextView txtfilterrmaxadius, txtfilterrmaxtime;
    private GoogleApiClient mGoogleApiClient;

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
        sharedPreferences = new SessionManager(getContext());
        HashMap<String, String> user = sharedPreferences.getUserDetails();
        strUserId = user.get(sharedPreferences.USER_ID);
        try {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .enableAutoManage(getActivity() /* FragmentActivity */, this /* OnConnectionFailedListener */)
                    .addApi(Auth.GOOGLE_SIGN_IN_API)
                    .build();
        } catch (Exception e) {

        }

/////////////////// Commented by Bhakti
        //------------------------------------notification bell status
        /*Notification_status_map = sharedPreferences.get_notification_status();
        Notification_status = Notification_status_map.get(sharedPreferences.KEY_NOTIFICATION);
        try {
            if (Notification_status.equals("ON")) {
                TaggedneedsActivity.imgappbarsetting.setImageResource(R.drawable.notifyon);
            } else if (Notification_status.equals("OFF")) {
                TaggedneedsActivity.imgappbarsetting.setImageResource(R.drawable.notify);
            } else {
                TaggedneedsActivity.imgappbarsetting.setImageResource(R.drawable.notifyon);
            }
        } catch (Exception e) {
            StringWriter writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));
            Bugreport bg = new Bugreport();
            bg.sendbug(writer.toString());
        }

        //------------------------------notification bell click
        TaggedneedsActivity.imgappbarsetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sharedPreferenceManager = new SharedPreferenceManager(getContext());
                Notification_status_map = sharedPreferences.get_notification_status();
                Notification_status = Notification_status_map.get(sharedPreferences.KEY_NOTIFICATION);
                if (Notification_status.equals("")) {
                    sharedPreferences.set_notification_status("OFF");
                    TaggedneedsActivity.imgappbarsetting.setImageResource(R.drawable.notify);
                    // Toast.makeText(getContext(), "OFF", Toast.LENGTH_LONG).show();
                } else {
                    if (Notification_status.equals("ON")) {
                        sharedPreferences.set_notification_status("OFF");
                        Toast.makeText(getContext(), "OFF", Toast.LENGTH_SHORT).show();
                        TaggedneedsActivity.imgappbarsetting.setImageResource(R.drawable.notify);
                    } else if (Notification_status.equals("OFF")) {
                        sharedPreferences.set_notification_status("ON");
                        Toast.makeText(getContext(), "ON", Toast.LENGTH_SHORT).show();
                        TaggedneedsActivity.imgappbarsetting.setImageResource(R.drawable.notifyon);
                    } else {
                        sharedPreferences.set_notification_status("OFF");
                        // Toast.makeText(getContext(), "OFF", Toast.LENGTH_LONG).show();
                        TaggedneedsActivity.imgappbarsetting.setImageResource(R.drawable.notify);
                    }
                }
            }
        });*/
/////////////////// Commented by Bhakti


        init();
        mDialog = new SimpleArcDialog(getContext());
        TaggedneedsActivity.imgfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filternotificationlayout();
            }
        });
        getAllNotificationCount();

        /////////////////// Commented by Bhakti
        /*btnfilternotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filternotificationlayout();
            }
        });*/
        /////////////////// Commented by Bhakti
        return rootview;
    }

    public void init() {
        layout_filternotification = rootview.findViewById(R.id.layout_filternotification);
        btnfilternotification = rootview.findViewById(R.id.btnfilternotification);
    }

    public void getAllNotificationCount() {
        mDialog.setConfiguration(new ArcConfiguration(getContext()));
        mDialog.show();
        mDialog.setCancelable(false);
        notilist = new ArrayList<>();
        //final RowData rowData = new RowData();
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        AllNotificationInterface service = retrofit.create(AllNotificationInterface.class);
        Call<AllNotificationModel> call = service.fetchData(strUserId);
        call.enqueue(new Callback<AllNotificationModel>() {
            @Override
            public void onResponse(Response<AllNotificationModel> response, Retrofit retrofit) {
                try {

                    AllNotificationModel deedDetailsModel = response.body();
                    int isblock = 0;
                    try {
                        isblock = deedDetailsModel.getIsBlocked();
                    } catch (Exception e) {
                        isblock = 0;
                    }
                    if (isblock == 1) {
                        mDialog.dismiss();
                        FacebookSdk.sdkInitialize(getActivity());
                        Toast.makeText(getContext(), getResources().getString(R.string.block_toast), Toast.LENGTH_SHORT).show();
                        sharedPreferences.createUserCredentialSession(null, null, null);
                        LoginManager.getInstance().logOut();
                        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                                new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(Status status) {
                                        //updateUI(false);
                                    }
                                });
                        int i = new DBGAD(getContext()).delete_row_message();
                        sharedPreferences.set_notification_status("ON");

                        Intent loginintent = new Intent(getActivity(), LoginActivity.class);
                        loginintent.putExtra("message", "Charity");
                        startActivity(loginintent);
                    } else {
                        AllNotificationModel notificationCountModel = response.body();
                        notilist = notificationCountModel.getNotifications();
                        DBGAD db_gad = new DBGAD(getContext());
                        mDialog.dismiss();
                        for (int i = 0; i < notilist.size(); i++) {
                            db_gad.insert_msg(notilist.get(i).getDate(), notilist.get(i).getTime(), notilist.get(i).getNtType(), notilist.get(i).getTagType(), notilist.get(i).getGeopoint(), notilist.get(i).getNeedName(), notilist.get(i).getTagid());
                        }
                        db_gad.delete_messages();
                        try {
                            displaynotifications();
                        } catch (ParseException e) {
                            e.printStackTrace();
                            mDialog.dismiss();
                        }
                    }
                    //  String count = notificationCountModel.getNotifications();
                } catch (Exception e) {
                    mDialog.dismiss();
                    // Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
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

    public void displaynotifications() throws ParseException {
        // img_sync=(ImageView) rootview.findViewById(R.id.imgsync);
        layout_data_not_found = rootview.findViewById(R.id.layout_data_not_found);
        layout_message = (LinearLayout) rootview.findViewById(R.id.layout_back);
        //rview_message=(RecyclerView) rootview.findViewById(R.id.recycle_message);
        Cursor cursor_data_new = new DBGAD(getContext()).fetch_all_data();
        // String stralldata=cursor_data_new.getString(4);
        if (cursor_data_new != null && cursor_data_new.moveToFirst()) {
            //fetch_all_data
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String dd_str = sdf.format(date);
            NotificationAdapter adpter_notificationnew[] = new NotificationAdapter[7];

            for (int i = 0; i < 7; i++) {
//int j=0;
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.DAY_OF_YEAR, -i);
                Date newDate = calendar.getTime();
                String fromdate = sdf.format(newDate);
                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTime(date);
                calendar1.add(Calendar.DAY_OF_YEAR, -i + 1);
                Date newDate1 = calendar1.getTime();
                String enddate = sdf.format(newDate1);
                notification_list = new ArrayList<>();
                notification_list.clear();
                Cursor msg_data_new = new DBGAD(getContext()).fetch(fromdate, enddate);
                String value_sql = "";

                //value_sql=msg_data_new.getString(1);
           /* if (value_sql.equals("")){

            }else {*/
                if (msg_data_new.moveToFirst()) {
                    TextView tv = new TextView(getContext());
                    if (i == 0) {
                        tv.setText("Today");
                        tv.setBackgroundResource(R.drawable.textviewbackgroundpagehead);
                        tv.setTextColor(Color.parseColor("#ffffff"));
                        LinearLayout.LayoutParams textParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        textParam.gravity = Gravity.CENTER_HORIZONTAL;
                        textParam.setMargins(5, 20, 5, 20);
                        tv.setLayoutParams(textParam);
                        tv.setBackgroundResource(R.drawable.textviewbackgroundpagehead);
                        tv.setGravity(Gravity.CENTER);
                        tv.setTextSize(15);
                        tv.setPadding(50, 10, 50, 10);
                    } /*else if (i == 1) {
                    tv.setText("Yesterday");
                }*/ else {
                        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                        DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
                        String inputDateStr = fromdate;
                        Date date1 = inputFormat.parse(inputDateStr);
                        String outputDateStr = outputFormat.format(date1);
                        tv.setText(outputDateStr);
                        LinearLayout.LayoutParams textParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        textParam.gravity = Gravity.CENTER_HORIZONTAL;
                        textParam.setMargins(5, 20, 5, 20);
                        tv.setLayoutParams(textParam);
                        tv.setBackgroundResource(R.drawable.notification_datebackground);
                        tv.setGravity(Gravity.CENTER);
                        tv.setTextSize(15);
                        tv.setPadding(50, 10, 50, 10);
                    }
                    tv.setId(i + 5);
                    tv.setGravity(Gravity.CENTER);
                    tv.setPadding(50, 10, 50, 10);
                    layout_message.addView(tv);
                    RecyclerView res1 = new RecyclerView(getContext());
                    res1.setHasFixedSize(true);
                    res1.setId(i);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                    res1.setLayoutManager(layoutManager);
                    //  res1.setLayoutManager();

                    do {
                        Notification a1 = new Notification();
                        String geopoints = msg_data_new.getString(5);
                        double current_latitude = new GPSTracker(getContext()).getLatitude();
                        double current_longitude = new GPSTracker(getContext()).getLongitude();
                        Location myLocation = new Location("My Location");
                        myLocation.setLatitude(current_latitude);
                        myLocation.setLongitude(current_longitude);
                        String[] words = geopoints.split(",");
                        if (words.length > 1) {
                            Location tagLocation2 = new Location("tag Location");
                            tagLocation2.setLatitude(Double.parseDouble(words[0]));
                            tagLocation2.setLongitude(Double.parseDouble(words[1]));
                            float dist1 = myLocation.distanceTo(tagLocation2);

                            a1.setDate(msg_data_new.getString(1));
                            a1.setTime(msg_data_new.getString(2));
                            a1.setNtType(msg_data_new.getString(3));
                            a1.setTagType(msg_data_new.getString(4));
                            a1.setGeopoint(msg_data_new.getString(5));
                            a1.setNeedName(msg_data_new.getString(6));
                            a1.setTagid(msg_data_new.getString(7));

                            Log.d("date", msg_data_new.getString(1));
                            Log.d("time", msg_data_new.getString(2));
                            Log.d("nttype", msg_data_new.getString(3));
                            Log.d("mapingid", msg_data_new.getString(4));
                            System.out.print(msg_data_new.getString(1));
                            System.out.print(msg_data_new.getString(2));
                            System.out.print(msg_data_new.getString(3));
                            System.out.print(msg_data_new.getString(4));
                            System.out.print(msg_data_new.getString(5));
                            if (dist1 < notification_radius) {
                                notification_list.add(a1);
                            }
                        }
                    } while ((msg_data_new.moveToNext()));
                    System.out.print(notification_list.size());
                    adpter_notificationnew[i] = new NotificationAdapter(notification_list, getContext());
                    adpter_notificationnew[i].notifyDataSetChanged();
                    res1.setAdapter(adpter_notificationnew[i]);
                    //  mDialog.dismiss();
                    layout_message.addView(res1);
                } else {

                }
                //}
                //new DBGAD(getContext()).exportDB();
            }
        } else {
            layout_message.setVisibility(View.GONE);
            layout_data_not_found.setVisibility(View.VISIBLE);
        }
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
                Needtype signupPOJO1 = new Needtype();
                signupPOJO1.setNeedMappingID("0");
                signupPOJO1.setType("All");
                signupPOJO1.setCharacterPath("");
                categories.add(signupPOJO1);
                if (categoryType.getNeedtype().size() > 0) {
                    for (int i = 0; i < categoryType.getNeedtype().size(); i++) {
                        Needtype signupPOJO = new Needtype();
                        signupPOJO.setNeedMappingID(categoryType.getNeedtype().get(i).getNeedMappingID().toString());
                        signupPOJO.setType(categoryType.getNeedtype().get(i).getNeedName().toString());
                        signupPOJO.setCharacterPath(categoryType.getNeedtype().get(i).getCharacterPath());
                        // signupPOJO.setCharacterpath(categoryType.getNeedtype().get(i).getCharacterPath());
                        // signupPOJO.setPhotoPath(categoryType.getNeedtype().get(i).getIconPath());
                        categories.add(signupPOJO);
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

    public void filterNotification() throws ParseException {
        mDialog.show();
        layout_message = (LinearLayout) rootview.findViewById(R.id.layout_back);
        //rview_message=(RecyclerView) rootview.findViewById(R.id.recycle_message);
        layout_message_two = rootview.findViewById(R.id.layout_back_two);
        layout_message.setVisibility(View.GONE);
        layout_message_two.setVisibility(View.VISIBLE);
        if (((LinearLayout) layout_message_two).getChildCount() > 0)
            ((LinearLayout) layout_message_two).removeAllViews();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dd_str = sdf.format(date);
        NotificationAdapter adpter_notificationnew[] = new NotificationAdapter[7];
        for (int i = 0; i < days; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_YEAR, -i);
            Date newDate = calendar.getTime();
            String fromdate = sdf.format(newDate);
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(date);
            calendar1.add(Calendar.DAY_OF_YEAR, -i + 1);
            Date newDate1 = calendar1.getTime();
            String enddate = sdf.format(newDate1);
            notification_list = new ArrayList<>();
            notification_list.clear();
            Cursor msg_data_new = new DBGAD(getContext()).fetch(fromdate, enddate);
            if (msg_data_new.moveToFirst()) {
                TextView tv = new TextView(getContext());
                RecyclerView res1 = new RecyclerView(getContext());
                res1.setHasFixedSize(true);
                res1.setId(i);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                res1.setLayoutManager(layoutManager);
                TextView txtnot_found = new TextView(getContext());
                do {
                    Notification a1 = new Notification();
                    String geopoints = msg_data_new.getString(5);
                    double current_latitude = new GPSTracker(getContext()).getLatitude();
                    double current_longitude = new GPSTracker(getContext()).getLongitude();
                    Location myLocation = new Location("My Location");
                    myLocation.setLatitude(current_latitude);
                    myLocation.setLongitude(current_longitude);

                    String[] words = geopoints.split(",");
                    if (words.length > 1) {
                        Location tagLocation2 = new Location("tag Location");
                        tagLocation2.setLatitude(Double.parseDouble(words[0]));
                        tagLocation2.setLongitude(Double.parseDouble(words[1]));
                        float dist1 = myLocation.distanceTo(tagLocation2);

                        a1.setDate(msg_data_new.getString(1));
                        a1.setTime(msg_data_new.getString(2));
                        a1.setNtType(msg_data_new.getString(3));
                        a1.setTagType(msg_data_new.getString(4));
                        a1.setGeopoint(msg_data_new.getString(5));
                        a1.setNeedName(msg_data_new.getString(6));
                        a1.setTagid(msg_data_new.getString(7));

                        Log.d("date", msg_data_new.getString(1));
                        Log.d("time", msg_data_new.getString(2));
                        Log.d("nttype", msg_data_new.getString(3));
                        Log.d("mapingid", msg_data_new.getString(4));
                        System.out.print(msg_data_new.getString(1));
                        System.out.print(msg_data_new.getString(2));
                        System.out.print(msg_data_new.getString(3));
                        System.out.print(msg_data_new.getString(4));
                        System.out.print(msg_data_new.getString(5));
                        if (dist1 < notification_radius) {
                            if (strFiltertype.equals(msg_data_new.getString(6))) {
                                notification_list.add(a1);
                            } else if (strFiltertype.equals("All")) {
                                notification_list.add(a1);
                            }
                        }
                        System.out.print(msg_data_new.getString(7));
                    }
                } while ((msg_data_new.moveToNext()));
                System.out.print("Notification size: " + notification_list.size());
                Log.d("Notification size:", String.valueOf(notification_list.size()));
                if (notification_list.size() > 0) {
                    if (i == 0) {
                        tv.setText("Today");
                        tv.setBackgroundResource(R.drawable.textviewbackgroundpagehead);
                        tv.setTextColor(Color.parseColor("#ffffff"));
                        LinearLayout.LayoutParams textParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        textParam.gravity = Gravity.CENTER_HORIZONTAL;
                        textParam.setMargins(5, 20, 5, 20);
                        tv.setLayoutParams(textParam);
                        tv.setBackgroundResource(R.drawable.textviewbackgroundpagehead);
                        tv.setGravity(Gravity.CENTER);
                        tv.setTextSize(15);
                        tv.setPadding(50, 10, 50, 10);
                    } /*else if (i == 1) {
                    tv.setText("Yesterday");
                }*/ else {
                        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                        DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
                        String inputDateStr = fromdate;
                        Date date1 = inputFormat.parse(inputDateStr);
                        String outputDateStr = outputFormat.format(date1);

                        tv.setText(outputDateStr);
                        LinearLayout.LayoutParams textParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        textParam.gravity = Gravity.CENTER_HORIZONTAL;
                        textParam.setMargins(5, 10, 5, 10);
                        tv.setLayoutParams(textParam);
                        tv.setBackgroundResource(R.drawable.notification_datebackground);
                        tv.setGravity(Gravity.CENTER);
                        tv.setTextSize(15);
                        tv.setPadding(50, 10, 50, 10);

                    }
                    tv.setId(i + 5);
                    tv.setGravity(Gravity.CENTER);
                    tv.setPadding(30, 10, 30, 10);

                    layout_message_two.addView(tv);
                    adpter_notificationnew[i] = new NotificationAdapter(notification_list, getContext());
                    adpter_notificationnew[i].notifyDataSetChanged();
                    res1.setAdapter(adpter_notificationnew[i]);
                    layout_message_two.addView(res1);
                }/*else{
                    txtnot_found.setText("No notification found");
                    layout_message_two.addView(res1);
                }*/
                mDialog.dismiss();
            }
        }
    }

    //----------------------------------------filter popup
    private void filternotificationlayout() {
        AlertDialog.Builder alertdialog = new AlertDialog.Builder(getActivity());
        LayoutInflater li = LayoutInflater.from(getActivity());
        View confirmDialog = li.inflate(R.layout.filter_notification_dialouge, null);
        searchnotification = (Button) confirmDialog.findViewById(R.id.searchnotification);

        txtfilterrmaxadius = confirmDialog.findViewById(R.id.txtfilterrmaxadius);
        txtfilterrmaxtime = confirmDialog.findViewById(R.id.txtfilterrmaxtime);
        notificationProgressbar_distance = confirmDialog.findViewById(R.id.notificationProgressbar_distance);
        notificationProgressbar_time = confirmDialog.findViewById(R.id.notificationProgressbar_time);
        ednotificationfiltercategory = confirmDialog.findViewById(R.id.ednotificationfiltercategory);
        //ednotificationfiltercategory.setText(strFiltertype);

        //-------------Adding dialog box to the view of alert dialog
        alertdialog.setView(confirmDialog);
        alertdialog.setCancelable(true);

        //----------------Creating an alert dialog
        alertDialogreturn = alertdialog.create();
        //alertDialogForgot.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        // alertDialogForgot.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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
        txtfilterrmaxtime.setText(String.valueOf(days) + " day(s)");
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
                } catch (ParseException e) {
                    e.printStackTrace();
                }
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
                    android.support.v4.app.FragmentTransaction fragmentTransaction =
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
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        mGoogleApiClient.connect();
    }
}
