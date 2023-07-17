package com.tours.sawpuzzle.ui.welcome;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.tours.sawpuzzle.databinding.ActivityWeclomeBinding;
import com.tours.sawpuzzle.ui.select.SelectActivity;

/**
 * @author Administrator
 */
public class WelcomeActivity extends AppCompatActivity {

    private ActivityWeclomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWeclomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.startGame.setOnClickListener(view -> startActivity(new Intent(this, SelectActivity.class)));
    }
}