/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.Needdetails;

import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by I-Sys on 05-Mar-18.
 */

public interface EndorsedeedInterface {
    String path= WebServices.endorsedeed;
    @FormUrlEncoded
    @POST(path)
    Call<StatusModel> fetchData(@Field("userId") String User_ID, @Field("deedId") String deedId);
}
