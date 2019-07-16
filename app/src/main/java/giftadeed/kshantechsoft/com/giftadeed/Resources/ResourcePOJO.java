package giftadeed.kshantechsoft.com.giftadeed.Resources;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.Needtype;

public class ResourcePOJO {
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
    @SerializedName("resource_details")
    @Expose
    private List<ResDetailsPojo> resDetailsPojos = null;
    @SerializedName("id")
    @Expose
    String res_id;
    @SerializedName("group_name")
    @Expose
    String group_name;
    @SerializedName("resource_name")
    @Expose
    private String resName;
    @SerializedName("geopoint")
    @Expose
    private String geopoint;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("created_date")
    @Expose
    private String created_date;
    @SerializedName("need_name")
    @Expose
    private String needName;

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

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public List<ResDetailsPojo> getResDetailsPojos() {
        return resDetailsPojos;
    }

    public void setResDetailsPojos(List<ResDetailsPojo> resDetailsPojos) {
        this.resDetailsPojos = resDetailsPojos;
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }


    public String getRes_id() {
        return res_id;
    }

    public void setRes_id(String res_id) {
        this.res_id = res_id;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getNeedName() {
        return needName;
    }

    public void setNeedName(String needName) {
        this.needName = needName;
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

    public String getGeopoint() {
        return geopoint;
    }

    public void setGeopoint(String geopoint) {
        this.geopoint = geopoint;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
