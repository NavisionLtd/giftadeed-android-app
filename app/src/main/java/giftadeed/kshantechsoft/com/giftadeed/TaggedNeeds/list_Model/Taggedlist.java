package giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.list_Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Taggedlist {
    @SerializedName("is_blocked")
    @Expose
    private Integer isBlocked;
    @SerializedName("Tagged_ID")
    @Expose
    private String taggedID;
    @SerializedName("Tagged_Title")
    @Expose
    private String taggedTitle;
    @SerializedName("Address")
    @Expose
    private String address;
    @SerializedName("Geopoint")
    @Expose
    private String geopoint;
    @SerializedName("Tagged_Photo_Path")
    @Expose
    private String taggedPhotoPath;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("Tagged_Datetime")
    @Expose
    private String taggedDatetime;
    @SerializedName("cat_type")
    @Expose
    private String catType;
    @SerializedName("Category_Type")
    @Expose
    private String categoryType;
    @SerializedName("Icon_Path")
    @Expose
    private String iconPath;
    @SerializedName("Character_Path")
    @Expose
    private String characterPath;
    @SerializedName("Fname")
    @Expose
    private String fname;
    @SerializedName("Lname")
    @Expose
    private String lname;
    @SerializedName("Privacy")
    @Expose
    private String privacy;
    @SerializedName("User_ID")
    @Expose
    private String userID;
    @SerializedName("Need_Name")
    @Expose
    private String needName;
    @SerializedName("Total_TaggedCredit_Points")
    @Expose
    private String totalTaggedCreditPoints;
    @SerializedName("Total_FulfilledCredit_Points")
    @Expose
    private String totalFulfilledCreditPoints;
    @SerializedName("TagStatus")
    @Expose
    public String tagStatus;
    @SerializedName("FullFilled_Photo_Path")
    @Expose
    private String fullFilledPhotoPath;
    @SerializedName("FullFilled_Datetime")
    @Expose
    private String fullFilledDatetime;
    @SerializedName("FullFilled_Points")
    @Expose
    private String fullFilledPoints;
    @SerializedName("Views")
    @Expose
    private String views;
    @SerializedName("Endorse")
    @Expose
    private String endorse;
    @SerializedName("is_permanent")
    @Expose
    private String permanent;
    @SerializedName("all_groups")
    @Expose
    private String all_groups;
    @SerializedName("user_grp_ids")
    @Expose
    private String userGrpIds;
    @SerializedName("from_group")
    @Expose
    private String fromGroupID;
    @SerializedName("user_group_names")
    @Expose
    private String userGroupNames;

    public Taggedlist(String taggedPhotoPath, String address, String taggedTitle, String charpath, String taggedDatetime,
                      String status, String views, String endorse, String permanent, String catType, String fullFilledPhotoPath,
                      String fullFilledDatetime, String fullFilledPoints) {
        this.taggedPhotoPath = taggedPhotoPath;
        this.address = address;
        this.taggedTitle = taggedTitle;
        this.characterPath = charpath;
        this.taggedDatetime = taggedDatetime;
        this.tagStatus = status;
        this.views = views;
        this.endorse = endorse;
        this.permanent = permanent;
        this.categoryType = catType;
        this.fullFilledPhotoPath = fullFilledPhotoPath;
        this.fullFilledDatetime = fullFilledDatetime;
        this.fullFilledPoints = fullFilledPoints;
    }

    public Integer getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(Integer isBlocked) {
        this.isBlocked = isBlocked;
    }

    public String getPermanent() {
        return permanent;
    }

    public void setPermanent(String permanent) {
        this.permanent = permanent;
    }

    public String getEndorse() {
        return endorse;
    }

    public void setEndorse(String endorse) {
        this.endorse = endorse;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getTaggedID() {
        return taggedID;
    }

    public void setTaggedID(String taggedID) {
        this.taggedID = taggedID;
    }

    public String getTaggedTitle() {
        return taggedTitle;
    }

    public void setTaggedTitle(String taggedTitle) {
        this.taggedTitle = taggedTitle;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGeopoint() {
        return geopoint;
    }

    public void setGeopoint(String geopoint) {
        this.geopoint = geopoint;
    }

    public String getTaggedPhotoPath() {
        return taggedPhotoPath;
    }

    public void setTaggedPhotoPath(String taggedPhotoPath) {
        this.taggedPhotoPath = taggedPhotoPath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTaggedDatetime() {
        return taggedDatetime;
    }

    public void setTaggedDatetime(String taggedDatetime) {
        this.taggedDatetime = taggedDatetime;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public String getCharacterPath() {
        return characterPath;
    }

    public void setCharacterPath(String characterPath) {
        this.characterPath = characterPath;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getNeedName() {
        return needName;
    }

    public void setNeedName(String needName) {
        this.needName = needName;
    }

    public String getTotalTaggedCreditPoints() {
        return totalTaggedCreditPoints;
    }

    public void setTotalTaggedCreditPoints(String totalTaggedCreditPoints) {
        this.totalTaggedCreditPoints = totalTaggedCreditPoints;
    }

    public String getTotalFulfilledCreditPoints() {
        return totalFulfilledCreditPoints;
    }

    public void setTotalFulfilledCreditPoints(String totalFulfilledCreditPoints) {
        this.totalFulfilledCreditPoints = totalFulfilledCreditPoints;
    }

    public String getTagStatus() {
        return tagStatus;
    }

    public void setTagStatus(String tagStatus) {
        this.tagStatus = tagStatus;
    }


    public String getFullFilledPhotoPath() {
        return fullFilledPhotoPath;
    }

    public void setFullFilledPhotoPath(String fullFilledPhotoPath) {
        this.fullFilledPhotoPath = fullFilledPhotoPath;
    }

    public String getFullFilledDatetime() {
        return fullFilledDatetime;
    }

    public void setFullFilledDatetime(String fullFilledDatetime) {
        this.fullFilledDatetime = fullFilledDatetime;
    }

    public String getFullFilledPoints() {
        return fullFilledPoints;
    }

    public void setFullFilledPoints(String fullFilledPoints) {
        this.fullFilledPoints = fullFilledPoints;
    }

    public String getCatType() {
        return catType;
    }

    public void setCatType(String catType) {
        this.catType = catType;
    }

    public String getAll_groups() {
        return all_groups;
    }

    public void setAll_groups(String all_groups) {
        this.all_groups = all_groups;
    }

    public String getUserGrpIds() {
        return userGrpIds;
    }

    public void setUserGrpIds(String userGrpIds) {
        this.userGrpIds = userGrpIds;
    }

    public String getFromGroupID() {
        return fromGroupID;
    }

    public void setFromGroupID(String fromGroupID) {
        this.fromGroupID = fromGroupID;
    }

    public String getUserGroupNames() {
        return userGroupNames;
    }

    public void setUserGroupNames(String userGroupNames) {
        this.userGroupNames = userGroupNames;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }
}