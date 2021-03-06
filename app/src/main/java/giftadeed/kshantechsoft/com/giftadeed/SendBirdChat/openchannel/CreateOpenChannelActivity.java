/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.SendBirdChat.openchannel;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.sendbird.android.OpenChannel;
import com.sendbird.android.SendBirdException;

import org.jetbrains.annotations.Nullable;

import giftadeed.kshantechsoft.com.giftadeed.R;


/**
 * Allows a user to create an Open Channel.
 * Dialog instead of activity?
 */

public class CreateOpenChannelActivity extends AppCompatActivity {
    private TextInputEditText mNameEditText;
    private boolean enableCreate = false;
    private Button mCreateButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_open_channel);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_create_open_channel);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            //disable left arrow
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_left_white_24_dp);
        }

        mNameEditText = (TextInputEditText) findViewById(R.id.edittext_create_open_channel_name);

        mCreateButton = (Button) findViewById(R.id.button_create_open_channel);

        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createOpenChannel(mNameEditText.getText().toString());
            }
        });

        mCreateButton.setEnabled(enableCreate);

        mNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    if (enableCreate) {
                        mCreateButton.setEnabled(false);
                        enableCreate = false;
                    }
                } else {
                    if (!enableCreate) {
                        mCreateButton.setEnabled(true);
                        enableCreate = true;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void createOpenChannel(String name) {
        OpenChannel.createChannelWithOperatorUserIds(name, null, null, null, new OpenChannel.OpenChannelCreateHandler() {
            @Override
            public void onResult(OpenChannel openChannel, SendBirdException e) {
                if (e != null) {
                    // Error!
                    return;
                }

                // Open Channel created
                finish();
            }
        });
    }


}
