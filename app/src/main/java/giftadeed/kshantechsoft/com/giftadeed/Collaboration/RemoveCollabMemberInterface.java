package giftadeed.kshantechsoft.com.giftadeed.Collaboration;

import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

public interface RemoveCollabMemberInterface {
    String path = WebServices.Remove_Collab_Member;

    @FormUrlEncoded
    @POST(path)
    Call<CollabResponseStatus> sendData(@Field("collaboration_id") String collabid, @Field("user_id") String userid);
}