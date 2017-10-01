package com.myserver.search;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import static android.animation.Animator.AnimatorListener;

/**
 * 创建者：leiwang
 * <p>时间：17/9/15 21:55
 * <p>类描述：
 * <p>修改人：
 * <p>修改时间：
 * <p>修改备注：
 */
public class MyLayout extends RelativeLayout {


    private Context context;
    private EditText et;
    private ImageView iv;
    private boolean isInitSize;
    private int ivHeight;
    private int width;
    private float i6;//每一次缩小或者放大的值
    private float layoutWidth = 0;
    private boolean isOpenOrClose;//当前是进入打开editext还是关闭的状态，默认假为关闭，需要进入打开
    private boolean isOngoingAnim;//动画是否在进行中，
    private int smallIvSize;//缩小后的ImageView的宽高
    private int bigIvSize;//放大后的ImageView的宽高
    private boolean smallSpringback, bigSpringback;
    private Handler handler;
    private float uu = 0;
    private int backgroundLineheight = 10;
    private int marginLeft = 30;
    private boolean isStartAnim;
    private int UpdateUITime = 30;
    private float v2;


    public MyLayout(Context context) {
        super(context);
        init(context);
    }

    public MyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.search_layout, this, true);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        Toast.makeText(context, "onWindowFocusChanged被调用了", Toast.LENGTH_SHORT).show();
        if (!isInitSize) {

            isInitSize = true;
            width = getWidth();
            int height = getHeight();
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width * 2, height);
            setLayoutParams(lp);
            setX(marginLeft);
            setY(height / 3);
            et = (EditText) findViewById(R.id.view_drawable);
            et.setVisibility(View.INVISIBLE);
            iv = (ImageView) findViewById(R.id.iv11);
            iv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "", Toast.LENGTH_SHORT).show();
                    but();
                    if (!isOpenOrClose) {//打开
                        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    } else {//关闭
                        if (getSearchETText != null)
                            getSearchETText.getSearchETText(et.getText().toString());
                        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);// 控制小键盘的那个类
                        imm.hideSoftInputFromWindow(et.getWindowToken(), 0); // 强制隐藏键盘
                    }
                }
            });

            float i5 = iv.getHeight() / 3;//图片一共要缩小的范围
            ivHeight = iv.getHeight();
            float i = width - marginLeft;
            float i4 = i / UpdateUITime;//一共会移动多少次
            i6 = i5 / i4;//每一次缩小或者放大的值
            layoutWidth = iv.getWidth();
            alphaAnim = false;
        }
    }

    private void but() {
        if (!isOngoingAnim && !isOngoingAnim) {
            isOngoingAnim = true;//准许开启动画
            setX(0);
            if (isStartAnim)
                isStartAnim = false;
            alphaAnim = true;
            openEtAnimHandler();
            openET();
        }
    }

    private void openET() {
        new Thread() {
            @Override
            public void run() {
                while (isOngoingAnim) {
                    try {
                        sleep(UpdateUITime);
                        if (handler != null)
                            handler.sendEmptyMessage(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    boolean alphaAnim;

    private void openEtAnimHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                float startValue;
                float endValue;
                if (!isOpenOrClose) {//当前是进入打开editext还是关闭的状态，默认假为关闭，需要进入打开
                    openEtMethod();
                    startValue = 0f;
                    endValue = 1f;
                } else {
                    closeEtMethod();
                    startValue = 1f;
                    endValue = 0f;
                }
                if (alphaAnim) {
                    et.setText("");
                    alphaAnim = false;
                    int i = width - marginLeft * 2;//动画执行的总距离
                    int i1 = UpdateUITime * 3;//动画执行的每一次的距离
                    int i2 = i / i1 * UpdateUITime;//动画执行的时间
                    ObjectAnimator animator = ObjectAnimator.ofFloat(et, "alpha", startValue, endValue);
                    animator.setDuration(i2);
                    animator.start();
                    et.setVisibility(View.VISIBLE);
                }
            }
        };
    }

    private void closeEtMethod() {
        uu += i6;
        if (bigSpringback || iv.getX() < 0 - ivHeight / 2) {
            bigSpringback = true;

            viewLayoutParamsSet(marginLeft + bigIvSize, bigIvSize);

            if (!isStartAnim) {
                isStartAnim = true;
                backgroundAnim();
            }
            handler.removeCallbacksAndMessages(null);
            handler = null;
            isOngoingAnim = false;//结束动画
            bigSpringback = false;
            isOpenOrClose = false;
            uu = 0;
        } else {
            layoutWidth -= UpdateUITime * 3;
            viewLayoutParamsSet((int) layoutWidth, ivHeight);

            bigIvSize = (int) (smallIvSize + uu);
            if (bigIvSize > ivHeight)
                bigIvSize = ivHeight;
            LayoutParams ivLp = new LayoutParams(bigIvSize, bigIvSize);
            iv.setLayoutParams(ivLp);
            iv.setX(layoutWidth - iv.getWidth());
            iv.setY((ivHeight + backgroundLineheight - bigIvSize) / 2);
        }
    }

    private void openEtMethod() {
        uu += i6;
        if (smallSpringback || layoutWidth > width + marginLeft + smallIvSize) {
            smallSpringback = true;
            if (!isStartAnim) {
                isStartAnim = true;
                backgroundAnim();
            }
            if (layoutWidth < width - marginLeft * 2) {
                handler.removeCallbacksAndMessages(null);
                handler = null;
                isOngoingAnim = false;//结束动画
                smallSpringback = false;
                isOpenOrClose = true;
                uu = 0;

                viewLayoutParamsSet(width - marginLeft * 2, ivHeight);
                return;
            }
//            layoutWidth -= 15;
            layoutWidth -= UpdateUITime;
            viewLayoutParamsSet((int) layoutWidth, ivHeight);
        } else {
            layoutWidth += UpdateUITime * 3;

            viewLayoutParamsSet((int) layoutWidth, ivHeight);

            smallIvSize = (int) (ivHeight - uu);
            LayoutParams ivLp = new LayoutParams(smallIvSize, smallIvSize);
            iv.setLayoutParams(ivLp);
            iv.setX(layoutWidth - iv.getWidth() - 10);
            iv.setY((ivHeight + backgroundLineheight - smallIvSize) / 2);
        }
    }

    private void viewLayoutParamsSet(int width, int height) {
        LayoutParams lp = new LayoutParams(width < 1 ? 1 : width, height + backgroundLineheight);
        lp.setMargins(marginLeft, 0, 0, 0);
        et.setLayoutParams(lp);
    }

    private void backgroundAnim() {
        float v;
        float v1;
        float tension;
        if (!isOpenOrClose) {//进入时,出去右边界往回弹,
            v = width - marginLeft - smallIvSize - backgroundLineheight * 2;//图片回来之后的位置
            v1 = layoutWidth - smallIvSize;//超出边界后的位置!  大的位置
            v2 = Math.abs(v1 - v / 15 * UpdateUITime);//回弹的时间
            tension = 2f;
        } else {
            v1 = 0 - ivHeight / 2;//超出边界后的位置!  大的位置
            v = marginLeft;//图片回来之后的位置
            tension = 4f;
        }
        ObjectAnimator translationY1 = ObjectAnimator.ofFloat(iv, "translationX", v1, v);
        translationY1.setDuration((int) v2);
        translationY1.setInterpolator(new OvershootInterpolator(tension));//从0到最右的动画
        translationY1.start();
        translationY1.addListener(animatorListener);
    }

    AnimatorListener animatorListener = new AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
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
    };

    public int getMarginLeft() {
        return marginLeft;
    }

    public void setMarginLeft(int marginLeft) {
        if (iv == null)
            iv = (ImageView) findViewById(R.id.iv11);
        float i5 = iv.getHeight() / 5;//图片一共要缩小的范围
        float i = width - marginLeft;
        float i4 = i / UpdateUITime;//一共会移动多少次
        i6 = i5 / i4;//每一次缩小或者放大的值
        this.marginLeft = marginLeft;
    }

    public void setGetSearchETText(GetSearchETText getSearchETText) {
        this.getSearchETText = getSearchETText;
    }

    GetSearchETText getSearchETText;

    interface GetSearchETText {
        void getSearchETText(String text);
    }
}
