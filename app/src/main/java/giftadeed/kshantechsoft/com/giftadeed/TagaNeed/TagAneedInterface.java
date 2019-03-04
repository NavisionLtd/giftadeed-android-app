package giftadeed.kshantechsoft.com.giftadeed.TagaNeed;

import giftadeed.kshantechsoft.com.giftadeed.Signup.MobileModel;
import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by I-Sys on 11-May-17.
 */


public interface TagAneedInterface {
    String path = WebServices.taganeed;

    @FormUrlEncoded
    @POST(path)
    Call<MobileModel> sendData(@Field("User_ID") String uid, @Field("NeedMapping_ID") String mappingid, @Field("Geopoint") String geopoints,
                               @Field("Tagged_Photo_Path") String photopath, @Field("Tagged_Title") String title, @Field("Description") String description,
                               @Field("Address") String address, @Field("container") String container, @Field("validity") String validity, @Field("PAddress") String paddress,
                               @Field("sub_type_pref") String subTypePref, @Field("all_groups") String allGroups, @Field("all_individuals") String allIndi, @Field("user_grp_ids") String userGrps);
}
