/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds;

import giftadeed.kshantechsoft.com.giftadeed.Group.GroupResponseStatus;
import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by I-Sys on 10-May-17.
 */

public interface RemoveLocationInterface {
    String path = WebServices.Remove_Location;

    @FormUrlEncoded
    @POST(path)
    Call<GroupResponseStatus> sendData(@Field("user_id") String userid, @Field("tag_id") String tagid);
}