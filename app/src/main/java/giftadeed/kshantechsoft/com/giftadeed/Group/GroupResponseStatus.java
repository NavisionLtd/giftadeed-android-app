
package giftadeed.kshantechsoft.com.giftadeed.Group;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import giftadeed.kshantechsoft.com.giftadeed.Signup.Checkstatus;

public class GroupResponseStatus {
    @SerializedName("is_blocked")
    @Expose
    private Integer isBlocked;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("success_message")
    @Expose
    private String successMsg;
    @SerializedName("error_message")
    @Expose
    private String errorMsg;
    @SerializedName("group_id")
    @Expose
    private Integer groupid;
    @SerializedName("img_path")
    @Expose
    private String grpImagePath;

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

    public Integer getGroupid() {
        return groupid;
    }

    public void setGroupid(Integer groupid) {
        this.groupid = groupid;
    }

    public String getSuccessMsg() {
        return successMsg;
    }

    public void setSuccessMsg(String successMsg) {
        this.successMsg = successMsg;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getGrpImagePath() {
        return grpImagePath;
    }

    public void setGrpImagePath(String grpImagePath) {
        this.grpImagePath = grpImagePath;
    }
}
