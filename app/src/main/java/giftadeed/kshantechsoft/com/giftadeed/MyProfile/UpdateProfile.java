/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.MyProfile;

import giftadeed.kshantechsoft.com.giftadeed.Signup.MobileModel;
import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by I-Sys on 03-May-17.
 */

public interface UpdateProfile {
    String path= WebServices.updateprofil;
    @FormUrlEncoded
    @POST(path)
    Call<MobileModel> sendData(@Field("User_ID") String userid,@Field("Fname") String fnmae,@Field("Lname") String lnmae,@Field("Email")
            String email,@Field("Mobile") String mobile,@Field("Address") String address,@Field("Country_ID")
            String cntry_id,@Field("State_ID") String state_id,@Field("City_ID") String city_id,@Field("Gender") String get,@Field("Password")
            String password,@Field("Privacy") String privacy);

}
