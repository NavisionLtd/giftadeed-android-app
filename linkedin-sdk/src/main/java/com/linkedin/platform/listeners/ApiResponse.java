/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package com.linkedin.platform.listeners;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class ApiResponse {
    private static final String TAG = ApiResponse.class.getSimpleName();
    private static final String LOCATION = "Location";
    private static final String STATUS_CODE = "StatusCode";
    private static final String DATA = "responseData";

    private final int statusCode;
    private final String responseData;
    private final String locationHeader;

    public static synchronized ApiResponse buildApiResponse(JSONObject apiResponseAsJson) {
        try {
            int statusCode = apiResponseAsJson.optInt(STATUS_CODE);
            String locationHeader = apiResponseAsJson.optString(LOCATION);
            String responseData = apiResponseAsJson.getString(DATA);
            return new ApiResponse(statusCode, responseData, locationHeader);
        } catch (JSONException e) {
            Log.d(TAG, e.getMessage());
        }
        return null;
    }

    public ApiResponse(int statusCode, String responseData, String locationHeader) {
        this.statusCode = statusCode;
        this.responseData = responseData;
        this.locationHeader = locationHeader;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getResponseDataAsString() {
        return responseData;
    }

    public JSONObject getResponseDataAsJson() {
        if (responseData == null || "".equals(responseData)) {
            return null;
        }
        try {
            return new JSONObject(responseData);
        } catch (JSONException e) {
            Log.d(TAG, e.getMessage(), e);
        }
        return null;
    }

    public String getLocationHeader() {
        return locationHeader;
    }

    @Override
    public String toString() {
        JSONObject apiResponseAsJson = new JSONObject();
        try {
            apiResponseAsJson.put(STATUS_CODE, statusCode);
            apiResponseAsJson.put(DATA, responseData);
            apiResponseAsJson.put(LOCATION, locationHeader);
        } catch (JSONException e) {
            Log.d(TAG, e.getMessage());
        }
        return apiResponseAsJson.toString();
    }
}
