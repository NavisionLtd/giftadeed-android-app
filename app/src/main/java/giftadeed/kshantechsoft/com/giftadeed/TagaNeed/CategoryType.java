
/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.TagaNeed;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryType {
    @SerializedName("g_need")
    @Expose
    private List<Needtype> needtype = null;
    @SerializedName("c_need")
    @Expose
    private List<CustomNeedtype> customneedtype = null;

    public List<Needtype> getNeedtype() {
        return needtype;
    }

    public void setNeedtype(List<Needtype> needtype) {
        this.needtype = needtype;
    }

    public List<CustomNeedtype> getCustomneedtype() {
        return customneedtype;
    }

    public void setCustomneedtype(List<CustomNeedtype> customneedtype) {
        this.customneedtype = customneedtype;
    }
}
