package com.example.suitcase;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import com.example.suitcase.databinding.ActivityLoginPageBinding;

public class Login_page extends AppCompatActivity {
    ActivityLoginPageBinding binding;
    LoginDBHelper loginDBHelper;
    boolean isReady = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        View content = findViewById(android.R.id.content);
        content.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (isReady){
                    content.getViewTreeObserver().removeOnPreDrawListener(this);
                }
                dismissSplashScreen();
                return false;
            }
        });
        super.onCreate(savedInstanceState);
        binding = ActivityLoginPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginDBHelper = new LoginDBHelper(this);

        // Hide action bar in splash screen
        getSupportActionBar().hide();

        //getSupportActionBar().setTitle("Activity name"); used to re-name the display name on the actionbar

        //adding click listener to login button
        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.txtLoginEmail.getText().toString().trim();
                String password = binding.txtLoginPassword.getText().toString().trim();

                // Perform validation on email and password, e.g., check if they are not empty
                // Check if the email and password match in the database
                if (loginDBHelper.checkEmailPassword(email, password)){
                    Toast.makeText(Login_page.this, "Login Successfully !", Toast.LENGTH_SHORT).show();

                    //After the successfully login the user must be redirected to Home activity or targeted activity
                    //So, redirecting the user into the targeted activity and finishing the previous logina ctivity
                    Intent intent = new Intent(Login_page.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(Login_page.this, "Invalid Email or Password !", Toast.LENGTH_SHORT).show();
                    binding.txtLoginPassword.getText().clear();
                }
            }
        });
        binding.createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Signup_Page.class);
                startActivity(intent);
            }
        });
    }

    private void dismissSplashScreen() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isReady = true;
            }
        },1000);
    }
}