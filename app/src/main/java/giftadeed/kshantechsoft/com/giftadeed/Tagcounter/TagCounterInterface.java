/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.Tagcounter;

import giftadeed.kshantechsoft.com.giftadeed.Needdetails.StatusModel;
import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by I-Sys on 17-Mar-18.
 */

public interface TagCounterInterface {
    String path= WebServices.tagcounter;
    @FormUrlEncoded
    @POST(path)
    Call<TagCounterModel> fetchData(@Field("userId") String User_ID);
}
