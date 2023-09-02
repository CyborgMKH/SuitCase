package com.example.suitcase;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.CursorWindow;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.suitcase._db.MyDbHelper;
import com.example.suitcase.databinding.ActivityDrawerHeaderBinding;
import com.example.suitcase.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;


import java.lang.reflect.Field;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //declaring instances
    ActivityMainBinding binding;
    MyDbHelper mdb;
    RecyclerView recyclerView;
    ArrayList<String> name, price, descriptions;
    ArrayList<byte[]> image;
    CustomAdapter customAdapter;
    BottomNavigationView bottomNavigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;

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
                        return true;
                    case R.id.user:
                        startActivity(new Intent(getApplicationContext(), Suitcase_Main_Menu.class));
                        overridePendingTransition(0, 0);
                        break;
                }
                return false;
            }
        });
        // Adding custom title in the action bar and custom icon for up button
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.home_title);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // Initialize the RecyclerView from the layout using data binding
        recyclerView = binding.recyclerViewHome;

        // Initialize a database helper
        mdb = new MyDbHelper(MainActivity.this);

        // Initialize lists to store data for the RecyclerView
        image = new ArrayList<>(); // List to store image data
        name = new ArrayList<>();  // List to store name data
        price = new ArrayList<>(); // List to store price data
        descriptions = new ArrayList<>(); // List to store descriptions data

        // Fetch data from the database
        Cursor cursor = mdb.getAllData();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                image.add(cursor.getBlob(0));
                name.add(cursor.getString(1));
                price.add(cursor.getString(2));
                descriptions.add(cursor.getString(3));
            } while (cursor.moveToNext());

            cursor.close();
        }
        // Create and set up the adapter
        customAdapter = new CustomAdapter(this, image, name, price, descriptions);
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
        // Initialize the ActionBarDrawerToggle
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                binding.drawerLayout, // This should be your DrawerLayout
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );

        // Set the drawer toggle as the DrawerListener
        binding.drawerLayout.addDrawerListener(actionBarDrawerToggle);

        // Sync the state of the toggle with the drawer
        //and adding switch case for performing corresponding activities
        actionBarDrawerToggle.syncState();
        binding.nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id)
                {

                    case R.id.nav_home:
                        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.nav_profile:
                        Intent intent2=new Intent(getApplicationContext(),Suitcase_Main_Menu.class);
                        startActivity(intent2);
                        overridePendingTransition(0, 0);
                        Toast.makeText(MainActivity.this, "Profile",Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.nav_purchased:
                        Intent intent3=new Intent(getApplicationContext(),Purchased_item_list_page.class);
                        startActivity(intent3);
                        overridePendingTransition(0, 0);
                        Toast.makeText(MainActivity.this, "Purchased",Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.nav_mainList:
                        Intent intent4=new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent4);
                        overridePendingTransition(0, 0);
                        Toast.makeText(MainActivity.this, "Main List",Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.nav_aboutUs:
                        Intent intent5=new Intent(getApplicationContext(),AboutUs.class);
                        startActivity(intent5);
                        overridePendingTransition(0, 0);
                        Toast.makeText(MainActivity.this, "About us",Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.nav_rate:
                        showRatingDialog();
                        binding.drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.nav_logout:
                        confirmLogoutDialog();
                        binding.drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    default:
                        return true;

                }
                return true;
            }
        });
    }

    //creating custom menu in actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //fonOptionSelected for action bar items such as menu, menu drawer
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        } else if (item.getItemId() == R.id.delete_all) {
            confirmDialog();
        } else if (item.getItemId() == R.id.shareAll){
            shareAllItems();
        }else if (item.getItemId() == R.id.home){
            binding.drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //refresh the activity after performing crud functionality
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            recreate();
        }
    }
    //method to open confirm dialog and perform deletion of all listed items
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
                overridePendingTransition(0, 0);
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
    //function for sharing all the items shown in the recycler view as a list
    private void shareAllItems() {
        StringBuilder messageBuilder = new StringBuilder("--------ITEMS LISTS--------\n\n");
        int itemNumber = 1;

        for (int i = 0; i < name.size(); i++) {
            String itemName = name.get(i);
            String itemPrice = price.get(i);
            String itemDescription = descriptions.get(i);

            // Append item number and details to the message
            messageBuilder.append(itemNumber).append(".\n")
                    .append("Item: ").append(itemName).append("\n")
                    .append("Price: ").append(itemPrice).append("\n")
                    .append("Description: ").append(itemDescription).append("\n\n");

            itemNumber++; // Increment the item number
        }

        String message = messageBuilder.toString().trim();

        // Create an intent to share the item information
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, message);

        // Check if there is an app available to handle the intent
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "No messaging app found to share.", Toast.LENGTH_SHORT).show();
        }
    }

    //confirmDialog method for the operation of logging out from SuitCase app in users choice Yes/no
    void confirmLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Log out from SuitCase?");
        builder.setMessage("Are you sure you want to log out?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Refresh activity and user will be logout of the suitcase app
                // and activity will be finished and again user have to login to enter inside Suitcase app
                Intent intent = new Intent(MainActivity.this, Login_page.class);
                startActivity(intent);
                finish();
                Toast.makeText(MainActivity.this, "Logged out from SuitCase!", Toast.LENGTH_SHORT).show();
                overridePendingTransition(0, 0);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user click No on the dialogue box user stays in the same page
            }
        });
        builder.create().show();
    }

    // Function to show the rating dialog
    private void showRatingDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.rating_dialog, null);

        RatingBar ratingBar = dialogView.findViewById(R.id.ratingBar);
        Button submitButton = dialogView.findViewById(R.id.submitRatingButton);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView)
                .setTitle("Rate the App")
                .setCancelable(true);

        final AlertDialog dialog = builder.create();

        // Handle the rating submission
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float rating = ratingBar.getRating();
                // Send the rating to your dummy API or perform any required action
                // For this example, we'll just display a toast
                Toast.makeText(MainActivity.this, "Thanks for rating us: " + rating, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        // Show the dialog
        dialog.show();
    }
}
