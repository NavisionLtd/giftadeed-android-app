package giftadeed.kshantechsoft.com.giftadeed.Resources;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubType {
    @SerializedName("need_name")
    @Expose
    private String needname;
    @SerializedName("sub_type_name")
    @Expose
    private String subTypeName;

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
}
