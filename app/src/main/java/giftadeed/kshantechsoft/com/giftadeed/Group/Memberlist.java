
package giftadeed.kshantechsoft.com.giftadeed.Group;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.list_Model.Taggedlist;

public class Memberlist {
    @SerializedName("is_blocked")
    @Expose
    private Integer isBlocked;
    @SerializedName("tot_member")
    @Expose
    String totalMembers;
    @SerializedName("mem_list")
    @Expose
    public List<GroupMember> memlist = null;

    public List<GroupMember> getMemlist() {
        return memlist;
    }
    public Integer getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(Integer isBlocked) {
        this.isBlocked = isBlocked;
    }
    public void setMemlist(List<GroupMember> memberList) {
        this.memlist = memberList;
    }

    public String getTotalMembers() {
        return totalMembers;
    }

    public void setTotalMembers(String totalMembers) {
        this.totalMembers = totalMembers;
    }
}
