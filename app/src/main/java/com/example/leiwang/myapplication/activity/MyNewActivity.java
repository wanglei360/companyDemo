package com.example.leiwang.myapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.example.leiwang.myapplication.BaseSpringSystem;
import com.example.leiwang.myapplication.SimpleSpringListener;
import com.example.leiwang.myapplication.Spring;
import com.example.leiwang.myapplication.SpringSystem;
import com.example.leiwang.myapplication.SpringUtil;
import com.facebook.rebound.playground.R;

/**
 * 创建者：leiwang
 * <p>时间：17/9/13 22:44
 * <p>类描述：
 * <p>修改人：
 * <p>修改时间：
 * <p>修改备注：
 */
public class MyNewActivity extends Activity {

    private final BaseSpringSystem mSpringSystem = SpringSystem.create();
    private final ExampleSpringListener mSpringListener = new ExampleSpringListener();
    private FrameLayout mRootView;
    private Spring mScaleSpring;
    private View mImageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        mRootView = (FrameLayout) findViewById(R.id.root_view);
        mImageView = mRootView.findViewById(R.id.image_view);

        // Create the animation spring.
        mScaleSpring = mSpringSystem.createSpring();

        // Add an OnTouchListener to the root view.
        mRootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // When pressed start solving the spring to 1.
                        mScaleSpring.setEndValue(1);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        // When released start solving the spring to 0.
                        mScaleSpring.setEndValue(0);
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Add a listener to the spring when the Activity resumes.
        mScaleSpring.addListener(mSpringListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Remove the listener to the spring when the Activity pauses.
        mScaleSpring.removeListener(mSpringListener);
    }

    private class ExampleSpringListener extends SimpleSpringListener {
        @Override
        public void onSpringUpdate(Spring spring) {
            // On each update of the spring value, we adjust the scale of the image view to match the
            // springs new value. We use the SpringUtil linear interpolation function mapValueFromRangeToRange
            // to translate the spring's 0 to 1 scale to a 100% to 50% scale range and apply that to the View
            // with setScaleX/Y. Note that rendering is an implementation detail of the application and not
            // Rebound itself. If you need Gingerbread compatibility consider using NineOldAndroids to update
            // your view properties in a backwards compatible manner.
            float mappedValue = (float) SpringUtil.mapValueFromRangeToRange(spring.getCurrentValue(), 0, 1, 1, 0.5);
            mImageView.setScaleX(mappedValue);
            mImageView.setScaleY(mappedValue);
        }
    }

}
