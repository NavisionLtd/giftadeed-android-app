
package giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.list_Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Modelsoslist {
    @SerializedName("is_blocked")
    @Expose
    private Integer isBlocked;
    @SerializedName("sos_list")
    @Expose
    public List<SOSlist> soslist = null;
    @SerializedName("marker_path")
    @Expose
    private String marker_path;

    public Integer getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(Integer isBlocked) {
        this.isBlocked = isBlocked;
    }

    public List<SOSlist> getSoslist() {
        return soslist;
    }

    public void setSoslist(List<SOSlist> soslist) {
        this.soslist = soslist;
    }

    public String getMarker_path() {
        return marker_path;
    }

    public void setMarker_path(String marker_path) {
        this.marker_path = marker_path;
    }
}
