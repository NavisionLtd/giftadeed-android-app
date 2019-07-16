package giftadeed.kshantechsoft.com.giftadeed.Disclaimer;

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
//     Shows disclaimer of app                                  //
/////////////////////////////////////////////////////////////////
public class Disclaimer extends Fragment {
    static FragmentManager fragmgr;
    View rootview;
    WebView txtAgreementDetails;
    public static Disclaimer newInstance(int sectionNumber) {
        Disclaimer fragment = new Disclaimer();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootview=inflater.inflate(R.layout.fragment_disclaimer, container, false);
        //---------------------------------------------------------------------------
        TaggedneedsActivity.fragname= Disclaimer.newInstance(0);
        TaggedneedsActivity.updateTitle(getResources().getString(R.string.dis_heading));
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
        txtAgreementDetails= (WebView)rootview. findViewById(R.id.txtdisclaimer);
        txtAgreementDetails.loadUrl("file:///android_asset/Disclaimer.html");
        WebSettings settings = txtAgreementDetails.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setTextZoom(100);
        txtAgreementDetails.getSettings().setJavaScriptEnabled(true);
        txtAgreementDetails.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        txtAgreementDetails.getSettings().setAppCacheEnabled(true);
        txtAgreementDetails.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        txtAgreementDetails.setLongClickable(false);

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