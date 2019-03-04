package giftadeed.kshantechsoft.com.giftadeed.MyFullFillTag;

import giftadeed.kshantechsoft.com.giftadeed.Mytags.ModelMytaglist;
import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by Nilesh on 5/22/2017.
 */

public interface InterfaceMyFFillTag {
    String path= WebServices.myFullfillTagsURL;
    @FormUrlEncoded
    @POST(path)
    Call<ModalMyFullfillTag> fetchData(@Field("User_ID") String User_ID);
}
