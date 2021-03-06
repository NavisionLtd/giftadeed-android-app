/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.ContactUs;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

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
import giftadeed.kshantechsoft.com.giftadeed.Needdetails.StatusModel;
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


/////////////////////////////////////////////////////////////////
//                                                             //
//      Contacting to admin                                    //
/////////////////////////////////////////////////////////////////

public class Contactus extends Fragment {
    static FragmentManager fragmgr;
    View rootview;
    TextView txtadminemail;
    EditText edContactUsMessage;
    Button btnContactUsMessage;
    SharedPrefManager sharedPrefManager;
    SimpleArcDialog mDialog;


    public static Contactus newInstance(int sectionNumber) {
        Contactus fragment = new Contactus();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_contactus, container, false);
        //---------------------------------------------------------------------------
        TaggedneedsActivity.fragname = Contactus.newInstance(0);
        TaggedneedsActivity.updateTitle(getResources().getString(R.string.contact_us_heading));
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
        txtadminemail = rootview.findViewById(R.id.txtadminemail);
        edContactUsMessage = rootview.findViewById(R.id.edContactUsMessage);
        btnContactUsMessage = rootview.findViewById(R.id.btnContactUsMessage);
        txtadminemail.setTypeface(new FontDetails(getActivity()).fontStandardForPage);
        edContactUsMessage.setTypeface(new FontDetails(getActivity()).fontStandardForPage);
        btnContactUsMessage.setTypeface(new FontDetails(getActivity()).fontStandardForPage);
        txtadminemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + "admin@navisionltd.com"));
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Contacting Gift-a-Deed");
                    // intent.putExtra(Intent.EXTRA_TEXT, "your_text");
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    //TODO smth
                }
            }
        });

        edContactUsMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edContactUsMessage.getText().toString().length() > 499) {
                    //Show toast here
                    Toast.makeText(getContext(), getString(R.string.length_msg), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnContactUsMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int len = edContactUsMessage.getText().toString().length();
                Log.d("count", String.valueOf(len));
                if (edContactUsMessage.getText().toString().trim().length() >= 1) {
                    contactUs();
                } else {
                    Toast.makeText(getContext(), "Please write something to send message", Toast.LENGTH_SHORT).show();
                }
            }
        });

        TaggedneedsActivity.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuGrid menuGrid = new MenuGrid();
                fragmgr.beginTransaction().replace(R.id.content_frame, menuGrid).commit();
            }
        });
        return rootview;
    }


//------------------------------sending data to server

    public void contactUs() {
        mDialog.setConfiguration(new ArcConfiguration(getContext()));
        mDialog.show();
        sharedPrefManager = new SharedPrefManager(getActivity());
        HashMap<String, String> user = sharedPrefManager.getUserDetails();
        String user_id = user.get(sharedPrefManager.USER_ID);
        String message = edContactUsMessage.getText().toString();
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MAIN_API_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();

        ContactusInterface servvice = retrofit.create(ContactusInterface.class);
        Call<StatusModel> call = servvice.fetchData(user_id, message);
        call.enqueue(new Callback<StatusModel>() {
            @Override
            public void onResponse(Response<StatusModel> response, Retrofit retrofit) {
                try {

                    StatusModel statusModel1 = response.body();
                    int isblock = 0;
                    try {
                        isblock = statusModel1.getIsBlocked();
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


                        StatusModel statusModel = response.body();
                        int strStatus = statusModel.getStatus();
                        if (strStatus == 1) {
                            mDialog.dismiss();
                            edContactUsMessage.setText("");
                            Toast.makeText(getContext(), "Message send successfully", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getContext(), "Message sending failed", Toast.LENGTH_SHORT).show();
                            mDialog.dismiss();
                        }
                    }
                } catch (Exception e) {
                    StringWriter writer = new StringWriter();
                    e.printStackTrace(new PrintWriter(writer));
                    Bugreport bg = new Bugreport();
                    bg.sendbug(writer.toString());
                    mDialog.dismiss();
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