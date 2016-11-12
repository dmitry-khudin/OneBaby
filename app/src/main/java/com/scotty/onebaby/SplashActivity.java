package com.scotty.onebaby;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.facebook.FacebookSdk;
import com.facebook.internal.LoginAuthorizationType;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.scotty.utils.Constants;
import com.scotty.utils.General;

import java.util.HashMap;

public class SplashActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
   /// FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_splash);
        DisplayMetrics displayMetrics = new DisplayMetrics();

        WindowManager windowmanager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
        Constants.deviceWidth = displayMetrics.widthPixels;
        Constants.deviceHeight = displayMetrics.heightPixels;
        Constants.Comment_Database =  FirebaseDatabase.getInstance().getReference(Constants.DB_COMMENT);
        Constants.Article_Database =  FirebaseDatabase.getInstance().getReference(Constants.DB_ARTICLE);
        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                General.currentUser = firebaseAuth.getCurrentUser();
            }
        });
    //    General.currentUser = mAuth.getCurrentUser();
        String flag = General.GetStringData(getApplicationContext(), Constants.USER_LOGINED);
        if (flag.equals("true"))
            General.logined = true;
        else
            General.logined = false;

        new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d("time", String.valueOf(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                //    if (millisUntilFinished >= 1000)
                {
                    overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
                }

                if (General.currentUser != null) {
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(Constants.USER_DB).
                            child(General.currentUser.getUid());
                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            HashMap<String, String> map  = (HashMap<String, String>) dataSnapshot.getValue();
                            General.screenName = map.get("account_id");
                            General.firstName = map.get("user_firstname");
                            General.lastName = map.get("user_lastname");
                            General.email = map.get("user_email");
                            General.UID = General.currentUser.getUid();
                           Intent intent = new Intent(SplashActivity.this, LogInActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                            finish();
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                } else {
                    Intent intent = new Intent(SplashActivity.this, LogInActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                    finish();
                }
            }
        }.start();

    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
        //  overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }
}
