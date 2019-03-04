
package giftadeed.kshantechsoft.com.giftadeed.Needdetails;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeedDetailsModel {
    @SerializedName("is_blocked")
    @Expose
    private Integer isBlocked;
    @SerializedName("deed_details")
    @Expose
    private List<DeedDetail> deedDetails = null;

    public List<DeedDetail> getDeedDetails() {
        return deedDetails;
    }

    public void setDeedDetails(List<DeedDetail> deedDetails) {
        this.deedDetails = deedDetails;
    }

    public Integer getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(Integer isBlocked) {
        this.isBlocked = isBlocked;
    }
}
