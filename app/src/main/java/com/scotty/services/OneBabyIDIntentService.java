package com.scotty.services;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.scotty.utils.Constants;
import com.scotty.utils.General;

/**
 * Created by bryden_mac on 17/09/16.
 */
public class OneBabyIDIntentService extends FirebaseInstanceIdService {
    private static final String TAG = "OneBabyIDService";


    @Override
    public void onTokenRefresh() {
//        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token)
    {
        General.SetStringData(getApplicationContext(), Constants.USER_TOKEN, token);
    }
}
