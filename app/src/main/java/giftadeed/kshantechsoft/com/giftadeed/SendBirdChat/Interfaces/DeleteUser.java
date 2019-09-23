package giftadeed.kshantechsoft.com.giftadeed.SendBirdChat.Interfaces;

import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.http.DELETE;
import retrofit.http.Headers;
import retrofit.http.Path;

/**
 * Created by Nilesh on 3/13/2018.
 */

public interface DeleteUser {

    String pathUrl= WebServices.DELETE_USER_ACOUNT;
    String apitoken = WebServices.SENDBIRD_API_TOKEN;
    @Headers({
            "Content-Type: application/json",
            "Api-Token: " + apitoken

    })
    @DELETE(pathUrl+"{user_id}")
    Call<Object> deleteUser(@Path("user_id") String userId);
}
