package com.example.suitcase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.suitcase.databinding.ActivitySignupPageBinding;

public class Signup_Page extends AppCompatActivity {
    ActivitySignupPageBinding binding;
    LoginDBHelper loginDBHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loginDBHelper = new LoginDBHelper(this);

        // Hide action bar in splash screen
        getSupportActionBar().hide();

        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.txtSignupEmail.getText().toString().trim();
                String password = binding.txtSignupPassword.getText().toString().trim();
                String conPassword = binding.txtSignUpConpassword.getText().toString().trim();

                //validating email and password e.g. if the fields are empty or not
                if (email.equals("")|| password.equals("")||conPassword.equals("")){
                    Toast.makeText(Signup_Page.this, "All fields are mandatory !", Toast.LENGTH_SHORT).show();
                }
                //checks if the password matches confirm password
                else if(!password.equals(conPassword)){
                    Toast.makeText(Signup_Page.this, "Confirm Password do not matched !", Toast.LENGTH_SHORT).show();
                }
                //check the email whether it's already exists or not
                else if(loginDBHelper.checkEmail(email)){
                    Toast.makeText(Signup_Page.this, "Email already exists !", Toast.LENGTH_SHORT).show();
                }
                //insert data into the database (sign up user)
                else if (loginDBHelper.insertUsers(email, password)){
                    Toast.makeText(Signup_Page.this, "Signup Successfully !", Toast.LENGTH_SHORT).show();

                    //redirecting user to login page after successfully signup
                    Intent intent = new Intent(Signup_Page.this,Login_page.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(Signup_Page.this, "Signup Failed !", Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.signupLoginNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login_page.class);
                startActivity(intent);
            }
        });
    }
}