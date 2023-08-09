package com.tours.sawpuzzle.ui.crop;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tours.sawpuzzle.databinding.AcitvityCropBinding;
import com.tours.sawpuzzle.databinding.ActivityMainBinding;

/**
 * @Description:
 * @Author: Jian
 * @Date: 2023/7/18
 **/
public class CropActivity extends AppCompatActivity {

    private AcitvityCropBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AcitvityCropBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = this.getIntent();
        String path = intent.getStringExtra("select");
    }
}
