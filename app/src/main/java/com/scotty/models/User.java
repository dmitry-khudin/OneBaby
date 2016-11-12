package com.scotty.models;

import com.scotty.onebaby.SelectedTitlePage;
import com.scotty.onebaby.SettingsActivity;
import com.scotty.utils.Constants;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by bryden_mac on 20/09/16.
 */
public class User implements Serializable{
    private String account_id;
    private String device_id;
    private String user_email;
    private String user_firstname;
    private String user_lastname;
    private String user_photo;

    public User(String account_id, String device_id, String user_email, String user_firstname, String user_lastname, String user_photo) {
        this.account_id = account_id;
        this.device_id = device_id;
        this.user_email = user_email;
        this.user_firstname = user_firstname;
        this.user_lastname = user_lastname;
        this.user_photo = user_photo;
    }

    public User() {
    }

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_firstname() {
        return user_firstname;
    }

    public void setUser_firstname(String user_firstname) {
        this.user_firstname = user_firstname;
    }

    public String getUser_lastname() {
        return user_lastname;
    }

    public void setUser_lastname(String user_lastname) {
        this.user_lastname = user_lastname;
    }

    public String getUser_photo() {
        return user_photo;
    }

    public void setUser_photo(String user_photo) {
        this.user_photo = user_photo;
    }

    public HashMap<String , String> getHashMap()
    {
        HashMap<String , String > map = new HashMap<>();
        map.put(Constants.ACCOUNT_ID, this.account_id);
        map.put(Constants.USER_FIRSTNAME, this.user_firstname);
        map.put(Constants.USER_LASTNAME, this.user_lastname);
        map.put(Constants.USER_EMAIL, this.user_email);
        map.put(Constants.USER_PHOTO, this.user_photo);
        map.put(Constants.DEVICE_ID, this.device_id);
        return map;
    }
}
