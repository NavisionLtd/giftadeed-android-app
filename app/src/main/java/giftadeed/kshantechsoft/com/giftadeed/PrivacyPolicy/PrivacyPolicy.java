/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.PrivacyPolicy;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import giftadeed.kshantechsoft.com.giftadeed.GridMenu.MenuGrid;
import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsActivity;

////////////////////////////////////////////////////////////////////
//                                                               //
//     Shows privacy policies of app                                  //
/////////////////////////////////////////////////////////////////
public class PrivacyPolicy extends Fragment {
    static FragmentManager fragmgr;
    View rootview;
    private WebView txtPrivacyPolicy;

    public static PrivacyPolicy newInstance(int sectionNumber) {
        PrivacyPolicy fragment = new PrivacyPolicy();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_privacypolicy, container, false);
        fragmgr = getFragmentManager();
        TaggedneedsActivity.fragname = PrivacyPolicy.newInstance(0);
        TaggedneedsActivity.updateTitle(getResources().getString(R.string.privacy_heading));
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

        txtPrivacyPolicy = (WebView) rootview.findViewById(R.id.txtprivacypolicy);
//        txtAgreementDetails.loadUrl("file:///android_asset/PrivacyPolicy.html");
        txtPrivacyPolicy.loadUrl("https://giftadeed.com/pages/privacy_policy_app.html");

        WebSettings settings = txtPrivacyPolicy.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setTextZoom(100);
        txtPrivacyPolicy.getSettings().setJavaScriptEnabled(true);
        txtPrivacyPolicy.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        txtPrivacyPolicy.getSettings().setAppCacheEnabled(true);
        txtPrivacyPolicy.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        txtPrivacyPolicy.setLongClickable(false);
        TaggedneedsActivity.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuGrid menuGrid = new MenuGrid();
                fragmgr.beginTransaction().replace(R.id.content_frame, menuGrid).commit();
            }
        });

        /*MainActivity.imgHamburger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = 0;
                FragmentTransaction transaction = fragmgr.beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_right, R.anim.slide_left);
                transaction.replace(R.id.content_main, MenuGrid.newInstance(i));
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });*/
        //MainActivity.fragname= Contactus.newInstance(0);
        // Inflate the layout for this fragment
        return rootview;
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
