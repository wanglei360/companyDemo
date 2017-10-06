package com.myserver.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.myserver.myapplication.R;

/**
 * 创建者：wanglei
 * <p>时间：17/10/1  11:26
 * <p>类描述：
 * <p>修改人：
 * <p>修改时间：
 * <p>修改备注：
 */
public class RolloverView extends View {
    public RolloverView(Context context) {
        super(context);
    }

    public RolloverView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RolloverView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (oldBitmap != null)
            myCanvas(canvas);
    }

    public void startAnim() {
        bb = true;
        skewKy = 0f;
        new Thread() {
            @Override
            public void run() {
                while (bb) {
                    try {
                        if (handler == null)
                            return;
                        else {
                            skewKy -= 0.01f;
                            Thread.sleep(40);
                            handler.sendEmptyMessage(0);
                        }
                    } catch (Exception e) {
                        if (handler == null)
                            Log.e("Exception", "<<<<<<<<<<<<<<<<<<handler == null");
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    private Handler handler;
    private int showViewStartY;
    private boolean isTopItem = false;
    private boolean bb;
    private float skewKy = 0f;
    private Bitmap oldBitmap;
    private int width;
    private int height;
    private int titleHeight;
    private int popupViewHeight;
    private int viewY;
    private int afterChangeWidth;
    private int surplusHeight;
    private double AlteredHeight;

    public void onDestroy() {
        bb = false;
        handler.removeCallbacksAndMessages(null);
        handler = null;
        oldBitmap = null;
    }

    /**
     * @param bm                 bitmap
     * @param width              当前View的宽
     * @param height             当前View的高
     * @param popupViewHeight    当前popupView的高
     * @param surplusHeight      popupWindow减去ItemView的差值
     * @param viewY              按下去时item的Y轴起始点坐标
     * @param bitLyaoutTopHeight 当前RecyclerView上面的高度,不加上状态栏的
     */
    public void initInfo(Bitmap bm, int width, int height, int popupViewHeight, int surplusHeight, int viewY, int bitLyaoutTopHeight) {
        if (handler == null) {
            handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    invalidate();
                }
            };
        }
        this.oldBitmap = bm;
        this.width = width;
        this.height = height;
        this.popupViewHeight = popupViewHeight;
        this.viewY = viewY;
        this.titleHeight = bitLyaoutTopHeight;
        this.surplusHeight = surplusHeight;
        afterChangeWidth = width;
        //if(viewY<差值)就用{viewY}else{正常}
        isTopItem = viewY + bitLyaoutTopHeight < surplusHeight;
        if (isTopItem)
            showViewStartY = viewY + bitLyaoutTopHeight;
        else
            showViewStartY = popupViewHeight - height;
        AlteredHeight = height * 1.1;

    }

    private void myCanvas(Canvas canvas) {

//        BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(R.mipmap.default_icon);
//        oldBitmap = drawable.getBitmap();

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

        afterChangeWidth -= 4;
        Rect mSrcRect = new Rect(0, 0, newWidth, newHeight);//代表要绘制的bitmap 区域
        Rect mDestRect = new Rect(0, startY + showViewStartY, afterChangeWidth, endY + showViewStartY);//代表的是要将bitmap 绘制在屏幕的什么地方
        canvas.drawBitmap(matrixBitmap, mSrcRect, mDestRect, null);

        rightParallelogram(canvas, newHeight, startY);
        bottomTriangle(canvas, newHeight);

        int eraseHeight = 0;
        if (viewY < 0) {
            int i = surplusHeight + Math.abs(viewY);
            if (i < titleHeight)
                eraseHeight = i;
            else {
                eraseHeight = titleHeight;
            }
        } else if (viewY < AlteredHeight) {
            eraseHeight = popupViewHeight - height - viewY;
        }
        eraseTop(canvas, eraseHeight);
        bb = AlteredHeight > matrixBitmap.getHeight();
    }

    private void eraseTop(Canvas canvas, int eraseHeight) {
        Paint mpaint = new Paint();
        mpaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        mpaint.setARGB(0x00, 0xff, 0xff, 0xff);
        canvas.drawRect(0, 0, width, eraseHeight, mpaint);
    }

    /**
     * 右边的平行四边形
     */
    private void rightParallelogram(Canvas canvas, int newHeight, int startY) {
        int bottomRightY;
        int bottomLeftY;
        int topLeftY;
        if (isTopItem) {
            bottomRightY = viewY + height + titleHeight;
            //todo 显示的-差值(newHeight - height)
            bottomLeftY = (viewY + height) - (newHeight - height) + titleHeight;
            topLeftY = startY + showViewStartY;
        } else {
            bottomRightY = popupViewHeight;
            bottomLeftY = popupViewHeight - (newHeight - height);
            topLeftY = popupViewHeight - newHeight;
        }
        Paint mpaint = new Paint();
        mpaint.setColor(getResources().getColor(R.color.colorAccent1));
        mpaint.setAntiAlias(true);
        canvas.save();
        mpaint.setStyle(Paint.Style.FILL);//填充
        Path mPath = new Path();
        mPath.moveTo(width, showViewStartY);//移动画笔到指定位置
        mPath.lineTo(width, bottomRightY);
        mPath.lineTo(afterChangeWidth, bottomLeftY);
        mPath.lineTo(afterChangeWidth, topLeftY);
        mPath.close();
        canvas.drawPath(mPath, mpaint);
        canvas.restore();
    }

    /**
     * 下面的三角形
     */
    private void bottomTriangle(Canvas canvas, int newHeight) {
        int bottomRightY;
        int bottomLeftY;
        int topRightY;
        if (isTopItem) {
            bottomLeftY = viewY + height + titleHeight;
            bottomRightY = viewY + height + titleHeight;
            topRightY = (viewY + height) - (newHeight - height) + titleHeight;
        } else {
            bottomLeftY = popupViewHeight;
            bottomRightY = popupViewHeight;
            topRightY = popupViewHeight - (newHeight - height);
        }

        Paint mpaint = new Paint();
        mpaint.setColor(getResources().getColor(R.color.colorAccent2));
        mpaint.setAntiAlias(true);
        canvas.save();
        mpaint.setStyle(Paint.Style.FILL);//填充
        Path mPath = new Path();

        mPath.moveTo(0, bottomLeftY);//移动画笔到指定位置
        mPath.lineTo(width, bottomRightY);
        mPath.lineTo(afterChangeWidth, topRightY);

        mPath.close();
        canvas.drawPath(mPath, mpaint);
        canvas.restore();
    }

    /**
     * bitmap错切后会有黑边,七个参数的Bitmap.createBitmap会调用四个参数的Bitmap.createBitmap在调用native方法时写死的黑色
     * 去除错切后的黑边的方法
     *
     * @param color       黑边变成什么颜色
     * @param orginBitmap 要去除bitmap
     * @return 返回一个没有黑边的bitmap
     */
    private Bitmap drawBg4Bitmap(int color, Bitmap orginBitmap) {
        Paint paint = new Paint();
        paint.setColor(color);
        Bitmap bitmap = Bitmap.createBitmap(orginBitmap.getWidth(), orginBitmap.getHeight(), orginBitmap.getConfig());
        Canvas canvas = new Canvas(bitmap);
        canvas.drawRect(0, 0, orginBitmap.getWidth(), orginBitmap.getHeight(), paint);
        canvas.drawBitmap(orginBitmap, 0, 0, paint);
        return bitmap;
    }
}
