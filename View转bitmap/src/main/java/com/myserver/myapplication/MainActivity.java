package com.myserver.myapplication;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout layout;
    private LinearLayout bottom_layout;
    private int width;
    private int height;
    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv = (ImageView) findViewById(R.id.iv);
        layout = (RelativeLayout) findViewById(R.id.my_layout);
        bottom_layout = (LinearLayout) findViewById(R.id.bottom_layout);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Display dd = getWindowManager().getDefaultDisplay();
        width = dd.getWidth();
        height = dd.getHeight();
    }

    public void but(View view) {
        Bitmap bitmap = ShotUtils.shotView(layout, 0 , 300 , width / 2, height / 2);
        bottom_layout.setVisibility(View.GONE);
        iv.setVisibility(View.VISIBLE);
        iv.setImageBitmap(bitmap);
    }
}
