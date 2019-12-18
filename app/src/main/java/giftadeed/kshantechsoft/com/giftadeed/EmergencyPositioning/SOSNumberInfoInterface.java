/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.EmergencyPositioning;

import java.util.List;

import giftadeed.kshantechsoft.com.giftadeed.Group.GroupInfoPOJO;
import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by I-Sys on 11-May-17.
 */


public interface SOSNumberInfoInterface {
    String path = WebServices.GET_COUNTRY_SOS_NUMBER;

    @FormUrlEncoded
    @POST(path)
    Call<SOSNumberInfoPOJO> sendData(@Field("geopoints") String geo_points);
}
