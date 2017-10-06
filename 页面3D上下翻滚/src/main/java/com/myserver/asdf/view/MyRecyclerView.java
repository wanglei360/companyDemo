package com.myserver.asdf.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * 创建者：wanglei
 * <p>时间：17/10/3  10:26
 * <p>类描述：
 * <p>修改人：
 * <p>修改时间：
 * <p>修改备注：//        float y1;
 * //        int aa = ((GridLayoutManager)getLayoutManager()).findFirstVisibleItemPosition();
 * //        View childAt = getLayoutManager().getChildAt(aa);
 * //        if(childAt!=null)
 * //            y1 = childAt.getY();
 * //        if(y1 == 0f){
 * //                return false;
 * //            }
 */
public class MyRecyclerView extends RecyclerView {
    public MyRecyclerView(Context context) {
        super(context);
        init();
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
    }

    private boolean bb;
    private int mDownX;
    private int mDownY;
    private int mDownY2;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if(ev!=null)
//            return false;
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                mDownY2 = (int) ev.getY();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                int mDownY1 = (int) ev.getY();
//                int i = mDownY1 - mDownY;
//                if (mDownY1 < mDownY2) {//从下往上滑动
//
//                } else {//从上往下滑动
//                    float y1 = 0;
//                    int aa = ((GridLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
//                    View childAt = getLayoutManager().getChildAt(aa);
//                    if (childAt != null) {
//                        y1 = childAt.getY();
//                        if (y1 == 0f) {
//                            Log.e("GridLayoutManager", "《《《《《《《《《《《《《《《false");
//                            return false;
//                        }
//                    }
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//                break;
//        }
        return super.dispatchTouchEvent(ev);
    }

    int yyy;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        Log.e("GridLayoutManager", "GridLayoutManagerGridLayoutManagerGridLayoutManagerGridLayoutManager");
//        isSliding = false;
//        return true;
        return super.onInterceptTouchEvent(ev);
    }

    boolean isSliding = false;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(ev!=null){
            return false;
        }
        if (isSliding) {
            Log.e("GridLayoutManager", "    isSliding = " + isSliding);
            return false;
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownY2 = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int mDownY1 = (int) ev.getY();
                int i = mDownY1 - mDownY2;
                Log.e("GridLayoutManager", "i = " + i + "   mDownY1 = " + mDownY1 + "   mDownY2 = " + mDownY2 + "     yyy = " + (++yyy));
                if (mDownY1 < mDownY2) {//从下往上滑动
//                    isSliding = false;
//                    return super.onTouchEvent(ev);
                } else {//从上往下滑动
                    float y1 = 0;
                    int aa = ((GridLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
                    View childAt = getLayoutManager().getChildAt(aa);
                    if (childAt != null) {
                        y1 = childAt.getY();
                        if (y1 == 0f) {
                            Log.e("GridLayoutManager", "《《《《《《《《《《《《《《《   false");
                            isSliding = true;
                            return false;
                        }
                    }
                }
        }
        return super.onTouchEvent(ev);
    }
}
