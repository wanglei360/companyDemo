package com.example.asdopiupoiewutopqiwuer.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

/**
 * 创建者：wanglei
 * <p>时间：17/10/6  15:31
 * <p>类描述：
 * <p>修改人：
 * <p>修改时间：
 * <p>修改备注：
 */
public class Photograph {


    private AlertDialog dialog;
    private Uri uri;
    private Activity activity;
    private int width;
    private int height;
    int output = 300;//图片输出,说是440位上线,但实测400也崩溃,但是没有崩溃日志
    public final static int PHOTOGRAPH = 1;//拍照回来
    public final static int SHEAR = 2;//剪切回来
    public final static int PICK = 3;//相册选择回来

    public Photograph(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * @param activity 拍照完需要剪切,所以必须用startActivityForResult,所以必须有Activity
     */
    public void photographOrPick(Activity activity) {
        this.activity = activity;
        uri = Uri.fromFile(storagePath());
        dialog = new AlertDialog.Builder(activity).setItems(
                new String[]{"拍照", "相册"},
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            photograph();
                        } else {
                            pick();
                        }
                    }
                }).create();
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    /**
     * 相册选择
     */
    private void pick() {
        Intent intent = new Intent("android.intent.action.PICK");
        intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
        intent.putExtra("output", uri);
        intent.putExtra("crop", "true");
        intent.putExtra("outputX", output);
        intent.putExtra("outputY", output);
        activity.startActivityForResult(intent, PICK);
    }

    public Bitmap pickBitmap(Intent data) {
        Bitmap bitmap = getBitmap(data.getData().getPath());
        dialogDestroy();
        return bitmap;
    }

    /**
     * 拍照
     */
    private void photograph() {
        Intent cameraintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraintent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        activity.startActivityForResult(cameraintent, PHOTOGRAPH);
    }

    /**
     * 拍照完成进来剪切
     */
    public void shear() {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("output", uri);
        intent.putExtra("aspectX", 1);//裁剪框的比例
        intent.putExtra("aspectY", 1);//裁剪框的比例
        intent.putExtra("outputX", output);
        intent.putExtra("outputY", output);
        intent.putExtra("return-data", true);
        activity.startActivityForResult(intent, SHEAR);
    }

    public Bitmap photographShearBitmap(Intent data) {
        Bitmap bitmap = getBitmap(storagePath().getAbsolutePath());
        dialogDestroy();
        return bitmap;
    }

    private File storagePath() {
        String folder;
        if (Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            folder = Environment.getExternalStorageDirectory()
                    + File.separator;
        } else {
            folder = Environment.getRootDirectory() + File.separator;
        }
        File file = new File(folder);
        if (!file.exists()) {
            file.mkdirs();
        }
        String fileName = folder + "123" + ".jpg";
        return new File(fileName);
    }

    private Bitmap getBitmap(String path) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, opts);

        Bitmap bitmap;
        if (opts.outWidth < width && opts.outHeight < height) {
            bitmap = BitmapFactory.decodeFile(path);
        } else {
            float xScale = (float) opts.outWidth / (float) (width);
            float yScale = (float) opts.outHeight / (float) (height);
            float scale = xScale > yScale ? xScale : yScale;
            opts.inJustDecodeBounds = false;
            opts.inSampleSize = (int) scale;
            bitmap = BitmapFactory.decodeFile(path, opts);
        }
        return bitmap;
    }

    public void dialogDestroy() {
        if (dialog.isShowing())
            dialog.dismiss();
        dialog = null;
        uri = null;
        activity = null;
        File file = storagePath();
        boolean delete = file.delete();
    }
}
