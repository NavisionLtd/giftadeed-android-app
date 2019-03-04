

package giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.list_Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Resourcelist {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("geopoint")
    @Expose
    private String geopoints;
    @SerializedName("resource_name")
    @Expose
    private String resname;

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

    public String getResname() {
        return resname;
    }

    public void setResname(String resname) {
        this.resname = resname;
    }
}