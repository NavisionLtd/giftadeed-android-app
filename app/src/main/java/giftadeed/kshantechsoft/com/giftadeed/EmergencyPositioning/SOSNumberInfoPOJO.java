package giftadeed.kshantechsoft.com.giftadeed.EmergencyPositioning;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SOSNumberInfoPOJO {
    @SerializedName("is_blocked")
    @Expose
    private Integer isBlocked;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("success_message")
    @Expose
    private String successMsg;
    @SerializedName("error_message")
    @Expose
    private String errorMsg;
    @SerializedName("emergency_contact_number")
    @Expose
    private String contactNo;

    public Integer getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(Integer isBlocked) {
        this.isBlocked = isBlocked;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }
}
