/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.termsandconditions;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import giftadeed.kshantechsoft.com.giftadeed.Landing.MainActivity;
import giftadeed.kshantechsoft.com.giftadeed.Login.LoginActivity;
import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.Signup.SignUp;

////////////////////////////////////////////////////////////////////
//                                                               //
//     Shows terms and conditions of app on signup page          //
/////////////////////////////////////////////////////////////////
public class Terms_Conditions extends AppCompatActivity {
    String message;
    ImageView imgback;
    WebView txtAgreementDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms__conditions);
        Bundle bundle = getIntent().getExtras();
        message = bundle.getString("message");
        imgback= (ImageView) findViewById(R.id.tc_backbutton);
        txtAgreementDetails= (WebView) findViewById(R.id.txtAgreementText);
        txtAgreementDetails.loadUrl("file:///android_asset/Terms_and_condition.html");

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
        /*Intent intent = new Intent(Terms_Conditions.this, SignUp.class);
        intent.putExtra("message", message);
       // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
        startActivity(intent);*/

    }

}
