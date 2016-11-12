package com.scotty.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by bryden on 10/4/16.
 */
public class CommentTableModel implements Serializable {
    private String articleID;
    private String userID;
    private String username;
    private String comments;
    private int likes;
    private String postdate;
    private String commentID;
    private HashMap<String, HashMap<String, String>> replies;
    private HashMap<String, HashMap<String, String>> flaglist;
    private HashMap<String, HashMap<String, String>> likelist;
    private String timestamp;
    private String deviceToken;
    public int getLikes() {
        return likes;
    }

//    public CommentTableModel(String articleID, String userID, String username, String comments, int likes, String postdate, String commentID, HashMap<String, HashMap<String, String>> replies, HashMap<String, HashMap<String, String>> flaglist, HashMap<String, HashMap<String, String>> likelist, String timestamp) {
//        this.articleID = articleID;
//        this.userID = userID;
//        this.username = username;
//        this.comments = comments;
//        this.likes = likes;
//        this.postdate = postdate;
//        this.commentID = commentID;
//        this.replies = replies;
//        this.flaglist = flaglist;
//        this.likelist = likelist;
//        this.timestamp = timestamp;
//    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public CommentTableModel(String articleID, String userID, String username, String comments, int likes, String postdate, String commentID, HashMap<String, HashMap<String, String>> replies, HashMap<String, HashMap<String, String>> flaglist, HashMap<String, HashMap<String, String>> likelist, String timestamp, String deviceToken) {
        this.articleID = articleID;
        this.userID = userID;
        this.username = username;
        this.comments = comments;
        this.likes = likes;
        this.postdate = postdate;
        this.commentID = commentID;
        this.replies = replies;
        this.flaglist = flaglist;
        this.likelist = likelist;
        this.timestamp = timestamp;
        this.deviceToken = deviceToken;
    }

    public CommentTableModel() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCommentID() {
        return commentID;
    }

    public void setCommentID(String commentID) {
        this.commentID = commentID;
    }

    public void setLikes(int like) {
        this.likes = like;
    }


    public String getArticleID() {
        return articleID;
    }

    public void setArticleID(String articleID) {
        this.articleID = articleID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public HashMap<String, HashMap<String, String>> getReplies() {
        return replies;
    }

    public void setReplies(HashMap<String, HashMap<String, String>> replies) {
        this.replies = replies;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public List<ReplyModel> getReplyModels()
    {
        List<ReplyModel> map_list = new ArrayList<>();
        ReplyModel model = new ReplyModel();
        HashMap<String, String> temp_map;
        if (this.replies != null) {
            for (String key : this.replies.keySet()) {
                temp_map = this.replies.get(key);
                map_list.add(ReplyModel.getFromHashMap(temp_map));
            }
        }
        return map_list;
    }

    public HashMap<String, HashMap<String, String>> getFlaglist() {
        return flaglist;
    }

    public void setFlaglist(HashMap<String, HashMap<String, String>> flaglist) {
        this.flaglist = flaglist;
    }

    public HashMap<String, HashMap<String, String>> getLikelist() {
        return likelist;
    }

    public void setLikelist(HashMap<String, HashMap<String, String>> likelist) {
        this.likelist = likelist;
    }

    public String getPostdate() {
        return postdate;
    }

    public void setPostdate(String postdate) {
        this.postdate = postdate;
    }
}
