
package giftadeed.kshantechsoft.com.giftadeed.giftaneed;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FulfilDistance {
    @SerializedName("distancevalue")
    @Expose
    private List<Distancevalue> distancevalue = null;

    public List<Distancevalue> getDistancevalue() {
        return distancevalue;
    }

    public void setDistancevalue(List<Distancevalue> distancevalue) {
        this.distancevalue = distancevalue;
    }
}
