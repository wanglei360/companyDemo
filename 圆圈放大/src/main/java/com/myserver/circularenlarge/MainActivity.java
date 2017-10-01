package com.myserver.circularenlarge;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private View v;
    private Button but;
    private int animTime = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        v = findViewById(R.id.v);
        but = (Button) findViewById(R.id.but);
    }

    public void but(View view) {
        but.setVisibility(View.INVISIBLE);
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(v, "scaleX", 1f, 10f);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(v, "scaleY", 1f, 10f);
        AnimatorSet set = new AnimatorSet();
        //同时沿X,Y轴放大
        set.play(animatorX).with(animatorY);
        set.setDuration(animTime);
        set.start();

    }
}
