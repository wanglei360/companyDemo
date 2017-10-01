package com.myserver.list_view_anim;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.myserver.list_view_anim.adapter.MyAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private List<Integer> list;
    private MyAdapter mMyAnimListAdapter;
    private ListView lv;
    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = (ListView) findViewById(R.id.chainListView);

        lv.setDividerHeight(0);

        setAdapter();
    }

    private void setAdapter() {
        list = new ArrayList<Integer>();
        for (int i = 0; i < 77; i++) {
            list.add(i);
        }
        mMyAnimListAdapter = new MyAdapter(list,lv.getHeight(),lv);
        lv.setAdapter(mMyAnimListAdapter);
    }
}
