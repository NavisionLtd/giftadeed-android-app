package giftadeed.kshantechsoft.com.giftadeed.Resources;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubType {
    @SerializedName("Need_ID")
    @Expose
    private String needId;
    @SerializedName("need_name")
    @Expose
    private String needname;
    @SerializedName("sub_type_name")
    @Expose
    private String subTypeName;
    @SerializedName("sub_type_id")
    @Expose
    private String subTypeId;

    public String getNeedId() {
        return needId;
    }

    public void setNeedId(String needId) {
        this.needId = needId;
    }

    public String getNeedname() {
        return needname;
    }

    public void setNeedname(String needname) {
        this.needname = needname;
    }

    public String getSubTypeName() {
        return subTypeName;
    }

    public void setSubTypeName(String subTypeName) {
        this.subTypeName = subTypeName;
    }

    public String getSubTypeId() {
        return subTypeId;
    }

    public void setSubTypeId(String subTypeId) {
        this.subTypeId = subTypeId;
    }
}
