
/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.Signup;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class countrydata {

    @SerializedName("Cntry_ID")
    @Expose
    private String cntryID;
    @SerializedName("Cntry_Name")
    @Expose
    private String cntryName;

    public String getCntryID() {
        return cntryID;
    }

    public void setCntryID(String cntryID) {
        this.cntryID = cntryID;
    }

    public String getCntryName() {
        return cntryName;
    }

    public void setCntryName(String cntryName) {
        this.cntryName = cntryName;
    }

}
