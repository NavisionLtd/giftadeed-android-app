
package giftadeed.kshantechsoft.com.giftadeed.Signup;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CountryModel {

    @SerializedName("countrydata")
    @Expose
    private List<giftadeed.kshantechsoft.com.giftadeed.Signup.countrydata> countrydata = null;

    public List<giftadeed.kshantechsoft.com.giftadeed.Signup.countrydata> getCountrydata() {
        return countrydata;
    }

    public void setCountrydata(List<giftadeed.kshantechsoft.com.giftadeed.Signup.countrydata> countrydata) {
        this.countrydata = countrydata;
    }

}
