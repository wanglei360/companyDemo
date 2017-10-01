package com.myserver.listgo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private ListView lv;
    private ArrayList<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = (ListView) findViewById(R.id.lv);
        list = new ArrayList<>();
        for (int x = 0; x < 100; x++) {
            list.add(x + "");
        }
        lv.setAdapter(new MyAdapter());
    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return list.size()/3+1;
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
            View view = View.inflate(MainActivity.this, R.layout.item, null);

            TextView tv1 = (TextView) view.findViewById(R.id.item_tv1);
            TextView tv2 = (TextView) view.findViewById(R.id.item_tv2);
            TextView tv3 = (TextView) view.findViewById(R.id.item_tv3);

            int size = list.size();
            int position1 = position * 3;
            int position2 = position * 3 + 1;
            int position3 = position * 3 + 2;

            if (position1 < size)
                tv1.setText(list.get(position1));//0
            else
                tv1.setVisibility(View.INVISIBLE);

            if (position2 < size)
                tv2.setText(list.get(position2));//1
            else
                tv2.setVisibility(View.INVISIBLE);

            if (position3 < size)
                tv3.setText(list.get(position3));//2
            else
                tv3.setVisibility(View.INVISIBLE);

            return view;
        }
    }
}
