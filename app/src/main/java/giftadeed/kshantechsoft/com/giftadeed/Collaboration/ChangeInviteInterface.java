/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.Collaboration;

import giftadeed.kshantechsoft.com.giftadeed.Group.GroupResponseStatus;
import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by I-Sys on 10-May-17.
 */

public interface ChangeInviteInterface {
    String path = WebServices.Accept_Reject_Request;

    @FormUrlEncoded
    @POST(path)
    Call<CollabResponseStatus> sendData(@Field("user_id") String userid, @Field("collaboration_id") String cid, @Field("invitation_status") String status);
}