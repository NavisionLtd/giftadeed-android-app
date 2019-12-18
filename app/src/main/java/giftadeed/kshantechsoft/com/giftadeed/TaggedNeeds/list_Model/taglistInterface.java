/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.list_Model;

import giftadeed.kshantechsoft.com.giftadeed.Signup.MobileModel;
import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by Pranali on 29-Apr-17.
 */
////interface for fetching data

public interface taglistInterface {
    String path= WebServices.taglist_fetch;
    @FormUrlEncoded
    @POST(path)
    Call<Modeltaglist> fetchData(@Field("user_id") String User_ID);
}