package com.scotty.models;

import java.io.Serializable;

/**
 * Created by my_mac on 9/12/16.
 */
public class PurchaseHistoryModel implements Serializable{
    private String imgURL;
    private String contents1;
    private String contents2;
    private String money;
    private String date;

    public String getImgURL() {
        return imgURL;
    }

    public String getContents1() {
        return contents1;
    }

    public String getContents2() {
        return contents2;
    }

    public String getMoney() {
        return money;
    }

    public String getDate() {
        return date;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public void setContents1(String contents1) {
        this.contents1 = contents1;
    }

    public void setContents2(String contents2) {
        this.contents2 = contents2;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public PurchaseHistoryModel(String imgURL, String contents1, String contents2, String money, String date) {
        this.imgURL = imgURL;
        this.contents1 = contents1;
        this.contents2 = contents2;
        this.money = money;
        this.date = date;
    }
}
