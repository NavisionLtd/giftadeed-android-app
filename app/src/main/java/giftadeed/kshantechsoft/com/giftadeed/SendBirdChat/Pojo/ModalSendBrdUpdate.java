package giftadeed.kshantechsoft.com.giftadeed.SendBirdChat.Pojo;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModalSendBrdUpdate {
    @SerializedName("unread_message_count")
    @Expose
    private Long unreadMessageCount;
    @SerializedName("is_distinct")
    @Expose
    private Boolean isDistinct;
    @SerializedName("custom_type")
    @Expose
    private String customType;
    @SerializedName("is_ephemeral")
    @Expose
    private Boolean isEphemeral;
    @SerializedName("cover_url")
    @Expose
    private String coverUrl;
    @SerializedName("freeze")
    @Expose
    private Boolean freeze;
    @SerializedName("members")
    @Expose
    private List<Member> members = null;
    @SerializedName("is_public")
    @Expose
    private Boolean isPublic;
    @SerializedName("data")
    @Expose
    private String data;
    @SerializedName("is_super")
    @Expose
    private Boolean isSuper;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("member_count")
    @Expose
    private Integer memberCount;
    @SerializedName("created_at")
    @Expose
    private Integer createdAt;
    @SerializedName("last_message")
    @Expose
    private Object lastMessage;
    @SerializedName("max_length_message")
    @Expose
    private Integer maxLengthMessage;
    @SerializedName("channel_url")
    @Expose
    private String channelUrl;
    @SerializedName("channel")
    @Expose
    private Channel channel;

    public Long getUnreadMessageCount() {
        return unreadMessageCount;
    }

    public void setUnreadMessageCount(Long unreadMessageCount) {
        this.unreadMessageCount = unreadMessageCount;
    }

    public Boolean getIsDistinct() {
        return isDistinct;
    }

    public void setIsDistinct(Boolean isDistinct) {
        this.isDistinct = isDistinct;
    }

    public String getCustomType() {
        return customType;
    }

    public void setCustomType(String customType) {
        this.customType = customType;
    }

    public Boolean getIsEphemeral() {
        return isEphemeral;
    }

    public void setIsEphemeral(Boolean isEphemeral) {
        this.isEphemeral = isEphemeral;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public Boolean getFreeze() {
        return freeze;
    }

    public void setFreeze(Boolean freeze) {
        this.freeze = freeze;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Boolean getIsSuper() {
        return isSuper;
    }

    public void setIsSuper(Boolean isSuper) {
        this.isSuper = isSuper;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(Integer memberCount) {
        this.memberCount = memberCount;
    }

    public Integer getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Integer createdAt) {
        this.createdAt = createdAt;
    }

    public Object getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(Object lastMessage) {
        this.lastMessage = lastMessage;
    }

    public Integer getMaxLengthMessage() {
        return maxLengthMessage;
    }

    public void setMaxLengthMessage(Integer maxLengthMessage) {
        this.maxLengthMessage = maxLengthMessage;
    }

    public String getChannelUrl() {
        return channelUrl;
    }

    public void setChannelUrl(String channelUrl) {
        this.channelUrl = channelUrl;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}