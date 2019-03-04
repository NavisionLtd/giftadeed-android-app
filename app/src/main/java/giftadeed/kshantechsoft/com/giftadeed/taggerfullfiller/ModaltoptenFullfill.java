
package giftadeed.kshantechsoft.com.giftadeed.taggerfullfiller;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModaltoptenFullfill {
    @SerializedName("is_blocked")
    @Expose
    private Integer isBlocked;
    @SerializedName("RESULTFFILLER")
    @Expose
    private List<RESULTFFILLER> rESULTFFILLER = null;

    public List<RESULTFFILLER> getRESULTFFILLER() {
        return rESULTFFILLER;
    }

    public void setRESULTFFILLER(List<RESULTFFILLER> rESULTFFILLER) {
        this.rESULTFFILLER = rESULTFFILLER;
    }

    public Integer getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(Integer isBlocked) {
        this.isBlocked = isBlocked;
    }
}
