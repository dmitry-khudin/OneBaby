package com.scotty.models;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by bryden on 10/15/16.
 */
public class ArticleModel implements Serializable{

    private String key;
    private String authorID;
    private String name;
    private String image;
    private String postdate;
    private int commentCount;

    public ArticleModel(String key, String authorID, String name, String image, String postdate) {
        this.key = key;
        this.authorID = authorID;
        this.name = name;
        this.image = image;
        this.postdate = postdate;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public ArticleModel(String key, String authorID, String name, String image, String postdate, int commentCount) {
        this.key = key;
        this.authorID = authorID;
        this.name = name;
        this.image = image;
        this.postdate = postdate;
        this.commentCount = commentCount;
    }

    public ArticleModel() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getAuthorID() {
        return authorID;
    }

    public void setAuthorID(String authorID) {
        this.authorID = authorID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPostdate() {
        return postdate;
    }

    public void setPostdate(String postdate) {
        this.postdate = postdate;
    }

    public HashMap<String, Object> getHashFromModel()
    {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("name", getName());
        hashMap.put("key", getKey());
        hashMap.put("image", getImage());
        hashMap.put("postdate", getPostdate());
        hashMap.put("authorID", getAuthorID());
        hashMap.put("commentCount", getCommentCount());
        return hashMap;
    }
}
