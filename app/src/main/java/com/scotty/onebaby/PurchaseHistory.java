package com.scotty.onebaby;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.scotty.adapters.PurchaseHistoryAdapter;
import com.scotty.models.PurchaseHistoryModel;

import java.util.ArrayList;
import java.util.List;

public class PurchaseHistory extends AppCompatActivity {

    RecyclerView puchaseView;
    PurchaseHistoryAdapter mAdapter;
    List<PurchaseHistoryModel> purchaseHistoryModelList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_history);
        puchaseView = (RecyclerView) findViewById(R.id.recycle_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        puchaseView.setLayoutManager(mLayoutManager);
        puchaseView.setItemAnimator(new DefaultItemAnimator());

        GetData();

       // puchaseView.add

    }
    public void OnBack(View view)
    {
        finish();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }

    public void GetData()
    {
        purchaseHistoryModelList.add(new PurchaseHistoryModel("image", "contents1", "contents2", "5$", "2016-09-10"));
        purchaseHistoryModelList.add(new PurchaseHistoryModel("image", "contents1", "contents2", "5$", "2016-09-10"));
        purchaseHistoryModelList.add(new PurchaseHistoryModel("image", "contents1", "contents2", "5$", "2016-09-10"));
        purchaseHistoryModelList.add(new PurchaseHistoryModel("image", "contents1", "contents2", "5$", "2016-09-10"));
        purchaseHistoryModelList.add(new PurchaseHistoryModel("image", "contents1", "contents2", "5$", "2016-09-10"));
        purchaseHistoryModelList.add(new PurchaseHistoryModel("image", "contents1", "contents2", "5$", "2016-09-10"));
        mAdapter = new PurchaseHistoryAdapter(this, purchaseHistoryModelList);
        puchaseView.setAdapter(mAdapter);
    }
}
