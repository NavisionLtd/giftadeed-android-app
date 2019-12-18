/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.SendBirdChat.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;
import java.util.HashMap;

import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.SendBirdChat.groupchannel.GroupChannelActivity;
import giftadeed.kshantechsoft.com.giftadeed.SendBirdChat.openchannel.OpenChannelActivity;
import giftadeed.kshantechsoft.com.giftadeed.SendBirdChat.utils.PreferenceUtils;
import giftadeed.kshantechsoft.com.giftadeed.SendBirdChat.utils.PushUtils;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsFrag;
import giftadeed.kshantechsoft.com.giftadeed.Utils.SessionManager;

public class SendBirdLoginActivity extends Fragment {
    View rootview;
    static FragmentManager fragmgr;
    private CoordinatorLayout mLoginLayout;
    private TextInputEditText mUserIdConnectEditText, mUserNicknameEditText;
    private Button mConnectButton;
    private ContentLoadingProgressBar mProgressBar;
    public SessionManager sessionManager;
    private String strUsername;
    private String strUserID;
    public Context mContext;
    public String strDesired_Page = "";
    public String strClubUrl = "";
    public String strRedirectionPage = "";

    public static SendBirdLoginActivity newInstance(int sectionNumber) {
        SendBirdLoginActivity fragment = new SendBirdLoginActivity();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.activity_sendbird_login, container, false);
        fragmgr = getFragmentManager();
        mContext = getActivity();
        Bundle bundle = this.getArguments();
        // Bundle bundle = this.getArguments();
        if (bundle != null) {
            strDesired_Page = bundle.getString("CHATPAGE");
            strClubUrl = bundle.getString("CLUBURL");
            strRedirectionPage=bundle.getString("PAGE");
        }

        mLoginLayout = (CoordinatorLayout) rootview.findViewById(R.id.layout_login);
        mUserIdConnectEditText = (TextInputEditText) rootview.findViewById(R.id.edittext_login_user_id);
        mUserNicknameEditText = (TextInputEditText) rootview.findViewById(R.id.edittext_login_user_nickname);
        //add sharePreference
        sessionManager = new SessionManager(getActivity());
        HashMap<String, String> user = sessionManager.getUserDetails();
        strUserID = user.get(sessionManager.USER_ID);
        strUsername = user.get(sessionManager.USER_NAME);
        mUserIdConnectEditText.setText(PreferenceUtils.getUserId(getActivity()));
        mUserNicknameEditText.setText(PreferenceUtils.getNickname(getActivity()));

        /*//check the page and tranfer to them at certain page
        if (strDesired_Page.equalsIgnoreCase("MSGPAGE")) {
            // Proceed to ChoiceActivity for open chaneel activity
            Intent intent = new Intent(getActivity(), GroupChannelActivity.class);
            startActivity(intent);
            //getActivity().finish();

        } else if (strDesired_Page.equalsIgnoreCase("SHARECOMMUNITY")) {
            Intent intent = new Intent(getActivity(), OpenChannelActivity.class);
            startActivity(intent);
            // getActivity().finish();
        }*/
        mConnectButton = (Button) rootview.findViewById(R.id.button_login_connect);
        mConnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // String userId = mUserIdConnectEditText.getText().toString();
                // Remove all spaces from userID
                if (strUserID != null && strUsername != null) {
                    strUserID = strUserID.replaceAll("\\s", "");
                    // String userNickname = mUserNicknameEditText.getText().toString();
                    PreferenceUtils.setUserId(mContext, strUserID);
                    PreferenceUtils.setNickname(mContext, strUsername);
                    connectToSendBird(strUserID, strUsername);
                } else {
                    Toast.makeText(getActivity(), "UnAuthorized Access", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mUserIdConnectEditText.setSelectAllOnFocus(true);
        mUserNicknameEditText.setSelectAllOnFocus(true);
        // A loading indicator
        mProgressBar = (ContentLoadingProgressBar) rootview.findViewById(R.id.progress_bar_login);
        return rootview;
    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    Bundle bundle = new Bundle();
                    bundle.putString("tab", "tab2");
                    fragmgr.beginTransaction().replace(R.id.content_frame, TaggedneedsFrag.newInstance(1)).addToBackStack(null).commit();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (PreferenceUtils.getConnected(mContext)) {
            connectToSendBird(PreferenceUtils.getUserId(mContext), PreferenceUtils.getNickname(mContext));
        }
    }

    /**
     * Attempts to connect a user to SendBird.
     *
     * @param userId       The unique ID of the user.
     * @param userNickname The user's nickname, which will be displayed in chats.
     */
    private void connectToSendBird(final String userId, final String userNickname) {
        // Show the loading indicator
        showProgressBar(true);
        mConnectButton.setEnabled(false);
        ConnectionManager.login(userId, new SendBird.ConnectHandler() {
            @Override
            public void onConnected(User user, SendBirdException e) {
                // Callback received; hide the progress bar.
                showProgressBar(false);
                if (e != null) {
                    // Error!
                    Toast.makeText(
                            getActivity(), "" + e.getCode() + ": " + e.getMessage(),
                            Toast.LENGTH_SHORT)
                            .show();

                    // Show login failure snackbar
//                    showSnackbar("Login to SendBird failed");
                    mConnectButton.setEnabled(true);
                    PreferenceUtils.setConnected(mContext, false);
                    return;
                }
                PreferenceUtils.setUserId(mContext, user.getUserId());
                PreferenceUtils.setNickname(mContext, user.getNickname());
                PreferenceUtils.setProfileUrl(mContext, user.getProfileUrl());
                PreferenceUtils.setConnected(mContext, true);

                // Update the user's nickname
                updateCurrentUserInfo(userNickname);
                updateCurrentUserPushToken();
                if (strDesired_Page.equalsIgnoreCase("MSGPAGE")) {
                    // group chat
                    // Proceed to ChoiceActivity for open chaneel activity
                    Intent intent = new Intent(getActivity(), GroupChannelActivity.class);
                    intent.putExtra("groupChannelUrl", strClubUrl);
                    intent.putExtra("REDIRECTPAGE", strRedirectionPage);
                    startActivity(intent);
                } else if (strDesired_Page.equalsIgnoreCase("SHARECOMMUNITY")) {
                    // global chat functionality
                    Intent intent = new Intent(getActivity(), OpenChannelActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * Update the user's push token.
     */
    private void updateCurrentUserPushToken() {
        PushUtils.registerPushTokenForCurrentUser(getActivity(), null);
    }

    /**
     * Updates the user's nickname.
     *
     * @param userNickname The new nickname of the user.
     */
    private void updateCurrentUserInfo(final String userNickname) {
        SendBird.updateCurrentUserInfo(userNickname, null, new SendBird.UserInfoUpdateHandler() {
            @Override
            public void onUpdated(SendBirdException e) {
                if (e != null) {
                    // Error!
                    Toast.makeText(
                            getActivity(), "" + e.getCode() + ":" + e.getMessage(),
                            Toast.LENGTH_SHORT)
                            .show();

                    // Show update failed snackbar
                    showSnackbar("Update user nickname failed");
                    return;
                }
                Log.d("TAG", "NickName189" + userNickname);
                PreferenceUtils.setNickname(mContext, userNickname);
            }
        });
    }

    // Displays a Snackbar from the bottom of the screen
    private void showSnackbar(String text) {
        Snackbar snackbar = Snackbar.make(mLoginLayout, text, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    // Shows or hides the ProgressBar
    private void showProgressBar(boolean show) {
        if (show) {
            mProgressBar.show();
        } else {
            mProgressBar.hide();
        }
    }
}
