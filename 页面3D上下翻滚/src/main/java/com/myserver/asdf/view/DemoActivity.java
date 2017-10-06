package com.myserver.asdf.view;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.myserver.asdf.R;

import java.util.ArrayList;

/**
 * 创建者：wanglei
 * <p>时间：17/10/1  10:51
 * <p>类描述：
 * <p>修改人：
 * <p>修改时间：
 * <p>修改备注：
 */
public class DemoActivity extends Activity implements RvAdapter.OnItemClickListener, PopupWindow.OnDismissListener {

    RecyclerView rv/*,rv1*/;
    private ArrayList<Integer> list;
    private RvAdapter rvAdapter;
    private View title;
    private RolloverView popupView;

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
        title = findViewById(R.id.title);
        rv = (RecyclerView) findViewById(R.id.recycler_view);
        rv.setLayoutManager(new GridLayoutManager(this, 3));
        rv.setHasFixedSize(true);
        RvAdapter rvAdapter1 = new RvAdapter(list,0);
        rv.addItemDecoration(new DividerGridItemDecoration(this));
        rv.setAdapter(rvAdapter1);
        rvAdapter1.setOnItemClickListener(this);


//        rv1 = (RecyclerView) findViewById(R.id.recycler_view1);
//        rv1.setLayoutManager(new GridLayoutManager(this, 3));
//        rv1.setHasFixedSize(true);
//        rv1.addItemDecoration(new DividerGridItemDecoration(this));
//        RvAdapter rvAdapter = new RvAdapter(list,1);
//        rv1.setAdapter(rvAdapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        int titleHeight = title.getHeight();
        showPopupWindow(view,(int) view.getX(), (int) view.getY(), view.getWidth(), view.getHeight(),titleHeight);
    }

    private void showPopupWindow(final View view, final int viewX, final int viewY, final int viewWidth, final int viewHeight, final int titleHeight) {
        final int surplusHeight = viewHeight / 3;
        final int popupViewHeight = viewHeight + surplusHeight;
        popupView = (RolloverView) View.inflate(this, R.layout.rollover_popup_view, null);

        // TODO: 2016/5/17 创建PopupWindow对象，指定宽度和高度
        final PopupWindow window = new PopupWindow(popupView, viewWidth, popupViewHeight);
        window.setOnDismissListener(this);
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
            final int poputWindowY;
            if (viewY + viewHeight < viewHeight + surplusHeight) {//
                poputWindowY = viewY + statusBarHeight1 - surplusHeight;
            } else {
                poputWindowY = viewY + statusBarHeight1 - surplusHeight;
            }
            window.showAsDropDown(popupView, viewX, poputWindowY + titleHeight);
            popupView.post(new Runnable() {
                @Override
                public void run() {
                    view.setDrawingCacheEnabled(true);
                    popupView.initInfo(view.getDrawingCache(), viewWidth, viewHeight, popupViewHeight, surplusHeight, viewY, titleHeight);
                    popupView.startAnim();
                }
            });
        }
    }

    @Override
    public void onDismiss() {
        if (popupView != null)
            popupView.onDestroy();
    }

    public void viewBut(View view) {
        Toast.makeText(this,"我是title中View",Toast.LENGTH_SHORT).show();
    }
}
