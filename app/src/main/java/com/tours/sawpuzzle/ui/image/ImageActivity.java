package com.tours.sawpuzzle.ui.image;

import android.Manifest;
import android.annotation.SuppressLint;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.tours.sawpuzzle.databinding.ActivityImageBinding;
import com.tours.sawpuzzle.utils.PermissionsUtils;

import java.util.ArrayList;
import java.util.List;

public class ImageActivity extends AppCompatActivity {

    private static final String TAG = "ImageActivity";

    private ActivityImageBinding binding;

    private String[] permissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        PermissionsUtils.requestRequiredPermissions(this, permissions);
    }

    private void image() {
        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, null, null, null);
        List<Bitmap> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            //获取图片的路径
            @SuppressLint("Range") byte[] data = cursor.getBlob(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            String location = new String(data, 0, data.length - 1);
            //根据路径获取图片
            Bitmap bm = BitmapFactory.decodeFile(location);
            //获取图片的详细信息
            list.add(bm);
        }
        Log.e(TAG, "image: " + list.size());
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionsUtils.handleRequestPermissionsResult(this, requestCode, permissions, (it -> {
            if (!it) {
                Toast.makeText(this, "权限获取失败", Toast.LENGTH_SHORT).show();
            } else {
                new Thread(() -> {
                    image();
                }).start();
            }
        }));
    }
}