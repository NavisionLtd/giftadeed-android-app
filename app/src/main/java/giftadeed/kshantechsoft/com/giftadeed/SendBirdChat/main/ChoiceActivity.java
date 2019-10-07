package giftadeed.kshantechsoft.com.giftadeed.SendBirdChat.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;

import giftadeed.kshantechsoft.com.giftadeed.ActiveUser.GiftAdeed;
import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.SendBirdChat.groupchannel.GroupChannelActivity;
import giftadeed.kshantechsoft.com.giftadeed.SendBirdChat.openchannel.OpenChannelActivity;
import giftadeed.kshantechsoft.com.giftadeed.SendBirdChat.utils.PreferenceUtils;

public class ChoiceActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(mToolbar);

        findViewById(R.id.linear_layout_group_channels).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChoiceActivity.this, GroupChannelActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.linear_layout_open_channels).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChoiceActivity.this, OpenChannelActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.button_disconnect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Unregister push tokens and disconnect
                disconnect();
            }
        });

        // Displays the SDK version in a TextView
        /*String sdkVersion = String.format(getResources().getString(R.string.all_app_version),
                GiftAdeed.VERSION, SendBird.getSDKVersion());
        ((TextView) findViewById(R.id.text_main_versions)).setText(sdkVersion);*/
    }

    /**
     * Unregisters all push tokens for the current user so that they do not receive any notifications,
     * then disconnects from SendBird.
     */
    private void disconnect() {
        SendBird.unregisterPushTokenAllForCurrentUser(new SendBird.UnregisterPushTokenHandler() {
            @Override
            public void onUnregistered(SendBirdException e) {
                if (e != null) {
                    // Error!
                    e.printStackTrace();
                    // Don't return because we still need to disconnect.
                }

                ConnectionManager.logout(new SendBird.DisconnectHandler() {
                    @Override
                    public void onDisconnected() {
                        PreferenceUtils.setConnected(ChoiceActivity.this, false);

                        finish();
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_main) {
            Intent intent = new Intent(ChoiceActivity.this, SendBirdSettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }
}
