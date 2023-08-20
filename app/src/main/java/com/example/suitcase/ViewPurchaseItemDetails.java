package com.example.suitcase;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.suitcase._db.MyDbHelper;
import com.example.suitcase.databinding.ActivityViewPurchaseItemDetailsBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ViewPurchaseItemDetails extends AppCompatActivity {
    ActivityViewPurchaseItemDetailsBinding binding;
    ImageView Img;
    TextView txtName, txtPrice, txtLocation, txtDescription;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewPurchaseItemDetailsBinding.inflate(getLayoutInflater());
        //Adding custom title in the action bar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.edit_items_title);

        //Add Icon to the left of the title in the action bar in android
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(binding.getRoot());

        //setting up bottom navigator
        bottomNavigationView = findViewById(R.id.bottom_navigation);

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
                        startActivity(new Intent(getApplicationContext(), Suitcase_Main_Menu.class));
                        overridePendingTransition(0, 0);
                        break;
                }
                return false;
            }
        });

        Img = binding.addItemDummyImg1;
        txtName = binding.txtAddItemName;
        txtPrice = binding.txtAddItemPrice;
        txtLocation = binding.txtAddItemLocation;
        txtDescription = binding.txtAddItemDescription;

        //disabling fields
        Img.setEnabled(false);
        txtName.setEnabled(false);
        txtPrice.setEnabled(false);
        txtLocation.setEnabled(false);
        txtDescription.setEnabled(false);


        getAndSetIntentData();
        MyDbHelper myDB = new MyDbHelper(ViewPurchaseItemDetails.this);

        binding.purchasedItemDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //methods to delete the item
                confirmDialog();
            }
        });

    }

    private void getAndSetIntentData() {
        if (getIntent().hasExtra("name") && getIntent().hasExtra("price") && getIntent().hasExtra("location") && getIntent().hasExtra("descriptions") && getIntent().hasExtra("image")) {

            byte[] imageBytes = getIntent().getByteArrayExtra("image"); // Retrieve the image data as a byte array
            String name = getIntent().getStringExtra("name");
            String price = getIntent().getStringExtra("price");
            String location = getIntent().getStringExtra("location");
            String descriptions = getIntent().getStringExtra("descriptions");

            // Convert the byte array back to a Bitmap and set it to the ImageView
            Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            binding.addItemDummyImg1.setImageBitmap(imageBitmap);

            // Set the retrieved data to the respective TextView fields
            binding.txtAddItemName.setText(name);
            binding.txtAddItemPrice.setText(price);
            binding.txtAddItemLocation.setText(location);
            binding.txtAddItemDescription.setText(descriptions);

            // Log the retrieved data
            Log.d("View_Update_Delete", "image"+imageBitmap+"name: " + name + ", price: " + price + ", location: " + location + ", descriptions: " + descriptions);
        } else {
            // Handle the case where data is not available in the Intent
            // You can display a toast message or take any other appropriate action
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }
    }
    void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete All Data?");
        builder.setMessage("Are you sure to delete all Data?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MyDbHelper myDB = new MyDbHelper(ViewPurchaseItemDetails.this);

                // Get the text from the txtName TextView
                String name = txtName.getText().toString().trim();
                myDB.deleteOnePurchasedRow(name);

                // Refresh activity
                Intent intent = new Intent(ViewPurchaseItemDetails.this, Purchased_item_list_page.class);
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            recreate();
        }
    }
}