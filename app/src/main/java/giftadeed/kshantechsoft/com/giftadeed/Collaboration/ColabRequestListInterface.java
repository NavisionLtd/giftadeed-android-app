package giftadeed.kshantechsoft.com.giftadeed.Collaboration;

import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by I-Sys on 10-May-17.
 */

public interface ColabRequestListInterface {
    String path = WebServices.Collab_Request_List;

    @FormUrlEncoded
    @POST(path)
    Call<CollabPOJO> sendData(@Field("user_id") String userid);
}