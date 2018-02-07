package com.example.asdopiupoiewutopqiwuer.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.asdopiupoiewutopqiwuer.R;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.util.List;

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

    /*
    fragment有也有这个方法,不用强制回到Activity中
     */
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE) {
            Log.d("", "");
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                takePhoto();
//            } else {
            // Permission Denied
//                Toast.makeText(DemoActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
//            }
        }


        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE2) {
            Log.d("", "");
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "得到权限", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "没有得到权限", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                    intent.setData(Uri.fromParts("package", getPackageName(), null));
                    startActivity(intent);
                }
            }
//                choosePhoto();
//                Toast.makeText(DemoActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();

//            } else {
            // Permission Denied
//                Toast.makeText(DemoActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
//            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    public static int MY_PERMISSIONS_REQUEST_CALL_PHONE2 = 9;
    public static int MY_PERMISSIONS_REQUEST_CALL_PHONE = 8;


    @Override
    protected void onDestroy() {
        super.onDestroy();
        photograph.dialogDestroy();
    }
}
