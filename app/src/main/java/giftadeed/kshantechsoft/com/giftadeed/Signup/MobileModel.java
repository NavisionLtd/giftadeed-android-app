
package giftadeed.kshantechsoft.com.giftadeed.Signup;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MobileModel {
    @SerializedName("is_blocked")
    @Expose
    private Integer isBlocked;
    @SerializedName("checkstatus")
    @Expose
    private List<Checkstatus> checkstatus = null;

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
}
