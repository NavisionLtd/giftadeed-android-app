package giftadeed.kshantechsoft.com.giftadeed.Collaboration;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Creatorslist {
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("group_name")
    @Expose
    private String groupNames;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    private boolean isSelected;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGroupNames() {
        return groupNames;
    }

    public void setGroupNames(String groupNames) {
        this.groupNames = groupNames;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}