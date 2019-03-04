package giftadeed.kshantechsoft.com.giftadeed.Notifications;

import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.NotificationCountModel;
import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by I-Sys on 26-Mar-18.
 */

public interface AllNotificationInterface {
    String path= WebServices.Allnotificationcount;
    @FormUrlEncoded
    @POST(path)
    Call<AllNotificationModel> fetchData(@Field("userId") String User_ID);
}
