package com.myserver.list_view_anim.adapter;

import android.animation.ObjectAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.myserver.list_view_anim.R;

import java.util.HashMap;
import java.util.List;

/**
 * 创建者：wanglei
 * <p>时间：17/9/18  11:34
 * <p>类描述：
 * <p>修改人：
 * <p>修改时间：
 * <p>修改备注：
 */
public class MyAdapter extends BaseAdapter {
    ListView lv;
    private List<Integer> list;
    private HashMap<Integer, View> map;
    private int listViewheight;
    private int height;
    private boolean isBeginAnim;

    public MyAdapter(List<Integer> list, int listViewheight, ListView lv) {
        this.list = list;
        this.lv = lv;
        this.listViewheight = listViewheight;
        map = new HashMap<Integer, View>();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder vh;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
            vh = new ViewHolder();
            vh.text = (TextView) convertView.findViewById(R.id.cell_name_textview);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.text.setText(position + "");

        if (!isBeginAnim)
            startAnim(convertView, position);

        return convertView;
    }

    public class ViewHolder {
        public TextView text;
    }

    private void startAnim(final View convertView, int position) {
        if (height == 0 || listViewheight == 0) {
            convertView.measure(0, 0);
            height = convertView.getMeasuredHeight();
            listViewheight = lv.getHeight();
        }

        if (!map.containsKey(position)) {
            map.put(position, convertView);
        }
        convertView.setVisibility(View.INVISIBLE);

        float v = (float) (((float) listViewheight / (float) height) + 0.99);

        if (position + 1 == (int) v) {
            isBeginAnim = true;
            adapterAnimator1(height, v);
        }
    }

    public void adapterAnimator1(int height, final float total) {
        int position = 0;
        while (map.size() > 0) {
            final View view = map.remove(position);
            if (view != null) {
                final ObjectAnimator translationY = ObjectAnimator.ofFloat(view, "translationY", total * (-height * 2), (position + 1) * 30f, (position + 1) * -30f, (position + 1) * 20f, 0f);
                translationY.setDuration(1500);
                translationY.start();
                view.setVisibility(View.VISIBLE);
                position++;
            }
        }
    }
}
