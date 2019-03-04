package giftadeed.kshantechsoft.com.giftadeed.EmergencyPositioning;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EmergencyInfoPOJO {
    @SerializedName("geopoints")
    @Expose
    private String geopoints;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("sos_type")
    @Expose
    private String sostype;
    @SerializedName("c_date")
    @Expose
    private String cdate;
    @SerializedName("user_name")
    @Expose
    private String username;

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

    public String getSostype() {
        return sostype;
    }

    public void setSostype(String sostype) {
        this.sostype = sostype;
    }

    public String getCdate() {
        return cdate;
    }

    public void setCdate(String cdate) {
        this.cdate = cdate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
