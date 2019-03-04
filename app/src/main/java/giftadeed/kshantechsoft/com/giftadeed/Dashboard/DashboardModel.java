
package giftadeed.kshantechsoft.com.giftadeed.Dashboard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DashboardModel {

    @SerializedName("lastDeedDate")
    @Expose
    private String lastDeedDate;
    @SerializedName("totTags")
    @Expose
    private String totTags;
    @SerializedName("totFulfills")
    @Expose
    private String totFulfills;
    @SerializedName("tagSuccessPercent")
    @Expose
    private Double tagSuccessPercent;
    @SerializedName("totPoints")
    @Expose
    private String totPoints;
    @SerializedName("is_blocked")
    @Expose
    private Integer isBlocked;
    public String getLastDeedDate() {
        return lastDeedDate;
    }

    public void setLastDeedDate(String lastDeedDate) {
        this.lastDeedDate = lastDeedDate;
    }

    public String getTotTags() {
        return totTags;
    }

    public void setTotTags(String totTags) {
        this.totTags = totTags;
    }

    public String getTotFulfills() {
        return totFulfills;
    }

    public void setTotFulfills(String totFulfills) {
        this.totFulfills = totFulfills;
    }

    public Double getTagSuccessPercent() {
        return tagSuccessPercent;
    }

    public void setTagSuccessPercent(Double tagSuccessPercent) {
        this.tagSuccessPercent = tagSuccessPercent;
    }

    public String getTotPoints() {
        return totPoints;
    }

    public void setTotPoints(String totPoints) {
        this.totPoints = totPoints;
    }

    public Integer getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(Integer isBlocked) {
        this.isBlocked = isBlocked;
    }
}
