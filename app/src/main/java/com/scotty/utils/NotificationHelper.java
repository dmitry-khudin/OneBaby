package com.scotty.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.scotty.models.ArticleModel;
import com.scotty.models.AuthorModel;
import com.scotty.models.CommentTableModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;



/**
 * Created by bryden on 10/15/16.
 */
public class NotificationHelper {
    private static final String AUTH_KEY = "key=AIzaSyC3xCjuBbut8btSMYcdvkLfkhURQes1qS4";
    private Context mContext;

    List<String> tokenLists;
    public NotificationHelper(Context mContext) {
        this.mContext = mContext;
    }

    public NotificationHelper(Context mContext, List<String> tokenLists) {
        this.mContext = mContext;
        this.tokenLists = tokenLists;
    }

    public void SendNewPostArticle(final ArticleModel model)
    {
        DatabaseReference ref = Constants.Author_Database.child(model.getAuthorID()).child("favorites");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, HashMap<String, String>> hashMap = (HashMap<String, HashMap<String, String>>) dataSnapshot.getValue();
                tokenLists = new ArrayList<String>();
                for (String key : hashMap.keySet())
                {
                    HashMap<String, String> map = hashMap.get(key);
                    tokenLists.add(map.get("deviceToken"));
                }
                SendNotification("post", "OneBaby", "New Article " + model.getName() + " Posted!");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void SendPushReply(final CommentTableModel commentTableModel)
    {
       // DatabaseReference ref = Constants.Comment_Database.child(commentTableModel.getCommentID());
        tokenLists = new ArrayList<String>();
        tokenLists.add(commentTableModel.getDeviceToken());
        SendNotification("reply", "OneBaby", General.screenName  + " replied to your comment!");
    }
    public void SendUpdateArticle(final ArticleModel model)
    {
        DatabaseReference ref = Constants.Author_Database.child(model.getAuthorID()).child("favorites");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, HashMap<String, String>> hashMap = (HashMap<String, HashMap<String, String>>) dataSnapshot.getValue();
                tokenLists = new ArrayList<String>();
                if (hashMap != null) {
                    for (String key : hashMap.keySet()) {
                        HashMap<String, String> map = hashMap.get(key);
                        tokenLists.add(map.get("deviceToken"));
                    }
                    SendNotification("post", "OneBaby", model.getName() + " is updated!");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void SendUpdateAuthor(AuthorModel model)
    {
        DatabaseReference ref = Constants.Author_Database.child(model.getKey());//.child("favorites");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Change", dataSnapshot.toString());
                AuthorModel model = dataSnapshot.getValue(AuthorModel.class);
                HashMap<String, HashMap<String, String>> hashMap = model.getFavorites();
                if (hashMap != null) {
                    tokenLists = new ArrayList<String>();
                    for (String key : hashMap.keySet()) {
                        HashMap<String, String> map = hashMap.get(key);
                        tokenLists.add(map.get("deviceToken"));
                    }
                    SendNotification("update", "OneBaby", model.getName() + " updated!!!");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void SendNotification(String type, String title, String body)
    {
        sendWithOtherThread(type, title, body);
    }


    private void sendWithOtherThread(final String type, final String title, final String bodyString) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                pushNotification(type, title, bodyString);
            }
        }).start();
    }

    private void pushNotification(String type, String scribeTitle, String bodyString) {
        JSONObject jPayload = new JSONObject();
        JSONObject jNotification = new JSONObject();
        JSONObject jData = new JSONObject();
        try {
            jNotification.put("title", scribeTitle);
//            if (type.equals(Constants.ARTICLE_POST))
//            {
//                bodyString = "New "
//            }
            jNotification.put("body", bodyString);
            jNotification.put("sound", "default");
            jNotification.put("badge", "1");
            jNotification.put("click_action", "OPEN_ACTIVITY_1");
            JSONArray ja = new JSONArray();
            for (int i = 0; i < tokenLists.size(); i++)
            {
                ja.put(tokenLists.get(i));
                Log.d("device", tokenLists.get(i));
            }
          //  ja.put(FirebaseInstanceId.getInstance().getToken());
//            ja.put("dfehVbeBBeo:APA91bEiGcS8OLGw71gFArdo0Tkd4fCOIpaiTu6WIb_1YHBk3jXVuv31iQXZ2RX8enCW2qV8jNQs9dcm49kgR5OFlkIGb1mSwmaPFjVrqeqY0ZEXyxXw9dhaaYkFwb9ITsqSqRObR0N6");
//            ja.put("dBUzrJhVmZ4:APA91bHsqBBdAx40D27_kxYavYwZ-y9rcv5x6MqlNuEkSM_CuO4_T0KefGaqXF-P0OK7sOHI0kURxti5za-2h2wsrITt4-O-9vomVUbQeNo-7vW-bx5aAYEihYt4kzv2H2HAecKeJ8pc");
            jPayload.put("registration_ids", ja);
            jData.put("picture_url", "http://opsbug.com/static/google-io.jpg");
            jData.put("type", type);
            jPayload.put("priority", "high");
            jPayload.put("notification", jNotification);
            jPayload.put("data", jData);

            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", AUTH_KEY);
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
            Log.d("Push", e.getMessage());
        }
    }

    private String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next().replace(",", ",\n") : "";
    }

}
