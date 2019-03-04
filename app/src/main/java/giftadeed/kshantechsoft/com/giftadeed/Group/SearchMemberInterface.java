package giftadeed.kshantechsoft.com.giftadeed.Group;

import java.util.List;

import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by I-Sys on 10-May-17.
 */

public interface SearchMemberInterface {
    String path = WebServices.Search_User;

    @FormUrlEncoded
    @POST(path)
    Call<List<MemberDetails>> sendData(@Field("user_id") String userid,@Field("email") String email,@Field("group_id") String groupid);
}