package giftadeed.kshantechsoft.com.giftadeed.Mytags;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.list_Model.Taggedlist;

/**
 * Created by Nilesh on 5/16/2017.
 */

public class ModelMytaglist {
    @SerializedName("is_blocked")
    @Expose
    private Integer isBlocked;
    @SerializedName("Taggedlist")
    @Expose
    public  List<Taggedlist> taggedlist = null;

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

