package giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.list_Model;

import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by Pranali on 29-Apr-17.
 */
////interface for fetching data

public interface SOSlistInterface {
    String path= WebServices.soslist_fetch;
    @FormUrlEncoded
    @POST(path)
    Call<Modelsoslist> fetchData(@Field("user_id") String User_ID);
}
