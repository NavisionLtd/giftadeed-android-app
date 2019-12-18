/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.Mytags;



import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by Nilesh on 5/16/2017.
 */

public interface MyTagsInterface {
    String path= WebServices.myTagsURL;
    @FormUrlEncoded
    @POST(path)
    Call<ModelMytaglist> fetchData(@Field("User_ID") String User_ID);
}
