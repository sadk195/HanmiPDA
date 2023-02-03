package com.example.gmax;

public class DataModel {
    String title;
    int image_path;

    public String getTitle() {
        return title;
    }

    public void setTitle(String pTitle) {
        this.title = pTitle;
    }

    public int getImage_path() {
        return image_path;
    }

    public void setImage_path(int pImage_path) {
        this.image_path = pImage_path;
    }

    public DataModel(String pTitle, int pImage_path) {
        this.title = pTitle;
        this.image_path = pImage_path;
    }
}
