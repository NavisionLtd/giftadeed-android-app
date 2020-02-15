/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.termsandconditions;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import giftadeed.kshantechsoft.com.giftadeed.R;

////////////////////////////////////////////////////////////////////
//                                                               //
//     Shows terms and conditions of app on signup page          //
/////////////////////////////////////////////////////////////////
public class Terms_Conditions extends AppCompatActivity {
    ImageView imgback;
    WebView txtAgreementDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms__conditions);
        imgback = (ImageView) findViewById(R.id.tc_backbutton);
        txtAgreementDetails = (WebView) findViewById(R.id.txtAgreementText);
//        txtAgreementDetails.loadUrl("file:///android_asset/Terms_and_condition.html");
        txtAgreementDetails.loadUrl("https://giftadeed.com/pages//Terms_and_condition.html");
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

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
