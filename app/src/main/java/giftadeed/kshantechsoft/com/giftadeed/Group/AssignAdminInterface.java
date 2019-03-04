package giftadeed.kshantechsoft.com.giftadeed.Group;

import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by I-Sys on 10-May-17.
 */

public interface AssignAdminInterface {
    String path = WebServices.Assign_Admin;

    @FormUrlEncoded
    @POST(path)
    Call<GroupResponseStatus> sendData(@Field("user_id") String userid, @Field("member_id") String email, @Field("group_id") String groupid);
}