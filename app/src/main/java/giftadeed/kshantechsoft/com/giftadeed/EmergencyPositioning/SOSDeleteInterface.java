/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.EmergencyPositioning;

import giftadeed.kshantechsoft.com.giftadeed.Group.GroupResponseStatus;
import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by I-Sys on 05-Mar-18.
 */

public interface SOSDeleteInterface {
    String path= WebServices.Delete_SOS;
    @FormUrlEncoded
    @POST(path)
    Call<GroupResponseStatus> fetchData(@Field("user_id") String User_ID, @Field("sos_id") String sosId);
}
