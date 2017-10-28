package com.demo.newdemo4.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 创建者：wanglei
 * <p>时间：17/10/27  09:23
 * <p>类描述：
 * <p>修改人：
 * <p>修改时间：
 * <p>修改备注：
 */
public class AboutYouView extends RelativeLayout implements VernierCaliper.InitTermination {

    private DashboardView dashboardView;
    private int vernierCaliperStopY;
    private int vernierCaliperStartY;
    private VernierCaliper vernierCaliperView;
    private float centerX;
    private float centerY;
    private Context context;
    private boolean isInit;
    private boolean gender;
    private String name;
    private double peopleWeight = 90;
    private int peopleHeight = 170;

    public AboutYouView(Context context) {
        super(context);
        init(context);
    }

    public AboutYouView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AboutYouView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (!isInit) {
            isInit = true;
            initBottomDashboard();
            initRightVernierCaliper();
        }
        int height = getHeight();
        int width = getWidth();
        centerX = width / 2;
        float myViewTopRegion = height - width / 2;
        centerY = myViewTopRegion + centerX;
    }

    @Override
    public void initTerminationListener() {
        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, vernierCaliperView.getStartY());
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(params);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(Gravity.CENTER);
        addView(linearLayout);

        LinearLayout.LayoutParams textParams =
                new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1.0f);
        TextView titleText = new TextView(context);
        titleText.setLayoutParams(textParams);
        titleText.setTextSize(TypedValue.COMPLEX_UNIT_SP,30);
        titleText.setGravity(Gravity.CENTER);
        titleText.setTextColor(Color.BLACK);
        titleText.setText("关于你");

        TextView nameText = new TextView(context);
        nameText.setLayoutParams(textParams);
        nameText.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
        nameText.setTextColor(Color.BLACK);
        nameText.setGravity(Gravity.CENTER_HORIZONTAL);
        nameText.setText(name);
        linearLayout.addView(titleText);
        linearLayout.addView(nameText);
    }

    /**
     * 初始化右边游标尺
     */
    private void initRightVernierCaliper() {
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        vernierCaliperView = new VernierCaliper(context);
        vernierCaliperView.setLayoutParams(params);
        vernierCaliperView.setGender(gender);
        vernierCaliperView.setInitTermination(this);
        addView(vernierCaliperView);
    }


    /**
     * 初始化下面的仪表盘
     */
    private void initBottomDashboard() {
        int width = getWidth();
        int sonMarginsBottomWidth = getWidth() / 2;

        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(0, 0, 0, -sonMarginsBottomWidth);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        dashboardView = new DashboardView(context);
        dashboardView.setPadding(width / 10, 0, width / 10, 0);
        dashboardView.setLayoutParams(params);
        addView(dashboardView);
    }

    private int getVernierCaliperStopY() {
        if (vernierCaliperStopY == 0)
            vernierCaliperStopY = vernierCaliperView.getStopY();
        if (vernierCaliperStartY == 0) {
            vernierCaliperStartY = vernierCaliperView.getStartY();
        }
        return vernierCaliperStopY;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        getVernierCaliperStopY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                float y = event.getY();
                if (y > dashboardView.getY()) {
                    peopleWeight = getRotationBetweenLines(centerX, centerY, event.getX(), event.getY());
                    dashboardView.setRealTimeValue((int) peopleWeight);
                    vernierCaliperView.setPeopleWidth((float) peopleWeight);
                } else if (y < dashboardView.getY() && y > vernierCaliperStartY) {
                    peopleHeight = vernierCaliperView.setNewValue((int) (y - vernierCaliperStartY));
                }
                break;
            case MotionEvent.ACTION_UP:
                if (peopleHeightAndWeight != null) {
                    peopleHeightAndWeight.PeopleHeightAndWeightListener((int) peopleWeight, peopleHeight);
                }
                break;
        }
        return true;
    }
// todo 已中间View为边界滑动区分,上面那个touch1以下面圆弧包括他的margin尺寸来区分的
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        getVernierCaliperStopY();
//        float y1 = dashboardView.getY();
//        Log.e("MotionEvent","y1 = "+y1);
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//            case MotionEvent.ACTION_MOVE:
//                float y = event.getY();
//                if (y > vernierCaliperStopY) {
//                    double peopleWeight = getRotationBetweenLines(centerX, centerY, event.getX(), event.getY());
//                    dashboardView.setRealTimeValue((int) peopleWeight);
//                } else if (y < vernierCaliperStopY && y > vernierCaliperStartY) {
//                    vernierCaliperView.setNewValue((int) (y - vernierCaliperStartY));
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//                break;
//        }
//        return true;
//    }

    private int getRotationBetweenLines(float centerX, float centerY, float eventX, float eventY) {
        double rotation = 0;

        double k1 = (double) (centerY - centerY) / (centerX * 2 - centerX);
        double k2 = (double) (eventY - centerY) / (eventX - centerX);
        double tmpDegree = Math.atan((Math.abs(k1 - k2)) / (1 + k1 * k2)) / Math.PI * 180;

        if (eventX > centerX && eventY < centerY) {  //第一象限 TODO 右上
            rotation = 90 + 90 - tmpDegree;
//            Log.d("onTouchEvent", "   peopleWeight = " + rotation + "  第一象限  右上");
        } else if (eventX > centerX && eventY > centerY) {//第二象限    TODO 右下
            rotation = 90 + tmpDegree;
//            Log.d("onTouchEvent", "   peopleWeight = " + rotation + "  第二象限  右下");
        } else if (eventX < centerX && eventY > centerY) { //第三象限   TODO 左下
            rotation = 270 - tmpDegree;
//            Log.d("onTouchEvent", "   peopleWeight = " + rotation + "  第三象限  左下");
        } else if (eventX < centerX && eventY < centerY) { //第四象限   TODO 左上
            rotation = tmpDegree;
//            Log.d("onTouchEvent", "   peopleWeight = " + rotation + "  第四象限  左上");
        } else if (eventX == centerX && eventY < centerY) {
            rotation = 0;
        } else if (eventX == centerX && eventY > centerY) {
            rotation = 180;
        }

        return (int) rotation;
    }

    private PeopleHeightAndWeight peopleHeightAndWeight;

    public interface PeopleHeightAndWeight {
        /**
         * @param weight 人的体重
         * @param height 人的身高
         */
        void PeopleHeightAndWeightListener(int weight, int height);
    }

    public void setPeopleHeightAndWeight(PeopleHeightAndWeight peopleHeightAndWeight) {
        this.peopleHeightAndWeight = peopleHeightAndWeight;
    }

    /**
     * @param gender true是男的,否则是女的
     */
    public void setGender(boolean gender) {
        this.gender = gender;
    }

    /**
     * @param name 设置名字
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return 人的胖度, 单位kg
     */
    public double getPeopleWeight() {
        return peopleWeight;
    }

    /**
     * @return 人的身高, 单位cm
     */
    public int getPeopleHeight() {
        return peopleHeight;
    }
}
