/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.Collaboration;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Colabinfo {
    @SerializedName("user_name")
    @Expose
    private String username;
    @SerializedName("user_id")
    @Expose
    private String userid;
    @SerializedName("collaboration_name")
    @Expose
    private String collabname;
    @SerializedName("collaboration_description")
    @Expose
    private String collabdesc;
    @SerializedName("collaboration_start_date")
    @Expose
    private String collabstartDate;
    @SerializedName("group_id")
    @Expose
    private String groupid;
    @SerializedName("group_name")
    @Expose
    private String groupname;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getCollabname() {
        return collabname;
    }

    public void setCollabname(String collabname) {
        this.collabname = collabname;
    }

    public String getCollabdesc() {
        return collabdesc;
    }

    public void setCollabdesc(String collabdesc) {
        this.collabdesc = collabdesc;
    }

    public String getCollabstartDate() {
        return collabstartDate;
    }

    public void setCollabstartDate(String collabstartDate) {
        this.collabstartDate = collabstartDate;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }
}