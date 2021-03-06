/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.Login;

import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by I-Sys on 12-Jan-18.
 */

public interface GoogleLoginInterface {
    String path= WebServices.socialmedia;
    @FormUrlEncoded
    @POST(path)
    Call<GoogleLoginModel> sendData(@Field("Fname") String fname,
                                    @Field("Lname") String lname,
                                    @Field("Email") String email,
                                    @Field("Device_ID") String did,
                                    @Field("Login_Type") String Login_Type);
}
