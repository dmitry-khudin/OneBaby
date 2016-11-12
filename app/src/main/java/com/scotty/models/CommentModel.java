package com.scotty.models;

import java.io.Serializable;

/**
 * Created by bryden_mac on 19/09/16.
 */
public class CommentModel implements Serializable{

    private String userID;
    private String contents;
    private String commentDate;
    private String likeCount;

    public CommentModel(String userID, String contents, String commentDate, String likeCount) {
        this.userID = userID;
        this.contents = contents;
        this.commentDate = commentDate;
        this.likeCount = likeCount;
    }

    public String getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(String likeCount) {
        this.likeCount = likeCount;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(String commentDate) {
        this.commentDate = commentDate;
    }
}
