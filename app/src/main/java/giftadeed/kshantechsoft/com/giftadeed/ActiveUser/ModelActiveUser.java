package giftadeed.kshantechsoft.com.giftadeed.ActiveUser;

/**
 * Created by I-Sys on 18-Jun-18.
 */


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelActiveUser {

    @SerializedName("status")
    @Expose
    private Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
