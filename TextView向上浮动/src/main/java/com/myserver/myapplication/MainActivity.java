package com.myserver.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

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
            return list.size()/2+1;
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
            TextView tv1 = (TextView) view.findViewById(R.id.item_1);
            TextView tv2 = (TextView) view.findViewById(R.id.item_2);
            final MyRelativeLayout layout_1 = (MyRelativeLayout) view.findViewById(R.id.layout_1);
            final MyRelativeLayout layout_2 = (MyRelativeLayout) view.findViewById(R.id.layout_2);

            tv1.setText(list.get(position * 2));
            tv2.setText(list.get(position * 2+1));

            tv1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    layout_1.setTvText("我是");
                }
            });

            tv2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    layout_2.setTvText("我是TV1被点击了我是TV1被点击了");
                }
            });


            return view;
        }
    }
}

