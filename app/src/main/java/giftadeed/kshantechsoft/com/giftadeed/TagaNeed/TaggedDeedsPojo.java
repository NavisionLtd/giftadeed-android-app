/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.TagaNeed;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TaggedDeedsPojo {
    @SerializedName("is_blocked")
    @Expose
    private Integer isBlocked;
    @SerializedName("tag_id")
    @Expose
    private String tagid;
    @SerializedName("need_name")
    @Expose
    private String needname;
    @SerializedName("sub_types")
    @Expose
    private String subtypes;
    @SerializedName("icon_path")
    @Expose
    private String iconpath;

    public String getTagid() {
        return tagid;
    }

    public void setTagid(String tagid) {
        this.tagid = tagid;
    }

    public String getNeedname() {
        return needname;
    }

    public void setNeedname(String needname) {
        this.needname = needname;
    }

    public String getIconpath() {
        return iconpath;
    }

    public void setIconpath(String iconpath) {
        this.iconpath = iconpath;
    }

    public Integer getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(Integer isBlocked) {
        this.isBlocked = isBlocked;
    }

    public String getSubtypes() {
        return subtypes;
    }

    public void setSubtypes(String subtypes) {
        this.subtypes = subtypes;
    }
}
