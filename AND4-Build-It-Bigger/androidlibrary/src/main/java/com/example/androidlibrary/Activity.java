package com.example.androidlibrary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Activity extends AppCompatActivity {

    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_);

        tv = (TextView) findViewById(R.id.textview);

        Intent intent = getIntent();

        if(intent.hasExtra("joke")){
            String joke = intent.getExtras().getString("joke");
            tv.setText(joke);
        }
    }
}
