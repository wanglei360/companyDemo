package com.myserver.before_login;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 创建者：wanglei
 * <p>时间：17/9/20  13:05
 * <p>类描述：
 * <p>修改人：
 * <p>修改时间：
 * <p>修改备注：
 */
public class SplashView extends RelativeLayout {
    private Context context;
    private RelativeLayout view;
    private int width;
    private int height;
    private float downY;
    private ArrayList<ImageView> listIV;
    private int showPosition;
    private int distance = 150;
    private int animTime = 1000;
    private boolean isShowAnim;
    private ImageView iv;
    private ImageView peopleIV;
    private HashMap<Integer, Float> topMoveStartY, topMoveStartX;
    private HashMap<Integer, Float> topMoveStartRotation;
    private float frame = 5;//白边的宽
    private Button cancelBut;
    private Button registerBut;
    private int cancelButY;
    private int registerButY;
    private HashMap<View, Float> deleteList;

    public SplashView(Context context) {
        super(context);
        init(context);
    }

    public SplashView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SplashView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        view = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.splash_layout, this);
        listIV = new ArrayList<>();
        topMoveStartY = new HashMap<>();
        topMoveStartX = new HashMap<>();
        topMoveStartRotation = new HashMap<>();
        deleteList = new HashMap<>();
    }


    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (view != null)
            view.removeAllViews();
        width = getWidth();
        height = getHeight();

        int butHeight = height / 4 / 4;//高度可以放四个按钮
        int butLayoutStartY = height / 4 * 3;

        LayoutParams layoutParams = new LayoutParams(width / 6 * 5, butHeight);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

        cancelBut = new Button(context);
        cancelButY = butLayoutStartY + butHeight + butHeight / 2;
        cancelBut.setY(butLayoutStartY + butHeight + butHeight / 2);
        cancelBut.setLayoutParams(layoutParams);
        cancelBut.setBackgroundResource(R.drawable.login_but_background);
        cancelBut.setText("确定");
        cancelBut.setTextColor(Color.WHITE);

        registerBut = new Button(context);
        registerButY = butLayoutStartY + butHeight + butHeight / 2 + butHeight + butHeight / 4;
        registerBut.setY(registerButY);
        registerBut.setLayoutParams(layoutParams);
        registerBut.setBackgroundResource(R.drawable.register_but_background);
        registerBut.setText("取消");
        registerBut.setTextColor(Color.BLACK);

        cancelBut.setVisibility(View.INVISIBLE);
        registerBut.setVisibility(View.INVISIBLE);
        view.addView(cancelBut);
        view.addView(registerBut);
        addIvToList();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (showPosition > 7)
            return super.dispatchTouchEvent(ev);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = ev.getY();
                break;
            case MotionEvent.ACTION_UP:
                float newDistance = Math.abs(ev.getY() - downY);
                if (newDistance > distance && !isShowAnim) {
                    isShowAnim = true;
                    if (showPosition < 6) {
                        showAnimSwitch(showPosition);
                    } else if (showPosition == 6) {
                        driftTopAnim();
                    } else if (showPosition == 7) {
                        intermediateMagnificationAnim();
                        return super.dispatchTouchEvent(ev);
                    }
                }
                break;
        }
        return true;
    }

    private void intermediateMagnificationAnim() {
//        for (ImageView iv : listIV) {
//            view.removeView(iv);
//        }
//        listIV.clear();
//        deleteList
        AnimatorSet deleteAnim = new AnimatorSet();
        for (ImageView iv : listIV) {
            Float aFloat = deleteList.get(iv);
            if (aFloat != null && aFloat > 0) {
                ObjectAnimator translationY = ObjectAnimator.ofFloat(iv, "translationY", aFloat, aFloat - 50, height);
                translationY.setDuration(animTime);
                deleteAnim.play(translationY);
            }
        }
        deleteAnim.start();


//        final ImageView image = new ImageView(context);
//        LayoutParams layoutParams = new LayoutParams(width / 3 * 2, width / 3 * 2);
//        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
//        image.setBackgroundResource(R.mipmap.image7);
//        image.setLayoutParams(layoutParams);
//        view.addView(image);
//
//        ObjectAnimator peopleIScaleX = ObjectAnimator.ofFloat(image, "scaleX", 0f, 1f);
//        peopleIScaleX.setDuration(animTime);
//        ObjectAnimator peopleIScaleY = ObjectAnimator.ofFloat(image, "scaleY", 0f, 1f);
//        peopleIScaleY.setDuration(animTime);
//
//        AnimatorSet set = new AnimatorSet();
//        set.play(peopleIScaleX).with(peopleIScaleY);
//        set.start();
//        peopleIScaleY.addListener(animatorListener);
//        cancelBut.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (goLoginPageButListener != null) {
//                    image.setBackgroundResource(R.mipmap.image8);
//                    image.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            goLoginPageButListener.cancelButClickListener();
//                        }
//                    }, 1000);
//                    showPosition = 0;
//                }
//            }
//        });
//        registerBut.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (goLoginPageButListener != null) {
//                    image.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            goLoginPageButListener.registerButClickListener();
//                        }
//                    }, 1000);
//                    showPosition = 0;
//                }
//            }
//        });
    }

    public void setGoLoginPageButListener(GoLoginPageButListener goLoginPageButListener) {
        this.goLoginPageButListener = goLoginPageButListener;
    }

    private GoLoginPageButListener goLoginPageButListener;

    interface GoLoginPageButListener {
        void cancelButClickListener();

        void registerButClickListener();
    }

    private void driftTopAnim() {
        AnimatorSet set = new AnimatorSet();
        ImageView peopleIV = listIV.get(6);

        LayoutParams layoutParams = new LayoutParams(width, width / 5);
        peopleIV.setBackgroundResource(R.mipmap.image9);
        peopleIV.setLayoutParams(layoutParams);
        int peopleIVY = height / 4 - width / 5;
        peopleIV.setY(peopleIVY);
        peopleIV.setX(0);

        ObjectAnimator peopleIScaleX = ObjectAnimator.ofFloat(peopleIV, "scaleX", 1f, .8f);
        peopleIScaleX.setDuration(animTime / 2);
        ObjectAnimator peopleIScaleY = ObjectAnimator.ofFloat(peopleIV, "scaleY", 1f, .8f);
        peopleIScaleY.setDuration(animTime / 2);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(peopleIV, "translationY", height / 4 - width / 5, peopleIVY - ((height / 4 - peopleIVY) / 2));
        translationY.setDuration(animTime / 10);
        AnimatorSet.Builder animBuilder = set.play(peopleIScaleX).with(peopleIScaleY).with(translationY);

        int oneWidth = width / 16;
        float ivLyaoutWidth = oneWidth * 14;
        float newIVWidth;
        for (int x = 0; x < 6; x++) {
            ImageView driftTopIV = listIV.get(x);
            driftTopIV.setPivotY(0);
            driftTopIV.setPivotX(0);
            float ivStartY = topMoveStartY.get(x);
            float ivStartX = topMoveStartX.get(x);
            float ivStartRotation = topMoveStartRotation.get(x);
            switch (x) {
                case 0:
                    newIVWidth = (ivLyaoutWidth / 3) / iv.getWidth();
                    san(animBuilder, driftTopIV, ivStartY, ivStartRotation, ivStartX,
                            height / 4 + (iv.getWidth() * newIVWidth) * 2 + frame * 3, oneWidth - frame, newIVWidth);
                    break;
                case 1:
                    newIVWidth = (ivLyaoutWidth / 3) / iv.getWidth();
                    san(animBuilder, driftTopIV, ivStartY, ivStartRotation, ivStartX,
                            height / 4 + (iv.getWidth() * newIVWidth) * 2 + frame * 3, oneWidth + ivLyaoutWidth / 3, newIVWidth);
                    break;
                case 2:
                    newIVWidth = (ivLyaoutWidth / 3) / iv.getWidth();//不同的0.87
                    san(animBuilder, driftTopIV, ivStartY, ivStartRotation, ivStartX,
                            height / 4 + (iv.getWidth() * newIVWidth) * 2 + frame * 3, oneWidth + (ivLyaoutWidth / 3 * 2) + frame, newIVWidth);
                    break;
                case 3:
                    newIVWidth = (ivLyaoutWidth / 3) / iv.getWidth();//不同的0.87
                    san(animBuilder, driftTopIV, ivStartY, ivStartRotation, ivStartX,
                            height / 4 + iv.getWidth() * newIVWidth + frame + (frame / 2), oneWidth + (ivLyaoutWidth / 3 * 2) + frame, newIVWidth);
                    break;
                case 4:
                    newIVWidth = (ivLyaoutWidth / 3 * 2 + frame * 2) / iv.getWidth();//不同的0.87
                    san(animBuilder, driftTopIV, ivStartY, ivStartRotation, ivStartX,
                            height / 4, oneWidth - frame * 2, newIVWidth);
                    break;
                case 5:
                    newIVWidth = (ivLyaoutWidth / 3) / iv.getWidth();//不同的0.87
                    san(animBuilder, driftTopIV, ivStartY, ivStartRotation, ivStartX,
                            height / 4, oneWidth + (ivLyaoutWidth / 3 * 2) + frame, newIVWidth);
                    break;
            }
        }

        ObjectAnimator cancelButYAnim = ObjectAnimator.ofFloat(cancelBut, "translationY", height, cancelButY);
        cancelButYAnim.setInterpolator(new OvershootInterpolator(5));
        translationY.setDuration(animTime * 2);
        ObjectAnimator determineButYAnim = ObjectAnimator.ofFloat(registerBut, "translationY", height, registerButY);
        determineButYAnim.setInterpolator(new OvershootInterpolator(5));
        translationY.setDuration(animTime * 2);
        animBuilder.with(cancelButYAnim).with(determineButYAnim);
        cancelBut.setVisibility(View.VISIBLE);
        registerBut.setVisibility(View.VISIBLE);
        set.start();
        translationY.addListener(animatorListener);
    }


    /**
     * @param animBuilder     现成的
     * @param iv              获取的
     * @param ivStartY        获取的
     * @param ivStartRotation 获取的
     * @param ivStartX        获取的
     * @param ivEndY          计算的
     * @param ivEndX          计算的
     * @param newIVWidth      计算的
     */
    private void san(AnimatorSet.Builder animBuilder, ImageView iv, float ivStartY, float ivStartRotation, float ivStartX, float ivEndY, float ivEndX, float newIVWidth) {
        deleteList.put(iv, ivEndY);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(iv, "translationY", ivStartY, ivEndY);
        translationY.setInterpolator(new OvershootInterpolator(1));//从0到最右的动画
        translationY.setDuration(animTime);

        ObjectAnimator translationX = ObjectAnimator.ofFloat(iv, "translationX", ivStartX, ivEndX);
        translationX.setInterpolator(new OvershootInterpolator(2));
        translationX.setDuration(animTime);

        ObjectAnimator rotation = ObjectAnimator.ofFloat(iv, "rotation", ivStartRotation, 0f);
        rotation.setDuration(animTime);

        ObjectAnimator scaleX = ObjectAnimator.ofFloat(iv, "scaleX", newIVWidth, newIVWidth);
        scaleX.setDuration(animTime);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(iv, "scaleY", newIVWidth, newIVWidth);
        scaleY.setDuration(animTime);

        animBuilder.with(translationY).with(translationX).with(rotation).with(scaleX).with(scaleY);
    }


    private void showAnimSwitch(int showPosition) {
        iv = listIV.get(showPosition);
        iv.setPivotY(0);
        iv.setPivotX(0);
        int startY = 0 - iv.getHeight();
        iv.setVisibility(View.VISIBLE);

        int enX = (width - iv.getWidth()) / 2;
        int endY = height / 2 - (iv.getHeight() / 2);

        ObjectAnimator translationY1 = ObjectAnimator.ofFloat(iv, "translationY", startY, endY + iv.getHeight() / 5, endY - iv.getHeight() / 8, endY);
        translationY1.setInterpolator(new OvershootInterpolator(1));//从0到最右的动画
        translationY1.setDuration(animTime);

        ObjectAnimator translationX1 = ObjectAnimator.ofFloat(iv, "translationX", 0, enX);
        translationX1.setInterpolator(new OvershootInterpolator(2));//从0到最右的动画
        translationX1.setDuration(animTime / 2);

        ObjectAnimator animatorX = ObjectAnimator.ofFloat(iv, "scaleX", 1f, .65f);
        animatorX.setDuration(animTime / 2);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(iv, "scaleY", 1f, .65f);
        animatorY.setDuration(animTime / 2);

        switch (showPosition) {
            case 0:
                float i = height - height / 2;//屏幕的一半
                v2 = iv.getWidth() * 0.65f / 2;//图一半
                enY1 = i + i / 2 - v2;
                anim2(animatorX, animatorY, enY1, height - height / 5 * 4, 5f, 0 - iv.getWidth() / 10);
                break;
            case 1:
                anim2(animatorX, animatorY, enY1 + v2 / 4, enX, -7f, iv.getWidth() / 3);
                break;
            case 2:
                anim2(animatorX, animatorY, enY1, enX, 9f, width / 2);
                break;
            case 3:
                anim2(animatorX, animatorY, enY1 + v2 / 5, enX, -5f, (float) (width - iv.getWidth() * 0.6 / 5 * 3));
                break;
            case 4:
                anim2(animatorX, animatorY, enY1 - v2 / 3, enX, 6f, width / 5);
                break;
            case 5:
                anim2(animatorX, animatorY, enY1 - v2 / 10, enX, -4f, width / 2);
                break;
        }
        AnimatorSet set = new AnimatorSet();
        set.play(translationY1).with(translationX1);
        set.start();
    }

    float v2;//小图一半宽度
    float enY1;//屏幕一半的一半的小图的Y终点
    List list = new ArrayList<Integer>();

    /**
     * @param animatorX
     * @param animatorY     两个对象,放上面和放这里都行
     * @param endY          往下移动Y轴的终点
     * @param startX        x轴的起点
     * @param rotationAngle 旋转的角度
     * @param endX          x轴的终点
     */
    private void anim2(ObjectAnimator animatorX, ObjectAnimator animatorY, float endY, int startX, float rotationAngle, float endX) {
        int startY = height / 2 - (iv.getHeight() / 2);

        topMoveStartY.put(showPosition, endY);
        topMoveStartX.put(showPosition, endX);
        topMoveStartRotation.put(showPosition, rotationAngle);

        ObjectAnimator translationY = ObjectAnimator.ofFloat(iv, "translationY", startY, endY);
        translationY.setInterpolator(new OvershootInterpolator(1));
        translationY.setDuration(animTime / 2);

        ObjectAnimator rotation = ObjectAnimator.ofFloat(iv, "rotation", 0f, rotationAngle);
        rotation.setDuration(animTime / 2);

        ObjectAnimator translationX2 = ObjectAnimator.ofFloat(iv, "translationX", startX, endX);
        translationX2.setInterpolator(new OvershootInterpolator(2));
        translationX2.setDuration(animTime / 2);

        AnimatorSet set1 = new AnimatorSet();

        if (showPosition < 6) {
            if (showPosition == 5) {
                set1.play(animatorX).with(animatorY).with(translationY).with(rotation).with(translationX2).after(animTime);
                fiveAnima();
            } else
                set1.play(animatorX).with(animatorY).with(translationY).with(rotation).with(translationX2).after(animTime);
        }
        set1.start();

        set1.addListener(animatorListener);
    }

    /**
     * 控制小人从小到大然后张嘴的
     */
    private void fiveAnima() {
        peopleIV = listIV.get(6);
        int topIVWidth = height / 2 < width ? height / 2 : width;
        int ivWidth = topIVWidth / 5 * 3;
        LayoutParams lp = new LayoutParams(ivWidth, ivWidth);
        peopleIV.setX((width - ivWidth) / 2);
        peopleIV.setY((height / 2 - ivWidth) / 2);
        peopleIV.setLayoutParams(lp);

        ObjectAnimator scaleX = ObjectAnimator.ofFloat(peopleIV, "scaleX", 0f, 1f);
        scaleX.setDuration(animTime / 2);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(peopleIV, "scaleY", 0f, 1f);
        scaleY.setDuration(animTime / 2);

        AnimatorSet set1 = new AnimatorSet();
        set1.play(scaleX).with(scaleY).after(animTime + animTime / 2);
        set1.start();
        scaleY.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                peopleIV.setBackgroundResource(R.mipmap.image8);
                peopleIV.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public void addIvToList() {
        listIV.add(getIV(R.mipmap.image1));//0
        listIV.add(getIV(R.mipmap.image2));//1
        listIV.add(getIV(R.mipmap.image3));//2
        listIV.add(getIV(R.mipmap.image4));//3
        listIV.add(getIV(R.mipmap.image5));//4
        listIV.add(getIV(R.mipmap.image6));//5
        listIV.add(getIV(R.mipmap.image7));//6

        for (int x = 0; x < listIV.size(); x++) {
            ImageView imageView = listIV.get(x);
            imageView.setVisibility(View.INVISIBLE);
            view.addView(imageView);
        }
    }

    private ImageView getIV(int imageID) {
        ImageView image = new ImageView(context);
        LayoutParams layoutParams = new LayoutParams(width / 3 * 2, width / 3 * 2);
        image.setBackgroundResource(imageID);
        image.setLayoutParams(layoutParams);
        return image;
    }

    Animator.AnimatorListener animatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            isShowAnim = false;
            showPosition++;
        }

        @Override
        public void onAnimationCancel(Animator animation) {
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
        }
    };
}