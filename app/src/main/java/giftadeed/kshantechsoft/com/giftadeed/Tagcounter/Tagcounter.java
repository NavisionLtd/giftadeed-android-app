/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.Tagcounter;

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
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import giftadeed.kshantechsoft.com.giftadeed.Bug.Bugreport;
import giftadeed.kshantechsoft.com.giftadeed.GridMenu.MenuGrid;
import giftadeed.kshantechsoft.com.giftadeed.Login.LoginActivity;
import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsActivity;
import giftadeed.kshantechsoft.com.giftadeed.Utils.SharedPrefManager;
import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

////////////////////////////////////////////////////////////////////
//                                                               //
//     Shows tag counts done for that day                       //
/////////////////////////////////////////////////////////////////
public class Tagcounter extends Fragment {

    static FragmentManager fragmgr;
    View rootview;
    SharedPrefManager sharedPrefManager;
    SimpleArcDialog mDialog;
    TextView txttotaltaggeddeede,txttotalfulfilleddeede;

    public static Tagcounter newInstance(int sectionNumber) {
        Tagcounter fragment = new Tagcounter();

        return fragment;
    }





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootview=inflater.inflate(R.layout.fragment_tagcounter, container, false);
        fragmgr = getFragmentManager();
        TaggedneedsActivity.fragname= Tagcounter.newInstance(0);
        //TaggedneedsActivity.fragname= Dashboard.newInstance(0);
        TaggedneedsActivity.updateTitle(getResources().getString(R.string.tag_counter_heading));
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
        mDialog = new SimpleArcDialog(getContext());
        txttotalfulfilleddeede=rootview.findViewById(R.id.txttotalfulfilleddeede);
        txttotaltaggeddeede=rootview.findViewById(R.id.txttotaltaggeddeede);
        getCounter();
        TaggedneedsActivity.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuGrid menuGrid = new MenuGrid();
                fragmgr.beginTransaction().replace(R.id.content_frame, menuGrid).commit();
            }
        });
        return rootview;
    }
//------------------Get tag ocointer from server-----------------------------------------------------
    public void getCounter() {
        mDialog.setConfiguration(new ArcConfiguration(getContext()));
        mDialog.show();
        mDialog.setCancelable(false);
        sharedPrefManager = new SharedPrefManager(getActivity());
        HashMap<String, String> user = sharedPrefManager.getUserDetails();
        String user_id = user.get(sharedPrefManager.USER_ID);

        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MAIN_API_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();

        TagCounterInterface servvice = retrofit.create(TagCounterInterface.class);
        Call<TagCounterModel> call = servvice.fetchData(user_id);
        call.enqueue(new Callback<TagCounterModel>() {
            @Override
            public void onResponse(Response<TagCounterModel> response, Retrofit retrofit) {
                try {
                    TagCounterModel deedDetailsModel = response.body();
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
                        sharedPrefManager.createUserCredentialSession(null, null,null);
                        LoginManager.getInstance().logOut();
                        sharedPrefManager.set_notification_status("ON");
                        Intent loginintent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(loginintent);
                    } else {


                        TagCounterModel statusModel = response.body();
                        mDialog.dismiss();
                        txttotaltaggeddeede.setText(statusModel.getTagged());
                        txttotalfulfilleddeede.setText(statusModel.getFulfilled());
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
