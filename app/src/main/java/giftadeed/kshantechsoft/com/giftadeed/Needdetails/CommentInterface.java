package giftadeed.kshantechsoft.com.giftadeed.Needdetails;

import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by I-Sys on 06-Mar-18.
 */

public interface CommentInterface {
    String path= WebServices.commentdeed;
    @FormUrlEncoded
    @POST(path)
    Call<StatusModel> fetchData(@Field("userId") String User_ID, @Field("deedId") String deedId,@Field("comment") String comment);
}
