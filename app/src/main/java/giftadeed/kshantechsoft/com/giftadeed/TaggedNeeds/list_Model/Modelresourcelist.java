
/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.list_Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Modelresourcelist {
    @SerializedName("is_blocked")
    @Expose
    private Integer isBlocked;
    @SerializedName("resource_list")
    @Expose
    public List<Resourcelist> reslist = null;
    @SerializedName("marker")
    @Expose
    private String marker_path;

    public Integer getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(Integer isBlocked) {
        this.isBlocked = isBlocked;
    }

    public List<Resourcelist> getReslist() {
        return reslist;
    }

    public void setReslist(List<Resourcelist> reslist) {
        this.reslist = reslist;
    }

    public String getMarker_path() {
        return marker_path;
    }

    public void setMarker_path(String marker_path) {
        this.marker_path = marker_path;
    }
}
