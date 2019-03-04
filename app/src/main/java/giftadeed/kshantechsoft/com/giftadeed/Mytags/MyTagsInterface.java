package giftadeed.kshantechsoft.com.giftadeed.Mytags;



import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by Nilesh on 5/16/2017.
 */

public interface MyTagsInterface {
    String path= WebServices.myTagsURL;
    @FormUrlEncoded
    @POST(path)
    Call<ModelMytaglist> fetchData(@Field("User_ID") String User_ID);
}
