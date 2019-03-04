package giftadeed.kshantechsoft.com.giftadeed.FirstLogin;

import giftadeed.kshantechsoft.com.giftadeed.Login.LinkedInLoginModel;
import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by I-Sys on 15-Jan-18.
 */

public interface FirstLoginInterface {
    String path= WebServices.firstlogin;
    @FormUrlEncoded
    @POST(path)
    Call<FirstLoginModel> sendData(@Field("Country_ID") String countryid,
                                      @Field("State_ID") String stateid,
                                      @Field("City_ID") String cityid,
                                      @Field("Email") String emailid,
                                      @Field("User_ID")String userid);
}
