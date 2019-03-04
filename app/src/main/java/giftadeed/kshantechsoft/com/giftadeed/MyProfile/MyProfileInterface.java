package giftadeed.kshantechsoft.com.giftadeed.MyProfile;

import giftadeed.kshantechsoft.com.giftadeed.Signup.CityModel;
import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by I-Sys on 02-May-17.
 */

public interface MyProfileInterface {

    String path= WebServices.fetchprofile;
    @FormUrlEncoded
    @POST(path)
    Call<FetchProfileData> sendData(@Field("User_ID") String userid);
}
