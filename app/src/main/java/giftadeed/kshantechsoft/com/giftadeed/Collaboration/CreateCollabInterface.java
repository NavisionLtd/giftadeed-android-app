package giftadeed.kshantechsoft.com.giftadeed.Collaboration;

import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by I-Sys on 11-May-17.
 */


public interface CreateCollabInterface {
    String path = WebServices.Create_Collab;

    @FormUrlEncoded
    @POST(path)
    Call<CollabResponseStatus> sendData(@Field("user_id") String userid, @Field("group_id") String groupid,
                                        @Field("collaboration_name") String collabname, @Field("collaboration_description") String desc);
}
