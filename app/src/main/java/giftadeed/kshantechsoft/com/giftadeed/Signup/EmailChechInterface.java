package giftadeed.kshantechsoft.com.giftadeed.Signup;

import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by I-Sys on 27-Apr-17.
 */

public interface EmailChechInterface {
    String path= WebServices.emailcheck;
    @FormUrlEncoded
    @POST(path)
    Call<MobileModel> sendData(@Field("Email") String email);
}
