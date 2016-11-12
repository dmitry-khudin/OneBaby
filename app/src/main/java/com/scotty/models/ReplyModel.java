package com.scotty.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by bryden on 10/4/16.
 */
public class ReplyModel implements Serializable{
    private String userID;
    private String username;
    private String usercomment;

    private String postdate;

    private String deviceToken;
    public ReplyModel() {
    }


    public ReplyModel(String userID, String username, String usercomment, String postdate, String deviceToken) {
        this.userID = userID;
        this.username = username;
        this.usercomment = usercomment;
        this.postdate = postdate;
        this.deviceToken = deviceToken;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsercomment() {
        return usercomment;
    }

    public void setUsercomment(String usercomment) {
        this.usercomment = usercomment;
    }

    public HashMap<String, String> getHashReply()
    {
        HashMap<String, String> map = new HashMap<>();
        map.put("usercomment", this.getUsercomment());
        map.put("username", this.getUsername());
        map.put("userID", this.getUserID());
        map.put("postdate", this.getPostdate());
        return map;
    }
    public static ReplyModel getFromHashMap(HashMap<String, String> map)
    {
        ReplyModel model = new ReplyModel();
        model.setUsercomment(map.get("usercomment"));
        model.setUserID(map.get("userID"));
        model.setUsername(map.get("username"));
        model.setPostdate(map.get("postdate"));
        return model;

    }

    public String getPostdate() {
        return postdate;
    }

    public void setPostdate(String postdate) {
        this.postdate = postdate;
    }
}
