
/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.Login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LinkedInLoginModel {

    @SerializedName("checkstatus")
    @Expose
    private Checkstatus checkstatus;

    public Checkstatus getCheckstatus() {
        return checkstatus;
    }

    public void setCheckstatus(Checkstatus checkstatus) {
        this.checkstatus = checkstatus;
    }

}
