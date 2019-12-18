
/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationCountModel {

    @SerializedName("nt_count")
    @Expose
    private String ntCount;

    public String getNtCount() {
        return ntCount;
    }

    public void setNtCount(String ntCount) {
        this.ntCount = ntCount;
    }

}
