package giftadeed.kshantechsoft.com.giftadeed.Collaboration;

import giftadeed.kshantechsoft.com.giftadeed.Group.GroupResponseStatus;
import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by I-Sys on 11-May-17.
 */


public interface EditCollabInterface {
    String path = WebServices.Edit_Group;

    @FormUrlEncoded
    @POST(path)
    Call<CollabResponseStatus> sendData(@Field("name") String name, @Field("user_id") String userid, @Field("desc") String desc, @Field("group_id") String groupid);
}
