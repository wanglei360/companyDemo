package com.myserver.spring.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateInterpolator;
import android.widget.AbsListView;
import android.widget.ListView;

import com.nineoldandroids.view.ViewPropertyAnimator;

public class JazzyListView extends ListView {

    private final JazzyHelper mHelper;
    /**
     * 最大滑动距离
     */
    private static final float MAX_DELTAY = 200;
    /**
     * 分离后恢复的动画时长
     */
    private static final long SEPARATE_RECOVER_DURATION = 300;
    /**
     * 摩擦系数
     */
    private static final float FACTOR = 0.25f;
    /**
     * 按下x的缩放比例
     */
    private static final float SCALEX = 0.98f;
    /**
     * 按下y的缩放比例
     */
    private static final float SCALEY = 0.9f;
    /**
     * 展开全部
     */
    private boolean separateAll;

    /**
     * 到达边界时，滑动的起始位置
     */
    private float startY;
    /**
     * 按下时的View
     */
    private View downView;

    private int touchSlop;

    private boolean separate = false;
    private boolean showDownAnim = false;

    /**
     * 原始按下位置(在所有Item中的位置)
     */
    private int originDownPosition;
    /**
     * 按下的位置(在屏幕中的位置)
     */
    private int downPosition;

    /**
     * 上次滑动的位置，用于判断方向
     */
    private float preY;

    private float deltaY;
    private boolean reachTop, reachBottom, move;

    public JazzyListView(Context context) {
        super(context);
        mHelper = init(context, null);
    }

    public JazzyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHelper = init(context, attrs);
    }

    public JazzyListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mHelper = init(context, attrs);
    }

    private JazzyHelper init(Context context, AttributeSet attrs) {
        //不知道怎么让divider和selector和Item一起移动，所以去除，需要自己加分割线
        this.setDivider(null);
        this.setSelector(new BitmapDrawable());
        this.setDividerHeight(10);
        touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        JazzyHelper helper = new JazzyHelper(context, attrs);
        helper.setOnScrollListener(listener);
        super.setOnScrollListener(helper);
        return helper;
    }

    @Override
    public final void setOnScrollListener(OnScrollListener l) {
        mHelper.setOnScrollListener(l);
    }

    //核心代码
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        float currentY = ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float downX = ev.getX();
                float downY = ev.getY();
                //记录按下位置，当isSeparateAll()返回false时，会用到
                originDownPosition = pointToPosition((int) downX, (int) downY);
                downPosition = originDownPosition - getFirstVisiblePosition();
                if (showDownAnim) {
                    performDownAnim(downPosition);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                //记录到达顶部或底部时手指的位置
                if (!separate) {
                    startY = currentY;
                }
                deltaY = currentY - startY;
                //到达顶部
                if (reachTop) {
//                    if (!separateFromTop(currentY)) {
//                        return super.dispatchTouchEvent(ev);
//                    }
                    return !separateFromTop(currentY) && super.dispatchTouchEvent(ev);
                }
//              到达底部
                if (reachBottom) {
//                    if (!separateFromBottom(currentY)) {
//                        return super.dispatchTouchEvent(ev);
//                    }
                    return !separateFromBottom(currentY) && super.dispatchTouchEvent(ev);
                }
                preY = currentY;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                preY = 0;
                recoverDownView();
                if (separate) {
                    separate = false;
                    recoverSeparate();
                    //移动，不响应点击事件
                    if (move) {
                        move = false;
                        return false;
                    }
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean separateFromTop(float currentY) {
        //不能放在外部，否则在顶部滑动没有Fling效果
        if (deltaY > touchSlop) {
            move = true;
        }
        separate = true;
        //超过滑动允许的最大距离，则将起始位置向下移
        if (deltaY > MAX_DELTAY) {
            startY = currentY - MAX_DELTAY;
            //超过最大距离时，出现overScroll效果//有问题
            //return super.dispatchTouchEvent(ev);
        } else if (deltaY < 0) { //为负值时（说明反方向超过了起始位置startY）归0
            deltaY = 0;
            separate = false;
        }

        if (deltaY <= MAX_DELTAY) {//MAX_DELTAY = 200
            for (int index = 0; index < getChildCount(); index++) {
                View child = getChildAt(index);
                int multiple = index;
                if (!separateAll) {
                    if (index > downPosition) {
                        multiple = Math.max(1, downPosition);
                    }
                }
                float distance = multiple * deltaY * FACTOR;
                Log.i("PullSeparateListView", " distance = " + distance);
                child.setTranslationY(distance);
            }
            //向分离方向的反方向滑动，但位置还未复原时
            if (deltaY != 0 && currentY - preY < 0) {
                return true;
            }
            //deltaY=0，说明位置已经复原，然后交给父类处理
        }
        return deltaY != 0;
    }

    private boolean separateFromBottom(float currentY) {
        if (Math.abs(deltaY) > touchSlop) {
            move = true;
        }
        separate = true;
        //超过滑动允许的最大距离，则将起始位置向上移
        if (Math.abs(deltaY) > MAX_DELTAY) {
            startY = currentY + MAX_DELTAY;
            //超过最大距离时，出现overScroll效果
            //return super.dispatchTouchEvent(ev);
        } else if (deltaY > 0) { //为正值时（说明反方向移动超过起始位置startY），归0
            deltaY = 0;
            separate = false;
        }
        if (Math.abs(deltaY) <= MAX_DELTAY) {
            int visibleCount = getChildCount();
            for (int inedex = 0; inedex < visibleCount; inedex++) {
                View child = getChildAt(inedex);
                int multiple = visibleCount - inedex - 1;
                if (!separateAll) {
                    if (inedex < downPosition) {
                        multiple = Math.max(1, visibleCount - downPosition - 1);
                    }
                }
                float distance = multiple * deltaY * FACTOR;
                child.setTranslationY(distance);
            }
            //向分离方向的反方向滑动，但位置还未复原时
            if (deltaY != 0 && currentY - preY > 0) {
                return true;
            }
            //deltaY=0，说明位置已经复原，然后交给父类处理
            if (deltaY == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 恢复
     */
    private void recoverSeparate() {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            ViewPropertyAnimator.animate(child)
                    .translationY(0).setDuration(SEPARATE_RECOVER_DURATION)
                    .setInterpolator(new AccelerateInterpolator());
        }
    }

    /**
     * 按下的动画
     *
     * @param downPosition 在屏幕中的位置
     */
    private void performDownAnim(int downPosition) {
        downView = getChildAt(downPosition);
        if (downView != null) {
            ViewPropertyAnimator.animate(downView)
                    .scaleX(SCALEX).scaleY(SCALEY).setDuration(50)
                    .setInterpolator(new AccelerateInterpolator());
        }
    }

    /**
     * 恢复点击的View
     */
    private void recoverDownView() {
        if (showDownAnim && downView != null) {
            ViewPropertyAnimator.animate(downView)
                    .scaleX(1f).scaleY(1f).setDuration(separate ? SEPARATE_RECOVER_DURATION : 100)
                    .setInterpolator(new AccelerateInterpolator());
        }
    }


    private OnScrollListener listener = new OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            //是否到达顶部
            if (firstVisibleItem == 0) {
                View firstView = getChildAt(firstVisibleItem);
                if (firstView != null && (firstView.getTop() + getPaddingTop()) >= 0) {
                    downPosition = originDownPosition;
                    reachTop = true;
                } else {
                    reachTop = false;
                }
            } else {
                reachTop = false;
            }
            //是否到达底部
            if (firstVisibleItem + visibleItemCount == getCount()) {
                View lastView = getChildAt(visibleItemCount - 1);
                if (lastView != null && (lastView.getBottom() + getPaddingBottom()) <= getHeight() && getCount() > getChildCount()) {
                    downPosition = originDownPosition - firstVisibleItem;
                    reachBottom = true;
                } else {
                    reachBottom = false;
                }
            } else {
                reachBottom = false;
            }
        }
    };
}
