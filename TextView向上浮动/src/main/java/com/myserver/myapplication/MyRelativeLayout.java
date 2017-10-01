package com.myserver.myapplication;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 创建者：wanglei
 * <p>时间：17/9/19  10:54
 * <p>类描述：
 * <p>修改人：
 * <p>修改时间：
 * <p>修改备注：
 */
public class MyRelativeLayout extends RelativeLayout {
    private Context context;
    private TextView tv;
    private LinearLayout anim_view_layout;
    private View view, view1, view2, view3, view4;
    private int viewHeight;
    private String tvText;
    private float viewX;
    private float viewY;
    private int animTiem = 1000;

    public ObjectAnimator myAnim(Object view, String propertyName, long animTiem, float... values) {
        ObjectAnimator textAnimator = ObjectAnimator.ofFloat(view, propertyName, values);
        textAnimator.setDuration(animTiem);
        return textAnimator;
    }

    public void setTvText(String tvText) {
        tv.setText(tvText);
        tv.post(new Runnable() {
            @Override
            public void run() {
                int height = tv.getHeight();
                int width = tv.getWidth();
                LayoutParams lp = new LayoutParams(width, viewHeight);
                anim_view_layout.setLayoutParams(lp);
                anim_view_layout.setVisibility(View.VISIBLE);

                ObjectAnimator textAnimator = myAnim(tv, "translationY", animTiem / 2, viewHeight, viewHeight / 2 - height / 2);

                ObjectAnimator view1Animator = myAnim(view1, "translationY", animTiem / 2, viewHeight + height / 2, viewHeight / 2);

                ObjectAnimator view1CurtailAnimator = myAnim(view1, "scaleY", animTiem, 1f, 0.01f);
                view1.setPivotY(3);

                ObjectAnimator view2Animator = myAnim(view2, "translationY", animTiem / 2, viewHeight + height / 2, viewHeight / 2);

                ObjectAnimator view2CurtailAnimator = myAnim(view2, "scaleY", (long) (animTiem * 0.8), 1f, 0.01f);
                view2.setPivotY(3);

                ObjectAnimator view3Animator = myAnim(view3, "translationY", animTiem / 2, viewHeight + height / 2, viewHeight / 2);

                ObjectAnimator view3CurtailAnimator = myAnim(view3, "scaleY", (long) (animTiem * 0.9), 1f, 0.01f);
                view3.setPivotY(3);

                ObjectAnimator view4Animator = myAnim(view4, "translationY", (long) (animTiem * 0.9), viewHeight + viewHeight + height / 2, viewHeight / 2);

                ObjectAnimator view4CurtailAnimator = myAnim(view4, "scaleY", animTiem, 1f, 0.01f);
                view4.setPivotY(10);


                AnimatorSet set = new AnimatorSet();
                set.play(textAnimator).with(view1Animator).with(view1CurtailAnimator).
                        with(view2Animator).with(view2CurtailAnimator).
                        with(view3Animator).with(view3CurtailAnimator).
                        with(view4Animator).with(view4CurtailAnimator);
                set.start();
            }
        });
    }

    public MyRelativeLayout(Context context) {
        super(context);
        init(context);
    }

    public MyRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        view = LayoutInflater.from(context).inflate(R.layout.my_text_layout, this, true);
        tv = (TextView) view.findViewById(R.id.tv);
        view1 = view.findViewById(R.id.view1);
        view2 = view.findViewById(R.id.view2);
        view3 = view.findViewById(R.id.view3);
        view4 = view.findViewById(R.id.view4);
        anim_view_layout = (LinearLayout) view.findViewById(R.id.anim_view_layout);
        anim_view_layout.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        viewHeight = view.getHeight();
        viewX = view.getX();
        viewY = view.getY();
    }
}
