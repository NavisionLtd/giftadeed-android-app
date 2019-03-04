
package giftadeed.kshantechsoft.com.giftadeed.TagaNeed;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import giftadeed.kshantechsoft.com.giftadeed.Signup.Checkstatus;

public class SuggestType {
    @SerializedName("is_blocked")
    @Expose
    private Integer isBlocked;
    @SerializedName("checkstatus")
    @Expose
    private List<Checkstatus> checkstatus = null;
    @SerializedName("status")
    @Expose
    private Integer status;

    public List<Checkstatus> getCheckstatus() {
        return checkstatus;
    }

    public void setCheckstatus(List<Checkstatus> checkstatus) {
        this.checkstatus = checkstatus;
    }

    public Integer getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(Integer isBlocked) {
        this.isBlocked = isBlocked;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
