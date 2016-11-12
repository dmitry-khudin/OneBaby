package com.scotty.models;

import java.io.Serializable;

/**
 * Created by bryden_mac on 19/09/16.
 */
public class VariourTitleModel implements Serializable{
    private String title;
    private String imageURL;
    private Integer ids;

    public VariourTitleModel(String title, String imageURL, Integer ids) {
        this.title = title;
        this.imageURL = imageURL;
        this.ids = ids;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Integer getIds() {
        return ids;
    }

    public void setIds(Integer ids) {
        this.ids = ids;
    }
}
