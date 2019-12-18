
/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.Tagcounter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TagCounterModel {
    @SerializedName("is_blocked")
    @Expose
    private Integer isBlocked;
    @SerializedName("tagged")
    @Expose
    private String tagged;
    @SerializedName("fulfilled")
    @Expose
    private String fulfilled;

    public String getTagged() {
        return tagged;
    }

    public void setTagged(String tagged) {
        this.tagged = tagged;
    }

    public String getFulfilled() {
        return fulfilled;
    }

    public void setFulfilled(String fulfilled) {
        this.fulfilled = fulfilled;
    }

    public Integer getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(Integer isBlocked) {
        this.isBlocked = isBlocked;
    }
}
