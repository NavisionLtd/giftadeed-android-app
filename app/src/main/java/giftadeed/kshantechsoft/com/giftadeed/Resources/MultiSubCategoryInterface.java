package giftadeed.kshantechsoft.com.giftadeed.Resources;

import java.util.List;

import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.SubCategories;
import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by I-Sys on 10-May-17.
 */

public interface MultiSubCategoryInterface {
    String path = WebServices.Multi_Subtype;

    @FormUrlEncoded
    @POST(path)
    Call<List<MultiSubCategories>> sendData(@Field("user_id") String userid,@Field("need_ids") String typeid);
}