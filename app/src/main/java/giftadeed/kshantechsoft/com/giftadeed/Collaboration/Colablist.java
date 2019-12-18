/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.Collaboration;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Colablist {
    @SerializedName("collaboration_id")
    @Expose
    private String id;
    @SerializedName("collaboration_name")
    @Expose
    private String colabName;
    @SerializedName("user_role")
    @Expose
    private String userRole;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getColabName() {
        return colabName;
    }

    public void setColabName(String colabName) {
        this.colabName = colabName;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
}