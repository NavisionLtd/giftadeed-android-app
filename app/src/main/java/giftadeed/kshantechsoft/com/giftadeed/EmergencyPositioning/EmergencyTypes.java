package giftadeed.kshantechsoft.com.giftadeed.EmergencyPositioning;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class EmergencyTypes implements Serializable {
    @SerializedName("id")
    @Expose
    private String typeid;
    @SerializedName("emergency_name")
    @Expose
    private String type;
    private static final long serialVersionUID = 1L;
    private boolean isSelected;

    public EmergencyTypes(String type, boolean isSelected) {
        this.type = type;
        this.isSelected = isSelected;
    }

    public EmergencyTypes() {

    }

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
