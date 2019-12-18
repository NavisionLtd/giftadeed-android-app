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
 * Created by I-Sys on 10-May-17.
 */

public interface MemberListInterface {
    String path = WebServices.Member_List;

    @FormUrlEncoded
    @POST(path)
    Call<Memberlist> sendData(@Field("user_id") String userid, @Field("group_id") String groupid, @Field("role") String role);
}