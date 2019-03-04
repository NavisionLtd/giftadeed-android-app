package giftadeed.kshantechsoft.com.giftadeed.Resources;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MultiSubCategories {
    @SerializedName("type_id")
    @Expose
    private String typeid;
    @SerializedName("Need_Name")
    @Expose
    private String needname;
    @SerializedName("id")
    @Expose
    private String subCatId;
    @SerializedName("name")
    @Expose
    private String subCatName;
    private Integer qty;
    private Boolean isChecked;

    public String getSubCatId() {
        return subCatId;
    }

    public void setSubCatId(String subCatId) {
        this.subCatId = subCatId;
    }

    public String getSubCatName() {
        return subCatName;
    }

    public void setSubCatName(String subCatName) {
        this.subCatName = subCatName;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }

    public String getNeedname() {
        return needname;
    }

    public void setNeedname(String needname) {
        this.needname = needname;
    }
}
