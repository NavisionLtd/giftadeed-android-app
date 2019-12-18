
/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.Signup;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class statedata {

    @SerializedName("State_ID")
    @Expose
    private String stateID;
    @SerializedName("State_Name")
    @Expose
    private String stateName;

    public String getStateID() {
        return stateID;
    }

    public void setStateID(String stateID) {
        this.stateID = stateID;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

}
