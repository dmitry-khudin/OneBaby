package com.scotty.onebaby;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvingResultCallbacks;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.server.converter.StringToIntConverter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;

import com.scotty.utils.Constants;
import com.scotty.utils.General;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SettingsActivity extends AppCompatActivity {

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

    }
    public void OnBack(View view)
    {
        finish();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }

    public void OnPurchaseHistory(View view)
    {
        /*
        Intent intent = new Intent(this, PurchaseHistory.class);
        startActivity(intent);
        finish();*/
        
//        JSONArray array = new JSONArray();
//        array.put("dfehVbeBBeo:APA91bEiGcS8OLGw71gFArdo0Tkd4fCOIpaiTu6WIb_1YHBk3jXVuv31iQXZ2RX8enCW2qV8jNQs9dcm49kgR5OFlkIGb1mSwmaPFjVrqeqY0ZEXyxXw9dhaaYkFwb9ITsqSqRObR0N6");
//        array.put("dBUzrJhVmZ4:APA91bHsqBBdAx40D27_kxYavYwZ-y9rcv5x6MqlNuEkSM_CuO4_T0KefGaqXF-P0OK7sOHI0kURxti5za-2h2wsrITt4-O-9vomVUbQeNo-7vW-bx5aAYEihYt4kzv2H2HAecKeJ8pc");
//        SendMessage(array, "title", "FirstMessage", "http://opsbug.com/static/google-io.jpg",
//        "message");
     //   sendWithOtherThread("topic");

    }





    ///// Send push notification to one device;




    /*
    private String postToFCM(String bodyString) throws IOException{
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, bodyString);
        Log.d("bodyString", bodyString);
        Request request = new Request.Builder()
                .url(Constants.FCM_MESSAGE_URL)
                .post(body)
                .addHeader("Authorization", "key=" + getString(R.string.server_key))
                .build();
        Response response = mClient.newCall(request).execute();
        return response.body().toString();
    }


    public void SendMessage(final JSONArray recipients, final String title,
                            final String body, final String icon, final String message)
    {
        new AsyncTask<String, String, String>()
        {
            @Override
            protected String doInBackground(String... params) {
                JSONObject root = new JSONObject();
                JSONObject notification = new JSONObject();
                JSONObject data = new JSONObject();
                try {
                    notification.put("body", body);
                    notification.put("title", title);
                    notification.put("icon", icon);
                    notification.put("sound", "default");
                    notification.put("badge", "+1");
                    notification.put("click_action", "OPEN_ACTIVITY_1");
                    data.put("message", message);
                    root.put("notification", notification);
                    root.put("data", data);
                    root.put("registration_ids", recipients);
                    String result = postToFCM(root.toString());
                    Log.d("Main Activity", "Result : " + result);
                    return result;
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String result) {
               try{
                   JSONObject resultJson = new JSONObject(result);
                   int success, failure;
                   success = resultJson.getInt("success");
                   failure = resultJson.getInt("failure");
                   Toast.makeText(SettingsActivity.this, "Message Success: " + success
                   + "Message Failed " + failure, Toast.LENGTH_LONG).show();
               }
               catch (JSONException e){
                   e.printStackTrace();
                   Toast.makeText(getApplicationContext(), "Message Error ", Toast.LENGTH_LONG).show();
               }
            }
        }.execute();
    }
*/


}
