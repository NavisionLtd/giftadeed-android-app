
package giftadeed.kshantechsoft.com.giftadeed.Login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LinkedInLoginModel {

    @SerializedName("checkstatus")
    @Expose
    private Checkstatus checkstatus;

    public Checkstatus getCheckstatus() {
        return checkstatus;
    }

    public void setCheckstatus(Checkstatus checkstatus) {
        this.checkstatus = checkstatus;
    }

}
