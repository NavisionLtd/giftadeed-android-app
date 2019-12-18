/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.Group;

import java.util.List;

import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by I-Sys on 11-May-17.
 */


public interface GroupInfoInterface {
    String path = WebServices.Group_Info;

    @FormUrlEncoded
    @POST(path)
    Call<List<GroupInfoPOJO>> sendData(@Field("user_id") String userid, @Field("group_id") String groupid);
}
