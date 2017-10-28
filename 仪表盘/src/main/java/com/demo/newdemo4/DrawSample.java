package com.demo.newdemo5;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * 创建者：wanglei
 * <p>时间：17/10/26  11:09
 * <p>类描述：
 * <p>修改人：
 * <p>修改时间：
 * <p>修改备注：
 */
public class DrawSample extends View {

    private Paint mPaint;
    private int width;
    private int height;

    /**
     * 调用顺序
     * DrawSample()         构造
     * onFinishInflate()    View中所有的子控件均被映射成xml后触发
     * onMeasure()          View放置到父容器时调用
     * onSizeChanged()      控件大小发生改变时调用,初始化会被调用一次
     * onDraw()             绘制图形
     */
    public DrawSample(Context context) {super(context);}

    public DrawSample(Context context, AttributeSet attrs) {super(context, attrs);}

    public DrawSample(Context context, AttributeSet attrs, int defStyleAttr) {super(context, attrs, defStyleAttr);}

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        todo 可以通过以下方式获取控件的宽和高
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        //TODO 或者调整大小
//        setMeasuredDimension(100,100);
    }

    /**
     * 控件大小发生改变时调用。初始化会被调用一次
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;//控件的宽
        height = h;//控件的高
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(width / 2, height / 2);

        半圆线(canvas);
    }

    private void 半圆(Canvas canvas) {
        RectF rect = new RectF(0, 0, 100, 100);
        //由右向上向左画半圆
        canvas.drawArc(rect, //弧线所使用的矩形区域大小
                0,  //开始角度
                -180, //扫过的角度
                true, //是否使用中心
                getPaint());
    }

    private void 线(Canvas canvas) {
        canvas.drawLine(40, 100, 100, 100, getPaint());
    }

    private void 半圆线(Canvas canvas) {
        Paint mPaint = new Paint();//初始化一个画笔
        mPaint.setStrokeWidth(1);//画笔的宽度
        mPaint.setColor(Color.BLACK);

        Paint mPaint1 = new Paint();//初始化一个画笔
        mPaint1.setStrokeWidth(4);//画笔的宽度
        mPaint1.setColor(Color.BLACK);
        int dusu = 180;
        canvas.rotate(90);//线从下面旋转90度 到左边平的
        for (int i = 0; i <= dusu; i++) {
            if (i % 10 == 0) {
                canvas.drawLine(0, width / 2 - 100, 0, width / 2 - 50, mPaint1);
            } else {
                canvas.drawLine(0, width / 2 - 70, 0, width / 2 - 50, mPaint);
            }
            canvas.rotate(1);//旋转10度
        }
    }

    private Paint getPaint() {
        if (mPaint == null) {
            mPaint = new Paint();
            mPaint.setStrokeWidth(1);// 画笔的宽度
            mPaint.setColor(Color.RED);// 画笔的颜色
            mPaint.setAntiAlias(true);// 设置画笔的锯齿效果。 true是去除，大家一看效果就明白了
        }
        return mPaint;
    }
}