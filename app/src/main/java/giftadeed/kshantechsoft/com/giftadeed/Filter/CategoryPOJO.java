package giftadeed.kshantechsoft.com.giftadeed.Filter;

/**
 * Created by I-Sys on 27-Mar-18.
 */

public class CategoryPOJO {
    String id;
    String name;
    String characterpath;
    String PhotoPath;

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
}
