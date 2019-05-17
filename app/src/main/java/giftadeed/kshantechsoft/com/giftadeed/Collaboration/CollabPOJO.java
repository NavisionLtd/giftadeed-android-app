package giftadeed.kshantechsoft.com.giftadeed.Collaboration;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CollabPOJO {
    @SerializedName("is_blocked")
    @Expose
    private Integer isBlocked;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("users_collaboration_list")
    @Expose
    public List<Colablist> colablist = null;
    @SerializedName("request_list")
    @Expose
    public List<Colabrequestlist> colab_requestlist = null;

    public Integer getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(Integer isBlocked) {
        this.isBlocked = isBlocked;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Colablist> getColablist() {
        return colablist;
    }

    public void setColablist(List<Colablist> colablist) {
        this.colablist = colablist;
    }

    public List<Colabrequestlist> getColab_requestlist() {
        return colab_requestlist;
    }

    public void setColab_requestlist(List<Colabrequestlist> colab_requestlist) {
        this.colab_requestlist = colab_requestlist;
    }
}
