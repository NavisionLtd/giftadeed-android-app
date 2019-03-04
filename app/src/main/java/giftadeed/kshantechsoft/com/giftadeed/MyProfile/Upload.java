package giftadeed.kshantechsoft.com.giftadeed.MyProfile;

public class Upload {
    public String id;
    public String url;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public Upload() {
    }

    public Upload(String id, String url) {
        this.id = id;
        this.url= url;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }
}
