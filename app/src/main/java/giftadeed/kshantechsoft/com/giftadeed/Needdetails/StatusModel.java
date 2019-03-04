
package giftadeed.kshantechsoft.com.giftadeed.Needdetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StatusModel {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("is_blocked")
    @Expose
    private Integer isBlocked;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(Integer isBlocked) {
        this.isBlocked = isBlocked;
    }
}
