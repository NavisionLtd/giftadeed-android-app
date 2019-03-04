package giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by I-Sys on 12-May-17.
 */

public class RowData {
    private String id;
    private String title;
    private String date;
    private String address;
    private double distance;
    private String imagepath;
    private String views;
    private String endorse;
    private String getIconPath;
    private String permanent;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }



    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }




    private String taggedID;




    private String geopoint;

    private String taggedPhotoPath;

    private String description;





    private String characterPath;

    private String fname;

    private String lname;

    private String privacy;

    private String userID;

    private String needName;

    private String totalTaggedCreditPoints;

    private String totalFulfilledCreditPoints;

    public String getTaggedID() {
        return taggedID;
    }

    public void setTaggedID(String taggedID) {
        this.taggedID = taggedID;
    }

    public String getGetIconPath() {
        return getIconPath;
    }

    public void setGetIconPath(String getIconPath) {
        this.getIconPath = getIconPath;
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
}
