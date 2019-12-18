/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.Collaboration;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Colabrequestlist {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("collaboration_id")
    @Expose
    private String collabId;
    @SerializedName("creator_id")
    @Expose
    private String creator_id;
    @SerializedName("collaboration_name")
    @Expose
    private String colabName;
    @SerializedName("collaboration_description")
    @Expose
    private String colabDesc;
    @SerializedName("collaboration_start_date")
    @Expose
    private String colabStartDate;
    @SerializedName("invitation_status")
    @Expose
    private String inviteStatus;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCollabId() {
        return collabId;
    }

    public void setCollabId(String collabId) {
        this.collabId = collabId;
    }

    public String getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(String creator_id) {
        this.creator_id = creator_id;
    }

    public String getColabName() {
        return colabName;
    }

    public void setColabName(String colabName) {
        this.colabName = colabName;
    }

    public String getColabDesc() {
        return colabDesc;
    }

    public void setColabDesc(String colabDesc) {
        this.colabDesc = colabDesc;
    }

    public String getColabStartDate() {
        return colabStartDate;
    }

    public void setColabStartDate(String colabStartDate) {
        this.colabStartDate = colabStartDate;
    }

    public String getInviteStatus() {
        return inviteStatus;
    }

    public void setInviteStatus(String inviteStatus) {
        this.inviteStatus = inviteStatus;
    }
}