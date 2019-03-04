package giftadeed.kshantechsoft.com.giftadeed.Group;

import java.util.List;

import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.SubCategories;
import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by I-Sys on 10-May-17.
 */

public interface GroupsInterface {
    String path = WebServices.Group_List;

    @FormUrlEncoded
    @POST(path)
    Call<List<GroupPOJO>> sendData(@Field("user_id") String userid);
}