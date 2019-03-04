package giftadeed.kshantechsoft.com.giftadeed.TagaNeed;

import java.util.List;

import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by I-Sys on 10-May-17.
 */

public interface TaggedDeedsInterface {
    String path = WebServices.Check_Deed;

    @FormUrlEncoded
    @POST(path)
    Call<List<TaggedDeedsPojo>> sendData(@Field("user_id") String userid, @Field("geopoints") String geopoints, @Field("need_id") String needid);
}