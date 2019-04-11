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