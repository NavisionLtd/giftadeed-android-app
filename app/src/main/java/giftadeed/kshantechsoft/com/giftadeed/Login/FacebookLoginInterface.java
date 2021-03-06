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
 * Created by I-Sys on 18-Jan-18.
 */

public interface FacebookLoginInterface {

        String path = WebServices.facebooklogin;
        @FormUrlEncoded
        @POST(path)
        Call<GoogleLoginModel> sendData(@Field("Fname") String fname,
                                        @Field("Lname") String lname,
                                        @Field("Email") String email,
                                        @Field("Device_ID") String did,
                                        @Field("Social_Login_ID") String fb_id);

    }
