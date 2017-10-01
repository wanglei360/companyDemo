package com.myserver.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * 创建者：wanglei
 * <p>时间：17/10/1  11:26
 * <p>类描述：
 * <p>修改人：
 * <p>修改时间：
 * <p>修改备注：
 */
public class MyImageView extends View {
    public MyImageView(Context context) {
        super(context);
    }

    public MyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (oldBitmap != null)
            myCanvas(canvas);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            invalidate();
        }
    };

    public void er() {
        bb = true;
        skewKy = 0f;
        new Thread() {
            @Override
            public void run() {
                while (bb) {
                    try {
                        skewKy -= 0.01f;
                        Thread.sleep(60);
                        handler.sendEmptyMessage(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    private boolean bb;
    private float skewKy = 0f;
    private Bitmap oldBitmap;
    private int width;
    private int height;
    private int popupViewHeight;
    private int surplusHeight;
    private float afterChangeWidth;

    /**
     * @param bm              bitmap
     * @param width           当前View的宽
     * @param height          当前View的高
     * @param popupViewHeight 当前popupView的高
     * @param surplusHeight   popupView减去ItemView的高
     */
    public void initInfo(Bitmap bm, int width, int height, int popupViewHeight, int surplusHeight) {
        this.oldBitmap = bm;
        this.width = width;
        this.height = height;
        this.popupViewHeight = popupViewHeight;
        this.surplusHeight = surplusHeight;
        afterChangeWidth = width;
    }

    private void myCanvas(Canvas canvas) {

        Matrix matrix = new Matrix();
        matrix.setSkew(0f, skewKy);
        float scaleWidth = (float) width / (float) oldBitmap.getWidth();
        float scaleHeight = (float) height / (float) oldBitmap.getHeight();
        matrix.postScale(scaleWidth, scaleHeight);

        Bitmap matrixBitmap = Bitmap.createBitmap(oldBitmap, 0, 0, oldBitmap.getWidth(), oldBitmap.getHeight(), matrix, true);

        int newWidth = matrixBitmap.getWidth();
        int newHeight = matrixBitmap.getHeight();

        int startY = 0 - (newHeight - height);
        int endY = newHeight - (newHeight - height);

        afterChangeWidth -= 5;
        Rect mSrcRect = new Rect(0, 0, newWidth, newHeight);//代表要绘制的bitmap 区域
//        Rect mDestRect = new Rect(0, startY + (popupViewHeight-surplusHeight), (int) afterChangeWidth, endY + (popupViewHeight-surplusHeight));//代表的是要将bitmap 绘制在屏幕的什么地方
        Rect mDestRect = new Rect(0, startY+(popupViewHeight-height) , (int) afterChangeWidth, endY+(popupViewHeight-height));//代表的是要将bitmap 绘制在屏幕的什么地方

        Log.e("Rect","startY+ = "+(startY + (popupViewHeight-height))+"   endY+ = "+(endY + (popupViewHeight-height))+" startY = "+startY+" endY = "+endY);


        Paint mPaint = new Paint();//创建画笔
        Bitmap bitmap = drawBg4Bitmap(0xff0000ff, matrixBitmap);
        canvas.drawBitmap(bitmap, mSrcRect, mDestRect, mPaint);

        bottomTriangle(canvas, newHeight);
        rightParallelogram(canvas, newHeight);
        bb = height * 1.1 > matrixBitmap.getHeight();
    }

    /**
     * 右边的平行四边形
     */
    private void rightParallelogram(Canvas canvas, int newHeight) {
        Paint mpaint = new Paint();
//        mpaint.setColor(Color.GRAY);
        mpaint.setColor(getResources().getColor(R.color.colorAccent1));
        mpaint.setAntiAlias(true);
        canvas.save();
        mpaint.setStyle(Paint.Style.FILL);//填充
        Path mPath = new Path();
        mPath.moveTo(width, popupViewHeight-height);//移动画笔到指定位置
        mPath.lineTo(width, popupViewHeight);
        mPath.lineTo(afterChangeWidth, popupViewHeight - (newHeight - height));
        mPath.lineTo(afterChangeWidth, popupViewHeight - newHeight);
        mPath.close();
        canvas.drawPath(mPath, mpaint);
        canvas.restore();
    }

    /**
     * 下面的三角形
     */
    private void bottomTriangle(Canvas canvas, int newHeight) {
        Paint mpaint = new Paint();
//        mpaint.setColor(Color.BLUE);
        mpaint.setColor(getResources().getColor(R.color.colorAccent2));
        mpaint.setAntiAlias(true);
        canvas.save();
        mpaint.setStyle(Paint.Style.FILL);//填充
        Path mPath = new Path();

        mPath.moveTo(0, popupViewHeight);//移动画笔到指定位置
        mPath.lineTo(width, popupViewHeight);
        mPath.lineTo(afterChangeWidth, popupViewHeight - (newHeight - height));

        mPath.close();
        canvas.drawPath(mPath, mpaint);
        canvas.restore();
    }

    public Bitmap drawBg4Bitmap(int color, Bitmap orginBitmap) {
        Paint paint = new Paint();
        paint.setColor(color);
        Bitmap bitmap = Bitmap.createBitmap(orginBitmap.getWidth(), orginBitmap.getHeight(), orginBitmap.getConfig());
        Canvas canvas = new Canvas(bitmap);
        canvas.drawRect(0, 0, orginBitmap.getWidth(), orginBitmap.getHeight(), paint);
        canvas.drawBitmap(orginBitmap, 0, 0, paint);
        return bitmap;
    }
}
