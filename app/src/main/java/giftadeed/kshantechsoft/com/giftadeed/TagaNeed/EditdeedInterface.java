package giftadeed.kshantechsoft.com.giftadeed.TagaNeed;

import giftadeed.kshantechsoft.com.giftadeed.Needdetails.StatusModel;
import giftadeed.kshantechsoft.com.giftadeed.Signup.MobileModel;
import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by I-Sys on 09-Mar-18.
 */

public interface EditdeedInterface {
    String path = WebServices.editdeed;

    @FormUrlEncoded
    @POST(path)
    Call<StatusModel> sendData(@Field("userId") String uid,
                               @Field("deedId") String deedid,
                               @Field("NeedMapping_ID") String mappingid,
                               @Field("Geopoint") String geopoints,
                               @Field("Tagged_Photo_Path") String photopath,
                               @Field("Tagged_Title") String title,
                               @Field("Description") String description,
                               @Field("Address") String address,
                               @Field("container") String container,
                               @Field("validity") String validity,
                               @Field("sub_type_pref") String sub_type_pref,
                               @Field("PAddress") String PAddress);
}

