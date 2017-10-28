package com.demo.newdemo4.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.demo.newdemo4.R;

public class MainActivity1 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);
    }

    public void but(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("name", "蹦跶的小兔子");
        intent.putExtra("gender", "true");
        startActivity(intent);
    }


}
