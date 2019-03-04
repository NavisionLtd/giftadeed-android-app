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

public interface ResourcelistInterface {
    String path= WebServices.reslist_fetch;
    @FormUrlEncoded
    @POST(path)
    Call<Modelresourcelist> fetchData(@Field("user_id") String User_ID);
}
