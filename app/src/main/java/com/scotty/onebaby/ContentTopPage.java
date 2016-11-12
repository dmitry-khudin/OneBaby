package com.scotty.onebaby;

import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.api.model.StringList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.scotty.adapters.ArticleHorizontalAdapter;
import com.scotty.adapters.CommentAdapter;
import com.scotty.adapters.ExpandableCommentAdapter;
import com.scotty.expandablelayout.ExpandableRelativeLayout;
import com.scotty.expandablelayout.HorizontalListView;
import com.scotty.models.ArticleModel;
import com.scotty.models.CommentModel;
import com.scotty.models.CommentTableModel;
import com.scotty.models.ReplyModel;
import com.scotty.utils.Constants;
import com.scotty.utils.General;
import com.scotty.utils.NotificationHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static java.security.AccessController.getContext;

public class ContentTopPage extends BaseActivity {

    ExpandableRelativeLayout mExpandLayout;
    HorizontalListView articleList;

    LinearLayout relativeLayout;
    List<ArticleModel> articleModelList = new ArrayList<>();
    List<CommentModel> commentModelList = new ArrayList<>();
    ArticleHorizontalAdapter mArticleAdapter;
    CommentAdapter mCommentAdapter;
    ExpandableListView commentList;
    DatabaseReference mComments_Database;
    int size;
    List<CommentTableModel> commentTableModelList;// = new ArrayList<>();
    ExpandableCommentAdapter adapter;
    Button expandButton ;
    ImageButton collapseButton;
    String articleID;
    int count = 1;
    ToggleButton toggleButton;
    Button commentButton;
    EditText editText_Comment;
    ScrollView scrollView;
    NotificationHelper notificationHelper;
    ArticleModel articleModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_top_temp);
        Bundle bundle = getIntent().getExtras();
        articleModel = (ArticleModel) bundle.get(Constants.ONE_ARTICLE);
        Log.d("article_model", articleModel.getKey());
        notificationHelper = new NotificationHelper(this);
        collapseButton = (ImageButton) findViewById(R.id.imageButton9);
        toggleButton = (ToggleButton) findViewById(R.id.imageButton5);
        scrollView = (ScrollView) findViewById(R.id.scrollView3);
        commentButton = (Button) findViewById(R.id.button2);
        editText_Comment = (EditText) findViewById(R.id.editText13);
        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostComment(editText_Comment.getText().toString());
            }
        });
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        int DeviceWidth = displayMetrics.widthPixels;
        int DeviceHeight = displayMetrics.heightPixels;
        ImageView imageView = (ImageView) findViewById(R.id.imageView13);
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        layoutParams.height = DeviceHeight;
        layoutParams.width = DeviceWidth;
        imageView.setLayoutParams(layoutParams);

    //    Toast.makeText(this, General.currentUser.getDisplayName(), Toast.LENGTH_LONG).show();


        articleID = articleModel.getKey();
        collapseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("listsize", "" + size);
                if (count > 0) count--;
                General.setListViewHeight(commentList, count);
            }
        });
        expandButton = (Button) findViewById(R.id.button7);

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                OnReadMore(isChecked);
            }
        });
        mComments_Database = FirebaseDatabase.getInstance().getReference("Comments");

        relativeLayout = (LinearLayout) findViewById(R.id.expandableLayout) ;
        articleList = (HorizontalListView) findViewById(R.id.HorizontalList);
        commentList = (ExpandableListView) findViewById(R.id.listView);
        General.collapse(relativeLayout);
        GetData();
        mArticleAdapter = new ArticleHorizontalAdapter(articleModelList, this);
        mCommentAdapter = new CommentAdapter(commentModelList, this);
        articleList.setAdapter(mArticleAdapter);
      //  for (int i = 0; i < adapter.getGroupCount(); i++)

    //    commentList.setAdapter(mCommentAdapter);

        articleList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContentTopPage.this, ContentTopPage.class);
                startActivity(intent);
            }
        });
        expandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (size > 0){
                    if (count < adapter.getGroupCount()) count++;
                General.setListViewHeight(commentList, count);
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(View.FOCUS_DOWN);
                    }
                });
            }
            }
        });
     //   General.setListViewHeightBasedOnChildren(commentList);
       //  mExpandLayout.move(100);
       // mExpandLayout.setExpanded(false);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.addItemDecoration(new DividerItemDecoration(this));
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    public void Delete(CommentTableModel model)
    {
        Query applesQuery = mComments_Database.child(model.getCommentID());
        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                }
                DatabaseReference temp = Constants.Article_Database.child(articleModel.getKey());
                int comment_count ;
                comment_count = (articleModel.getCommentCount() > 0) ? articleModel.getCommentCount() : 0;
                comment_count--;
                articleModel.setCommentCount(comment_count);
                temp.setValue(articleModel.getHashFromModel());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("tag", "onCancelled", databaseError.toException());
            }
        });
    }
    private void GetData()
    {
        articleModelList.add(new ArticleModel("conents", "image", "2016-09-14", "1", "articleID"));
        articleModelList.add(new ArticleModel("conents", "image", "2016-09-14", "2", "articleID1"));
        articleModelList.add(new ArticleModel("conents", "image", "2016-09-14", "3", "articleID"));
        articleModelList.add(new ArticleModel("conents", "image", "2016-09-14", "4", "articleID1"));
        articleModelList.add(new ArticleModel("conents", "image", "2016-09-14", "5", "articleID1"));
        articleModelList.add(new ArticleModel("conents", "image", "2016-09-14", "6", "articleID1"));
        articleModelList.add(new ArticleModel("conents", "image", "2016-09-14", "7", "articleID"));

        commentList.setGroupIndicator(null);
        Query query = mComments_Database.orderByChild("articleID").equalTo(articleID).limitToLast(100);
        showProgressDialog();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                CommentTableModel model;
                commentTableModelList = new ArrayList<>();
                size = 0;

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    size++;
                    model = dataSnapshot1.getValue(CommentTableModel.class);
                    Log.d("like", "like " + model.getLikes());
                    commentTableModelList.add(model);
                }
                CommentTableModel model1, model2, temp;
                if (size > 0) {
                    for (int i = 0; i < size; i++)
                    {
                        for (int j = 0; j < size; j++)
                        {
                            model1 = commentTableModelList.get(i); model2 = commentTableModelList.get(j);
                            if (model1.getTimestamp() != null && model2.getTimestamp() != null) {
                                if (model1.getTimestamp().compareTo(model2.getTimestamp()) > 0) {
                                    commentTableModelList.set(i, model2);
                                    commentTableModelList.set(j, model1);
                                }
                            }
                        }
                    }
              //      Log.d("userID", General.UID);
                    adapter = new ExpandableCommentAdapter(commentTableModelList, ContentTopPage.this);
                    commentList.setAdapter(adapter);
                    for (int i = 0; i < adapter.getGroupCount(); i++)
                        commentList.expandGroup(i);
                    if (count > size) count = size;
                    General.setListViewHeight(commentList, count);
//                    scrollView.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            scrollView.fullScroll(View.FOCUS_DOWN);
//                        }
//                    });
                }
                hideProgressDialog();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setFlag(boolean value, CommentTableModel model)
    {
        DatabaseReference temp = mComments_Database.child(model.getCommentID()).child("flaglist").push();
        temp.setPriority(ServerValue.TIMESTAMP);
        if (value) {
            temp.child("username").setValue(General.screenName);
            temp.child("userID").setValue(General.UID);
          //  mComments_Database.child(model.getCommentID()).child("likes").setValue(model.getLikes() + 1);
        }
        else
        {
            Query applesQuery = mComments_Database.child(model.getCommentID()).child("flaglist").
                    orderByChild("userID").equalTo(General.UID);
            applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                        appleSnapshot.getRef().removeValue();
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("tag", "onCancelled", databaseError.toException());
                }
            });
        }
    }

    public void setLike(boolean value, CommentTableModel model)
    {
        DatabaseReference temp = mComments_Database.child(model.getCommentID()).child("likelist").push();
        temp.setPriority(ServerValue.TIMESTAMP);
        if (value) {
            temp.child("username").setValue(General.screenName);
            temp.child("userID").setValue(General.UID);
            temp.child("deviceToken").setValue(General.deviceToken);
            mComments_Database.child(model.getCommentID()).child("likes").setValue(model.getLikes() + 1);
        }
        else
        {
            Query applesQuery = mComments_Database.child(model.getCommentID()).child("likelist").
                    orderByChild("userID").equalTo(General.UID);
            mComments_Database.child(model.getCommentID()).child("likes").setValue(model.getLikes() - 1);
            applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                        appleSnapshot.getRef().removeValue();
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("tag", "onCancelled", databaseError.toException());
                }
            });
        }
    }
    public void Reply(String replyText, CommentTableModel model)
    {
        if (General.screenName.equals(""))
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("OneBaby");
            builder.setMessage("No screen name! Please go to Edit Account and input the screen name.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(ContentTopPage.this, EditAccount.class);
                    startActivity(intent);

                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create().show();
            return;
        }
        DatabaseReference temp = mComments_Database.child(model.getCommentID()).child("replies").push();
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String reportDate = df.format(date);
        ReplyModel tempModel = new ReplyModel(General.currentUser.getUid(), replyText, General.screenName, reportDate, General.deviceToken);
        // String key = temp.push();
        temp.setPriority(ServerValue.TIMESTAMP);
        notificationHelper.SendPushReply(model);
        temp.setValue(tempModel.getHashReply()).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
            }
        });
     //   Toast.makeText(this, replyText, Toast.LENGTH_LONG).show();
      //  Log.d("model", model.getCommentID());
    };

    public void OnBack(View view)
    {
        finish();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }


    public void PostComment(String replyText)
    {
        if (General.screenName.equals(""))
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("OneBaby");
            builder.setMessage("No screen name! Please go to Edit Account and input the screen name.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(ContentTopPage.this, EditAccount.class);
                    startActivity(intent);

                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create().show();
            return;
        }
        editText_Comment.setText("");
        General.hideSoftKeyboard(this, editText_Comment);
        DatabaseReference temp = mComments_Database.push();
        temp.setPriority(ServerValue.TIMESTAMP);
        String commentID = temp.getKey();
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat dd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String reportDate = df.format(date);
    //    HashMap<String, HashMap<String, String>> replies = new HashMap<>();
     //   replies.put("replies", new HashMap<String, String>());

        showProgressDialog();
        temp.child("timestamp").setValue(dd.format(date));
        temp.child("articleID").setValue(articleID);
        temp.child("commentID").setValue(commentID);
        temp.child("comments").setValue(replyText);
        temp.child("likes").setValue(0);
        temp.child("postdate").setValue(reportDate);
        temp.child("userID").setValue(General.UID);
        temp.child("username").setValue(General.screenName);
        temp.child("deviceToken").setValue(General.deviceToken);
        temp.child("replies").setValue(new HashMap<String, String>()).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                hideProgressDialog();
            }
        });
        temp = Constants.Article_Database.child(articleModel.getKey());
        int comment_count ;
        comment_count = (articleModel.getCommentCount() > 0) ? articleModel.getCommentCount() : 0;
        comment_count++;
        articleModel.setCommentCount(comment_count);
        temp.setValue(articleModel.getHashFromModel());
        count++;
        hideProgressDialog();
        //temp.setValue(model);
    }

    boolean flag = false;
    public void OnReadMore(boolean state)
    {

        if (state)
            General.expand(relativeLayout);
        else
            General.collapse(relativeLayout);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
       // mExpandLayout.toggle();
//        relativeLayout.setVisibility(View.VISIBLE);
//        relativeLayout.setAlpha(0.0f);


// Start the animation
//        relativeLayout.animate()
//                .translationY(view.getHeight())
//                .alpha(1.0f)
//        .setDuration(1200)
        ;
//        Animation bottomUp = AnimationUtils.loadAnimation(this,
//                R.anim.bottom_down);

    //    relativeLayout.startAnimation(bottomUp);
    //    relativeLayout.setVisibility(View.VISIBLE);
       // relativeLayout.setVisibility(View.VISIBLE);
    }
}
