/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.Dashboard;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.squareup.okhttp.OkHttpClient;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import giftadeed.kshantechsoft.com.giftadeed.Bug.Bugreport;
import giftadeed.kshantechsoft.com.giftadeed.GridMenu.MenuGrid;
import giftadeed.kshantechsoft.com.giftadeed.Login.LoginActivity;
import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsActivity;
import giftadeed.kshantechsoft.com.giftadeed.Utils.FontDetails;
import giftadeed.kshantechsoft.com.giftadeed.Utils.SharedPrefManager;
import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

////////////////////////////////////////////////////////////////////
//                                                               //
//     Shows details about users tagging details                //
/////////////////////////////////////////////////////////////////

public class Dashboard extends Fragment {
    static FragmentManager fragmgr;
    View rootview;
    TextView txtdashboard_date, txtnumberoftags, txtnumberoffulfilments, txtsuccessfulpercent, txtdeedsscore,
            txtlsatdeed, txttotaltags, txttotalfulfills, txttagpercent, txttotalscore;
    SharedPrefManager sharedPrefManager;
    SimpleArcDialog mDialog;


    public static Dashboard newInstance(int sectionNumber) {
        Dashboard fragment = new Dashboard();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.dashboard, container, false);
        fragmgr = getFragmentManager();
        TaggedneedsActivity.fragname = Dashboard.newInstance(0);
        TaggedneedsActivity.updateTitle(getResources().getString(R.string.dashboard_heading));
        TaggedneedsActivity.imgappbarcamera.setVisibility(View.GONE);
        TaggedneedsActivity.imgappbarsetting.setVisibility(View.GONE);
        TaggedneedsActivity.imgfilter.setVisibility(View.GONE);
        TaggedneedsActivity.imgShare.setVisibility(View.GONE);
        TaggedneedsActivity.editprofile.setVisibility(View.GONE);
        TaggedneedsActivity.saveprofile.setVisibility(View.GONE);
        TaggedneedsActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        TaggedneedsActivity.toggle.setDrawerIndicatorEnabled(false);
        TaggedneedsActivity.back.setVisibility(View.VISIBLE);
        TaggedneedsActivity.imgHamburger.setVisibility(View.GONE);
        fragmgr = getFragmentManager();
        init();
        mDialog = new SimpleArcDialog(getContext());
        getdashboard();
        TaggedneedsActivity.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuGrid menuGrid = new MenuGrid();
                fragmgr.beginTransaction().replace(R.id.content_frame, menuGrid).commit();
            }
        });
        return rootview;
    }


    //----------------geting dashboard detail from api
    public void getdashboard() {
        mDialog.setConfiguration(new ArcConfiguration(getContext()));
        mDialog.show();
        sharedPrefManager = new SharedPrefManager(getActivity());
        HashMap<String, String> user = sharedPrefManager.getUserDetails();
        String user_id = user.get(sharedPrefManager.USER_ID);
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();

        DashboardInterface servvice = retrofit.create(DashboardInterface.class);
        Call<DashboardModel> call = servvice.fetchData(user_id);
        call.enqueue(new Callback<DashboardModel>() {
            @Override
            public void onResponse(Response<DashboardModel> response, Retrofit retrofit) {
                try {
                    mDialog.dismiss();

                    DashboardModel deedDetailsModel = response.body();
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
                        sharedPrefManager.createUserCredentialSession(null, null, null);
                        LoginManager.getInstance().logOut();
                        /*Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                                new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(Status status) {
                                        //updateUI(false);
                                    }
                                });*/

                        sharedPrefManager.set_notification_status("ON");
                        Intent loginintent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(loginintent);
                    } else {
                        DashboardModel dashboardModel = response.body();
                        String dateString = dashboardModel.getLastDeedDate();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                        // use SimpleDateFormat to define how to PARSE the INPUT
                        Date date = null;
                        try {
                            date = sdf.parse(dateString);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        sdf = new SimpleDateFormat("dd-MMM-yyyy");
                        System.out.println(sdf.format(date));
                        txtdashboard_date.setText(sdf.format(date));

                        //txtdashboard_date.setText();
                        txtnumberoftags.setText(dashboardModel.getTotTags());
                        txtnumberoffulfilments.setText(dashboardModel.getTotFulfills());
                        txtsuccessfulpercent.setText(String.valueOf(dashboardModel.getTagSuccessPercent()) + "%");
                        txtdeedsscore.setText(dashboardModel.getTotPoints());
                    }
                } catch (Exception e) {
                    mDialog.dismiss();
                    StringWriter writer = new StringWriter();
                    e.printStackTrace(new PrintWriter(writer));
                    Bugreport bg = new Bugreport();
                    bg.sendbug(writer.toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getContext(), getResources().getString(R.string.server_response_error), Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            }
        });
    }


    public void init() {
        txtdashboard_date = rootview.findViewById(R.id.txtdashboard_date);
        txtnumberoftags = rootview.findViewById(R.id.txtnumberoftags);
        txtnumberoffulfilments = rootview.findViewById(R.id.txtnumberoffulfilments);
        txtsuccessfulpercent = rootview.findViewById(R.id.txtsuccessfulpercent);
        txtdeedsscore = rootview.findViewById(R.id.txtdeedsscore);
        txtlsatdeed = rootview.findViewById(R.id.txtlsatdeed);
        txttotaltags = rootview.findViewById(R.id.txttotaltags);
        txttotalfulfills = rootview.findViewById(R.id.txttotalfulfills);
        txttagpercent = rootview.findViewById(R.id.txttagpercent);
        txttotalscore = rootview.findViewById(R.id.txttotalscore);
        txtdashboard_date.setTypeface(new FontDetails(getActivity()).fontStandardForPage);
        txtnumberoftags.setTypeface(new FontDetails(getActivity()).fontStandardForPage);
        txtnumberoffulfilments.setTypeface(new FontDetails(getActivity()).fontStandardForPage);
        txtsuccessfulpercent.setTypeface(new FontDetails(getActivity()).fontStandardForPage);
        txtdeedsscore.setTypeface(new FontDetails(getActivity()).fontStandardForPage);
        txtlsatdeed.setTypeface(new FontDetails(getActivity()).fontStandardForPage);
        txttotaltags.setTypeface(new FontDetails(getActivity()).fontStandardForPage);
        txttotalfulfills.setTypeface(new FontDetails(getActivity()).fontStandardForPage);
        txttagpercent.setTypeface(new FontDetails(getActivity()).fontStandardForPage);
        txttotalscore.setTypeface(new FontDetails(getActivity()).fontStandardForPage);
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
                    MenuGrid menuGrid = new MenuGrid();
                    fragmgr.beginTransaction().replace(R.id.content_frame, menuGrid).commit();
                    return true;
                }
                return false;
            }
        });
    }



}
