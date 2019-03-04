package giftadeed.kshantechsoft.com.giftadeed.Login;

import giftadeed.kshantechsoft.com.giftadeed.Needdetails.StatusModel;
import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by I-Sys on 03-Apr-18.
 */

public interface ResendLinkInterface {
    String path= WebServices.Resend_link;
    @FormUrlEncoded
    @POST(path)
    Call<StatusModel> sendData(@Field("email") String email);
}
