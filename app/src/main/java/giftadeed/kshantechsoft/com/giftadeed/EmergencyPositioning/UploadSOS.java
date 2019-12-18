/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.EmergencyPositioning;

public class UploadSOS {
    public String sosid;
    public String sosurl;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public UploadSOS() {
    }

    public UploadSOS(String sosid, String sosurl) {
        this.sosid = sosid;
        this.sosurl= sosurl;
    }

    public String getSosid() {
        return sosid;
    }

    public void setSosid(String sosid) {
        this.sosid = sosid;
    }

    public String getSosurl() {
        return sosurl;
    }

    public void setSosurl(String sosurl) {
        this.sosurl = sosurl;
    }
}
