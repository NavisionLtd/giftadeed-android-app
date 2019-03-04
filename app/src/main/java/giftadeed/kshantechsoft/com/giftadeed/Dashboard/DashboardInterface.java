package giftadeed.kshantechsoft.com.giftadeed.Dashboard;

import giftadeed.kshantechsoft.com.giftadeed.Needdetails.StatusModel;
import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by I-Sys on 19-Mar-18.
 */

public interface DashboardInterface {
    String path= WebServices.dashBoard;
    @FormUrlEncoded
    @POST(path)
    Call<DashboardModel> fetchData(@Field("userId") String User_ID);
}
