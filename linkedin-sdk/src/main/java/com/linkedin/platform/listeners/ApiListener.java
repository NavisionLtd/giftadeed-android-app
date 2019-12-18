/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package com.linkedin.platform.listeners;

import com.linkedin.platform.errors.LIApiError;

public interface ApiListener {

    void onApiSuccess(ApiResponse apiResponse);

    void onApiError(LIApiError LIApiError);
}

