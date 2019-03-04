package giftadeed.kshantechsoft.com.giftadeed.Resources;

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

public interface OwnedGroupsInterface {
    String path = WebServices.Owned_Groups;

    @FormUrlEncoded
    @POST(path)
    Call<List<GroupPOJO>> sendData(@Field("user_id") String userid);
}