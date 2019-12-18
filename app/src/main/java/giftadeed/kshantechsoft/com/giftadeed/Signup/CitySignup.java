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
 * Created by I-Sys on 26-Apr-17.
 */

public interface CitySignup {
    String path= WebServices.city;
    @FormUrlEncoded
    @POST(path)
    Call<CityModel> sendData(@Field("State_ID") String country);
}
