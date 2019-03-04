package giftadeed.kshantechsoft.com.giftadeed.Resources;

import java.util.List;

import giftadeed.kshantechsoft.com.giftadeed.Group.GroupResponseStatus;
import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by I-Sys on 05-Mar-18.
 */

public interface ResourceDeleteInterface {
    String path= WebServices.Delete_Resource;
    @FormUrlEncoded
    @POST(path)
    Call<GroupResponseStatus> fetchData(@Field("user_id") String User_ID, @Field("resource_id") String resId);
}
