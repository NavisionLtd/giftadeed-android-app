package giftadeed.kshantechsoft.com.giftadeed.SendBirdChat.Interfaces;

import com.google.gson.JsonObject;

import giftadeed.kshantechsoft.com.giftadeed.SendBirdChat.Pojo.ModalSendBrdUpdate;
import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.Headers;
import retrofit.http.PUT;
import retrofit.http.Path;

/**
 * Created by Nilesh on 3/12/2018.
 */

public interface UpdateGrpChannel {
    String pathUrl = WebServices.UPDATE_GRP_CHANNEL;
    String apitoken = WebServices.SENDBIRD_API_TOKEN;

    @Headers({
            "Content-Type: application/json",
//            "Api-Token: 2dcdfe3d32794628037846383f037b87db12a349"  // For live app
//            "Api-Token: cf709ee2fa69a3823f90bdc98647c0d2e850d3cf"  // For kshandemo development
            "Api-Token: " + apitoken
    })
    @PUT(pathUrl + "{channel_url}")
    Call<ModalSendBrdUpdate> sendData(@Path("channel_url") String channelUrl, @Body ModalSendBrdUpdate object);
}
