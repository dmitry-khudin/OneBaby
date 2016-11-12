package com.scotty.models;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by bryden on 10/15/16.
 */
public class AuthorModel implements Serializable{

    private String image;
    private String key;
    private String name;
    private String CreatedDate;
    private int favoriteCount;

    private HashMap<String, HashMap<String, String>> favorites;

    public AuthorModel(String image, String key, String name, String createdDate, int favoriteCount) {
        this.image = image;
        this.key = key;
        this.name = name;
        CreatedDate = createdDate;
        this.favoriteCount = favoriteCount;
    }

    public AuthorModel(String image, String key, String name, String createdDate, int favoriteCount, HashMap<String, HashMap<String, String>> favorites) {
        this.image = image;
        this.key = key;
        this.name = name;
        CreatedDate = createdDate;
        this.favoriteCount = favoriteCount;
        this.favorites = favorites;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        this.CreatedDate = createdDate;
    }

    public AuthorModel() {
    }

    public String getImage() {
        return image;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<String, String> getHashFromModel()
    {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("name", getName());
        hashMap.put("key", getKey());
        hashMap.put("image", getImage());
        hashMap.put("CreatedDate", getCreatedDate());
        return hashMap;
    }

    public HashMap<String, HashMap<String, String>> getFavorites() {
        return favorites;
    }

    public void setFavorites(HashMap<String, HashMap<String, String>> favorites) {
        this.favorites = favorites;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }
}
