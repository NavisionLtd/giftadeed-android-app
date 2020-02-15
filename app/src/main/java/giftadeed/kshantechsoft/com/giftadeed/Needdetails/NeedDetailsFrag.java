/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.Needdetails;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import giftadeed.kshantechsoft.com.giftadeed.Bug.Bugreport;
import giftadeed.kshantechsoft.com.giftadeed.Group.GroupDetailsFragment;
import giftadeed.kshantechsoft.com.giftadeed.Login.LoginActivity;
import giftadeed.kshantechsoft.com.giftadeed.Notifications.Notificationfrag;
import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.GPSTracker;
import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.TagaNeed;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsFrag;
import giftadeed.kshantechsoft.com.giftadeed.Utils.SharedPrefManager;
import giftadeed.kshantechsoft.com.giftadeed.Utils.ToastPopUp;
import giftadeed.kshantechsoft.com.giftadeed.Utils.Validation;
import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import giftadeed.kshantechsoft.com.giftadeed.giftaneed.GiftANeedFrag;
import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsActivity;
import giftadeed.kshantechsoft.com.giftadeed.Utils.FontDetails;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.Picasso;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

//////////////////////////////////////////////////////////////////////////
//                                                                      //
//  Shows details of deed and also can comment,endorse,report,edit deed//
////////////////////////////////////////////////////////////////////////

public class NeedDetailsFrag extends Fragment implements Animation.AnimationListener {
    FragmentActivity myContext;
    View rootview;
    ListView listviewcomments;
    LinearLayout locationTypeLayout, subTypeLayout, fromGroupLayout, lastEndorseLayout;
    CircleImageView imgcharacter;
    LinearLayout details_locationicon;
    String strImagepath = "";
    ImageView img, imgback, img_endorse, img_endorse_over;
    TextView txtheading, txtsubheading, txtaddress, txtdistance, txtDate, txt_name, txt_des, txtneeddetailsendorse, txtFromGroupName,
            txtLastEndorse, txtneeddetailsview, txtcontaineravilable, txtcontaineravilableyes_no, dialogtext, tvLocationType, txtPermanent, txtSubtypes;
    WebView detailsofgieft;
    Button btnCamera, btnGallery, btnPost, dialogbtnconfirm, dialogbtncancel, btnOk;
    private AlertDialog alertDialogForgot, alertDialogreturn;
    Button giftnow, btnReportUser, btnReportDeed;
    EditText edComment;
    ScrollView detailspagemainscroll;
    int str_isreported, is_endorse, requiredDistEndorse;
    String str_address, str_tagid, str_geopoint, str_taggedPhotoPath, str_description, str_catType, str_iconPath, str_characterPath, str_fname, str_lname,
            str_privacy, str_userID, str_needName, str_totalTaggedCreditPoints, str_totalFulfilledCreditPoints, str_title, str_date,
            str_subtypes, str_permanent, str_distance, tab, str_container, str_Views, str_endorse, str_validity, str_mappingId, strdeedowner_id, strGroup_id="", strGroup_name,
            strLastEndorseTime;
    static FragmentManager fragmgr;
    LinearLayout layout_editdeed, layout_endorsedeed, layout_viewdeed, detailspage_containerlayout, comments_layout;
    SharedPrefManager sharedPrefManager;
    List<Comment> commentslist;
    CommentAdapter commentadapter;
    SimpleArcDialog mDialog;

    Button img_view_send_comment;
    Animation animFadein;
    ImageView imageView;
    MediaPlayer mp;

    public static NeedDetailsFrag newInstance(int sectionNumber) {
        NeedDetailsFrag fragment = new NeedDetailsFrag();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TaggedneedsActivity.updateTitle(getResources().getString(R.string.deed_details_heading));
        rootview = inflater.inflate(R.layout.fragment_need_details, container, false);
        TaggedneedsActivity.toggle.setDrawerIndicatorEnabled(false);
        TaggedneedsActivity.back.setVisibility(View.VISIBLE);
        TaggedneedsActivity.imgappbarcamera.setVisibility(View.GONE);
        TaggedneedsActivity.imgappbarsetting.setVisibility(View.GONE);
        TaggedneedsActivity.imgfilter.setVisibility(View.GONE);
        TaggedneedsActivity.editprofile.setVisibility(View.GONE);
        TaggedneedsActivity.saveprofile.setVisibility(View.GONE);
        TaggedneedsActivity.imgHamburger.setVisibility(View.GONE);
        TaggedneedsActivity.imgShare.setVisibility(View.VISIBLE);
        TaggedneedsActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        // getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mDialog = new SimpleArcDialog(getContext());
        fragmgr = getFragmentManager();
        init();
        img.setImageResource(R.drawable.imagedefault);
        img.setScaleType(ImageView.ScaleType.FIT_CENTER);
        //detailsofgieft.loadDataWithBaseURL(null, getString(R.string.faq), "text/html", "utf-8", "");
        //-------------getting data
        sharedPrefManager = new SharedPrefManager(getActivity());
        HashMap<String, String> user = sharedPrefManager.getUserDetails();
        str_userID = user.get(sharedPrefManager.USER_ID);
        str_tagid = this.getArguments().getString("str_tagid");
        tab = this.getArguments().getString("tab");
        getDeed_Details();
        giftnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(Validation.isNetworkAvailable(myContext))) {
                    Toast.makeText(myContext, getString(R.string.network_validation), Toast.LENGTH_SHORT).show();
                } else {
                    isDeedDeleted("giftnow");

                }
            }
        });

        edComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 139) {
                    Toast.makeText(getContext(), getString(R.string.length_msg_2), Toast.LENGTH_SHORT).show();
                }
            }
        });

        TaggedneedsActivity.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tab.equals("group")) {
                    GroupDetailsFragment groupDetailsFragment = new GroupDetailsFragment();
                    fragmgr.beginTransaction().replace(R.id.content_frame, groupDetailsFragment).commit();
                } else if (tab.equals("notification")) {
                    Notificationfrag notificationfrag = new Notificationfrag();
                    fragmgr.beginTransaction().replace(R.id.content_frame, notificationfrag).commit();
                } else {
                    Bundle bundle = new Bundle();
                    int i = 3;
                    bundle.putString("tab", tab);
                    TaggedneedsFrag mainHomeFragment = new TaggedneedsFrag();
                    mainHomeFragment.setArguments(bundle);
                    fragmgr.beginTransaction().replace(R.id.content_frame, mainHomeFragment).commit();
                }
            }
        });

        TaggedneedsActivity.imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String android_shortlink = "http://tiny.cc/kwb33y";
                String ios_shortlink = "http://tiny.cc/h4533y";
                String website = "https://www.giftadeed.com/";
                String location = "http://maps.google.com/maps?saddr=" + str_geopoint;
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, "Hey! Someone needs your help to fulfil a deed. \n" +
                        str_needName + " is needed here " + location + "\n\n" +
                        "For more details download GiftADeed App: \n" +
                        "Android : " + android_shortlink + "\n" +
                        "iOS : " + ios_shortlink + "\n\n" +
                        "Also, check the website at " + website);
                startActivity(Intent.createChooser(share, "Share deed details on:"));
            }
        });

        layout_editdeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isDeedDeleted("edit");
            }
        });

        layout_endorsedeed.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (!(Validation.isNetworkAvailable(getContext()))) {
                    Toast.makeText(myContext, getString(R.string.network_validation), Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        double current_latitude = new GPSTracker(myContext).getLatitude();
                        // ----------------------Getting longitude of the current location-------------
                        double current_longitude = new GPSTracker(myContext).getLongitude();
                        Location myLocation = new Location("My Location");
                        myLocation.setLatitude(current_latitude);
                        myLocation.setLongitude(current_longitude);
                        String str_geo_point = str_geopoint;
                        //String str_geo_point = str_geopoint_new;
                        String[] words = str_geo_point.split(",");
                        if (words.length > 1) {
                            Location tagLocation2 = new Location("tag Location");
                            tagLocation2.setLatitude(Double.parseDouble(words[0]));
                            tagLocation2.setLongitude(Double.parseDouble(words[1]));

//--------------------------distance in km----------------------------------------------------------
                            float dist1 = myLocation.distanceTo(tagLocation2) / 1000;
//---------------------------distance in feet-------------------------------------------------------
                            float ft_distance = dist1 * 3280.8f;
                            if (is_endorse == 1) {
                                Toast.makeText(getContext(), "You have already endorsed", Toast.LENGTH_SHORT).show();
                            } else {
                                if (ft_distance > requiredDistEndorse) {
                                    Toast.makeText(getContext(), "You need to be within " + requiredDistEndorse + " feet area of the needy person", Toast.LENGTH_SHORT).show();
                                } else {
                                    isDeedDeleted("endorse");
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        edComment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if ((keyEvent != null && (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    //do what you want on the press of 'done'
                    //submit.performClick();
                    if (edComment.getText().toString().trim().length() >= 1) {
                        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                        isDeedDeleted("comment");

                    } else {
                        Toast.makeText(getContext(), "Please write comment to post", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
                return false;
            }
        });
//-------------------------give comment to deed
        img_view_send_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edComment.getText().toString().trim().length() >= 1) {
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                    isDeedDeleted("comment");

                } else {
                    Toast.makeText(getContext(), "Please write comment to post", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnReportUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (str_userID.equals(strdeedowner_id)) {
                    Toast.makeText(getContext(), "You cannot report yourself", Toast.LENGTH_SHORT).show();
                } else {

                    if (str_isreported == 1) {
                        Toast.makeText(getContext(), "You have already reported this user for this deed", Toast.LENGTH_SHORT).show();
                    } else {
                        isDeedDeleted("user");

                    }
                }
            }
        });
        btnReportDeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (str_userID.equals(strdeedowner_id)) {
                    Toast.makeText(getContext(), "You cannot report your own deed", Toast.LENGTH_SHORT).show();
                } else {
                    isDeedDeleted("deed");

                    //reportDeed();
                }
            }
        });

        details_locationicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isDeedDeleted("locationmap");

            }
        });
        return rootview;
    }

    public void getDeed_Details() {
        mDialog.setConfiguration(new ArcConfiguration(getContext()));
        mDialog.show();
        mDialog.setCancelable(false);
        commentslist = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        DeedDetailsInterface service = retrofit.create(DeedDetailsInterface.class);
        Call<DeedDetailsModel> call = service.fetchData(str_userID, str_tagid);
        Log.d("input_deeddetails", "" + str_userID + " : " + str_tagid);
        call.enqueue(new Callback<DeedDetailsModel>() {
            @Override
            public void onResponse(Response<DeedDetailsModel> response, Retrofit retrofit) {
                try {
                    DeedDetailsModel deedDetailsModel = response.body();
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
                        sharedPrefManager.set_notification_status("ON");
                        Intent loginintent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(loginintent);
                    } else {
                        // dist = 100;
                        //-------------change for doing endorse distace dynamically-----------------------
                        try {
                            requiredDistEndorse = Integer.parseInt(deedDetailsModel.getDeedDetails().get(0).getEndorseDist().toString());
                        } catch (Exception e) {

                        }
                        str_description = deedDetailsModel.getDeedDetails().get(0).getDesc();
                        str_fname = deedDetailsModel.getDeedDetails().get(0).getFName();
                        str_lname = deedDetailsModel.getDeedDetails().get(0).getLName();
                        str_privacy = deedDetailsModel.getDeedDetails().get(0).getPrivacy();
                        str_permanent = deedDetailsModel.getDeedDetails().get(0).getPermanent();
                        str_subtypes = deedDetailsModel.getDeedDetails().get(0).getSubtypes();
                        str_catType = deedDetailsModel.getDeedDetails().get(0).getCatType();
                        str_container = deedDetailsModel.getDeedDetails().get(0).getContainer();
                        str_Views = deedDetailsModel.getDeedDetails().get(0).getViews();
                        str_endorse = deedDetailsModel.getDeedDetails().get(0).getEndorse();
                        commentslist = deedDetailsModel.getDeedDetails().get(0).getComments();
                        str_validity = deedDetailsModel.getDeedDetails().get(0).getValidity();
                        is_endorse = deedDetailsModel.getDeedDetails().get(0).getIsEndorse();
                        str_address = deedDetailsModel.getDeedDetails().get(0).getAddress();
                        str_geopoint = deedDetailsModel.getDeedDetails().get(0).getGeoPts();
                        str_taggedPhotoPath = deedDetailsModel.getDeedDetails().get(0).getImgUrl();
                        str_characterPath = deedDetailsModel.getDeedDetails().get(0).getCharacterPath();
                        str_iconPath = deedDetailsModel.getDeedDetails().get(0).getIconPath();
                        str_needName = deedDetailsModel.getDeedDetails().get(0).getTagName();
                        str_title = deedDetailsModel.getDeedDetails().get(0).getTagName();
                        str_date = deedDetailsModel.getDeedDetails().get(0).getDate();
                        str_mappingId = deedDetailsModel.getDeedDetails().get(0).getNeedMapId();
                        str_isreported = deedDetailsModel.getDeedDetails().get(0).getIsReported();
                        strdeedowner_id = deedDetailsModel.getDeedDetails().get(0).getOwnerId();
                        strGroup_name = deedDetailsModel.getDeedDetails().get(0).getGroupName();
                        strGroup_id = deedDetailsModel.getDeedDetails().get(0).getGroupID();
                        strLastEndorseTime = deedDetailsModel.getDeedDetails().get(0).getLastEndorseTime();
                        Log.d("imgpath", str_taggedPhotoPath);
                        double current_latitude = new GPSTracker(myContext).getLatitude();
                        double current_longitude = new GPSTracker(myContext).getLongitude();
                        Location myLocation = new Location("My Location");
                        myLocation.setLatitude(current_latitude);
                        myLocation.setLongitude(current_longitude);

                        String[] words = str_geopoint.split(",");
                        if (words.length > 1) {
                            Location tagLocation2 = new Location("tag Location");
                            tagLocation2.setLatitude(Double.parseDouble(words[0]));
                            tagLocation2.setLongitude(Double.parseDouble(words[1]));
                            double radi = sharedPrefManager.getradius();
                            DecimalFormat df2 = new DecimalFormat("#.##");
                            double dist1 = myLocation.distanceTo(tagLocation2) / 1000;
                            str_distance = String.valueOf(dist1);

                            if (str_mappingId.equals("1") || str_mappingId.equals("21")) {
                                detailspage_containerlayout.setVisibility(View.VISIBLE);
                                if (str_container.equals("1")) {
                                    txtcontaineravilableyes_no.setText("Available");
                                    txtcontaineravilableyes_no.setTextColor(getResources().getColor(R.color.colorPrimary));
                                } else {
                                    txtcontaineravilableyes_no.setText("Not available");
                                    txtcontaineravilableyes_no.setTextColor(getResources().getColor(R.color.grey));
                                }
                            } else {
                                detailspage_containerlayout.setVisibility(View.GONE);
                            }

                            /*if (is_endorse == 1) {
                                img_endorse.setVisibility(View.GONE);
                                img_endorse_over.setVisibility(View.VISIBLE);
                            } else {
                                img_endorse.setVisibility(View.VISIBLE);
                                img_endorse_over.setVisibility(View.GONE);
                            }*/
                            txtneeddetailsendorse.setText(str_endorse);
                            txtneeddetailsview.setText(str_Views);
                            txtsubheading.setText(str_needName);
                            if (str_permanent.equals("Y")) {
                                txtaddress.setText(getResources().getString(R.string.deed_location) + "\n" + str_address.trim() + " [Permanent Location]");
                            } else {
                                txtaddress.setText(getResources().getString(R.string.deed_location) + "\n" + str_address.trim());
                            }
                            txtdistance.setText(String.format("%.2f", Double.parseDouble(str_distance)) + " km(s) away");

                            List<String> elephantList = null;
                            StringBuffer edited = new StringBuffer();
                            if (str_subtypes.length() > 0) {
                                subTypeLayout.setVisibility(View.VISIBLE);
                                if (str_subtypes.contains(",")) {
                                    elephantList = Arrays.asList(str_subtypes.split(","));
                                    for (int i = 0; i < elephantList.size(); i++) {
                                        edited.append(elephantList.get(i).replaceAll(":", " for ").trim() + "\n");
                                    }
                                    txtSubtypes.setText(str_needName + " " + getResources().getString(R.string.pref_1) + "\n" + edited);
                                } else {
                                    String str = str_subtypes.replaceAll(":", " for ");
                                    txtSubtypes.setText(str_needName + " " + getResources().getString(R.string.pref_1) + "\n" + str);
                                }
                            } else {
                                subTypeLayout.setVisibility(View.GONE);
                            }

                            txt_des.setText(getResources().getString(R.string.description) + "\n" + str_description);
                            txt_des.setMovementMethod(new ScrollingMovementMethod());
                            if (commentslist.size() > 0) {
                                comments_layout.setVisibility(View.VISIBLE);
                                commentadapter = new CommentAdapter(getContext(), R.layout.commentcard, commentslist);
                                listviewcomments.setAdapter(commentadapter);
                            } else {
                                comments_layout.setVisibility(View.GONE);
                            }
                            if (!str_privacy.equals("Anonymous")) {
                                txt_name.setText(str_fname + " " + str_lname);
                            } else {
                                txt_name.setText("Anonymous");
                            }

                            if (strGroup_name != null) {
                                fromGroupLayout.setVisibility(View.VISIBLE);
                                txtFromGroupName.setText(strGroup_name);
                            } else {
                                fromGroupLayout.setVisibility(View.GONE);
                            }

                            if (strLastEndorseTime != null) {
                                lastEndorseLayout.setVisibility(View.VISIBLE);
                                txtLastEndorse.setText(strLastEndorseTime);
                            } else {
                                lastEndorseLayout.setVisibility(View.GONE);
                            }

                            try {
                                String strImagepath = WebServices.MAIN_SUB_URL + str_taggedPhotoPath;
                                System.out.print("strImagepath" + strImagepath);
                                // Picasso.with(myContext).load(strImagepath).into(img);

                                if (strImagepath.length() > 57) {
                                    Picasso.with(myContext).load(strImagepath).placeholder(R.drawable.imagedefault).into(img);
                                    img.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                } else {
                                    img.setImageResource(R.drawable.imagedefault);
                                    img.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                }
                            } catch (Exception e) {
                                mDialog.dismiss();
                                StringWriter writer = new StringWriter();
                                e.printStackTrace(new PrintWriter(writer));
                                Bugreport bg = new Bugreport();
                                bg.sendbug(writer.toString());
                            }

                            try {
                                mDialog.dismiss();
                                if (str_catType.equals("C")) {
                                    strImagepath = WebServices.CUSTOM_CATEGORY_IMAGE_URL + str_characterPath;
                                } else {
                                    strImagepath = WebServices.MAIN_SUB_URL + str_characterPath;
                                }
                                Log.d("strImagepath", strImagepath);
                                Picasso.with(myContext).load(strImagepath).into(imgcharacter);
                            } catch (Exception e) {
                                StringWriter writer = new StringWriter();
                                e.printStackTrace(new PrintWriter(writer));
                                Bugreport bg = new Bugreport();
                                bg.sendbug(writer.toString());
                            }

                            //---------------change date format
                            String dateString = str_date;
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                            // use SimpleDateFormat to define how to PARSE the INPUT
                            Date date = null;
                            try {
                                date = sdf.parse(dateString);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            sdf = new SimpleDateFormat("dd-MMM-yyyy");
                            System.out.println(sdf.format(date));
                            txtDate.setText(sdf.format(date));
                        }
                        mDialog.dismiss();
                    }
                } catch (Exception e) {
                    mDialog.dismiss();
                    // Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    StringWriter writer = new StringWriter();
                    e.printStackTrace(new PrintWriter(writer));
                    Bugreport bg = new Bugreport();
                    bg.sendbug(writer.toString());
                }

            }

            @Override
            public void onFailure(Throwable t) {
                mDialog.dismiss();
                ToastPopUp.show(getContext(), getString(R.string.server_response_error));
            }
        });
    }

    public void endorseDeed() {
        // mDialog.show();
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();

        EndorsedeedInterface servvice = retrofit.create(EndorsedeedInterface.class);
        Call<StatusModel> call = servvice.fetchData(str_userID, str_tagid);
        call.enqueue(new Callback<StatusModel>() {
            @Override
            public void onResponse(Response<StatusModel> response, Retrofit retrofit) {
                try {

                    StatusModel deedDetailsModel = response.body();
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
                        sharedPrefManager.set_notification_status("ON");
                        Intent loginintent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(loginintent);
                    } else {

                        StatusModel statusModel = response.body();
                        int strStatus = statusModel.getStatus();
                        if (strStatus == 1) {
                            mDialog.dismiss();
                            is_endorse = 1;
                            int endor = Integer.parseInt(str_endorse) + 1;
                            txtneeddetailsendorse.setText(String.valueOf(endor));
//                            img_endorse.setVisibility(View.GONE);
//                            img_endorse_over.setVisibility(View.VISIBLE);


//                            gifDialog();
                            endorseDialog();
                        } else {
                            Toast.makeText(getContext(), "Endorsement failed", Toast.LENGTH_SHORT).show();
                            mDialog.dismiss();
                        }
                    }
                } catch (Exception e) {
                    StringWriter writer = new StringWriter();
                    e.printStackTrace(new PrintWriter(writer));
                    Bugreport bg = new Bugreport();
                    bg.sendbug(writer.toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                ToastPopUp.show(getContext(), getString(R.string.server_response_error));
                mDialog.dismiss();
            }
        });
    }

    public void commentDeed() {
        // mDialog.show();
        sharedPrefManager = new SharedPrefManager(getActivity());
        HashMap<String, String> user = sharedPrefManager.getUserDetails();
        String user_id = user.get(sharedPrefManager.USER_ID);
        String name = user.get(sharedPrefManager.USER_NAME);
        final String privacy = user.get(sharedPrefManager.PRIVACY);
        final String fname = name.split(" ")[0];
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        CommentInterface service = retrofit.create(CommentInterface.class);
        Call<StatusModel> call = service.fetchData(user_id, str_tagid, edComment.getText().toString());
        call.enqueue(new Callback<StatusModel>() {
            @Override
            public void onResponse(Response<StatusModel> response, Retrofit retrofit) {
                try {

                    StatusModel deedDetailsModel = response.body();
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
                        sharedPrefManager.set_notification_status("ON");
                        Intent loginintent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(loginintent);
                    } else {
                        StatusModel statusModel = response.body();
                        int strstatus = statusModel.getStatus();
                        if (strstatus == 1) {
                            mDialog.dismiss();
                            Comment comment = new Comment();
                            comment.setFName(fname);
                            comment.setPrivacy(privacy);
                            comment.setComment(edComment.getText().toString());
                            commentslist.add(comment);
                            comments_layout.setVisibility(View.VISIBLE);
                            commentadapter = new CommentAdapter(getContext(), R.layout.commentcard, commentslist);
                            listviewcomments.setAdapter(commentadapter);
                            commentadapter.notifyDataSetChanged();
                            edComment.setText("");
                        } else {
                            mDialog.dismiss();
                            Toast.makeText(getContext(), "comment was unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    StringWriter writer = new StringWriter();
                    e.printStackTrace(new PrintWriter(writer));
                    Bugreport bg = new Bugreport();
                    bg.sendbug(writer.toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                ToastPopUp.show(getContext(), getString(R.string.server_response_error));
                mDialog.dismiss();
            }
        });
    }

    public void reportDeed() {
        mDialog.show();
        sharedPrefManager = new SharedPrefManager(getActivity());
        HashMap<String, String> user = sharedPrefManager.getUserDetails();
        String user_id = user.get(sharedPrefManager.USER_ID);
        String name = user.get(sharedPrefManager.USER_NAME);
        final String fname = name.split(" ")[0];
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        ReportDeedInterface service = retrofit.create(ReportDeedInterface.class);
        Call<StatusModel> call = service.fetchData(user_id, str_tagid);
        call.enqueue(new Callback<StatusModel>() {
            @Override
            public void onResponse(Response<StatusModel> response, Retrofit retrofit) {
                try {
                    StatusModel deedDetailsModel = response.body();
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
                        sharedPrefManager.set_notification_status("ON");
                        Intent loginintent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(loginintent);
                    } else {
                        StatusModel statusModel = response.body();
                        int strstatus = statusModel.getStatus();
                        if (strstatus == 1) {
                            mDialog.dismiss();
                            Bundle bundle = new Bundle();
                            int i = 3;
                            bundle.putString("tab", tab);
                            TaggedneedsFrag mainHomeFragment = new TaggedneedsFrag();
                            mainHomeFragment.setArguments(bundle);
                            FragmentTransaction fragmentTransaction =
                                    getActivity().getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.content_frame, mainHomeFragment);
                            fragmentTransaction.commit();
                        } else {
                            Toast.makeText(getContext(), getResources().getString(R.string.server_response_error), Toast.LENGTH_SHORT).show();
                            mDialog.dismiss();
                        }
                    }
                } catch (Exception e) {
                    StringWriter writer = new StringWriter();
                    e.printStackTrace(new PrintWriter(writer));
                    Bugreport bg = new Bugreport();
                    bg.sendbug(writer.toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                mDialog.dismiss();
                ToastPopUp.show(getContext(), getString(R.string.server_response_error));
            }
        });
    }

    public void reportUser() {
        mDialog.show();
        sharedPrefManager = new SharedPrefManager(getActivity());
        HashMap<String, String> user = sharedPrefManager.getUserDetails();
        String user_id = user.get(sharedPrefManager.USER_ID);
        String name = user.get(sharedPrefManager.USER_NAME);
        final String fname = name.split(" ")[0];
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        ReportUserInterface service = retrofit.create(ReportUserInterface.class);
        Call<StatusModel> call = service.fetchData(user_id, str_tagid, strdeedowner_id);
        call.enqueue(new Callback<StatusModel>() {
            @Override
            public void onResponse(Response<StatusModel> response, Retrofit retrofit) {
                try {

                    StatusModel deedDetailsModel = response.body();
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

                        StatusModel statusModel = response.body();
                        int strstatus = statusModel.getStatus();
                        if (strstatus == 1) {
                            mDialog.dismiss();
                            str_isreported = 1;
                   /* Bundle bundle = new Bundle();
                    int i = 3;
                    bundle.putString("tab", tab);
                    TaggedneedsFrag mainHomeFragment = new TaggedneedsFrag();
                    mainHomeFragment.setArguments(bundle);
                    android.support.v4.app.FragmentTransaction fragmentTransaction =
                            getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, mainHomeFragment);
                    fragmentTransaction.commit();*/
                            Toast.makeText(getContext(), getResources().getString(R.string.user_reported), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), getResources().getString(R.string.server_response_error), Toast.LENGTH_SHORT).show();
                            mDialog.dismiss();
                        }
                    }
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

    public void isDeedDeleted(final String fromwhere) {
        mDialog.show();
        sharedPrefManager = new SharedPrefManager(getActivity());
        HashMap<String, String> user = sharedPrefManager.getUserDetails();
        String user_id = user.get(sharedPrefManager.USER_ID);
        String name = user.get(sharedPrefManager.USER_NAME);
        final String fname = name.split(" ")[0];
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        DeeddeletedInterface service = retrofit.create(DeeddeletedInterface.class);
        Call<DeeddeletedModel> call = service.fetchData(str_tagid);
        call.enqueue(new Callback<DeeddeletedModel>() {
            @Override
            public void onResponse(Response<DeeddeletedModel> response, Retrofit retrofit) {
                try {

                    DeeddeletedModel deedDetailsModel = response.body();
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
                        DeeddeletedModel statusModel = response.body();
                        int strstatus = statusModel.getIsDeleted();
                        if (strstatus == 0) {
                            //mDialog.dismiss();
                            Log.d("deed_id", str_tagid);
                            if (fromwhere.equals("deed")) {
                                mDialog.dismiss();
                                submitdialog("deed");
                            } else if (fromwhere.equals("user")) {
                                mDialog.dismiss();
                                submitdialog("user");
                            } else if (fromwhere.equals("endorse")) {
                                if (!(Validation.isNetworkAvailable(getActivity()))) {
                                    ToastPopUp.show(getActivity(), getString(R.string.network_validation));
                                } else {
                                    endorseDeed();
                                }
                            } else if (fromwhere.equals("comment")) {
                                if (!(Validation.isNetworkAvailable(getActivity()))) {
                                    ToastPopUp.show(getActivity(), getString(R.string.network_validation));
                                } else {
                                    commentDeed();
                                }
                            } else if (fromwhere.equals("locationmap")) {
                                //sdsfdcommentDeed();
                                mDialog.dismiss();
                                Bundle bundle = new Bundle();
                                bundle.putString("str_tagid", str_tagid);
                                bundle.putString("str_geopoint", str_geopoint);
                                bundle.putString("str_characterPath", strImagepath);
                                bundle.putString("tab", "tab2");
                                SingleDeedMap fragInfo = new SingleDeedMap();
                                fragInfo.setArguments(bundle);
                                fragmgr.beginTransaction().replace(R.id.content_frame, fragInfo).commit();
                            } else if (fromwhere.equals("edit")) {
                                mDialog.dismiss();
                                Bundle bundle = new Bundle();
                                // int i = 3;
                                bundle.putString("tab", tab);
                                bundle.putString("page", "detailspage");
                                bundle.putString("str_address", str_address);
                                bundle.putString("str_tagid", str_tagid);
                                bundle.putString("str_geopoint", str_geopoint);
                                bundle.putString("str_taggedPhotoPath", str_taggedPhotoPath);
                                bundle.putString("str_description", str_description);
                                bundle.putString("str_characterPath", strImagepath);
                                bundle.putString("str_fname", str_fname);
                                bundle.putString("str_lname", str_lname);
                                bundle.putString("str_privacy", str_privacy);
                                bundle.putString("str_userID", str_userID);
                                bundle.putString("str_needName", str_needName);
                                bundle.putString("str_permanent", str_permanent);
                                bundle.putString("str_subtypes", str_subtypes);
                                bundle.putString("str_fromgroupid", strGroup_id);
                                bundle.putString("str_fromgroup", strGroup_name);
                                bundle.putString("str_totalTaggedCreditPoints", str_totalTaggedCreditPoints);
                                bundle.putString("str_totalFulfilledCreditPoints", str_totalFulfilledCreditPoints);
                                bundle.putString("tab", tab);
                                bundle.putString("str_title", str_title);
                                bundle.putString("str_date", str_date);
                                bundle.putString("str_distance", str_distance);
                                bundle.putString("validity", str_validity);
                                bundle.putString("container", str_container);
                                bundle.putString("mappingId", str_mappingId);
                                TagaNeed mainHomeFragment = new TagaNeed();
                                mainHomeFragment.setArguments(bundle);
                                FragmentTransaction fragmentTransaction =
                                        getActivity().getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.content_frame, mainHomeFragment);
                                fragmentTransaction.commit();
                            } else if (fromwhere.equals("giftnow")) {
                                mDialog.dismiss();
                                Bundle bundle = new Bundle();
                                int i = 3;
                                bundle.putString("str_address", str_address);
                                bundle.putString("str_tagid", str_tagid);
                                bundle.putString("str_geopoint", str_geopoint);
                                bundle.putString("str_taggedPhotoPath", str_taggedPhotoPath);
                                bundle.putString("str_description", str_description);
                                bundle.putString("str_characterPath", strImagepath);
                                bundle.putString("str_fname", str_fname);
                                bundle.putString("str_lname", str_lname);
                                bundle.putString("str_privacy", str_privacy);
                                bundle.putString("str_userID", str_userID);
                                bundle.putString("str_needName", str_needName);
                                bundle.putString("str_permanent", str_permanent);
                                bundle.putString("str_subtypes", str_subtypes);
                                bundle.putString("str_totalTaggedCreditPoints", str_totalTaggedCreditPoints);
                                bundle.putString("str_totalFulfilledCreditPoints", str_totalFulfilledCreditPoints);
                                bundle.putString("tab", tab);
                                bundle.putString("str_title", str_title);
                                bundle.putString("str_date", str_date);
                                bundle.putString("str_distance", str_distance);
                                GiftANeedFrag fragInfo = new GiftANeedFrag();
                                fragInfo.setArguments(bundle);

                                fragmgr.beginTransaction().replace(R.id.content_frame, fragInfo).addToBackStack(null).commit();
                            }
                            // Toast.makeText(getContext(), "User reported successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "This deed does not exist anymore", Toast.LENGTH_SHORT).show();
                            mDialog.dismiss();
                            Bundle bundle = new Bundle();
                            int i = 3;
                            bundle.putString("tab", tab);
                            TaggedneedsFrag mainHomeFragment = new TaggedneedsFrag();
                            mainHomeFragment.setArguments(bundle);
                            FragmentTransaction fragmentTransaction =
                                    getActivity().getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.content_frame, mainHomeFragment);
                            fragmentTransaction.commit();
                        }
                    }
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

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    private void init() {
        try {
            //txtheading= (TextView)rootview. findViewById(R.id.txtheadingneeddetails);
            locationTypeLayout = (LinearLayout) rootview.findViewById(R.id.layout_location_type);
            tvLocationType = (TextView) rootview.findViewById(R.id.tv_location_type);
            giftnow = (Button) rootview.findViewById(R.id.btngiftnow);
            txtsubheading = (TextView) rootview.findViewById(R.id.txtneeddetailsubhead);
            txtaddress = (TextView) rootview.findViewById(R.id.txtneeddetailsaddress);
            txtdistance = (TextView) rootview.findViewById(R.id.txtneeddetailsdistance);
            txtSubtypes = (TextView) rootview.findViewById(R.id.tv_sub_types);
            subTypeLayout = (LinearLayout) rootview.findViewById(R.id.sub_type_layout);
            fromGroupLayout = (LinearLayout) rootview.findViewById(R.id.tagged_by_groupname_layout);
            lastEndorseLayout = (LinearLayout) rootview.findViewById(R.id.last_endorse_layout);
            txtLastEndorse = (TextView) rootview.findViewById(R.id.tv_last_endorse_time);
            txtFromGroupName = (TextView) rootview.findViewById(R.id.txt_from_group_name);
            txtDate = (TextView) rootview.findViewById(R.id.txtneeddetailsdate);
            img = (ImageView) rootview.findViewById(R.id.imgview);
            imgback = (ImageView) rootview.findViewById(R.id.backbutton);
            txt_name = (TextView) rootview.findViewById(R.id.txtname);
            txt_des = (TextView) rootview.findViewById(R.id.txtdiscription);
            imgcharacter = (CircleImageView) rootview.findViewById(R.id.img_character);
            edComment = (EditText) rootview.findViewById(R.id.edComment);
            detailspagemainscroll = (ScrollView) rootview.findViewById(R.id.detailspagemainscroll);
            layout_editdeed = rootview.findViewById(R.id.layout_editdeed);
            layout_endorsedeed = rootview.findViewById(R.id.layout_endorsedeed);
            layout_viewdeed = rootview.findViewById(R.id.layout_viewdeed);
            txtneeddetailsendorse = rootview.findViewById(R.id.txtneeddetailsendorse);
            txtneeddetailsview = rootview.findViewById(R.id.txtneeddetailsview);
            listviewcomments = rootview.findViewById(R.id.recycler_commentslist);
            details_locationicon = rootview.findViewById(R.id.details_locationicon);
            comments_layout = rootview.findViewById(R.id.comments_layout);
            btnReportDeed = rootview.findViewById(R.id.btnReportDeed);
            btnReportUser = rootview.findViewById(R.id.btnReportUser);
            img_endorse = rootview.findViewById(R.id.img_endorse);
            img_endorse_over = rootview.findViewById(R.id.img_endorse_over);
            txtcontaineravilable = rootview.findViewById(R.id.txtcontaineravilable);
            txtcontaineravilableyes_no = rootview.findViewById(R.id.txtcontaineravilableyes_no);
            detailspage_containerlayout = rootview.findViewById(R.id.detailspage_containerlayout);
            //  txtheading.setTypeface(new FontDetails(getActivity()).fontStandardForPage);
            //  txtcontaineravilable.setTypeface(new FontDetails(getActivity()).fontStandardForPage);
            //  txtcontaineravilableyes_no.setTypeface(new FontDetails(getActivity()).fontStandardForPage);
            txtsubheading.setTypeface(new FontDetails(getActivity()).fontStandardForPage);
            txtaddress.setTypeface(new FontDetails(getActivity()).fontStandardForPage);
            txtdistance.setTypeface(new FontDetails(getActivity()).fontStandardForPage);
            txtDate.setTypeface(new FontDetails(getActivity()).fontStandardForPage);
            txt_name.setTypeface(new FontDetails(getActivity()).fontStandardForPage);
            txt_des.setTypeface(new FontDetails(getActivity()).fontStandardForPage);
            edComment.setTypeface(new FontDetails(getActivity()).fontStandardForPage);
            txtneeddetailsview.setTypeface(new FontDetails(getActivity()).fontStandardForPage);
            txtneeddetailsendorse.setTypeface(new FontDetails(getActivity()).fontStandardForPage);
            //----------------------img icon for sending comment------------
            img_view_send_comment = rootview.findViewById(R.id.img_view_send_comment);
        } catch (Exception e) {
//            StringWriter writer = new StringWriter();
//            e.printStackTrace(new PrintWriter(writer));
//            Bugreport bg = new Bugreport();
//            bg.sendbug(writer.toString());
        }
    }

    private void submitdialog(final String report) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        LayoutInflater li = LayoutInflater.from(getActivity());
        View confirmDialog = li.inflate(R.layout.giftneeddialog, null);
        dialogbtnconfirm = (Button) confirmDialog.findViewById(R.id.btn_submit_mobileno);
        dialogbtncancel = (Button) confirmDialog.findViewById(R.id.btn_Cancel_mobileno);
        dialogtext = (TextView) confirmDialog.findViewById(R.id.txtgiftneeddialog);
        if (report.equals("user")) {
            dialogtext.setText(getResources().getString(R.string.report_msg));
        } else {
            dialogtext.setText(getResources().getString(R.string.report_deed_msg));
        }
        //-------------Adding dialog box to the view of alert dialog
        alert.setView(confirmDialog);
        alert.setCancelable(false);
        // dialog.setStyle(DialogFragment.STYLE_NO_FRAME, 0);

        //----------------Creating an alert dialog
        alertDialogForgot = alert.create();
        alertDialogForgot.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        alertDialogForgot.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // alertDialogForgot.setS
        //----------------Displaying the alert dialog
        alertDialogForgot.show();
//        btnPost.setEnabled(true);
        dialogbtnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.setConfiguration(new ArcConfiguration(getContext()));
                mDialog.show();
                mDialog.setCancelable(false);
//                btnPost.setEnabled(true);
                if (!(Validation.isNetworkAvailable(getActivity()))) {
                    ToastPopUp.show(getActivity(), getString(R.string.network_validation));
                } else {
                    if (report.equals("user")) {
                        reportUser();
                    } else {
                        reportDeed();
                    }
                }
                //checkimage();
                //sendaTag();
                alertDialogForgot.dismiss();
            }
        });
        dialogbtncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // btnPost.setEnabled(true);
                alertDialogForgot.dismiss();
            }
        });
    }

    @Override
    public void onResume() {

        super.onResume();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
       /* edComment.post(new Runnable() {
            @Override
            public void run() {
                edComment.requestFocus();
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                *//*InputMethodManager imgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imgr.showSoftInput(edstartwriting, InputMethodManager.SHOW_IMPLICIT);*//*
            }
        });*/
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    if (tab.equals("group")) {
                        GroupDetailsFragment groupDetailsFragment = new GroupDetailsFragment();
                        fragmgr.beginTransaction().replace(R.id.content_frame, groupDetailsFragment).commit();
                    } else if (tab.equals("notification")) {
                        Notificationfrag notificationfrag = new Notificationfrag();
                        fragmgr.beginTransaction().replace(R.id.content_frame, notificationfrag).commit();
                    } else {
                        Bundle bundle = new Bundle();
                        int i = 3;
                        bundle.putString("tab", tab);
                        TaggedneedsFrag mainHomeFragment = new TaggedneedsFrag();
                        mainHomeFragment.setArguments(bundle);
                        FragmentTransaction fragmentTransaction =
                                getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame, mainHomeFragment);
                        fragmentTransaction.commit();
                    }
                    return true;
                }
                return false;
            }
        });
    }



    //----------------------------------------gif showing after endorse
    private void gifDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.gif_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();
        ImageView img = (ImageView) dialog.findViewById(R.id.img_gif);
        Glide.with(getActivity())
                .load(R.drawable.thumbs_up)
                .into(img);
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (dialog.isShowing()) {
                    dialog.dismiss();

                }
            }
        };
        handler.postDelayed(runnable, 5000);
    }

    //----------------------------------------
    private void endorseDialog() {
        final Dialog dialog = new Dialog(getActivity());
        mp = MediaPlayer.create(getActivity(), R.raw.endorsed_sound);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.endorse_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.show();
        imageView = (ImageView) dialog.findViewById(R.id.image_endorsed);
        Glide.with(getActivity())
                .load(R.drawable.ic_endorsed_stamp)
                .into(imageView);
        // load the animation
        animFadein = AnimationUtils.loadAnimation(getContext(),
                R.anim.zoom_in);
        // set animation listener
        animFadein.setAnimationListener(this);
        // start the animation
        imageView.startAnimation(animFadein);
        mp.start();
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (dialog.isShowing()) {
                    dialog.dismiss();

                }
            }
        };
        handler.postDelayed(runnable, 5000);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
