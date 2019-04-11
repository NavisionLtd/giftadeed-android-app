package giftadeed.kshantechsoft.com.giftadeed.SendBirdChat.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Nilesh on 3/29/2018.
 */

public class RemoveUserFromClub {
    @SerializedName("user_ids")
    @Expose
    private List<String> userIds = null;

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }
}
