
package giftadeed.kshantechsoft.com.giftadeed.TagaNeed;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryType {
    @SerializedName("needtype")
    @Expose
    private List<Needtype> needtype = null;
    public List<Needtype> getNeedtype() {
        return needtype;
    }
    public void setNeedtype(List<Needtype> needtype) {
        this.needtype = needtype;
    }
}
