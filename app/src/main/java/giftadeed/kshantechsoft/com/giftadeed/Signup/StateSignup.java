package giftadeed.kshantechsoft.com.giftadeed.Signup;

import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by I-Sys on 26-Apr-17.
 */

public interface StateSignup {
    String path= WebServices.state;
    @FormUrlEncoded
    @POST(path)
    Call<StateModel> sendData(@Field("Cntry_ID") String states);
}
