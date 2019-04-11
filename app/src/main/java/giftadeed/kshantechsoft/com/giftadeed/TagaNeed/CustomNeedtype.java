
package giftadeed.kshantechsoft.com.giftadeed.TagaNeed;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomNeedtype {
    @SerializedName("NeedMapping_ID")
    @Expose
    private String needMappingID;
    @SerializedName("Need_Name")
    @Expose
    private String needName;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("Icon_Path")
    @Expose
    private String iconPath;
    @SerializedName("Character_Path")
    @Expose
    private String characterPath;
    private Boolean isChecked;

    public String getNeedMappingID() {
        return needMappingID;
    }

    public void setNeedMappingID(String needMappingID) {
        this.needMappingID = needMappingID;
    }

    public String getNeedName() {
        return needName;
    }

    public void setNeedName(String needName) {
        this.needName = needName;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public String getCharacterPath() {
        return characterPath;
    }

    public void setCharacterPath(String characterPath) {
        this.characterPath = characterPath;
    }

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
