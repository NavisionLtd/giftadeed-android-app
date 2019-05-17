package giftadeed.kshantechsoft.com.giftadeed.Collaboration;

import java.util.List;

import giftadeed.kshantechsoft.com.giftadeed.Group.GroupInfoPOJO;
import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by I-Sys on 11-May-17.
 */


public interface CollabInfoInterface {
    String path = WebServices.Collab_Info;

    @FormUrlEncoded
    @POST(path)
    Call<List<GroupInfoPOJO>> sendData(@Field("user_id") String userid, @Field("group_id") String groupid);
}
