package giftadeed.kshantechsoft.com.giftadeed.giftaneed;

import giftadeed.kshantechsoft.com.giftadeed.Signup.MobileModel;
import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by I-Sys on 21-Jun-17.
 */

public interface FulfillDistanceInterface {
    String path= WebServices.fulfildistance;
    @FormUrlEncoded
    @POST(path)
    Call<FulfilDistance> sendData(@Field("dist") String uid);
}
