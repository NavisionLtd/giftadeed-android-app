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
 * Created by I-Sys on 27-Apr-17.
 */

public interface MobilecheckInterface {

    String path= WebServices.mobilecheck;
    @FormUrlEncoded
    @POST(path)
    Call<MobileModel> sendData(@Field("Mobile") String mobile);
}
