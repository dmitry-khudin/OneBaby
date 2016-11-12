package com.scotty.utils;

import com.google.common.util.concurrent.SimpleTimeLimiter;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.scotty.onebaby.R;

/**
 * Created by my_mac on 9/11/16.
 */
public class Constants {
    public static final String USER_LOGINED = "user_logined";
    public static final String USER_NAME = "user_name";
    public static final String USER_EMAIL = "user_email";
    public static final String USER_PASSWORD = "user_password";
    public static final String USER_PHOTO = "user_photo";
    public static final String  USER_FIRSTNAME = "user_firstname";
    public static final String  USER_LASTNAME = "user_lastname";
    public static final String  USER_ID = "user_id";
    public static final String  ACCOUNT_ID = "account_id";
    public static final String GOOGLE_LOGIN = "google_login";
    public static final String FACEBOOK_LOGIN = "facebook_login";
    public static final String EMAIL_LOGIN = "email_login";

    public static final String DEVICE_ID = "device_id";
    public static final String OAUTH_REDIRECT_URL = "https://onebaby-f9aaf.firebaseapp.com/__/auth/handler";

    public static final String USER_TOKEN = "user_token";
    public static final String FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send";
    public static final String USER_DB = "users";
    public static final String ONE_ARTICLE = "one_article";
    public static final String COMMENTS = "comments";
    public static final String FAVORITE = "favorite";
    public static final String FAVORITE_FLAG = "favorite_flag";
    public static final String COMMENTS_FLAG = "comments_flag";
    public static final int ids[] =  {R.drawable.baby_image_1, R.drawable.baby_image_2, R.drawable.baby_image_3,
            R.drawable.baby_image_4, R.drawable.baby_image_5, R.drawable.baby_image_6, R.drawable.baby_image_7};
    public static String DB_ARTICLE = "Articles";
    public static String DB_AUTHOR= "Authors";
    public static String DB_COMMENT= "Comments";
    public static FirebaseUser currentUser;
    public static DatabaseReference Author_Database;
    public static DatabaseReference Article_Database;
    public static DatabaseReference Comment_Database;
    public static int deviceWidth; //= displayMetrics.widthPixels;
    public static int deviceHeight; // = displayMetrics.heightPixels;
   // public static final String
}
