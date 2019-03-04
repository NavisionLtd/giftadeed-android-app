package giftadeed.kshantechsoft.com.giftadeed.AdvisoryBoard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by roshan on 19/3/18.
 */

public class AdvisoryResponse {

    @SerializedName("advisory")
    @Expose
    private List<Advisory> advisory = null;

    public List<Advisory> getAdvisory() {
        return advisory;
    }

    public void setAdvisory(List<Advisory> advisory) {
        this.advisory = advisory;
    }

    public class Advisory {

        @SerializedName("imgUrl")
        @Expose
        private String imgUrl;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("desig")
        @Expose
        private String desig;
        @SerializedName("socialLinks")
        @Expose
        private String socialLinks;
        @SerializedName("desc")
        @Expose
        private String desc;

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDesig() {
            return desig;
        }

        public void setDesig(String desig) {
            this.desig = desig;
        }

        public String getSocialLinks() {
            return socialLinks;
        }

        public void setSocialLinks(String socialLinks) {
            this.socialLinks = socialLinks;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

    }
}
