package giftadeed.kshantechsoft.com.giftadeed.Signup;

import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by I-Sys on 26-Apr-17.
 */

public interface CountrySignup {
String path= WebServices.country;
    @FormUrlEncoded
    @POST(path)
    Call<CountryModel> sendData(@Field("country") String country);

}
