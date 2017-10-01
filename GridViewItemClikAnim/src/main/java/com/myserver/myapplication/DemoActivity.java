package com.myserver.myapplication;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.PopupWindow;

import java.util.ArrayList;

/**
 * 创建者：wanglei
 * <p>时间：17/10/1  10:51
 * <p>类描述：
 * <p>修改人：
 * <p>修改时间：
 * <p>修改备注：
 */
public class DemoActivity extends Activity implements RvAdapter.OnItemClickListener {

    RecyclerView rv;
    private ArrayList<Integer> list;
    private RvAdapter rvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_layout);

        list = new ArrayList<>();
        for (int x = 0; x < 100; x++) {
            list.add(x);
        }
        init();
    }

    private void init() {
        rv = (RecyclerView) findViewById(R.id.recycler_view);
        rv.setLayoutManager(new GridLayoutManager(this, 3));
        rv.setHasFixedSize(true);
        rvAdapter = new RvAdapter(list);
        rv.addItemDecoration(new DividerGridItemDecoration(this));
        rv.setAdapter(rvAdapter);
        rvAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(View view, int position) {
        showPopupWindow((int) view.getX(), (int) view.getY(), view);
        list.add(101);
        rvAdapter.notifyDataSetChanged();
    }


    private void showPopupWindow(final int viewX, final int viewY, final View view) {
        final int surplusHeight = view.getHeight() / 3;
        final int popupViewHeight = view.getHeight() + surplusHeight;
        final MyImageView popupView = (MyImageView) View.inflate(this, R.layout.popup_view, null);

        // TODO: 2016/5/17 创建PopupWindow对象，指定宽度和高度
        PopupWindow window = new PopupWindow(popupView, view.getWidth(), popupViewHeight);
        // TODO: 2016/5/17 设置背景颜色
        window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        // TODO: 2016/5/17 设置可以获取焦点
        window.setFocusable(true);
        // TODO: 2016/5/17 设置可以触摸弹出框以外的区域
        window.setOutsideTouchable(false);

        int statusBarHeight1;//todo 状态栏的高度
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight1 = getResources().getDimensionPixelSize(resourceId);
            window.showAsDropDown(popupView, viewX, viewY + statusBarHeight1 - surplusHeight);
            popupView.post(new Runnable() {
                @Override
                public void run() {
                    view.setDrawingCacheEnabled(true);
                    Bitmap bm = view.getDrawingCache();

                    popupView.initInfo(bm, view.getWidth(), view.getHeight(), popupViewHeight, surplusHeight);
                    popupView.er();
                }
            });
        }
    }
}
