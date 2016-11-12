package com.scotty.onebaby;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.common.collect.Lists;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.scotty.adapters.Various_Title_Adapter;
import com.scotty.fragments.FragmentSettings;
import com.scotty.fragments.FragmentTitlePage;
import com.scotty.springindicator.ScrollerViewPager;
import com.scotty.springindicator.SpringIndicator;
import com.scotty.utils.Constants;
import com.scotty.utils.General;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;
import github.chenupt.multiplemodel.viewpager.ModelPagerAdapter;
import github.chenupt.multiplemodel.viewpager.PagerModelManager;
import okhttp3.OkHttpClient;

public class VariousTitles_Page extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    ScrollerViewPager viewPager;
    FirebaseAuth mAuth;
    private static final String MESSAGE_TAG = "message_tag";
    private static final String TAG = "tag";
    FirebaseAuth.AuthStateListener mAuthListener;
    OkHttpClient mClient;
    String refreshedToken = "";
    JSONArray jsonArray = new JSONArray();

    //    name: textView4
//    profileImage: profile_image de.hdodenhof.circleimageview.CircleImageView
    TextView textName;
    CircleImageView imageProfile;
    GoogleApiClient mGoogleApiClient;
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_various_titles__page);
        initView();
    }


    private void initView()
    {
        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getResources().getString(R.string.default_web_client_id))
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        viewPager = (ScrollerViewPager) findViewById(R.id.view_pager);
        SpringIndicator springIndicator = (SpringIndicator) findViewById(R.id.indicator);
        //   ModelPagerAdapter modelPagerAdapter;
        PagerModelManager manager = new PagerModelManager();
        manager.addCommonFragment(FragmentTitlePage.class, getBgRes(), getTitles());
        manager.addFragment(FragmentSettings.newInstance("title", "title"), "Home");
        ModelPagerAdapter adapter = new ModelPagerAdapter(getSupportFragmentManager(), manager);
        viewPager.setAdapter(adapter);

        viewPager.fixScrollSpeed();
        // just set viewPager
        springIndicator.setViewPager(viewPager);
        calendar = Calendar.getInstance(TimeZone.getDefault());
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        Log.d("weekday", " " + day + " monday " + Calendar.MONDAY);
      //  viewPager.setCurrentItem(1);
        switch (day)
        {
            case Calendar.SUNDAY: {
                // Current day is Sunday
                viewPager.setCurrentItem(6);
                break;
            }
            case Calendar.MONDAY: {
                // Current day is Monday
                viewPager.setCurrentItem(0);
                break;
            }

            case Calendar.TUESDAY: {
                viewPager.setCurrentItem(1);
                break;
            }
            case Calendar.WEDNESDAY: {
                viewPager.setCurrentItem(2);
                break;
            }
            case Calendar.THURSDAY: {
                viewPager.setCurrentItem(3);
                break;
            }
            case Calendar.FRIDAY: {
                viewPager.setCurrentItem(4);
                break;
            }
            case Calendar.SATURDAY: {
                viewPager.setCurrentItem(5);
                break;
            }
            default:
                break;
//        }
        }
    }

    public void SendNotification(String type, String title)
    {
        sendWithOtherThread(type, title);
    }
    public void SignOut()
    {
        mAuth.signOut();
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {

            }
        });
        General.SetStringData(this, Constants.USER_LOGINED, "false");
        if (AccessToken.getCurrentAccessToken() != null)
        {
            //    General.ShowToast(getApplicationContext(), "already logined");
            LoginManager.getInstance().logOut();
        }
        Intent intent = new Intent(this, LogInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    private List<String> getTitles(){
        return Lists.newArrayList("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun");
    }
    private List<Integer> getBgRes(){
        return Lists.newArrayList(1, 2, 3, 4, 5, 6, 7);
    }

    public void OnSubScription(String title)
    {
        FirebaseMessaging.getInstance().subscribeToTopic("news");
        String msg = getString(R.string.msg_subscribed);
        Log.d(MESSAGE_TAG, msg);
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d("Token", token);
      //  Toast.makeText(VariousTitles_Page.this, msg, Toast.LENGTH_LONG).show();
    }

    private void sendWithOtherThread(final String type, final String tilte) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                pushNotification(type, tilte);
            }
        }).start();
    }

    private void pushNotification(String type, String scribeTitle) {
        JSONObject jPayload = new JSONObject();
        JSONObject jNotification = new JSONObject();
        JSONObject jData = new JSONObject();
        try {
            jNotification.put("title", "Google I/O 2016");
            jNotification.put("body", "Firebase Cloud Messaging (App)");
            jNotification.put("sound", "default");
            jNotification.put("badge", "1");
            jNotification.put("click_action", "OPEN_ACTIVITY_1");

            jData.put("picture_url", "http://opsbug.com/static/google-io.jpg");

            switch(type) {
                case "tokens":
                    JSONArray ja = new JSONArray();
                    ja.put("dfehVbeBBeo:APA91bEiGcS8OLGw71gFArdo0Tkd4fCOIpaiTu6WIb_1YHBk3jXVuv31iQXZ2RX8enCW2qV8jNQs9dcm49kgR5OFlkIGb1mSwmaPFjVrqeqY0ZEXyxXw9dhaaYkFwb9ITsqSqRObR0N6");
                    ja.put("dBUzrJhVmZ4:APA91bHsqBBdAx40D27_kxYavYwZ-y9rcv5x6MqlNuEkSM_CuO4_T0KefGaqXF-P0OK7sOHI0kURxti5za-2h2wsrITt4-O-9vomVUbQeNo-7vW-bx5aAYEihYt4kzv2H2HAecKeJ8pc");
                    jPayload.put("registration_ids", ja);
                    break;
                case "topic":
                    jPayload.put("to", "/topics/" + scribeTitle);
                    break;
                case "condition":
                    jPayload.put("condition", "'comments' in topics || 'favorite' in topics");
                    break;
                default:
                    jPayload.put("to", FirebaseInstanceId.getInstance().getToken());
            }

            jPayload.put("priority", "high");
            jPayload.put("notification", jNotification);
            jPayload.put("data", jData);

            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "key=AIzaSyC3xCjuBbut8btSMYcdvkLfkhURQes1qS4");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Send FCM message content.
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(jPayload.toString().getBytes());

            // Read FCM response.
            InputStream inputStream = conn.getInputStream();
            final String resp = convertStreamToString(inputStream);

            Handler h = new Handler(Looper.getMainLooper());
            h.post(new Runnable() {
                @Override
                public void run() {
                    //   mTextView.setText(resp);
                }
            });
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    private String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next().replace(",", ",\n") : "";
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (!connectionResult.isSuccess())
            Toast.makeText(getApplicationContext(), "Connection Failed", Toast.LENGTH_LONG).show();
    }
}
