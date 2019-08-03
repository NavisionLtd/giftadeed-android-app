package giftadeed.kshantechsoft.com.giftadeed.Resources;

import giftadeed.kshantechsoft.com.giftadeed.Group.GroupResponseStatus;
import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by I-Sys on 11-May-17.
 */


public interface CreateResourceInterface {
    String path = WebServices.Add_Resource;

    @FormUrlEncoded
    @POST(path)
    Call<GroupResponseStatus> sendData(@Field("user_id") String userid, @Field("group_id") String groupid, @Field("resource_name") String resourcename,
                                       @Field("description") String desc, @Field("sub_type_pref") String subtype, @Field("geopoint") String geopoint,
                                       @Field("address") String address, @Field("user_group_ids") String usergrpid, @Field("all_groups") String allgrps,
                                       @Field("main_category_ids") String mainCatIds, @Field("group_category_ids") String grpCatIds);
}
