package com.edit;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * 创建者：wanglei
 * <p>时间：17/10/31  11:32
 * <p>类描述：
 * <p>修改人：
 * <p>修改时间：
 * <p>修改备注：
 */
public class MyAdapter extends BaseAdapter {
    List<String> list;

    public MyAdapter(List<String> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View inflate = View.inflate(parent.getContext(), R.layout.item, null);
        TextView tv = inflate.findViewById(R.id.item_tv);
        tv.setText(list.get(position));
        return inflate;
    }
}