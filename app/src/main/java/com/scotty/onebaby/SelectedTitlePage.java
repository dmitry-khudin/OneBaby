package com.scotty.onebaby;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.scotty.adapters.ArticlesAdapter;
import com.scotty.models.ArticleModel;
import com.scotty.models.AuthorModel;
import com.scotty.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class SelectedTitlePage extends AppCompatActivity {

    RecyclerView articleView;
    List<ArticleModel> articleModelList = new ArrayList<>();
    ArticlesAdapter mAdapter;
    TextView titleTextView;
    DatabaseReference mDatabase;
    TextView txt_latestTitle, txt_latestDate, txt_latestNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_title_page);
     initView();
    }

    private void initView()
    {
        articleView = (RecyclerView) findViewById(R.id.ArticleViews);
        Bundle bundle = getIntent().getExtras();
        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DB_ARTICLE);
        titleTextView = (TextView) findViewById(R.id.textView9);
        AuthorModel model = (AuthorModel) bundle.get("model");
        Query query = mDatabase.orderByChild("authorID").equalTo(model.getKey());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                articleModelList = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    ArticleModel model = dataSnapshot1.getValue(ArticleModel.class);
                    articleModelList.add(model);
                }
                mAdapter = new ArticlesAdapter(articleModelList, SelectedTitlePage.this);
                articleView.setAdapter(mAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/symbol.ttf");
        titleTextView.setTypeface(tf);
        titleTextView.setText("One Baby");
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        articleView.setLayoutManager(mLayoutManager);
        articleView.setItemAnimator(new DefaultItemAnimator());
     //   mAdapter = new ArticlesAdapter(articleModelList, this);
        articleView.setAdapter(mAdapter);
       // GetData();
    }

    private void GetData()
    {
        articleModelList.add(new ArticleModel("contents", "a", "2016-09-20", "1", "articleID"));
        articleModelList.add(new ArticleModel("contents", "a", "2016-09-20", "2", "articleID1"));
        articleModelList.add(new ArticleModel("contents", "a", "2016-09-20", "3", "articleID1"));
        articleModelList.add(new ArticleModel("contents", "a", "2016-09-20", "4", "articleID"));
        articleModelList.add(new ArticleModel("contents", "a", "2016-09-20", "5", "articleID1"));
        articleModelList.add(new ArticleModel("contents", "a", "2016-09-20", "6", "articleID"));
        articleModelList.add(new ArticleModel("contents", "a", "2016-09-20", "7", "articleID"));
        mAdapter.notifyDataSetChanged();
    }

    public void OnBack(View view)
    {
        finish();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }
}
