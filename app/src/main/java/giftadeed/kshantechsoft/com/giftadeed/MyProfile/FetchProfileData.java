
/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.MyProfile;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FetchProfileData {
    @SerializedName("is_blocked")
    @Expose
    private Integer isBlocked;
    @SerializedName("profiledata")
    @Expose
    private List<Profiledatum> profiledata = null;

    public List<Profiledatum> getProfiledata() {
        return profiledata;
    }

    public void setProfiledata(List<Profiledatum> profiledata) {
        this.profiledata = profiledata;
    }

    public Integer getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(Integer isBlocked) {
        this.isBlocked = isBlocked;
    }
}
