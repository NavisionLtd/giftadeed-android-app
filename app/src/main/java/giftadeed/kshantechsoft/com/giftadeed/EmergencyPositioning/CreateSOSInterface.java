package giftadeed.kshantechsoft.com.giftadeed.EmergencyPositioning;

import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by I-Sys on 11-May-17.
 */


public interface CreateSOSInterface {
    String path = WebServices.Add_SOS;

    @FormUrlEncoded
    @POST(path)
    Call<SOSResponseStatus> sendData(@Field("user_id") String userid, @Field("device_id") String deviceid,
                                     @Field("geopoints") String geopoint, @Field("address") String address,
                                     @Field("sos_types") String sostypes, @Field("device_type") String devicetype);
}
