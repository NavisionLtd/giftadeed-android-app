package giftadeed.kshantechsoft.com.giftadeed.SendBirdChat.Interfaces;

import com.squareup.okhttp.ResponseBody;

import giftadeed.kshantechsoft.com.giftadeed.SendBirdChat.Pojo.RemoveUserFromClub;
import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.Headers;
import retrofit.http.PUT;
import retrofit.http.Path;

/**
 * Created by Nilesh on 3/28/2018.
 */

public interface RemoveMemberFromChannel {
    String pathUrl = WebServices.REMOVE_MEMBER;
    String apitoken = WebServices.SENDBIRD_API_TOKEN;

    @Headers({
            "Content-Type: application/json",
            //            "Api-Token: 2dcdfe3d32794628037846383f037b87db12a349"  // For live app
//            "Api-Token: cf709ee2fa69a3823f90bdc98647c0d2e850d3cf"  // For kshandemo development
            "Api-Token: " + apitoken
    })
    @PUT(pathUrl + "{channel_url}/leave")
    Call<ResponseBody> removeMembers(@Path("channel_url") String userId, @Body RemoveUserFromClub object);
}
