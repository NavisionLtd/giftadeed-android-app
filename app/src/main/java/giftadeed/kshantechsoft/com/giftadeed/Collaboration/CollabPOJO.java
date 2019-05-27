package giftadeed.kshantechsoft.com.giftadeed.Collaboration;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import giftadeed.kshantechsoft.com.giftadeed.Group.GroupMember;

public class CollabPOJO {
    @SerializedName("is_blocked")
    @Expose
    private Integer isBlocked;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("users_collaboration_list")
    @Expose
    public List<Colablist> colablist = null;
    @SerializedName("group_creators_list")
    @Expose
    public List<Creatorslist> groupCreatorsList = null;
    @SerializedName("request_list")
    @Expose
    public List<Colabrequestlist> colab_requestlist = null;
    @SerializedName("collaboration_members_list")
    @Expose
    public List<CollabMember> memlist = null;
    @SerializedName("collaboration_information")
    @Expose
    public Colabinfo colabinfo = null;

    public Integer getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(Integer isBlocked) {
        this.isBlocked = isBlocked;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
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

    public Colabinfo getColabinfo() {
        return colabinfo;
    }

    public void setColabinfo(Colabinfo colabinfo) {
        this.colabinfo = colabinfo;
    }

    public List<CollabMember> getMemlist() {
        return memlist;
    }

    public void setMemlist(List<CollabMember> memlist) {
        this.memlist = memlist;
    }

    public List<Creatorslist> getGroupCreatorsList() {
        return groupCreatorsList;
    }

    public void setGroupCreatorsList(List<Creatorslist> groupCreatorsList) {
        this.groupCreatorsList = groupCreatorsList;
    }
}
