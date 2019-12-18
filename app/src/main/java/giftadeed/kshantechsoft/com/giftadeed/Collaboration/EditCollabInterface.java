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
 * Created by I-Sys on 11-May-17.
 */


public interface EditCollabInterface {
    String path = WebServices.Edit_Collab;

    @FormUrlEncoded
    @POST(path)
    Call<CollabResponseStatus> sendData(@Field("collaboration_id") String cid, @Field("group_id") String gid,
                                        @Field("collaboration_name") String cname, @Field("collaboration_description") String cdesc);
}
