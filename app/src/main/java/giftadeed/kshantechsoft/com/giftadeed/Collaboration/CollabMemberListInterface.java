package giftadeed.kshantechsoft.com.giftadeed.Collaboration;

import giftadeed.kshantechsoft.com.giftadeed.Group.Memberlist;
import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by I-Sys on 10-May-17.
 */

public interface CollabMemberListInterface {
    String path = WebServices.Collab_Member_List;

    @FormUrlEncoded
    @POST(path)
    Call<CollabPOJO> sendData(@Field("collaboration_id") String collabid);
}