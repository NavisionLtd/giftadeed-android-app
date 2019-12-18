/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.TagaNeed;

import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by I-Sys on 10-May-17.
 */

public interface GetAddressInterface {
    String path = WebServices.GET_ADDRESS;

    @FormUrlEncoded
    @POST(path)
    Call<GetAddressResponse> sendData(@Field("latitude") String latitude, @Field("longitude") String longitude);
}