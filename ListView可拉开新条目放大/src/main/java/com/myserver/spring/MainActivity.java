package com.myserver.spring;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.myserver.spring.view.JazzyListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private JazzyListView mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ArrayList<String> strings = new ArrayList<>();
        for(int x = 0;x<100;x++)
            strings.add(""+x);

        mList = (JazzyListView) findViewById(android.R.id.list);
        mList.setAdapter(new ArrayAdapter<String>(this,R.layout.item,strings));

    }
}
