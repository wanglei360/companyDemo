package com.demo.newdemo4.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.demo.newdemo4.view.AboutYouView;
import com.demo.newdemo4.R;

public class MainActivity extends Activity implements AboutYouView.PeopleHeightAndWeight {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AboutYouView view = findViewById(R.id.view);
        view.setPeopleHeightAndWeight(this);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        boolean gender = Boolean.valueOf(intent.getStringExtra("gender"));

        view.setName(name);
        view.setGender(gender);

        int peopleHeight = view.getPeopleHeight();
        double peopleWeight = view.getPeopleWeight();

        Toast.makeText(this,"初始身高:"+peopleHeight+"cm  初始体重: "+peopleWeight+"kg",Toast.LENGTH_SHORT).show();
        Log.e("MainActivity","初始身高:"+peopleHeight+"cm  初始体重: "+peopleWeight+"kg");
    }

    @Override
    public void PeopleHeightAndWeightListener(int weight, int height) {
        Toast.makeText(this,"改变后身高:"+height+"cm  改变后体重: "+weight+"kg",Toast.LENGTH_SHORT).show();
        Log.e("MainActivity","改变后身高:"+height+"cm  改变后体重: "+weight+"kg");
    }
}
