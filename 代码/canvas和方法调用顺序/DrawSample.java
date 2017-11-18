package com.demo.newdemo4;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * 创建者：wanglei
 * <p>时间：17/10/26  11:09
 * <p>类描述： todo 几个方法的调用顺序,和画圆,画线的canvas实现 path画图形
 * <p>修改人：
 * <p>修改时间：
 * <p>修改备注：仪表盘/src/main/java/com/demo/newdemo4/DrawSample.java
 */
public class DrawSample extends View {
    private String TAG = "MyLayout";
    private Paint mPaint;
    private int width;
    private int height;

    /**
     * 调用顺序
     * DrawSample()         构造
     * onFinishInflate()    View中所有的子控件均被映射成xml后触发              一次
     * onMeasure()          View放置到父容器时调用 会被调用多次 值不准         四次
     * onSizeChanged()      初始化会被调用一次,控件大小发生改变时调用,相对准确   一次
     * onLayout                                                          两次
     * onWindowFocusChanged                                             一次
     * onDraw()             绘制图形
     */
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

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.d(TAG, "onFinishInflate");
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        Log.d(TAG, "onWindowFocusChanged");
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.d(TAG, "onLayout");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        todo 可以通过以下方式获取控件的宽和高
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        //TODO 或者调整大小
//        setMeasuredDimension(100,100);
        Log.d(TAG, "onMeasure");
    }

    /**
     * 控件大小发生改变时调用。初始化会被调用一次
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;//控件的宽
        height = h;//控件的高
        Log.d(TAG, "onSizeChanged");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(width / 2, height / 2);

//        半圆线(canvas);
//        半圆(canvas);
//        线(canvas);
//        三角形(canvas);
        画Bitmap(canvas);
        Log.d(TAG, "onDraw");
    }

    private void 半圆(Canvas canvas) {
        RectF rect = new RectF(0, 0, 300, 300);
        //由右向上向左画半圆
        canvas.drawArc(rect, //弧线所使用的矩形区域大小
                0,  //开始角度
                -180, //扫过的角度
                true, //是否使用中心
                mPaint);
    }

    private void 线(Canvas canvas) {
        canvas.drawLine(40, 100, 200, 100, mPaint);
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

    private void 三角形(Canvas canvas) {
        Paint mpaint = new Paint();
        mpaint.setColor(ContextCompat.getColor(getContext(), R.color.colorAccent));// 画笔的颜色
        mpaint.setAntiAlias(true);

//        Paint.Style.STROKE    只绘制图形轮廓（描边）
//        Paint.Style.FILL      只绘制图形内容 (填充)
//        Paint.Style.FILL_AND_STROKE 既绘制轮廓也绘制内容
        mpaint.setStyle(Paint.Style.STROKE);//描边

        canvas.save();
        Path mPath = new Path();
        mPath.moveTo(200, 100);//移动画笔到指定位置
        mPath.lineTo(350, 300);
        mPath.lineTo(50, 300);
        mPath.close();
        canvas.drawPath(mPath, mpaint);
        canvas.restore();
    }

    private void 画Bitmap(Canvas canvas) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.boy);
//Matrix的每种操作都有set、pre、post三种操作，set是清空队列再添加，pre是在队列最前面插入，post是在队列最后面插入。
//除了translate，其他三种操作都可以指定中心点。
        Matrix matrix = new Matrix();
//        matrix.setScale(sx, sy);      //缩放,按比例
//        matrix.postTranslate(px, dy); //位移,实际的点
//        matrix.setSkew(0f, skewKy);   //错切
//        matrix.setRotate(66);         //旋转
        canvas.drawBitmap(bitmap, matrix, null);
    }

    private void initPaint() {
        Log.d(TAG, "initPaint");
        mPaint = new Paint();
        mPaint.setStrokeWidth(1);// 画笔的宽度
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorAccent));// 画笔的颜色
        mPaint.setAntiAlias(true);// 设置画笔的锯齿效果。 true是去除，大家一看效果就明白了
    }
}