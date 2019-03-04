package giftadeed.kshantechsoft.com.giftadeed.ContactUs;

import giftadeed.kshantechsoft.com.giftadeed.Needdetails.StatusModel;
import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by I-Sys on 19-Mar-18.
 */

public interface ContactusInterface {
    String path= WebServices.contactUs;
    @FormUrlEncoded
    @POST(path)
    Call<StatusModel> fetchData(@Field("userId") String User_ID,@Field("message") String Message);
}
