
package giftadeed.kshantechsoft.com.giftadeed.FirstLogin;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FirstLoginModel {

    @SerializedName("checkstatus")
    @Expose
    private List<Checkstatus> checkstatus = null;

    public List<Checkstatus> getCheckstatus() {
        return checkstatus;
    }

    public void setCheckstatus(List<Checkstatus> checkstatus) {
        this.checkstatus = checkstatus;
    }

}
