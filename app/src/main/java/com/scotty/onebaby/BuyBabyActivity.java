package com.scotty.onebaby;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class BuyBabyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_baby);
    }
    public void OnBack(View view)
    {
        finish();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }
}
