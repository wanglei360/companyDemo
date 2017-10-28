package com.demo.newdemo4.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.demo.newdemo4.R;

/**
 * 创建者：wanglei
 * <p>时间：17/10/27  11:23
 * <p>类描述：
 * <p>修改人：
 * <p>修改时间：
 * <p>修改备注：
 */
public class VernierCaliper extends View {

    private Paint mPaint;
    private int stopY;
    private int startY;
    private int viewPart;
    private int startX;
    private int stopX;
    private int windowWidthPart;
    private Paint textPaint;
    private Rect mRectText;
    private Paint arrowPaint;
    private int cm = 200;
    private int newValue = 170;
    private int width;
    private int horizontalStartX;
    private int horizontalStopX;
    private int middleViewHeigh;

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    private boolean gender;
    private Bitmap bitmap;

    public VernierCaliper(Context context) {
        super(context);
        init(context);
    }

    public VernierCaliper(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VernierCaliper(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        //初始化一个画笔
        mPaint = new Paint();
        mPaint.setStrokeWidth(1);//画笔的宽度
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.dashboard_background));
        mPaint.setAntiAlias(true);// 设置画笔的锯齿效果。 true是去除，大家一看效果就明白了

        textPaint = new Paint();
        textPaint.setStrokeWidth(1);//画笔的宽度
        textPaint.setTextSize(sp2px(13));
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(ContextCompat.getColor(getContext(), R.color.dashboard_background));
        mRectText = new Rect();

        arrowPaint = new Paint();
        arrowPaint.setColor(ContextCompat.getColor(getContext(), R.color.dashboard_background));
        arrowPaint.setAntiAlias(true);
        arrowPaint.setStyle(Paint.Style.FILL);//填充
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width = resolveSize(dp2px(200), widthMeasureSpec);
        int heigh = resolveSize(dp2px(200), heightMeasureSpec);
        int windowPart = heigh / 20;
        startY = windowPart * 4;
        stopY = windowPart * 15;

        middleViewHeigh = stopY - startY;
        viewPart = middleViewHeigh / 100;

        windowWidthPart = width / 30;

        startX = width - windowWidthPart * 5;
        stopX = width - windowWidthPart * 4;
        if (gender) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.boy);
        } else {
            bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.girl);
        }
        horizontalStartX = windowWidthPart * 2;
        horizontalStopX = startX - windowWidthPart / 3 * 2;
        peopleWidth = setPeopleWidth(90);
        if (initTermination != null && !isInitText) {
            isInitText = true;
            initTermination.initTerminationListener();
        }
    }

    boolean isInitText;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        scale(canvas);// todo 画刻度
        arrow(canvas, newValue);//todo 画箭头
        people(canvas);
    }

    private void people(Canvas canvas) {
        Matrix matrix = new Matrix();
        float peopleHeight = viewPart * (newValue - 100);
        float bitmapHeight = bitmap.getHeight();
        float bitmapWidth = bitmap.getWidth();

        float scaleWidth = peopleWidth / bitmapWidth;
        float scaleHeight = peopleHeight / bitmapHeight;
        matrix.setScale(scaleWidth, scaleHeight);
        float newBitmapWidth = bitmapWidth * scaleWidth / 2;
        float dy = bootomY - peopleHeight;
//        float dy = startY + middleViewHeigh;
        float px = width / 2 - newBitmapWidth;
        matrix.postTranslate(px, dy);

        canvas.drawBitmap(bitmap, matrix, null);
    }

    private float peopleWidth;

    public float setPeopleWidth(float peopleWidth) {
        int part = (horizontalStopX - horizontalStartX) / 180;
        this.peopleWidth = peopleWidth * part;
        invalidate();
        return this.peopleWidth;
    }

    /**
     * 画箭头
     */
    private void arrow(Canvas canvas, int num) {
        int horizontalSize = horizontalStopX - horizontalStartX;
        int horizontalPart = horizontalSize / windowWidthPart;
        int eee = horizontalPart % 2 != 0 ? horizontalPart : --horizontalPart;
        int part = viewPart * (cm - num);
        for (int x = 0; x < horizontalPart; x++) {
            int newStartX = horizontalStartX + windowWidthPart * x;
            int newStopX = horizontalStartX + windowWidthPart * x + (windowWidthPart + windowWidthPart / 2);
            if (eee - 1 == x) {
                canvas.save();
                Path mPath = new Path();
                mPath.moveTo(newStopX, startY + part);//移动画笔到指定位置
                int arrowHeight = 20;
                mPath.lineTo(newStartX, startY + part - arrowHeight);
                mPath.lineTo(newStartX, startY + part + arrowHeight);
                mPath.close();
                canvas.drawPath(mPath, arrowPaint);
                canvas.restore();

                int textHeight1 = 0;
                String value = String.valueOf(num) + "cm";
                char[] cs = value.toCharArray();
                for (char c : cs) {
                    char[] txt = new char[]{c};
                    textPaint.getTextBounds(txt, 0, 1, mRectText);
                    textHeight1 = mRectText.bottom - mRectText.top;
                }
                int startx = horizontalStartX + windowWidthPart;
                canvas.drawText(value, startx + 50, startY + part - textHeight1, textPaint);
            } else {
                if (x % 2 != 0) {
                    int lineStartY = startY + part;
                    canvas.drawLine(newStartX, lineStartY, newStopX, lineStartY, mPaint);
                }
            }
        }
    }

    /**
     * 画刻度
     */
    private void scale(Canvas canvas) {
        // todo 画刻度线
        for (int x = 0; x < 101; x++) {
            bootomY = startY + (viewPart * x);
            if (x % 10 == 0) {//长线
                int textHeight = 0;
                int textWidth = 0;
                String scaleText = String.valueOf(cm - x) + "cm";
                char[] cs = String.valueOf(scaleText).toCharArray();
                for (char c : cs) {
                    char[] txt = new char[]{c};
                    textPaint.getTextBounds(txt, 0, 1, mRectText);
                    textHeight += mRectText.bottom - mRectText.top;
                    textWidth += mRectText.right - mRectText.left;
                }
                canvas.drawText(scaleText, stopX + textWidth-textWidth/3 , startY + viewPart * x, textPaint);
                canvas.drawLine(startX - windowWidthPart / 3 * 2, bootomY, stopX, bootomY, mPaint);
            } else {//短线
                canvas.drawLine(startX, bootomY, stopX, bootomY, mPaint);
            }
        }
    }

    private int bootomY;

    public int setNewValue(int i) {
        int newValue = cm - i / viewPart;
        this.newValue = newValue > 200 ? 200 : newValue < 100 ? 100 : newValue;
        invalidate();
        return this.newValue;
    }

    public int getStopY() {
        return stopY;
    }

    public int getStartY() {
        return startY;
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                Resources.getSystem().getDisplayMetrics());
    }

    private int sp2px(int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                Resources.getSystem().getDisplayMetrics());
    }

    public void setInitTermination(InitTermination initTermination) {
        this.initTermination = initTermination;
    }

    private InitTermination initTermination;

    public interface InitTermination {
        void initTerminationListener();
    }

}
