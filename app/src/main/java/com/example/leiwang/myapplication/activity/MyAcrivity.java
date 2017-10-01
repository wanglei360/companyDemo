package com.example.leiwang.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.leiwang.myapplication.activity.PlaygroundActivity;
import com.facebook.rebound.playground.R;

/**
 * 创建者：leiwang
 * <p>时间：17/9/13 22:41
 * <p>类描述：
 * <p>修改人：
 * <p>修改时间：
 * <p>修改备注：
 */
public class MyAcrivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_activity_layout);
    }

    public void but1(View view) {
        startActivity(new Intent(this, PlaygroundActivity.class));

    }
    public void but2(View view) {
        startActivity(new Intent(this, MyNewActivity.class));
    }
}
