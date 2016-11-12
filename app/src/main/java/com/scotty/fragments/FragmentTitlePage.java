package com.scotty.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.collect.Lists;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.scotty.adapters.Various_Title_Adapter;
import com.scotty.models.AuthorModel;
import com.scotty.models.VariourTitleModel;
import com.scotty.onebaby.R;
import com.scotty.utils.Constants;

import java.util.ArrayList;
import java.util.List;


public class FragmentTitlePage extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    DatabaseReference mDatabase;
    private View mainView;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private GridView gridView;


    private List<AuthorModel> variourTitleModelList = new ArrayList<>();
    private Various_Title_Adapter mAdapter;
    private int weekday;
    public FragmentTitlePage() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weekday = getArguments().getInt("data");
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      //  View view;

        //Toast.makeText(getContext(), "" + weekday, Toast.LENGTH_LONG).show();
        mainView = inflater.inflate(R.layout.fragment_fragment_title_page, container, false);
        initView();
        return mainView;
    }

    private void initView()
    {
//        TextView textView = (TextView)mainView.findViewById(R.id.textView22);
//        textView.setText("" + weekday);
        gridView = (GridView) mainView.findViewById(R.id.gridView4);
        GetData();

    }

    private void GetData()
    {

        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DB_AUTHOR);
        Constants.Author_Database = mDatabase;

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                variourTitleModelList = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    AuthorModel model = dataSnapshot1.getValue(AuthorModel.class);
                    variourTitleModelList.add(model);
                }
                Various_Title_Adapter adapter = new Various_Title_Adapter(variourTitleModelList, mainView.getContext());
                gridView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
