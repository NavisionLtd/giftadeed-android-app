package giftadeed.kshantechsoft.com.giftadeed.ActiveUser;

import giftadeed.kshantechsoft.com.giftadeed.Signup.MobileModel;
import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by I-Sys on 29-Apr-17.
 */

public interface ActiveUserInterface {

    String path= WebServices.Active_User;
    @FormUrlEncoded
    @POST(path)
    Call<ModelActiveUser> sendData(@Field("userId") String userId);
}
