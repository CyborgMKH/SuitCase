package com.example.suitcase;

import android.content.Intent;
import android.database.Cursor;
import android.database.CursorWindow;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.suitcase._db.MyDbHelper;
import com.example.suitcase._db.PurchaseCustomAdapter;
import com.example.suitcase.databinding.ActivityPurchasedItemListPageBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class Purchased_item_list_page extends AppCompatActivity {
    ActivityPurchasedItemListPageBinding binding;
    RecyclerView recyclerView;
    ArrayList<String> name, price, descriptions, location;
    ArrayList<byte[]> image;
    PurchaseCustomAdapter purchaseCustomAdapter;
    MyDbHelper mdb;
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

        binding = ActivityPurchasedItemListPageBinding.inflate(getLayoutInflater());

        //Adding custom title in the action bar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.purchased_title);

        //Add Icon to the left of the title in the action bar in android
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(binding.getRoot());
        //setting up bottom navigator
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.purchased);

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;

                switch (item.getItemId()) {
                    case R.id.purchased:
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.user:
                        startActivity(new Intent(getApplicationContext(), Suitcase_Main_Menu.class));
                        overridePendingTransition(0, 0);
                        break;
                }
                return false;
            }
        });

        recyclerView = binding.PurchasedRecyclerView;

        mdb = new MyDbHelper(Purchased_item_list_page.this);
        image = new ArrayList<>();
        name = new ArrayList<>();
        price = new ArrayList<>();
        descriptions = new ArrayList<>();
        location = new ArrayList<>();

        // Fetch data from the database
        Cursor cursor = mdb.getAllPurchasedData();
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
        purchaseCustomAdapter = new PurchaseCustomAdapter(this, image, name, price, descriptions, location);
        recyclerView.setAdapter(purchaseCustomAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(Purchased_item_list_page.this));
        purchaseCustomAdapter.notifyDataSetChanged();
    }
}