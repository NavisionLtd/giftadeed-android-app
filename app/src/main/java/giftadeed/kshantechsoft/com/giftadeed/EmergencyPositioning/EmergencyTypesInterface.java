/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.EmergencyPositioning;

import java.util.List;

import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by I-Sys on 10-May-17.
 */

public interface EmergencyTypesInterface {
    String path = WebServices.SOS_Types;

    @FormUrlEncoded
    @POST(path)
    Call<List<EmergencyTypes>> sendData(@Field("user_id") String userid);
}