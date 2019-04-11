
package giftadeed.kshantechsoft.com.giftadeed.SendBirdChat.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Member {

    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("friend_discovery_key")
    @Expose
    private Object friendDiscoveryKey;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("is_online")
    @Expose
    private Boolean isOnline;
    @SerializedName("is_active")
    @Expose
    private Boolean isActive;
    @SerializedName("last_seen_at")
    @Expose
    private Long lastSeenAt;
    @SerializedName("nickname")
    @Expose
    private String nickname;
    @SerializedName("profile_url")
    @Expose
    private String profileUrl;
    @SerializedName("metadata")
    @Expose
    private Metadata metadata;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Object getFriendDiscoveryKey() {
        return friendDiscoveryKey;
    }

    public void setFriendDiscoveryKey(Object friendDiscoveryKey) {
        this.friendDiscoveryKey = friendDiscoveryKey;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Boolean getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(Boolean isOnline) {
        this.isOnline = isOnline;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Long getLastSeenAt() {
        return lastSeenAt;
    }

    public void setLastSeenAt(Long lastSeenAt) {
        this.lastSeenAt = lastSeenAt;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

}
