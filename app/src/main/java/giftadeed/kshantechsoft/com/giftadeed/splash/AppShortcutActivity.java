/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.splash;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import java.util.HashMap;

import giftadeed.kshantechsoft.com.giftadeed.EmergencyPositioning.SOSOptionActivity;
import giftadeed.kshantechsoft.com.giftadeed.Login.LoginActivity;
import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsActivity;
import giftadeed.kshantechsoft.com.giftadeed.Utils.SharedPrefManager;
import giftadeed.kshantechsoft.com.giftadeed.Utils.ToastPopUp;

public class AppShortcutActivity extends AppCompatActivity {
    String selectedOption = "", strUserId = "";
    static FragmentManager fragmgr;
    SharedPrefManager sharedPrefManager;

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
            sharedPrefManager = new SharedPrefManager(getApplicationContext());
            HashMap<String, String> user = sharedPrefManager.getUserDetails();
            strUserId = user.get(sharedPrefManager.USER_ID);
            if (strUserId == null) {
                Intent log = new Intent(getApplicationContext(), LoginActivity.class);
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
