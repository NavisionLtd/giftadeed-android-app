
/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.SendBirdChat.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Channel {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("member_count")
    @Expose
    private Integer memberCount;
    @SerializedName("custom_type")
    @Expose
    private String customType;
    @SerializedName("channel_url")
    @Expose
    private String channelUrl;
    @SerializedName("created_at")
    @Expose
    private Integer createdAt;
    @SerializedName("cover_url")
    @Expose
    private String coverUrl;
    @SerializedName("max_length_message")
    @Expose
    private Integer maxLengthMessage;
    @SerializedName("data")
    @Expose
    private String data;

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

    public String getCustomType() {
        return customType;
    }

    public void setCustomType(String customType) {
        this.customType = customType;
    }

    public String getChannelUrl() {
        return channelUrl;
    }

    public void setChannelUrl(String channelUrl) {
        this.channelUrl = channelUrl;
    }

    public Integer getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Integer createdAt) {
        this.createdAt = createdAt;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public Integer getMaxLengthMessage() {
        return maxLengthMessage;
    }

    public void setMaxLengthMessage(Integer maxLengthMessage) {
        this.maxLengthMessage = maxLengthMessage;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

}
