/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.splash;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.HashMap;

import giftadeed.kshantechsoft.com.giftadeed.EmergencyPositioning.SOSOptionActivity;
import giftadeed.kshantechsoft.com.giftadeed.Login.LoginActivity;
import giftadeed.kshantechsoft.com.giftadeed.Mytags.MyTagsList;
import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.TagaNeed;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsActivity;
import giftadeed.kshantechsoft.com.giftadeed.Utils.SessionManager;
import giftadeed.kshantechsoft.com.giftadeed.Utils.ToastPopUp;

public class AppShortcutActivity extends AppCompatActivity {
    String selectedOption = "", strUserId = "";
    static FragmentManager fragmgr;
    SessionManager sessionManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_shortcut_layout);
        fragmgr = getSupportFragmentManager();
        selectedOption = getIntent().getStringExtra("selected_option");
        Log.d("selected_option", "" + selectedOption);

        if (selectedOption.equals("sos")) {
            Intent i = new Intent(this, SOSOptionActivity.class);
            startActivity(i);
            AppShortcutActivity.this.finish();
        } else if (selectedOption.equals("tagdeed")) {
            sessionManager = new SessionManager(getApplicationContext());
            HashMap<String, String> user = sessionManager.getUserDetails();
            strUserId = user.get(sessionManager.USER_ID);
            if (strUserId == null) {
                Intent log = new Intent(getApplicationContext(), LoginActivity.class);
                log.putExtra("message", "Charity");
                ToastPopUp.show(this,"For tag a deed login is required");
                startActivity(log);
            } else {
                Intent in = new Intent(AppShortcutActivity.this, TaggedneedsActivity.class);
                in.putExtra("selected_option","tagdeed");
                startActivity(in);
            }
        }
    }
}
