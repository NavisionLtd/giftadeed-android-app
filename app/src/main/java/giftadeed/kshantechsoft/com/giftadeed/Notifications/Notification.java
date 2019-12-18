
/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.Notifications;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Notification {
    @SerializedName("tag_id")
    @Expose
    private String tagid;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("nt_type")
    @Expose
    private String ntType;
    @SerializedName("tag_type")
    @Expose
    private String tagType;
    @SerializedName("Geopoint")
    @Expose
    private String geopoint;
    @SerializedName("Need_Name")
    @Expose
    private String needName;
    @SerializedName("seen")
    @Expose
    private String seen;
    @SerializedName("distance_in_kms")
    @Expose
    private String distanceInKms;

    public String getTagid() {
        return tagid;
    }

    public void setTagid(String tagid) {
        this.tagid = tagid;
    }

    public String getNeedName() {
        return needName;
    }

    public void setNeedName(String needName) {
        this.needName = needName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNtType() {
        return ntType;
    }

    public void setNtType(String ntType) {
        this.ntType = ntType;
    }

    public String getTagType() {
        return tagType;
    }

    public void setTagType(String tagType) {
        this.tagType = tagType;
    }

    public String getGeopoint() {
        return geopoint;
    }

    public void setGeopoint(String geopoint) {
        this.geopoint = geopoint;
    }

    public String getSeen() {
        return seen;
    }

    public void setSeen(String seen) {
        this.seen = seen;
    }

    public String getDistanceInKms() {
        return distanceInKms;
    }

    public void setDistanceInKms(String distanceInKms) {
        this.distanceInKms = distanceInKms;
    }
}
