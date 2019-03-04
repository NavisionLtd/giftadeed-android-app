
package giftadeed.kshantechsoft.com.giftadeed.EmergencyPositioning;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SOSResponseStatus {
    @SerializedName("is_blocked")
    @Expose
    private Integer isBlocked;
    @SerializedName("sos_id")
    @Expose
    private Integer sos_id;

    public Integer getSos_id() {
        return sos_id;
    }

    public void setSos_id(Integer sos_id) {
        this.sos_id = sos_id;
    }

    public Integer getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(Integer isBlocked) {
        this.isBlocked = isBlocked;
    }
}
