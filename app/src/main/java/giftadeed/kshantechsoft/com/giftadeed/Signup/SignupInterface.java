/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.Signup;

import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by I-Sys on 29-Apr-17.
 */

public interface SignupInterface {
    String path = WebServices.signup;

    @FormUrlEncoded
    @POST(path)
    Call<MobileModel> sendData(@Field("Fname") String fnmae,
                               @Field("Lname") String lnmae,
                               @Field("Email") String email,
                               @Field("Country_ID") String cntry_id,
                               @Field("Device_ID") String did);
}
