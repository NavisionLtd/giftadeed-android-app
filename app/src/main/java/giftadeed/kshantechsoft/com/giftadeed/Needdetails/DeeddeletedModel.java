
/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.Needdetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeeddeletedModel {
    @SerializedName("is_blocked")
    @Expose
    private Integer isBlocked;
    @SerializedName("is_deleted")
    @Expose
    private Integer isDeleted;

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Integer getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(Integer isBlocked) {
        this.isBlocked = isBlocked;
    }
}
