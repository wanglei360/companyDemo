package com.myserver.asdf.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

/**
 * 创建者：leiwang
 * <p>时间：17/10/2 21:11
 * <p>类描述：
 * <p>修改人：
 * <p>修改时间：
 * <p>修改备注：
 * <p/>
 * <p/>
 * dispatchTouchEvent走完MotionEvent.ACTION_DOWN后,
 * 走onInterceptTouchEvent-->MotionEvent.ACTION_DOWN,返回false,
 * dispatchTouchEvent走MotionEvent.ACTION_MOVE
 * <p/>
 * 走onInterceptTouchEvent如果返回true,onTouchEvent也要返回true,否则后续的MOVE和up不走了就
 */
public class NewHomeScrollerView extends RelativeLayout {

    private ScrollView view2;
    private RecyclerView rv;
    private GridLayoutManager glm;
    private int layoutHeight;
    private final int distance = 100;
    private boolean isRollDoing;
    private boolean isIntercept;
    private boolean isHome;//true就是home页,否则反之
    private int mDownY;
    private View topView;

    public NewHomeScrollerView(Context context) {
        super(context);
    }

    public NewHomeScrollerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NewHomeScrollerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        isHome = true;
        isRollDoing = true;
        layoutHeight = getHeight();
        rv = (RecyclerView) getChildAt(1);
        view2 = (ScrollView) getChildAt(0);
        myGetView(view2);
        glm = (GridLayoutManager) rv.getLayoutManager();
        view2.setRotationX(90);
        view2.setY(-layoutHeight);
//        view2.setPivotY(view2.getHeight());
//        Log.d("changeViewYAndDegrees", "setY = " + (-layoutHeight) + " 角度 = " + 90 + "  pivotY = " + pivotY);

//        view2.setFocusableInTouchMode(false);
//        rv.setOnTouchListener(new OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent ev) {
//                switch (ev.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        Log.d("setOnTouchListener", "DOWN");
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        Log.d("setOnTouchListener", "MOVE");
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        Log.d("setOnTouchListener", "UP");
//                        break;
//                }
//                return false;
//            }
//        });
    }

    private void myGetView(View v) {
        if (v instanceof ViewGroup) {
            myGetView(((ViewGroup) v).getChildAt(((ViewGroup) v).getChildCount() - 1));
            return;
        }
        topView = v;
    }


    /**
     * 这里判断onInterceptTouchEvent是否需要拦截,onTouchEvent判断自己是否处理完了,别再拦截了
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            mDownY = (int) ev.getY();
            isCanDownRoll = rv.getY() == 0;//true就可以向下滑
            isCanTopRoll = view2.getY() == 0;//true就可以向上滑
        }
        if (isHome) {
            View itemView = glm.getChildAt(glm.findFirstVisibleItemPosition());
            if (itemView != null) {
                if (ev.getAction() == MotionEvent.ACTION_MOVE) {
                    int mMoveY = (int) ev.getY();
                    isIntercept = mMoveY - mDownY > 30 && itemView.getY() == 0;
//                    Log.d("setOnTouchListener","isIntercept = "+isIntercept);
                }
            } else {
                isIntercept = false;
            }
        } else {
            if (ev.getAction() == MotionEvent.ACTION_MOVE) {
                int mMoveY = (int) ev.getY();
                if (mMoveY - mDownY > 30) {//从上往下划
                    isIntercept = false;
                } else if (mDownY - mMoveY > 30) {//从下往上划
                    int height = view2.getHeight();
                    int topViewHeight = topView.getHeight();
                    float topViewY = topView.getY();
                    int scrollY = view2.getScrollY();
                    isIntercept = height + scrollY - topViewHeight >= topViewY;
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isIntercept;
    }

    boolean isCanDownRoll;
    boolean isCanTopRoll;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int mMoveY = (int) ev.getY();
                float rollDistance = mMoveY - mDownY;
                float i = (float) layoutHeight / (float) 90;
                boolean isDownRoll = rollDistance > 0;
                if (isRollDoing) {
                    if (isDownRoll && isCanDownRoll) {//向下滑动并且上面View的y起始点<-10
                        changeViewYAndDegrees(view2, -layoutHeight + rollDistance, 90f - (rollDistance / i), view2.getHeight());
                        changeViewYAndDegrees(rv, rollDistance, 360f - (rollDistance / i), 0);
                    } else if (!isDownRoll && isCanTopRoll) {//向上滑动
                        float absRollDistance = Math.abs(rollDistance);
                        changeViewYAndDegrees(view2, 0 - absRollDistance, absRollDistance / i, view2.getHeight());
                        changeViewYAndDegrees(rv, layoutHeight - absRollDistance, 270f + absRollDistance / i, 0);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (rv.getY() > layoutHeight / 2)//往下滚
                    upDownRoll();
                else//否则往上滚
                    upTopRoll();
                break;
        }
        return true;
    }

    private void changeViewYAndDegrees(View view, float y, float rotationX, float pivotY) {
        view.setY(y);
        view.setRotationX(rotationX);
        view.setPivotY(pivotY);
        if (view instanceof ScrollView)
            Log.d("changeViewYAndDegrees", "setY = " + y + " 角度 = " + rotationX + "  pivotY = " + pivotY);
    }

    private void upTopRoll() {
        new Thread() {
            @Override
            public void run() {
                try {
                    sleep(40);
                    Message message = Message.obtain();
                    message.what = 1;
                    handler.sendMessage(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void upDownRoll() {
        new Thread() {
            @Override
            public void run() {
                try {
                    sleep(40);
                    Message message = Message.obtain();
                    message.what = 0;
                    handler.sendMessage(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            float i = (float) layoutHeight / (float) 90;
            isRollDoing = false;
            switch (msg.what) {
                case 0:
                    float view2DownY = view2.getY() + distance > 0 ? 0 : view2.getY() + distance;
                    float v = Math.abs(view2DownY) / i;
                    changeViewYAndDegrees(view2, view2DownY, v, view2.getHeight());

                    float rvDownY = rv.getY() + distance > layoutHeight ? layoutHeight : rv.getY() + distance;
                    float v3 = 360f - (90f - (Math.abs(view2DownY) / i));
                    changeViewYAndDegrees(rv, rvDownY, v3, 0);

                    if (v > 0)
                        upDownRoll();
                    else {
                        isRollDoing = true;
                        isIntercept = false;
                        isHome = false;
                    }
                    break;
                case 1:
                    float view2Y = view2.getY() - distance < -layoutHeight ? -layoutHeight : view2.getY() - distance;
                    float view2TopDegrees = Math.abs(view2Y) / i;
                    changeViewYAndDegrees(view2, view2Y, view2TopDegrees, view2.getHeight());

                    float rvY = rv.getY() - distance < 0 ? 0 : rv.getY() - distance;
                    changeViewYAndDegrees(rv, rvY, 270f + view2TopDegrees, 0);
                    if (view2Y != -layoutHeight)
                        upTopRoll();
                    else {
                        isRollDoing = true;
                        isIntercept = true;
                        isHome = true;
                    }
                    break;
            }
        }
    };
}
