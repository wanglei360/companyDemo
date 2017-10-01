package com.myserver.before_login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends Activity implements com.myserver.before_login.view.SplashView.GoLoginPageButListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        com.myserver.before_login.view.SplashView splashView = (com.myserver.before_login.view.SplashView) findViewById(R.id.splashView);
        splashView.setGoLoginPageButListener(this);
    }

    @Override
    public void cancelButClickListener() {
        Toast.makeText(this, "确定按钮被点击了", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        //第一个参数为启动时动画效果，第二个参数为退出时动画效果
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        finish();
    }

    @Override
    public void registerButClickListener() {
        Toast.makeText(this, "注册按钮被点击了", Toast.LENGTH_SHORT).show();
    }
}
