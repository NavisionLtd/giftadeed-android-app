package giftadeed.kshantechsoft.com.giftadeed.Resources;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.Needtype;

public class ResourcePOJO {
    @SerializedName("is_blocked")
    @Expose
    private Integer isBlocked;
    @SerializedName("id")
    @Expose
    String res_id;
    @SerializedName("group_name")
    @Expose
    String group_name;
    @SerializedName("creator_id")
    @Expose
    String creatorId;
    @SerializedName("resource_name")
    @Expose
    private String resName;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("sub_type")
    @Expose
    private List<SubType> subTypes = null;
    @SerializedName("geopoint")
    @Expose
    private String geopoint;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("created_at")
    @Expose
    private String created_at;
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

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public List<SubType> getSubTypes() {
        return subTypes;
    }

    public void setSubTypes(List<SubType> subTypes) {
        this.subTypes = subTypes;
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

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
}
