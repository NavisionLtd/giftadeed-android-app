/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.Group;

/**
 * Created by Nilesh on 2/27/2018.
 */
/*this Pojo class has been created for getting and adding the data from the group channel list detail on page PendingRequestMemeberListFragment*/
public class GroupListInfo {

    private String mData;
    private String mChannelName;
    private String mUrl;


    public String getmData() {
        return mData;
    }

    public void setmData(String mData) {
        this.mData = mData;
    }

    public String getmChannelName() {
        return mChannelName;
    }

    public void setmChannelName(String mChannelName) {
        this.mChannelName = mChannelName;
    }

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }


    public GroupListInfo(String mData, String mChannelName, String mUrl) {
        this.mData = mData;
        this.mChannelName = mChannelName;
        this.mUrl = mUrl;
    }



}
