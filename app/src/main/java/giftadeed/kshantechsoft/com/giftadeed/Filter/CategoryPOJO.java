/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.Filter;

/**
 * Created by I-Sys on 27-Mar-18.
 */

public class CategoryPOJO {
    String id;
    String name;
    String characterpath;
    String PhotoPath;
    boolean isChecked = true;

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

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
