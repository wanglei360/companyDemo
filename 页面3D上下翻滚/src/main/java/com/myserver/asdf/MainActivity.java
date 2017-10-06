package com.myserver.asdf;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.myserver.asdf.view.DividerGridItemDecoration;
import com.myserver.asdf.view.RvAdapter;

import java.util.ArrayList;

public class MainActivity extends Activity {
    private ArrayList<Integer> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = new ArrayList<>();
        for (int x = 0; x < 100; x++) {
            list.add(x);
        }
        init();
    }

    private void init() {
        View title = findViewById(R.id.title);
        RecyclerView rv = (RecyclerView) findViewById(R.id.recycler_view);
        rv.setLayoutManager(new GridLayoutManager(this, 3));
        rv.setHasFixedSize(true);
        RvAdapter rvAdapter1 = new RvAdapter(list, 0);
        rv.addItemDecoration(new DividerGridItemDecoration(this));
        rv.setAdapter(rvAdapter1);

//        ListView rv = (ListView) findViewById(R.id.recycler_view);
//        rv.setAdapter(new MyAdapter());
    }

    private class MyAdapter extends BaseAdapter {
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
            View inflate = View.inflate(parent.getContext(), R.layout.item_rv, null);
            return inflate;
        }
    }
}
