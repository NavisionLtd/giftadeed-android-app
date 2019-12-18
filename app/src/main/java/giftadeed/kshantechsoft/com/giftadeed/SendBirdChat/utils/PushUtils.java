/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.SendBirdChat.utils;

import android.content.Context;

import com.google.firebase.iid.FirebaseInstanceId;
import com.sendbird.android.SendBird;

public class PushUtils {

    public static void registerPushTokenForCurrentUser(final Context context, SendBird.RegisterPushTokenWithStatusHandler handler) {
        SendBird.registerPushTokenForCurrentUser(FirebaseInstanceId.getInstance().getToken(), handler);
    }

    public static void unregisterPushTokenForCurrentUser(final Context context, SendBird.UnregisterPushTokenHandler handler) {
        SendBird.unregisterPushTokenForCurrentUser(FirebaseInstanceId.getInstance().getToken(), handler);
    }

}
