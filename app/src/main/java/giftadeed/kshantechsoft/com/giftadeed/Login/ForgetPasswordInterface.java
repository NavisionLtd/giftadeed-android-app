/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.Login;

import giftadeed.kshantechsoft.com.giftadeed.Signup.MobileModel;
import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

import static giftadeed.kshantechsoft.com.giftadeed.Login.ForgetPasswordInterface.path;

/**
 * Created by I-Sys on 29-Apr-17.
 */

public interface ForgetPasswordInterface {
    String path= WebServices.forgotpassword;
    @FormUrlEncoded
    @POST(path)
    Call<MobileModel> sendData(@Field("Email") String email);
}
