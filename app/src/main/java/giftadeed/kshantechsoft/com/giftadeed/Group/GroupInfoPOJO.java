package giftadeed.kshantechsoft.com.giftadeed.Group;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GroupInfoPOJO {
    @SerializedName("is_blocked")
    @Expose
    private Integer isBlocked;
    @SerializedName("create_date")
    @Expose
    private String create_date;
    @SerializedName("grp_name")
    @Expose
    String group_name;
    @SerializedName("group_logo")
    @Expose
    String group_image;
    @SerializedName("description")
    @Expose
    private String group_desc;
    @SerializedName("creator_name")
    @Expose
    private String creator_name;
    @SerializedName("creator_id")
    @Expose
    private String creator_id;
    @SerializedName("admin_ids")
    @Expose
    private String admin_ids;

    public Integer getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(Integer isBlocked) {
        this.isBlocked = isBlocked;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getGroup_image() {
        return group_image;
    }

    public void setGroup_image(String group_image) {
        this.group_image = group_image;
    }

    public String getGroup_desc() {
        return group_desc;
    }

    public void setGroup_desc(String group_desc) {
        this.group_desc = group_desc;
    }

    public String getCreator_name() {
        return creator_name;
    }

    public void setCreator_name(String creator_name) {
        this.creator_name = creator_name;
    }

    public String getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(String creator_id) {
        this.creator_id = creator_id;
    }

    public String getAdmin_ids() {
        return admin_ids;
    }

    public void setAdmin_ids(String admin_ids) {
        this.admin_ids = admin_ids;
    }
}
