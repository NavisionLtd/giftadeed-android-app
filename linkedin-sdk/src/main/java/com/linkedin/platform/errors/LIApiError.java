/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package com.linkedin.platform.errors;

import com.android.volley.VolleyError;

import org.json.JSONException;

public class LIApiError extends Exception {

    public static enum ErrorType {
        accessTokenIsNotSet,
        apiErrorResponse,
        other;
    }

    private VolleyError volleyError;
    private int httpStatusCode = -1;
    private ApiErrorResponse apiErrorResponse;
    private ErrorType errorType;

    public static LIApiError buildLiApiError(VolleyError volleyError) {
        return new LIApiError(volleyError);
    }

    public LIApiError(String detailMessage, Throwable throwable) {
        this(ErrorType.other, detailMessage, throwable);
    }

    public LIApiError(ErrorType errorType, String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
        this.errorType = errorType;
    }

    public LIApiError(VolleyError volleyError) {
        super(volleyError.getMessage(), volleyError.fillInStackTrace());
        this.volleyError = volleyError;
        if (volleyError.networkResponse != null) {
            httpStatusCode = volleyError.networkResponse.statusCode;
            try {
                apiErrorResponse = ApiErrorResponse.build(volleyError.networkResponse.data);
                errorType = ErrorType.apiErrorResponse;
            } catch (JSONException e) {
                errorType = ErrorType.other;
            }
        }
    }

    public ApiErrorResponse getApiErrorResponse() {
        return apiErrorResponse;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    @Override
    public String toString() {
        return apiErrorResponse == null ? "exceptionMsg: " + super.getMessage() : apiErrorResponse.toString();
    }

}
