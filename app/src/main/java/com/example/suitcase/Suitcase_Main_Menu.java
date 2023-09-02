package com.example.suitcase;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.suitcase.databinding.ActivitySuitcaseMainMenuBinding;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Suitcase_Main_Menu extends AppCompatActivity {
    ActivitySuitcaseMainMenuBinding binding;
    BottomNavigationView bottomNavigationView;
    LoginDBHelper loginDBHelper;
    String userEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySuitcaseMainMenuBinding.inflate(getLayoutInflater());

        //Adding custom title in the action bar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.suitcase_menu_title);

        setContentView(binding.getRoot());

        //setting up bottom navigator
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.user);

        //setting up onSelectListener that navigate user to corresponding application classes
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.purchased:
                        startActivity(new Intent(getApplicationContext(), Purchased_item_list_page.class));
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.user:
                        break;
                }
                return false;
            }
        });

        //adding click listener for popping out the dialogue box with some texts given in method confirmDialog()
        binding.deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog();
            }
        });

        // Initialize loginDBHelper
        loginDBHelper = new LoginDBHelper(this);

        // Retrieve userEmail from SharedPreferences or wherever it is stored
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        userEmail = sharedPreferences.getString("userEmail", "");
        // Set the userEmail to the TextView
        TextView userEmailTextView = binding.userEmail;
        userEmailTextView.setText(userEmail);
    }

    //confirmDialog method for the deleting user account
    // Modify the confirmDialog method for deleting user account
    void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete your account?");
        builder.setMessage("Are you sure you want to delete your account?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Delete the user's account from the database
                boolean deleted = loginDBHelper.deleteUserByEmail(userEmail);

                if (deleted) {
                    // If the account was successfully deleted, you can perform any necessary cleanup
                    // (e.g., logging the user out, clearing SharedPreferences)

                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove("userEmail"); // Remove the stored email
                    editor.apply();

                    // Redirect the user to the login page
                    Intent intent = new Intent(Suitcase_Main_Menu.this, Login_page.class);
                    startActivity(intent);
                    Toast.makeText(Suitcase_Main_Menu.this, "Account deleted. Thanks for using SuitCase!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    // Handle account deletion failure
                    // You can show an error message to the user
                    Toast.makeText(Suitcase_Main_Menu.this, "Failed to delete account", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // If the user clicks "No" on the dialogue box, they stay on the same page
            }
        });
        builder.create().show();
    }
}