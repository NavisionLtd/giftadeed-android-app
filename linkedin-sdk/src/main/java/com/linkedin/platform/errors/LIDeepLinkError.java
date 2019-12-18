/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package com.linkedin.platform.errors;

import androidx.annotation.NonNull;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class LIDeepLinkError {

    private static final String TAG = LIDeepLinkError.class.getName();

    private LIAppErrorCode errorCode;
    private String errorMsg;

    public LIDeepLinkError(@NonNull String errorInfo, String errorMsg) {
        LIAppErrorCode liAppErrorCode = LIAppErrorCode.findErrorCode(errorInfo);
        this.errorCode = liAppErrorCode;
        this.errorMsg = errorMsg;
    }

    public LIDeepLinkError(LIAppErrorCode errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    @Override
    public String toString() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("errorCode", errorCode.name());
            jsonObject.put("errorMessage", errorMsg);
            return jsonObject.toString(2);
        } catch (JSONException e) {
            Log.d(TAG, e.getMessage());
        }
        return null;
    }

}