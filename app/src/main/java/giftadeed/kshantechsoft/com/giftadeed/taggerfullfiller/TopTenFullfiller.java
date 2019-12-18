/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.taggerfullfiller;

import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by Nilesh on 5/13/2017.
 */

public interface TopTenFullfiller {
    String path= WebServices.fetch_toptenfullfiller;
    @FormUrlEncoded
    @POST(path)
    Call<ModaltoptenFullfill> getTopTenFullfillerList(@Field("User_ID") String strUserID);
}
