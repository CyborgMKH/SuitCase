package com.example.suitcase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.suitcase.databinding.ActivitySplashScreenBinding;

public class splashScreen extends AppCompatActivity {
    ActivitySplashScreenBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Hide action bar in splash screen
        getSupportActionBar().hide();

        // Splash method
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), Login_page.class));
                overridePendingTransition(0, 0); // Disables the transition animation
                finish(); // Close this activity
            }
        }, 1500);
    }
}