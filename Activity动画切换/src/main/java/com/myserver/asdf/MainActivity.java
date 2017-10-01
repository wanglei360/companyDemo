package com.myserver.asdf;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void but(View view) {
        Intent intent=new Intent(MainActivity.this,myActivity.class);
        startActivity(intent);
        //第一个参数为启动时动画效果，第二个参数为退出时动画效果
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
//        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
    }
}
