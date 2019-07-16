package giftadeed.kshantechsoft.com.giftadeed.Needdetails;

import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.list_Model.Modeltaglist;
import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by I-Sys on 05-Mar-18.
 */

public interface DeedDetailsInterface {
    String path= WebServices.deeddetails;
    @FormUrlEncoded
    @POST(path)
    Call<DeedDetailsModel> fetchData(@Field("userId") String User_ID,@Field("deedId") String deedId);
}
