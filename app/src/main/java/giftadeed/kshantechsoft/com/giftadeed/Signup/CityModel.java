
/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.Signup;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CityModel {

    @SerializedName("citydata")
    @Expose
    private List<giftadeed.kshantechsoft.com.giftadeed.Signup.citydata> citydata = null;

    public List<giftadeed.kshantechsoft.com.giftadeed.Signup.citydata> getCitydata() {
        return citydata;
    }

    public void setCitydata(List<giftadeed.kshantechsoft.com.giftadeed.Signup.citydata> citydata) {
        this.citydata = citydata;
    }
}