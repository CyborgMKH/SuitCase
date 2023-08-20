package com.example.suitcase;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
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
import com.example.suitcase.databinding.ActivityViewUpdateDeleteBinding;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.ByteArrayOutputStream;

public class View_Update_Delete extends AppCompatActivity {
    ActivityViewUpdateDeleteBinding binding;
    ImageView Img;
    TextView txtName, txtPrice, txtLocation, txtDescription;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewUpdateDeleteBinding.inflate(getLayoutInflater());
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

        Img = binding.addItemImg;
        txtName = binding.txtItemName;
        txtPrice = binding.txtItemPrice;
        txtLocation = binding.txtItemLocation;
        txtDescription = binding.txtItemDescription;


        getAndSetIntentData();
        MyDbHelper myDB = new MyDbHelper(View_Update_Delete.this);

        //Disable the name edit text
        txtName.setEnabled(false);

        binding.updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byte[] image = imageViewToByte(Img);
                String name = txtName.getText().toString().trim();
                String price = txtPrice.getText().toString().trim();
                String location = txtLocation.getText().toString().trim();
                String descriptions = txtDescription.getText().toString().trim();
                myDB.updateRecord(image, name, price, location, descriptions);

                //start new activity immediately updating item
                Intent intent = new Intent(View_Update_Delete.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        binding.addItemImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(View_Update_Delete.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
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
            binding.addItemImg.setImageBitmap(imageBitmap);

            // Set the retrieved data to the respective TextView fields
            binding.txtItemName.setText(name);
            binding.txtItemPrice.setText(price);
            binding.txtItemLocation.setText(location);
            binding.txtItemDescription.setText(descriptions);

            // Log the retrieved data
            Log.d("View_Update_Delete", "image"+imageBitmap+"name: " + name + ", price: " + price + ", location: " + location + ", descriptions: " + descriptions);
        } else {
            // Handle the case where data is not available in the Intent
            // You can display a toast message or take any other appropriate action
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }
    }

    //imageViewToByte method to convert image into bytes and store it into array of byts and store it in database
    private byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    //ADDING MENU AND DECLARING FUNCTIONALITY OF MENU ITEMS
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.view_update_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete) {
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
                MyDbHelper myDB = new MyDbHelper(View_Update_Delete.this);

                // Get the text from the txtName TextView
                String name = txtName.getText().toString().trim();
                myDB.deleteOneMainRow(name);

                // Refresh activity
                Intent intent = new Intent(View_Update_Delete.this, MainActivity.class);
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
}