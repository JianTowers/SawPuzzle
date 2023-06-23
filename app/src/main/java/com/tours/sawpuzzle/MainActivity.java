package com.tours.sawpuzzle;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.tours.sawpuzzle.data.ImageBlock;
import com.tours.sawpuzzle.databinding.ActivityMainBinding;
import com.tours.sawpuzzle.ui.ImageAdapter;
import com.tours.sawpuzzle.utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private ImageAdapter imageAdapter;

    private List<ImageBlock> lists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bitmap bitmap = ImageUtils.getBitmap(this, R.mipmap.test);
        lists = ImageUtils.split(this, bitmap, 6);

        initAdapter();
        imageAdapter.updateData(lists);
    }

    private void initAdapter(){
        imageAdapter = new ImageAdapter();
        binding.recycler.setLayoutManager(new GridLayoutManager(this, 6));
        binding.recycler.setAdapter(imageAdapter);

        binding.recycler.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(2,2,2,2);
            }
        });
    }
}