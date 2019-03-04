package giftadeed.kshantechsoft.com.giftadeed.EmergencyPositioning;

import java.util.List;

import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by I-Sys on 05-Mar-18.
 */

public interface SOSDetailsInterface {
    String path= WebServices.SOS_Details;
    @FormUrlEncoded
    @POST(path)
    Call<List<EmergencyInfoPOJO>> fetchData(@Field("sos_id") String sosId);
}
