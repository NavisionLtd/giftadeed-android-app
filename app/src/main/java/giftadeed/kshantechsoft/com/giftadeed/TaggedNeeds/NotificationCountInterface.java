/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds;

import giftadeed.kshantechsoft.com.giftadeed.Mytags.ModelMytaglist;
import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by I-Sys on 26-Mar-18.
 */

public interface NotificationCountInterface {
    String path= WebServices.notificationcount;
    @FormUrlEncoded
    @POST(path)
    Call<NotificationCountModel> fetchData(@Field("userId") String User_ID,@Field("lat_long") String latlang);
}
