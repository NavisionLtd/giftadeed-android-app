

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
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("resource_name")
    @Expose
    private String resname;
    @SerializedName("group_name")
    @Expose
    private String group_name;
    @SerializedName("created_date")
    @Expose
    private String created_date;
    @SerializedName("need_name")
    @Expose
    private String need_name;

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

    public String getResname() {
        return resname;
    }

    public void setResname(String resname) {
        this.resname = resname;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getNeed_name() {
        return need_name;
    }

    public void setNeed_name(String need_name) {
        this.need_name = need_name;
    }
}