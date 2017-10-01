package com.example.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.myapplication.ee.HomeScrollerView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HomeScrollerView sv = (HomeScrollerView) findViewById(R.id.stereoView);
//        View.inflate(this,R.layout.layout_1,null);
//        View.inflate(this,R.layout.layout_2,null);
//        sv.myAddView(View.inflate(this,R.layout.layout_1,null),View.inflate(this,R.layout.layout_2,null));
    }
}
