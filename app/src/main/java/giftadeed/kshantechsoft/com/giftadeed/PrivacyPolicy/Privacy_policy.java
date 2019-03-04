package giftadeed.kshantechsoft.com.giftadeed.PrivacyPolicy;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsActivity;
import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;

public class Privacy_policy extends AppCompatActivity {
    // String message;
    ImageView imgback;
    WebView txtprivacypolicy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        Bundle bundle = getIntent().getExtras();
        // message = bundle.getString("message");
        imgback = (ImageView) findViewById(R.id.tc_backbutton);
        txtprivacypolicy = (WebView) findViewById(R.id.txtAgreementText);
//        txtprivacypolicy.loadUrl("file:///android_asset/PrivacyPolicy.html");
        txtprivacypolicy.loadUrl("https://giftadeed.com/pages/privacy_policy_app.html");

        WebSettings settings = txtprivacypolicy.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setTextZoom(100);
        txtprivacypolicy.getSettings().setJavaScriptEnabled(true);
        txtprivacypolicy.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        txtprivacypolicy.getSettings().setAppCacheEnabled(true);
        txtprivacypolicy.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        txtprivacypolicy.setLongClickable(false);

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
