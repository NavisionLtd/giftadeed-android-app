package giftadeed.kshantechsoft.com.giftadeed.Collaboration;

import java.util.List;

import giftadeed.kshantechsoft.com.giftadeed.Group.GroupPOJO;
import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by I-Sys on 10-May-17.
 */

public interface ColabInterface {
    String path = WebServices.Collab_List;

    @FormUrlEncoded
    @POST(path)
    Call<CollabPOJO> sendData(@Field("user_id") String userid);
}