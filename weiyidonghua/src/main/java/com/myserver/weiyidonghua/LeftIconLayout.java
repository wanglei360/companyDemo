package com.myserver.weiyidonghua;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * 创建者：wanglei
 * <p>时间：17/9/13  16:59
 * <p>类描述：
 * <p>修改人：
 * <p>修改时间：
 * <p>修改备注：
 */
public class LeftIconLayout extends LinearLayout {

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            myAnimation();
        }
    };
    private int toYDelta;

    public LeftIconLayout(Context context) {
        super(context);
        init();
    }

    public LeftIconLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LeftIconLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

    }


    private boolean isGoInto;
    private ImageView iv;
    private boolean isTopAnim;

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (!isGoInto) {
            iv = (ImageView) getChildAt(0);

            //布局的高度
            int height = getHeight();
            //里面小图片的高度
            int smsallIVHeight = getHeight() / 2;
            int width = getWidth() / 2;//布局的宽度

            LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) iv.getLayoutParams(); //取控件textView当前的布局参数 linearParams.height = 20;// 控件的高强制设成20
            linearParams.width = width;// 控件的宽强制设成30
            linearParams.height = smsallIVHeight;// 控件的宽强制设成30
            iv.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
            int ivX = width - width / 2;
            iv.setX(ivX);
            int i = height - smsallIVHeight;//可移动的空间
            //分成六分,上下各一份空白,上下各两份移动   height -  i1*5
            int i1 = i / 9;
            iv.setY(height - (height - i1 * 2));

            toYDelta = height - i1 * 4 - smsallIVHeight;

            isTopAnim = true;
            handler.sendEmptyMessage(0);
            new Thread() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            Thread.sleep(2000);
                            isTopAnim = !isTopAnim;
                            handler.sendEmptyMessage(0);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
            isGoInto = true;
        }
    }

    private void myAnimation() {
        TranslateAnimation aa;
        if (isTopAnim)
            aa = new TranslateAnimation(0, 0, 0, toYDelta);
        else
            aa = new TranslateAnimation(0, 0, toYDelta, 0);
        aa.setDuration(2000);
        iv.startAnimation(aa);
    }
}
