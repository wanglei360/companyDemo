package com.ObjectAnimator.demo;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.widget.ImageView;

/**
  放大，旋转，透明度，移动，四种动画，下面还有Interpolator（插补器）的介绍

 todo 设置动画为加速动画(动画播放中越来越快)
 android:interpolator="@android:anim/accelerate_interpolator"
 animation.setInterpolator(new AccelerateInterpolator());

 todo 设置动画为减速动画(动画播放中越来越慢)
 android:interpolator="@android:anim/decelerate_interpolator"
 animation.setInterpolator(new DecelerateInterpolator());

 todo 设置动画为先加速在减速(开始速度最快 逐渐减慢)
 android:interpolator="@android:anim/accelerate_decelerate_interpolator"
 animation.setInterpolator(new AccelerateDecelerateInterpolator());

 todo 先反向执行一段，然后再加速反向回来（相当于我们弹簧，先反向压缩一小段，然后在加速弹出）
 android:interpolator="@android:anim/anticipate_interpolator"
 animation.setInterpolator(new AnticipateInterpolator());

 todo 同上先反向一段，然后加速反向回来，执行完毕自带回弹效果（更形象的弹簧效果）
 android:interpolator="@android:anim/anticipate_overshoot_interpolator"
 animation.setInterpolator(new AnticipateOvershootInterpolator());

 todo 执行完毕之后会回弹跳跃几段（相当于我们高空掉下一颗皮球，到地面是会跳动几下）
 android:interpolator="@android:anim/bounce_interpolator"
 animation.setInterpolator(new BounceInterpolator());

 todo 循环，动画循环一定次数，值的改变为一正弦函数：Math.sin(2* mCycles* Math.PI* input)
 android:interpolator="@android:anim/cycle_interpolator"
 animation.setInterpolator(new CycleInterpolator(2));

 todo 线性均匀改变
 android:interpolator="@android:anim/linear_interpolator"
 animation.setInterpolator(new LinearInterpolator());

 todo 加速执行，结束之后回弹
 android:interpolator="@android:anim/overshoot_interpolator"
 animation.setInterpolator(new OvershootInterpolator());
 */
public class MainActivity extends Activity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.iv);
    }

    public void startAnim(View view) {
        scaleXAnim();
    }

    /**
     * 沿x轴 放大
     */
    public void scaleXAnim() {
        ObjectAnimator animation = ObjectAnimator.ofFloat(imageView, "scaleX", 1f, 2f, 1f);
        animation.setDuration(2000);
        animation.setInterpolator(new AnticipateInterpolator());
        animation.start();
    }

    /**
     * 沿x轴左 移动 300个像素
     */
    public void translationXAnim() {
        ObjectAnimator animation = ObjectAnimator.ofFloat(imageView, "translationX", 0f, -300f, 0f);
        animation.setDuration(2000);
        animation.start();
    }

    /**
     * 旋转 360度
     */
    public void rotationAnim() {
        ObjectAnimator animation = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 360f, 0f);
        animation.setDuration(2000);
        animation.start();
    }

    /**
     * 渐变，透明度从1变成0然后再变成1
     */
    public void alphaAnim() {
        ObjectAnimator animation = ObjectAnimator.ofFloat(imageView, "alpha", 1f, 0f, 1f);
        animation.setDuration(2000);
        animation.start();
    }

    /**
     * 组合动画
     */
    public void combinedAnim() {
        //沿x轴放大
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(imageView, "scaleX", 1f, 2f, 1f);
        //沿y轴放大
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(imageView, "scaleY", 1f, 2f, 1f);
        //移动
        ObjectAnimator translationXAnimator = ObjectAnimator.ofFloat(imageView, "translationX", 0f, 200f, 0f);
        //透明动画
        ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "alpha", 1f, 0f, 1f);
        AnimatorSet set = new AnimatorSet();
        //同时沿X,Y轴放大，且改变透明度，然后移动
        set.play(scaleXAnimator).with(scaleYAnimator).with(animator).before(translationXAnimator);
        //都设置3s，也可以为每个单独设置
        set.setDuration(3000);
        set.start();
    }

}
