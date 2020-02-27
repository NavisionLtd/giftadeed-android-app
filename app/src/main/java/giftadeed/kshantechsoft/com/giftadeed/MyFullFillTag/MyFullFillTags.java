/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.MyFullFillTag;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import giftadeed.kshantechsoft.com.giftadeed.GridMenu.MenuGrid;
import giftadeed.kshantechsoft.com.giftadeed.Login.LoginActivity;
import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsActivity;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsFrag;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.list_Model.Taggedlist;
import giftadeed.kshantechsoft.com.giftadeed.Utils.SharedPrefManager;
import giftadeed.kshantechsoft.com.giftadeed.Utils.ToastPopUp;
import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by I-Sys on 5/22/2017.
 */

////////////////////////////////////////////////////////////////////
//                                                               //
//     Shows list of tags fulfilled by user                     //
/////////////////////////////////////////////////////////////////
public class MyFullFillTags extends Fragment  {
    RecyclerView recyclerView;
    RelativeLayout relativeNoResultFound;
    View rootview;
    private RecyclerView.LayoutManager layoutManager;
    static FragmentManager fragmgr;
    FragmentActivity myContext;
    SharedPrefManager sharedPrefManager;
    String strUSERID;
    List<Taggedlist> lstMyfullfillTags = new ArrayList<>();
    Adapter_MyFullFillTags adapter;
    TextView txtmytags;
    SimpleArcDialog mDialog;
    String tab = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.activity_my_tags_list, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        TaggedneedsActivity.updateTitle(getResources().getString(R.string.my_fulfil_tags_heading));
        TaggedneedsActivity.fragname = MyFullFillTags.newInstance(0);
        mDialog = new SimpleArcDialog(getContext());
        //----------------------------------this code is used for taking user id from session preference
        sharedPrefManager = new SharedPrefManager(getActivity());
        HashMap<String, String> user = sharedPrefManager.getUserDetails();
        strUSERID = user.get(sharedPrefManager.USER_ID);
        myfullFillTagsFetchDetails(strUSERID);
        //-----------------------------------recycler view layout------------------
        recyclerView = (RecyclerView) rootview.findViewById(R.id.recycler_myTags);
        relativeNoResultFound = (RelativeLayout) rootview.findViewById(R.id.relNoResultMyTagsList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        txtmytags = (TextView) rootview.findViewById(R.id.txtmytaglist);
        txtmytags.setText(getResources().getString(R.string.list_of_myfulfilled_tags));

        //---------------------------------------------------------------------------
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
        tab = this.getArguments().getString("tab");
        rootview.getRootView().setFocusableInTouchMode(true);
        rootview.getRootView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                int i = 1;
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    MenuGrid menuGrid = new MenuGrid();
                    fragmgr.beginTransaction().replace(R.id.content_frame, menuGrid).commit();
                    return true;
                }
                return false;
            }
        });
        TaggedneedsActivity.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tab.equals("tabbar")) {
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
                } else {
                    MenuGrid menuGrid = new MenuGrid();
                    fragmgr.beginTransaction().replace(R.id.content_frame, menuGrid).commit();
                }
            }
        });
        return rootview;
    }

    public void myfullFillTagsFetchDetails(String strUserId) {
        mDialog.setConfiguration(new ArcConfiguration(getContext()));
        mDialog.show();
        mDialog.setCancelable(false);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WebServices.MAIN_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceMyFFillTag service = retrofit.create(InterfaceMyFFillTag.class);
        Call<ModalMyFullfillTag> call = service.fetchData(strUserId);
        call.enqueue(new Callback<ModalMyFullfillTag>() {
            @Override
            public void onResponse(Response<ModalMyFullfillTag> response, Retrofit retrofit) {
                try {

                    ModalMyFullfillTag deedDetailsModel = response.body();
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
                        ModalMyFullfillTag result = new ModalMyFullfillTag();
                        result = response.body();
                        int size = response.body().getTaggedlist().size();
                        for (int i = 0; i < size; i++) {
                            // Log.d("MyFullFilltag", "" + size);
                            lstMyfullfillTags.add(new Taggedlist(
                                    result.getTaggedlist().get(i).getFullFilledPhotoPath(),
                                    result.getTaggedlist().get(i).getAddress(),
                                    result.getTaggedlist().get(i).getTaggedTitle(),
                                    result.getTaggedlist().get(i).getCharacterPath(),
                                    "",
                                    result.getTaggedlist().get(i).getTagStatus(),
                                    result.getTaggedlist().get(i).getViews(),
                                    result.getTaggedlist().get(i).getEndorse(),
                                    "No",
                                    result.getTaggedlist().get(i).getCategoryType(),
                                    result.getTaggedlist().get(i).getFullFilledPhotoPath(),
                                    result.getTaggedlist().get(i).getFullFilledDatetime(),
                                    result.getTaggedlist().get(i).getFullFilledPoints()
                            ));
                        }

                        if (size == 0) {
                            recyclerView.setVisibility(View.GONE);
                            relativeNoResultFound.setVisibility(View.VISIBLE);
                            TextView txtnodatafound = (TextView) rootview.findViewById(R.id.txttoptenfullfillernoresult);
                            txtnodatafound.setText("Hey, looks like you haven't fulfilled any need yet");
                        } else {
                            relativeNoResultFound.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            adapter = new Adapter_MyFullFillTags(getActivity(), lstMyfullfillTags);
                            recyclerView.setAdapter(adapter);
                        }
                    }
                } catch (Exception e) {
//                    StringWriter writer = new StringWriter();
//                    e.printStackTrace(new PrintWriter(writer));
//                    Bugreport bg = new Bugreport();
//                    bg.sendbug(writer.toString());
                }
                mDialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                ToastPopUp.show(myContext, getString(R.string.server_response_error));
                mDialog.dismiss();
            }
        });
    }

    public static MyFullFillTags newInstance(int sectionNumber) {
        MyFullFillTags fragment = new MyFullFillTags();
                /*Bundle args = new Bundle();
                args.putInt(ARG_SECTION_NUMBER, sectionNumber);
                fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
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
                    if (tab.equals("tabbar")) {
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
                    } else {
                        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                        MenuGrid menuGrid = new MenuGrid();
                        fragmgr.beginTransaction().replace(R.id.content_frame, menuGrid).commit();
                    }
                    return true;
                }
                return false;
            }
        });
    }


}
