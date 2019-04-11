package giftadeed.kshantechsoft.com.giftadeed.Signup;

/**
 * Created by Nilesh on 4/26/2017.
 */

public class SignupPOJO {
    String id;
    String name;
    String type;
    String characterpath;
    String PhotoPath;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhotoPath() {
        return PhotoPath;
    }

    public void setPhotoPath(String photoPath) {
        PhotoPath = photoPath;
    }

    public String getCharacterpath() {
        return characterpath;
    }

    public void setCharacterpath(String characterpath) {
        this.characterpath = characterpath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }


}
