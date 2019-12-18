/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.AdvisoryBoard;

import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by roshan on 19/3/18.
 */

public interface AdvisoryDataInterface {
    String path = WebServices.advisoryurl;

    @FormUrlEncoded
    @POST(path)
    Call<AdvisoryResponse> sendData(@Field("User_ID ") String user_id
    );
}
