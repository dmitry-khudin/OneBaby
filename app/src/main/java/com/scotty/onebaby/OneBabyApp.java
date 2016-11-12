package com.scotty.onebaby;

import android.app.Application;

import com.facebook.FacebookSdk;

/**
 * Created by my_mac on 9/16/16.
 */
public class OneBabyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
    }


}
