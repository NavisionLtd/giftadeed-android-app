package giftadeed.kshantechsoft.com.giftadeed.Group;

import java.util.List;

import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.list_Model.Taggedlist;
import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by Pranali on 29-Apr-17.
 */
////interface for fetching data

public interface GrouptagsInterface {
    String path = WebServices.Groups_Tags;

    @FormUrlEncoded
    @POST(path)
    Call<List<Taggedlist>> fetchData(@Field("user_id") String User_ID, @Field("group_id") String Group_ID);
}
