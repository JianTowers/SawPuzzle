package com.tours.sawpuzzle.ui.select;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.tours.sawpuzzle.R;
import com.tours.sawpuzzle.databinding.ActivitySelectBinding;
import com.tours.sawpuzzle.ui.main.MainActivity;
import com.tours.sawpuzzle.ui.widget.GridDecoration;
import com.tours.sawpuzzle.utils.PermissionsUtils;

import java.util.ArrayList;
import java.util.List;

public class SelectActivity extends AppCompatActivity {

    private static final String TAG = "ImageActivity";

    private ActivitySelectBinding binding;

    private SelectAdapter selectAdapter;
    private String[] permissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.confirm.setOnClickListener(view -> {
            String item = selectAdapter.getImage();
            if (item == null || TextUtils.isEmpty(item)) {
                Toast.makeText(this, getString(R.string.not_select), Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(SelectActivity.this, MainActivity.class);
                intent.putExtra("select",item);
                startActivity(intent);
                finish();
            }
        });

        init();
        PermissionsUtils.requestRequiredPermissions(this, permissions);
    }

    private void addImage() {
        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, null, null, null);
        List<String> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            //获取图片的路径
            @SuppressLint("Range") byte[] data = cursor.getBlob(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            String location = new String(data, 0, data.length - 1);
            //根据路径获取图片
            Bitmap bm = BitmapFactory.decodeFile(location);
            //获取图片的详细信息
            list.add(location);
            if (list.size() > 50) {
                break;
            }
        }

        binding.recycler.post(() -> {
            selectAdapter.updateData(list);
        });
    }

    private void init() {
        binding.layoutTitle.title.setText(getString(R.string.select_image));
        binding.layoutTitle.ivBack.setOnClickListener(view -> finish());

        selectAdapter = new SelectAdapter();
        binding.recycler.setLayoutManager(new GridLayoutManager(this, 3));
        binding.recycler.setAdapter(selectAdapter);
        binding.recycler.addItemDecoration(new GridDecoration(5));
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionsUtils.handleRequestPermissionsResult(this, requestCode, permissions, (it -> {
            if (!it) {
                Toast.makeText(this, "权限获取失败", Toast.LENGTH_SHORT).show();
            } else {
                new Thread(this::addImage).start();
            }
        }));
    }
}