/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package com.linkedin.platform;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class AccessToken implements Serializable {

    private static final String ACCESS_TOKEN_VALUE = "accessTokenValue";
    private static final String EXPIRES_ON = "expiresOn";
    private static final String TAG = AccessToken.class.getSimpleName();

    private final String accessTokenValue;
    private final long expiresOn;

    /**
     * build an accessToken from a previously retrieved value
     * @param accessToken obtained by calling {@link AccessToken#toString()}
     * @return
     */
    public synchronized static AccessToken buildAccessToken(String accessToken) {
        if (accessToken == null || "".equals(accessToken)) {
            return null;
        }
        try {
            JSONObject jsonObject = new JSONObject(accessToken);
            return new AccessToken(jsonObject);
        } catch (JSONException e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }

    public synchronized static AccessToken buildAccessToken(JSONObject accessToken) {
        if (accessToken == null) {
            return null;
        }
        try {
            return new AccessToken(accessToken);
        } catch (JSONException e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }

    private AccessToken(JSONObject accessTokenJson) throws JSONException {
        accessTokenValue = accessTokenJson.getString(ACCESS_TOKEN_VALUE);
        expiresOn = accessTokenJson.getLong(EXPIRES_ON);
    }

    public AccessToken(String accessTokenValue, long expiresOn) {
        this.accessTokenValue = accessTokenValue;
        this.expiresOn = expiresOn;
    }

    public String getValue() {
        return accessTokenValue;
    }

    /**
     * @return the time when this AccessToken expires
     */
    public long getExpiresOn() {
        return expiresOn;
    }

    /**
     * @return true if access token is expired; false otherwise
     */
    public boolean isExpired() {
        return  System.currentTimeMillis() > getExpiresOn();
    }

    @Override
    public String toString() {
        try {
            JSONObject json = new JSONObject();
            json.put(ACCESS_TOKEN_VALUE, accessTokenValue);
            json.put(EXPIRES_ON, expiresOn);
            return json.toString();
        } catch (JSONException e) {
        }
        return null;
    }
}
