package com.myserver.weiyidonghua;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv = (ImageView) findViewById(R.id.iv);

    }

//(int fromXType, float fromXValue, int toXType, float toXValue, int fromYType, float fromYValue, int toYType, float toYValue

    /**
     * 参数fromXType：开始时x轴相对于组件的位置类型。
     * 参数fromXValue：开始时x轴的坐标。根据fromXType代表不同的意义。
     * 参数toXType：结束时x轴相对于组件的位置类型。
     * 参数toXValue：结束时x轴的坐标。根据toXType代表不同的意义。
     * 参数fromYType：开始时y轴相对于组件的位置类型。
     * 参数fromYValue：开始时y轴的坐标。根据fromYType代表不同的意义。
     * 参数toYType：结束时y轴相对于组件的位置类型。
     * 参数toYValue：结束时y轴的坐标。根据toYType代表不同的意义。
     * 构造用途：相比第二的构造，这个构造更加自定义，不在是固定的位置属性
     */
    public void playAnimation() {
//        TranslateAnimation aa = new TranslateAnimation(2, 0, 1, 2, 2, 0, 2, 1);
        TranslateAnimation aa = new TranslateAnimation(0, 300, 0, 300);
        aa.setDuration(2000);
        iv.startAnimation(aa);

//        Animation animation = AnimationUtils.loadAnimation(this, R.anim.text);
//        iv.startAnimation(animation);
    }

    public void but(View view) {
        playAnimation();
    }
}
