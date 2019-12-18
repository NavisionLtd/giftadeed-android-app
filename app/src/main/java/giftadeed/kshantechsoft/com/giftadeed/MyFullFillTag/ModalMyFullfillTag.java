
/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.MyFullFillTag;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.list_Model.Taggedlist;

public class ModalMyFullfillTag {
    @SerializedName("is_blocked")
    @Expose
    private Integer isBlocked;
    @SerializedName("Taggedlist")
    @Expose
    private List<Taggedlist> taggedlist = null;

    public List<Taggedlist> getTaggedlist() {
        return taggedlist;
    }

    public void setTaggedlist(List<Taggedlist> taggedlist) {
        this.taggedlist = taggedlist;
    }

    public Integer getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(Integer isBlocked) {
        this.isBlocked = isBlocked;
    }
}
