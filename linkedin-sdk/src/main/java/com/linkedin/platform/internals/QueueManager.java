/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package com.linkedin.platform.internals;

import android.content.Context;
import androidx.annotation.NonNull;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class QueueManager {

    private static final String TAG = QueueManager.class.getName();
    private static QueueManager queueManager;
    private Context ctx;
    private RequestQueue requestQueue;

    private QueueManager(Context context) {
        ctx = context.getApplicationContext();
        requestQueue = Volley.newRequestQueue(ctx);
    }

    public static void initQueueManager(@NonNull Context ctx) {
        getInstance(ctx);
    }

    public static synchronized QueueManager getInstance(Context context) {
        if (queueManager == null) {
            queueManager = new QueueManager(context);
        }
        return queueManager;
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }



}
