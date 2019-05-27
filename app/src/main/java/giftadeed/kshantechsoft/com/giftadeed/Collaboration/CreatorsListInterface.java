package giftadeed.kshantechsoft.com.giftadeed.Collaboration;

import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by I-Sys on 10-May-17.
 */

public interface CreatorsListInterface {
    String path = WebServices.Group_Creators_List;

    @FormUrlEncoded
    @POST(path)
    Call<CollabPOJO> sendData(@Field("user_id") String userid, @Field("collaboration_id") String collabid);
}