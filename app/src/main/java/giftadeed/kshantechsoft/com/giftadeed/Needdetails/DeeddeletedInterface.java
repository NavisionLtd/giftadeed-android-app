/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.Needdetails;

import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by I-Sys on 22-Mar-18.
 */

public interface DeeddeletedInterface {
    String path= WebServices.deeddeleted;
    @FormUrlEncoded
    @POST(path)
    Call<DeeddeletedModel> fetchData(@Field("deedId") String deedId);
}
