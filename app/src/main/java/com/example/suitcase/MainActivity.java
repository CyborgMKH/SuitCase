package com.example.suitcase;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorWindow;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.suitcase._db.MyDbHelper;
import com.example.suitcase.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    MyDbHelper mdb;
    RecyclerView recyclerView;
    ArrayList<String> name, price, descriptions, location;
    ArrayList<byte[]> image;
    CustomAdapter customAdapter;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Modify the CursorWindow size
        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 100 * 1024 * 1024); // 100MB is the new size
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Inflate the binding and set the content view
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //setting up bottom navigator
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home);

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
                        return true;
                    case R.id.user:
                        startActivity(new Intent(getApplicationContext(), Suitcase_Main_Menu.class));
                        overridePendingTransition(0, 0);
                        break;
                }
                return false;
            }
        });
        // Adding custom title in the action bar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.home_title);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = binding.recyclerViewHome;

        mdb = new MyDbHelper(MainActivity.this);
        image = new ArrayList<>();
        name = new ArrayList<>();
        price = new ArrayList<>();
        descriptions = new ArrayList<>();
        location = new ArrayList<>();

        // Fetch data from the database
        Cursor cursor = mdb.getAllData();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                image.add(cursor.getBlob(0));
                name.add(cursor.getString(1));
                price.add(cursor.getString(2));
                descriptions.add(cursor.getString(3));
                location.add(cursor.getString(4));
            } while (cursor.moveToNext());

            cursor.close();
        }
        // Create and set up the adapter
        customAdapter = new CustomAdapter(this, image, name, price, descriptions, location);
        recyclerView.setAdapter(customAdapter);
        customAdapter.setUpItemTouchHelper(recyclerView); // Enable swipe-to-delete
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        customAdapter.notifyDataSetChanged();

        // Adding click listener for floating action button to redirect to the add item page
        binding.floatingHomeAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Add_Items_page.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Handle the home/up navigation button click
            // Start your desired activity here
            Intent intent = new Intent(this, Suitcase_Main_Menu.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.delete_all) {
            confirmDialog();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            recreate();
        }
    }
    void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete All Data?");
        builder.setMessage("Are you sure to delete all Data?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MyDbHelper myDB = new MyDbHelper(MainActivity.this);
                myDB.deleteAllData();

                // Refresh activity
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }
    //Setting up Button Navigation


}
