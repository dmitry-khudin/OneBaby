package com.scotty.onebaby;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.scotty.adapters.ExpandableCommentAdapter;
import com.scotty.models.CommentTableModel;
import com.scotty.models.ReplyModel;
import com.scotty.utils.General;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class CommentViewActivity extends AppCompatActivity {

    ExpandableListView listView;
    DatabaseReference mComments_Database;
    List<CommentTableModel> commentTableModelList;
    ExpandableCommentAdapter adapter;
    HashMap<String, List<ReplyModel>> detailContent;
    Query query;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_view);
        commentTableModelList = new ArrayList<>();
       // detailContent = new HashMap<>();
        listView = (ExpandableListView) findViewById(R.id.expandableListView);
        listView.setGroupIndicator(null);
        mComments_Database = FirebaseDatabase.getInstance().getReference("Comments");
        query = mComments_Database.orderByChild("articleID").equalTo("articleID");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               CommentTableModel model;
                commentTableModelList = new ArrayList<>();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    model = dataSnapshot1.getValue(CommentTableModel.class);
//                    if (model != null) {
//                        Log.d("tag", model.getComments());
//                        commentTableModelList.add(model);
//                    }
                    commentTableModelList.add(model);
                    Log.d("model", "size " + model.getUsername());
                  //  detailContent.put(model.getCommentID(), model.getReplyModels());
                }
                adapter = new ExpandableCommentAdapter( commentTableModelList, CommentViewActivity.this);
                listView.setAdapter(adapter);
                Log.d("model", "size " + adapter.getChild(0, 0).getPostdate());
                for(int i=0; i < adapter.getGroupCount(); i++)
                    listView.expandGroup(i);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void Reply(String text, CommentTableModel model)
    {
        DatabaseReference temp = mComments_Database.child(model.getCommentID()).child("replies").push();
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String reportDate = df.format(date);
        ReplyModel tempModel = new ReplyModel(General.currentUser.getUid(), text, General.currentUser.getDisplayName(), reportDate, General.deviceToken);
       // String key = temp.push();
        temp.setValue(tempModel.getHashReply());
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
        Log.d("model", model.getCommentID());
    }
}
