package com.example.suitcase;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.example.suitcase.databinding.ActivityForgetPasswordPageBinding;

public class Forget_Password_Page extends AppCompatActivity {
    ActivityForgetPasswordPageBinding binding;
    LoginDBHelper loginDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgetPasswordPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Hide action bar in splash screen
        getSupportActionBar().hide();

        loginDBHelper = new LoginDBHelper(this);

        binding.btnValidEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validate email address
                String email = binding.txtForegetPassEmailValid.getText().toString().trim();
                if (loginDBHelper.checkEmail(email)) {
                    // Email is valid, show the new password fields
                    binding.textInputLayoutNewPassword.setVisibility(View.VISIBLE);
                    binding.textInputLayoutConfirmPassword.setVisibility(View.VISIBLE);

                    // Handle the logic for updating the password in the database
                    binding.btnValidEmail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String newPassword = binding.editTextNewPassword.getText().toString().trim();
                            String confirmPassword = binding.editTextConfirmPassword.getText().toString().trim();

                            if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                                // Check if newPassword or confirmPassword is empty
                                Toast.makeText(Forget_Password_Page.this, "Please enter both password fields", Toast.LENGTH_SHORT).show();
                            } else if (newPassword.equals(confirmPassword)) {
                                // Passwords match, update the password in the database
                                if (loginDBHelper.updatePassword(email, newPassword)) {
                                    // Password updated successfully
                                    Intent intent = new Intent(getApplicationContext(), Login_page.class);
                                    startActivity(intent);
                                    Toast.makeText(Forget_Password_Page.this, "Password reset successfully!", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Password update failed
                                    Toast.makeText(Forget_Password_Page.this, "Password reset failed!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // Passwords do not match, show an error message
                                Toast.makeText(Forget_Password_Page.this, "confirm passwords do not match!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(Forget_Password_Page.this, "Invalid Email Address !", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
