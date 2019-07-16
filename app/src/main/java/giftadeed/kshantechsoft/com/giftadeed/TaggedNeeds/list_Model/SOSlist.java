

package giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.list_Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SOSlist {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("geopoints")
    @Expose
    private String geopoints;
    @SerializedName("address")
    @Expose
    private String address;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGeopoints() {
        return geopoints;
    }

    public void setGeopoints(String geopoints) {
        this.geopoints = geopoints;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}