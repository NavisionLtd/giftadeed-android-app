
package giftadeed.kshantechsoft.com.giftadeed.Needdetails;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeedDetail {
    @SerializedName("is_permenant")
    @Expose
    private String permanent;
    @SerializedName("sub_types")
    @Expose
    private String subtypes;
    @SerializedName("views")
    @Expose
    private String views;
    @SerializedName("validity")
    @Expose
    private String validity;
    @SerializedName("container")
    @Expose
    private String container;
    @SerializedName("desc")
    @Expose
    private String desc;
    @SerializedName("cat_type")
    @Expose
    private String catType;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("geoPts")
    @Expose
    private String geoPts;
    @SerializedName("imgUrl")
    @Expose
    private String imgUrl;
    @SerializedName("tagName")
    @Expose
    private String tagName;
    @SerializedName("needMapId")
    @Expose
    private String needMapId;
    @SerializedName("characterPath")
    @Expose
    private String characterPath;
    @SerializedName("iconPath")
    @Expose
    private String iconPath;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("ownerId")
    @Expose
    private String ownerId;
    @SerializedName("fName")
    @Expose
    private String fName;
    @SerializedName("lName")
    @Expose
    private String lName;
    @SerializedName("privacy")
    @Expose
    private String privacy;
    @SerializedName("is_endorse")
    @Expose
    private Integer isEndorse;
    @SerializedName("endorse")
    @Expose
    private String endorse;
    @SerializedName("is_reported")
    @Expose
    private Integer isReported;
    @SerializedName("group_id")
    @Expose
    private String groupID;
    @SerializedName("group_name")
    @Expose
    private String groupName;
    @SerializedName("last_endorse_time")
    @Expose
    private String lastEndorseTime;
    @SerializedName("comments")
    @Expose
    private List<Comment> comments = null;
    @SerializedName("endorse_dist")
    @Expose
    private String endorseDist;
    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGeoPts() {
        return geoPts;
    }

    public void setGeoPts(String geoPts) {
        this.geoPts = geoPts;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getNeedMapId() {
        return needMapId;
    }

    public void setNeedMapId(String needMapId) {
        this.needMapId = needMapId;
    }

    public String getCharacterPath() {
        return characterPath;
    }

    public void setCharacterPath(String characterPath) {
        this.characterPath = characterPath;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getFName() {
        return fName;
    }

    public void setFName(String fName) {
        this.fName = fName;
    }

    public String getLName() {
        return lName;
    }

    public void setLName(String lName) {
        this.lName = lName;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public Integer getIsEndorse() {
        return isEndorse;
    }

    public void setIsEndorse(Integer isEndorse) {
        this.isEndorse = isEndorse;
    }

    public String getEndorse() {
        return endorse;
    }

    public void setEndorse(String endorse) {
        this.endorse = endorse;
    }

    public Integer getIsReported() {
        return isReported;
    }

    public void setIsReported(Integer isReported) {
        this.isReported = isReported;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getEndorseDist() {
        return endorseDist;
    }

    public void setEndorseDist(String endorseDist) {
        this.endorseDist = endorseDist;
    }

    public String getPermanent() {
        return permanent;
    }

    public void setPermanent(String permanent) {
        this.permanent = permanent;
    }

    public String getSubtypes() {
        return subtypes;
    }

    public void setSubtypes(String subtypes) {
        this.subtypes = subtypes;
    }

    public String getCatType() {
        return catType;
    }

    public void setCatType(String catType) {
        this.catType = catType;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getLastEndorseTime() {
        return lastEndorseTime;
    }

    public void setLastEndorseTime(String lastEndorseTime) {
        this.lastEndorseTime = lastEndorseTime;
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }
}