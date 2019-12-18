/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package com.linkedin.platform.listeners;

import com.linkedin.platform.errors.LIDeepLinkError;

public interface DeepLinkListener {

    void onDeepLinkSuccess();

    void onDeepLinkError(LIDeepLinkError error);
}

