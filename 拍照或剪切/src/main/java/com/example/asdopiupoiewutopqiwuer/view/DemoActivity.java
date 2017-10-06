package com.example.asdopiupoiewutopqiwuer.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;

import com.example.asdopiupoiewutopqiwuer.R;

/**
 * 创建者：wanglei
 * <p>时间：17/10/6  15:31
 * <p>类描述：
 * <p>修改人：
 * <p>修改时间：
 * <p>修改备注：
 */
public class DemoActivity extends Activity {

    private Photograph photograph;
    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_layout);
        iv = (ImageView) findViewById(R.id.iv);
    }

    public void but(View view) {
        Display dd = getWindowManager().getDefaultDisplay();
        int width = dd.getWidth();
        int height = dd.getHeight();
        if (photograph == null)
            photograph = new Photograph(width, height);
        photograph.photographOrPick(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {
                case Photograph.PHOTOGRAPH:
                    photograph.shear();
                    break;
                case Photograph.SHEAR:
                    if (data != null) {
                        Bitmap bitmap = photograph.photographShearBitmap(data);
                        iv.setImageBitmap(bitmap);
                    }
                    break;
                case Photograph.PICK:
                    if (data != null) {
                        Bitmap bitmap1 = photograph.pickBitmap(data);
                        iv.setImageBitmap(bitmap1);
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        photograph.dialogDestroy();
    }
}
