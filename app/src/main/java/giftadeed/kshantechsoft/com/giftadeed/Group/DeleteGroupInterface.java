package giftadeed.kshantechsoft.com.giftadeed.Group;

import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by I-Sys on 11-May-17.
 */


public interface DeleteGroupInterface {
    String path = WebServices.Delete_Group;

    @FormUrlEncoded
    @POST(path)
    Call<GroupResponseStatus> sendData(@Field("group_id") String group_id, @Field("user_id") String userid);
}
