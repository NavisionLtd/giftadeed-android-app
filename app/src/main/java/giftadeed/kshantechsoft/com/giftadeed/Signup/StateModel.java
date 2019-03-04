
package giftadeed.kshantechsoft.com.giftadeed.Signup;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StateModel {

    @SerializedName("statedata")
    @Expose
    private List<statedata> statedata = null;

    public List<statedata> getStatedata() {
        return statedata;
    }

    public void setStatedata(List<statedata> statedata) {
        this.statedata = statedata;
    }

}
