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

    private Paint outerCirclePaint;//外层圆的画笔
    private Paint mPaint;
    private Path mPath;

    public DrawSample(Context context) {
        super(context);
        initPaint();
    }

    public DrawSample(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public DrawSample(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
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

    public void 线(Canvas canvas) {

        canvas.drawLine(40, 100, 100, 100, getPaint());
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

    int dusu = 180;

    private void 半圆线(Canvas canvas) {

        Paint mPaint = new Paint();//初始化一个画笔
        mPaint.setStrokeWidth(1);//画笔的宽度
        mPaint.setColor(Color.BLACK);

        Paint mPaint1 = new Paint();//初始化一个画笔
        mPaint1.setStrokeWidth(4);//画笔的宽度
        mPaint1.setColor(Color.BLACK);

        canvas.rotate(90);
        for (int i = 0; i <= dusu; i++) {
            if (i % 10 == 0){
                canvas.drawLine(0, width / 2 - 100, 0, width / 2 - 50, mPaint1);
            }else{
                canvas.drawLine(0, width / 2 - 70, 0, width / 2 - 50, mPaint);
            }
            canvas.rotate(1);//旋转10度
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(width / 2, height / 2);

        半圆线(canvas);
    }


    private void initPaint() {
        mPath = new Path();
        if (outerCirclePaint == null) {
            outerCirclePaint = new Paint();
            outerCirclePaint.setStyle(Paint.Style.STROKE);
            outerCirclePaint.setColor(Color.BLACK);
        }
    }

    int width;
    int height;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }
}