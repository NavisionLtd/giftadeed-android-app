
/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.list_Model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Modeltaglist {
    @SerializedName("is_blocked")
    @Expose
    private Integer isBlocked;

    @SerializedName("deed_list")
    @Expose
    public List<Taggedlist> taggedlist = null;

    @SerializedName("p_marker")
    @Expose
    public String permanent_path;

    public List<Taggedlist> getTaggedlist() {
        return taggedlist;
    }
    public Integer getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(Integer isBlocked) {
        this.isBlocked = isBlocked;
    }
    public void setTaggedlist(List<Taggedlist> taggedlist) {
        this.taggedlist = taggedlist;
    }

    public String getPermanent_path() {
        return permanent_path;
    }

    public void setPermanent_path(String permanent_path) {
        this.permanent_path = permanent_path;
    }
}
