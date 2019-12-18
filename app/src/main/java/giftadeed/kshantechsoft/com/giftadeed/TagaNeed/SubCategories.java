/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.TagaNeed;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubCategories {
    @SerializedName("sub_type_id")
    @Expose
    private String subCatId;
    @SerializedName("sub_type_name")
    @Expose
    private String subCatName;
    private String qty;
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

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }
}
