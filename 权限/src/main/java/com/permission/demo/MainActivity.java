package com.permission.demo;

import android.Manifest;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ListView lv;
    private final int REQUEST_CODE_PERMISSION_MULTI = 200;
    private final int REQUEST_CODE_SETTING = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = (ListView) findViewById(R.id.lv_lxr);
    }

    public void but(View view) {
        // 申请权限。
        AndPermission.with(this)
                .requestCode(REQUEST_CODE_PERMISSION_MULTI)
                .permission(Manifest.permission.READ_CONTACTS)
                .callback(permissionListener)
                // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框；
                // 这样避免用户勾选不再提示，导致以后无法申请权限。
                // 你也可以不设置。
                .rationale(new RationaleListener() {
                    @Override
                    public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                        // 这里的对话框可以自定义，只要调用rationale.resume()就可以继续申请。
                        AndPermission.rationaleDialog(MainActivity.this, rationale).show();
                    }
                })
                .start();
    }

    /**
     * 回调监听。
     */
    private PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
            switch (requestCode) {//todo 成功,但是禁止该权限也有时成功,所以后面获取数据还要判断
                case REQUEST_CODE_PERMISSION_MULTI: {
                    Toast.makeText(MainActivity.this, R.string.successfully, Toast.LENGTH_SHORT).show();
                    setListViewInfo();
                    break;
                }
            }
        }

        @Override
        public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
            switch (requestCode) {//TODO 失败
                case REQUEST_CODE_PERMISSION_MULTI: {
                    Toast.makeText(MainActivity.this, R.string.failure, Toast.LENGTH_SHORT).show();
                    break;
                }
            }

            // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
            if (AndPermission.hasAlwaysDeniedPermission(MainActivity.this, deniedPermissions)) {
                // 第一种：用默认的提示语。
                AndPermission.defaultSettingDialog(MainActivity.this, REQUEST_CODE_SETTING).show();

                // 第二种：用自定义的提示语。
//             AndPermission.defaultSettingDialog(this, REQUEST_CODE_SETTING)
//                     .setTitle("权限申请失败")
//                     .setMessage("我们需要的一些权限被您拒绝或者系统发生错误申请失败，请您到设置页面手动授权，否则功能无法正常使用！")
//                     .setPositiveButton("好，去设置")
//                     .show();

//            第三种：自定义dialog样式。
//            SettingService settingService = AndPermission.defineSettingDialog(this, REQUEST_CODE_SETTING);
//            你的dialog点击了确定调用：
//            settingService.execute();
//            你的dialog点击了取消调用：
//            settingService.cancel();
            }
        }
    };



    private void setListViewInfo() {
        //得到访问者
        ContentResolver cr = getContentResolver();
        //定义一个接收联系人姓名和电话号码的集合
        List<Map<String, Object>> datalistView = new ArrayList<>();
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        Cursor cursor = cr.query(uri, null, null, null, null);
        if (cursor == null) {//todo 权限如果是禁止状态,申请的结果也是成功,所以要判断能不能获取到数据,不能就去设置
            AndPermission.defaultSettingDialog(MainActivity.this, REQUEST_CODE_SETTING).show();
            return;
        }
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            Uri uriData = Uri.parse("content://com.android.contacts/raw_contacts/" + id + "/data");
            Cursor contactData = cr.query(uriData, null, null, null, null);
            //用来装姓名
            String aa = "";
            //用来装号码
            String bb = "";
            if (contactData != null) {
                while (contactData.moveToNext()) {
                    String type = contactData.getString(contactData.getColumnIndex("mimetype"));
                    //如果获取的是vnd.android.cursor.item/phone_v2则是号码
                    if (type.equals("vnd.android.cursor.item/phone_v2")) {
                        bb = contactData.getString(contactData.getColumnIndex("data1"));
                        //如果获取的是vnd.android.cursor.item/name则是姓名
                    } else if (type.equals("vnd.android.cursor.item/name")) {
                        aa = contactData.getString(contactData.getColumnIndex("data1"));
                    }
                }
                //将用户名和号码放入Map集合中
                Map<String, Object> map = new HashMap<>();
                map.put("images", aa);
                map.put("titles", bb);
                datalistView.add(map);
            }
        }
        SimpleAdapter adapter = new SimpleAdapter(this, datalistView, R.layout.activity_xs, new String[]{"images", "titles"}, new int[]{R.id.tv_name, R.id.tv_telephone});
        lv.setAdapter(adapter);
    }
}

