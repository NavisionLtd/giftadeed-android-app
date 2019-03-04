package giftadeed.kshantechsoft.com.giftadeed.TagaNeed;

import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by I-Sys on 10-May-17.
 */

public interface CategoryInterface {
    String path= WebServices.needtype;
    @FormUrlEncoded
    @POST(path)
    Call<CategoryType> sendData(@Field("category") String country);
}