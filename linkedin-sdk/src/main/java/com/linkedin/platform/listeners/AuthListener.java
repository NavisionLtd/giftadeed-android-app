/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package com.linkedin.platform.listeners;

import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.errors.LIDeepLinkError;

public interface AuthListener {

    /**
     * called when the application has been granted authorization to access the LinkedIn member's data
     * see {@link com.linkedin.platform.APIHelper} and {@link com.linkedin.platform.DeepLinkHelper} for
     * how to access LinkedIn data and interact with the LinkedIn application
     */
    void onAuthSuccess();

    /**
     * called when the application has not been granted authorization to access the LinkedIn member's
     * data
     * @param error information on why the authorization did not occur
     */
    void onAuthError(LIAuthError error);
}

