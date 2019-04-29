package giftadeed.kshantechsoft.com.giftadeed.SendBirdChat.Interfaces;

import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.http.DELETE;
import retrofit.http.Headers;
import retrofit.http.Path;

public interface DeleteChannelGroup {
    String pathUrl= WebServices.UPDATE_GRP_CHANNEL;
    String apitoken = WebServices.SENDBIRD_API_TOKEN;
    @Headers({
            "Content-Type: application/json",
            //            "Api-Token: 2dcdfe3d32794628037846383f037b87db12a349"  // For live app
//            "Api-Token: cf709ee2fa69a3823f90bdc98647c0d2e850d3cf"  // For kshandemo development
            "Api-Token: " + apitoken

    })
    @DELETE(pathUrl+"{channel_url}")
    Call<Object> deleteChannels(@Path("channel_url") String channelUrl);
}
