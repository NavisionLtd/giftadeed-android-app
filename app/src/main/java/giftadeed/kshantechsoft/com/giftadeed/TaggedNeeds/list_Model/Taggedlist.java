

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

    public Taggedlist(String taggedTitle, String address, String fullFilledPhotoPath, String charpath, String fullFilledDatetime, String fullFilledPoints, String status,String views,String endorse,String permanent) {
        this.taggedTitle = taggedTitle;
        this.address = address;
        this.fullFilledPhotoPath = fullFilledPhotoPath;
        this.fullFilledDatetime = fullFilledDatetime;
        this.fullFilledPoints = fullFilledPoints;
        this.tagStatus = status;
        this.characterPath = charpath;
        this.views=views;
        this.endorse=endorse;
        this.permanent=permanent;
    }

    public Taggedlist(String taggedPhotoPath, String address, String taggedTitle, String charpath, String taggedDatetime, String status,String views,String endorse,String permanent) {
        this.taggedPhotoPath = taggedPhotoPath;
        this.address = address;
        this.taggedTitle = taggedTitle;
        this.characterPath = charpath;
        this.taggedDatetime = taggedDatetime;
        this.tagStatus = status;
        this.views=views;
        this.endorse=endorse;
        this.permanent=permanent;
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
}