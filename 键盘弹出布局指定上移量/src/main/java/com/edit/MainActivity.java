package com.edit;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements View.OnLayoutChangeListener {

    private EditText et;
    private LinearLayout et_bottom_layout;
    private RelativeLayout et_root_layout, title_layout;
    private ListView lv;
    private int bigHeight,SmallLVHeight;//ListView的两个高度

    private MyAdapter myAdapter;
    private ArrayList<String> list;
    private RelativeLayout root_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        //判断键盘弹出或者隐藏的监听器,通过键盘弹出与隐藏跟布局是否上移判断
        root_layout.addOnLayoutChangeListener(this);
    }

    private void initView() {
        lv = findViewById(R.id.lv);
        et = findViewById(R.id.et);
        root_layout = findViewById(R.id.root_layout);
        et_root_layout = findViewById(R.id.et_root_layout);
        title_layout = findViewById(R.id.title_layout);
        et_bottom_layout = findViewById(R.id.et_bottom_layout);

        list = new ArrayList<>();
        for (int x = 0; x < 100; x++) {
            list.add(x + "");
        }
        myAdapter = new MyAdapter(list);
        lv.setAdapter(myAdapter);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int windowWidth = dm.widthPixels;
        int windowHeight = dm.heightPixels;
        bigHeight = windowHeight - title_layout.getHeight() - et_root_layout.getHeight();

    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        int height = 200;
        int keyboardMove = 100;
        if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyboardMove)) {//键盘弹起
            SmallLVHeight = SmallLVHeight == 0 ? lv.getHeight() : SmallLVHeight;
            setLayoutLocation(et_root_layout.getY() + height,
                    et_bottom_layout.getY() - height,
                    SmallLVHeight + height,
                    title_layout.getHeight(),
                    myAdapter.getCount() - 1);
        } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyboardMove)) {//键盘隐藏
            setLayoutLocation(et_root_layout.getY() - height,
                    et_bottom_layout.getY() + height,
                    bigHeight,
                    title_layout.getHeight(),
                    myAdapter.getCount() - 1);
        }
    }

    private void setLayoutLocation(float etRootY, float etBottomLayoutY, int paramsHeight, final int titleHeight, final int adapterCount) {
        et_root_layout.setY(etRootY);
        et_bottom_layout.setY(etBottomLayoutY);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, paramsHeight);
        lv.setLayoutParams(layoutParams);
        lv.post(new Runnable() {
            @Override
            public void run() {
                lv.setY(titleHeight);
                if (myAdapter != null && list != null) {
                    if (list.size() > 0) {
                        lv.smoothScrollToPosition(adapterCount);
                    }
                }
            }
        });
    }

}
