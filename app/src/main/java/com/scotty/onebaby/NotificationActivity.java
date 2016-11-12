package com.scotty.onebaby;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import com.google.firebase.messaging.FirebaseMessaging;
import com.kyleduo.switchbutton.SwitchButton;
import com.scotty.utils.Constants;
import com.scotty.utils.General;

public class NotificationActivity extends AppCompatActivity {

    SwitchButton switchButton1, switchButton2, switchButton3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        switchButton1 = (SwitchButton) findViewById(R.id.sb_ios);
        switchButton2 = (SwitchButton) findViewById(R.id.sb_ios1);
        switchButton3 = (SwitchButton) findViewById(R.id.sb_ios2);
        String comment_flag = General.GetStringData(this, Constants.COMMENTS_FLAG);
        String favorite_flag = General.GetStringData(this, Constants.FAVORITE_FLAG);
        switchButton1.setChecked(General.getString(comment_flag) && General.getString(favorite_flag));
        switchButton2.setChecked(General.getString(favorite_flag));
        switchButton3.setChecked(General.getString(comment_flag));
        switchButton1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                General.SetStringData(getApplicationContext(), Constants.FAVORITE_FLAG, General.getBool(isChecked));
                General.SetStringData(getApplicationContext(), Constants.COMMENTS_FLAG, General.getBool(isChecked));
                if (isChecked)
                {
                    FirebaseMessaging.getInstance().subscribeToTopic(Constants.COMMENTS);
                    FirebaseMessaging.getInstance().subscribeToTopic(Constants.FAVORITE);
                }
                else
                {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(Constants.COMMENTS);
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(Constants.FAVORITE);
                }
            }
        });
        switchButton2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                General.SetStringData(getApplicationContext(), Constants.FAVORITE_FLAG, General.getBool(isChecked));

                if (isChecked)
                {
                    FirebaseMessaging.getInstance().subscribeToTopic(Constants.FAVORITE);
                }
                else  FirebaseMessaging.getInstance().unsubscribeFromTopic(Constants.FAVORITE);
            }
        });
        switchButton3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                General.SetStringData(getApplicationContext(), Constants.COMMENTS_FLAG, General.getBool(isChecked));
                if (isChecked)
                {
                    FirebaseMessaging.getInstance().subscribeToTopic(Constants.COMMENTS);
                }
                else  FirebaseMessaging.getInstance().unsubscribeFromTopic(Constants.COMMENTS);
            }
        });
    }
    public void OnBack(View view)
    {
        finish();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }
}
