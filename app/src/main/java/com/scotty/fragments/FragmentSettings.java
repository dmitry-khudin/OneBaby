package com.scotty.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.common.net.HostAndPort;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.scotty.adapters.HomeListAdapter;
import com.scotty.models.ArticleModel;
import com.scotty.models.CommentTableModel;
import com.scotty.models.ReplyModel;
import com.scotty.models.User;
import com.scotty.onebaby.EditAccount;
import com.scotty.onebaby.NotificationActivity;
import com.scotty.onebaby.PurchaseHistory;
import com.scotty.onebaby.R;
import com.scotty.onebaby.VariousTitles_Page;
import com.scotty.utils.Constants;
import com.scotty.utils.General;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class FragmentSettings extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    DatabaseReference mDatabaseRef;
    DatabaseReference mComments_Database;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FirebaseAuth auth;
    HomeListAdapter adapter;
    List<ArticleModel> articleModelList;
    private VariousTitles_Page myActivity;
    private View mainView;
    private RecyclerView recyclerView;
    public FragmentSettings() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentSettings.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentSettings newInstance(String param1, String param2) {
        FragmentSettings fragment = new FragmentSettings();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myActivity = (VariousTitles_Page) getActivity();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("users");
        mComments_Database = FirebaseDatabase.getInstance().getReference("Comments");
        mainView = inflater.inflate(R.layout.fragment_fragment_settings, container, false);
        initView();
        return mainView;
    }

    private void getData()
    {
        articleModelList = new ArrayList<>();
        articleModelList.add(new ArticleModel("contents", "imgURL", "2013-04-02", "1", "articleID"));
        articleModelList.add(new ArticleModel("contents", "imgURL", "2013-04-02", "2", "articleID1"));
        articleModelList.add(new ArticleModel("contents", "imgURL", "2013-04-02", "3", "articleID"));
        articleModelList.add(new ArticleModel("contents", "imgURL", "2013-04-02", "4", "articleID2"));
        articleModelList.add(new ArticleModel("contents", "imgURL", "2013-04-02", "5", "articleID"));

        adapter = new HomeListAdapter(getContext(), articleModelList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
  //      recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

     //   recyclerView.setAdapter(adapter);

    }
    private void initView()
    {
        LinearLayout notificaiton = (LinearLayout)(mainView).findViewById(R.id.ID_Notification);
        LinearLayout editAccount = (LinearLayout)(mainView).findViewById(R.id.ID_EditAccount);
        LinearLayout Logout = (LinearLayout)(mainView).findViewById(R.id.ID_Logout);
        recyclerView = (RecyclerView) mainView.findViewById(R.id.listView2);
        getData();
      //  final TextView textView = (TextView)  mainView.findViewById(R.id.textView4);
     //   CircleImageView imageView = (CircleImageView) mainView.findViewById(R.id.profile_image);
        String imgPath = General.GetStringData(getContext(), Constants.USER_PHOTO);
        if (imgPath.equals("")) imgPath = "a";
      //  Picasso.with(getContext()).load(imgPath).error(R.drawable.icon_profile).into(imageView);
      //  textView.setText(General.currentUser.getDisplayName());
        mDatabaseRef.child(General.currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
               // textView.setText();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
       // Query query = mComments_Database.or();

        /*
        mComments_Database.child("article1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                CommentTableModel model = dataSnapshot.getValue(CommentTableModel.class);
                Log.d("comment_model", model.getComments());
                HashMap<String, HashMap<String, String>> hashMapHashMap = model.getReplies();
                for (String key : hashMapHashMap.keySet())
                {
                    HashMap<String, String> map = hashMapHashMap.get(key);
                    ReplyModel mm = ReplyModel.getHashMap(map);
                    Log.d("hashmap", "key " + key + "userID " + mm.getUserID());
                }
//                Log.d("child", dataSnapshot.get);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
       // textView.setText(General.GetStringData(getContext(), Constants.USER_NAME));
        notificaiton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   myActivity.SendNotification("condition", Constants.COMMENTS);
//                myActivity.SendNotification("topics", Constants.FAVORITE);
                Intent intent = new Intent(getContext(), NotificationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        editAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditAccount.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
             //   myActivity.OnSubScription("news");
            }
        });

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myActivity.SignOut();
            }
        });
    }

}
