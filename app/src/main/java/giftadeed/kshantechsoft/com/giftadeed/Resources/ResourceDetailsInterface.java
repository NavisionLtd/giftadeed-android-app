/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.Resources;

import java.util.List;

import giftadeed.kshantechsoft.com.giftadeed.Needdetails.DeedDetailsModel;
import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by I-Sys on 05-Mar-18.
 */

public interface ResourceDetailsInterface {
    String path= WebServices.Resource_Details;
    @FormUrlEncoded
    @POST(path)
    Call<ResourcePOJO> fetchData(@Field("user_id") String User_ID, @Field("resource_id") String resId);
}
