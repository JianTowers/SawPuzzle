package com.tours.sawpuzzle.ui.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tours.sawpuzzle.R;
import com.tours.sawpuzzle.data.ImageBlock;
import com.tours.sawpuzzle.databinding.ActivityMainBinding;
import com.tours.sawpuzzle.ui.widget.GridDecoration;
import com.tours.sawpuzzle.utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ImageListener {

    private ActivityMainBinding binding;

    private ImageAdapter imageAdapter;

    private List<ImageBlock> lists = new ArrayList<>();
    private static final int SIDE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = this.getIntent();
        String path = intent.getStringExtra("select");
        initAdapter();
        updateAdapter(path);
    }

    private void initAdapter() {
        imageAdapter = new ImageAdapter(this);
        binding.recycler.setLayoutManager(new GridLayoutManager(this, SIDE));
        binding.recycler.setAdapter(imageAdapter);
        binding.recycler.addItemDecoration(new GridDecoration(2));
    }

    private void updateAdapter(String path) {
        new Thread(() -> {
            Bitmap init = ImageUtils.getBitmap(path);
            Bitmap crop = ImageUtils.getSquare(init);
            lists = ImageUtils.split(crop, SIDE);
            binding.recycler.post(() -> {
                binding.image.setImageBitmap(crop);
                imageAdapter.updateData(lists);
            });
        }).start();
    }

    @Override
    public void puzzleFinish() {
        Toast.makeText(this, "拼图完成", Toast.LENGTH_SHORT).show();
    }
}