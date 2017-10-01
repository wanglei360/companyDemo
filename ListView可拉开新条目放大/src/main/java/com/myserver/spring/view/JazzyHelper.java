/*
 * Copyright (C) 2015 Two Toasters
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.myserver.spring.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AbsListView;

import java.util.HashSet;

public class JazzyHelper implements AbsListView.OnScrollListener {

    public static final int DURATION = 300;
    private boolean mIsScrolling = false;
    private int mFirstVisibleItem = -1;
    private int mLastVisibleItem = -1;
    private int mPreviousFirstVisibleItem = 0;
    private long mPreviousEventTime = 0;
    private double mSpeed = 0;
    private int mMaxVelocity = 0;
    public static final int MAX_VELOCITY_OFF = 0;

    private AbsListView.OnScrollListener mAdditionalOnScrollListener;

    private boolean mOnlyAnimateNewItems;
    private boolean mOnlyAnimateOnFling;
    private boolean mIsFlingEvent;
    private boolean mSimulateGridWithList;
    private final HashSet<Integer> mAlreadyAnimatedItems;

    public JazzyHelper(Context context, AttributeSet attrs, HashSet<Integer> mAlreadyAnimatedItems) {
        this(null, null);
    }

    public JazzyHelper(Context context, AttributeSet attrs) {
        mAlreadyAnimatedItems = new HashSet<>();
        int maxVelocity = 0;
        setMaxAnimationVelocity(maxVelocity);
    }

    public void setOnScrollListener(AbsListView.OnScrollListener l) {
        mAdditionalOnScrollListener = l;
    }

    /**
     * @see AbsListView.OnScrollListener#onScroll
     */
    @Override
    public final void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        onScrolled(view, firstVisibleItem, visibleItemCount, totalItemCount);
        notifyAdditionalOnScrollListener(view, firstVisibleItem, visibleItemCount, totalItemCount);
    }

    public final void onScrolled(ViewGroup viewGroup, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        boolean shouldAnimateItems = (mFirstVisibleItem != -1 && mLastVisibleItem != -1);

        int lastVisibleItem = firstVisibleItem + visibleItemCount - 1;
        if (mIsScrolling && shouldAnimateItems) {
            setVelocity(firstVisibleItem);
            int indexAfterFirst = 0;
            while (firstVisibleItem + indexAfterFirst < mFirstVisibleItem) {
                View item = viewGroup.getChildAt(indexAfterFirst);
                doJazziness(item, firstVisibleItem + indexAfterFirst, -1);
                indexAfterFirst++;
            }

            int indexBeforeLast = 0;
            while (lastVisibleItem - indexBeforeLast > mLastVisibleItem) {
                View item = viewGroup.getChildAt(lastVisibleItem - firstVisibleItem - indexBeforeLast);
                doJazziness(item, lastVisibleItem - indexBeforeLast, 1);
                indexBeforeLast++;
            }
        } else if (!shouldAnimateItems) {
            for (int i = firstVisibleItem; i < visibleItemCount; i++) {
                mAlreadyAnimatedItems.add(i);
            }
        }

        mFirstVisibleItem = firstVisibleItem;
        mLastVisibleItem = lastVisibleItem;
    }

    /**
     * Should be called in onScroll to keep take of current Velocity.
     *
     * @param firstVisibleItem
     *            The index of the first visible item in the ListView.
     */
    private void setVelocity(int firstVisibleItem) {
        if (mMaxVelocity > MAX_VELOCITY_OFF && mPreviousFirstVisibleItem != firstVisibleItem) {
            long currTime = System.currentTimeMillis();
            long timeToScrollOneItem = currTime - mPreviousEventTime;
            if (timeToScrollOneItem < 1) {
                double newSpeed = ((1.0d / timeToScrollOneItem) * 1000);
                // We need to normalize velocity so different size item don't
                // give largely different velocities.
                if (newSpeed < (0.9f * mSpeed)) {
                    mSpeed *= 0.9f;
                } else if (newSpeed > (1.1f * mSpeed)) {
                    mSpeed *= 1.1f;
                } else {
                    mSpeed = newSpeed;
                }
            } else {
                mSpeed = ((1.0d / timeToScrollOneItem) * 1000);
            }

            mPreviousFirstVisibleItem = firstVisibleItem;
            mPreviousEventTime = currTime;
        }
    }


    /**
     * Initializes the item view and triggers the animation.
     *
     * @param item The view to be animated.
     * @param position The index of the view in the list.
     * @param scrollDirection Positive number indicating scrolling down, or negative number indicating scrolling up.
     */
    private void doJazziness(View item, int position, int scrollDirection) {
        if (mIsScrolling) {
            if (mOnlyAnimateNewItems && mAlreadyAnimatedItems.contains(position))
                return;

            if (mOnlyAnimateOnFling && !mIsFlingEvent)
                return;

            if (mMaxVelocity > MAX_VELOCITY_OFF && mMaxVelocity < mSpeed)
                return;

            if (mSimulateGridWithList) {
                ViewGroup itemRow = (ViewGroup) item;
                for (int i = 0; i < itemRow.getChildCount(); i++)
                    doJazzinessImpl(itemRow.getChildAt(i), position, scrollDirection);
            } else {
                doJazzinessImpl(item, position, scrollDirection);
            }

            mAlreadyAnimatedItems.add(position);
        }
    }

    private void doJazzinessImpl(View item, int position, int scrollDirection) {
        ViewPropertyAnimator animator = item.animate().setDuration(DURATION)
                .setInterpolator(new AccelerateDecelerateInterpolator());

        item.setPivotX(item.getWidth() / 2);
        item.setPivotY(item.getHeight() / 2);
        item.setScaleX(0.01f);
        item.setScaleY(0.01f);
        animator.scaleX(1).scaleY(1);
        animator.start();
    }

    /**
     * @see AbsListView.OnScrollListener#onScrollStateChanged
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch(scrollState) {
            case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                mIsScrolling = false;
                mIsFlingEvent = false;
                break;
            case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                mIsFlingEvent = true;
                break;
            case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                mIsScrolling = true;
                mIsFlingEvent = false;
                break;
            default: break;
        }
        notifyAdditionalOnScrollStateChangedListener(view, scrollState);
    }



    public void setMaxAnimationVelocity(int itemsPerSecond) {
        mMaxVelocity = itemsPerSecond;
    }

    /**
     * Notifies the OnScrollListener of an onScroll event, since JazzyListView is the primary listener for onScroll events.
     */
    private void notifyAdditionalOnScrollListener(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (mAdditionalOnScrollListener != null) {
            mAdditionalOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }

    /**
     * Notifies the OnScrollListener of an onScrollStateChanged event, since JazzyListView is the primary listener for onScrollStateChanged events.
     */
    private void notifyAdditionalOnScrollStateChangedListener(AbsListView view, int scrollState) {
        if (mAdditionalOnScrollListener != null) {
            mAdditionalOnScrollListener.onScrollStateChanged(view, scrollState);
        }
    }
}
