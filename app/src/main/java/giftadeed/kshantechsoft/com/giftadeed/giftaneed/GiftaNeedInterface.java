package giftadeed.kshantechsoft.com.giftadeed.giftaneed;

import giftadeed.kshantechsoft.com.giftadeed.Signup.MobileModel;
import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by I-Sys on 22-May-17.
 */

public interface GiftaNeedInterface {
    String path = WebServices.FULLFIL_NEED;
    @FormUrlEncoded
    @POST(path)
    Call<MobileModel> sendData(@Field("User_ID") String uid, @Field("Tagged_ID") String taggedgid, @Field("Fulfilled_Photo_Path") String photopath,
                               @Field("Description") String description, @Field("is_partial") String is_partial, @Field("need") String need);
}
