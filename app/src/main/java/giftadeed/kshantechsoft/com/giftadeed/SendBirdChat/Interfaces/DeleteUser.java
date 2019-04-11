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
    @Headers({
            "Content-Type: application/json",
            //            "Api-Token: 2dcdfe3d32794628037846383f037b87db12a349"  // For live app
            "Api-Token: cf709ee2fa69a3823f90bdc98647c0d2e850d3cf"  // For kshandemo development

    })
    @DELETE(pathUrl+"{user_id}")
    Call<Object> deleteUser(@Path("user_id") String userId);
}
