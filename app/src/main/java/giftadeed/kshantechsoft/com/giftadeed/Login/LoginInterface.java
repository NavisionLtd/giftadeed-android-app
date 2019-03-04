package giftadeed.kshantechsoft.com.giftadeed.Login;

import giftadeed.kshantechsoft.com.giftadeed.Signup.CityModel;
import giftadeed.kshantechsoft.com.giftadeed.Signup.MobileModel;
import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by I-Sys on 29-Apr-17.
 */

public interface LoginInterface {
    String path= WebServices.login;
    @FormUrlEncoded
    @POST(path)
    Call<MobileModel> sendData(@Field("Email") String email, @Field("Password") String password,@Field("Device_ID") String did);
}
