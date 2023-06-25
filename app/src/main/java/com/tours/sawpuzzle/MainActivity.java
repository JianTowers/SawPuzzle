package com.tours.sawpuzzle;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tours.sawpuzzle.data.ImageBlock;
import com.tours.sawpuzzle.databinding.ActivityMainBinding;
import com.tours.sawpuzzle.ui.ImageAdapter;
import com.tours.sawpuzzle.ui.ImageListener;
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

        initAdapter();
        updateAdapter();
    }

    private void initAdapter() {
        imageAdapter = new ImageAdapter(this);
        binding.recycler.setLayoutManager(new GridLayoutManager(this, SIDE));
        binding.recycler.setAdapter(imageAdapter);

        binding.recycler.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(2, 2, 2, 2);
            }
        });
    }

    private void updateAdapter(){
        Bitmap bitmap = ImageUtils.getBitmap(this, R.mipmap.test);
        lists = ImageUtils.split(this, bitmap, SIDE);
        imageAdapter.updateData(lists);
    }

    @Override
    public void puzzleFinish() {
        Toast.makeText(this, "拼图完成", Toast.LENGTH_SHORT).show();
    }
}