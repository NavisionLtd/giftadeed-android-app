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
 * Created by I-Sys on 09-Mar-18.
 */

public interface ReportUserInterface {
    String path= WebServices.reportuser;
    @FormUrlEncoded
    @POST(path)
    Call<StatusModel> fetchData(@Field("reporterId") String User_ID, @Field("tagId") String deedId,@Field("taggerId") String taggerid);
}
