package giftadeed.kshantechsoft.com.giftadeed.Needdetails;

import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by I-Sys on 05-Mar-18.
 */

public interface EndorsedeedInterface {
    String path= WebServices.endorsedeed;
    @FormUrlEncoded
    @POST(path)
    Call<StatusModel> fetchData(@Field("userId") String User_ID, @Field("deedId") String deedId);
}
