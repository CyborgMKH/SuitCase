package com.example.suitcase;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.suitcase._db.MyDbHelper;
import com.example.suitcase.databinding.ActivityAddItemsPageBinding;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.ByteArrayOutputStream;

;

public class Add_Items_page extends AppCompatActivity {
    ActivityAddItemsPageBinding binding;
    MyDbHelper mdb;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddItemsPageBinding.inflate(getLayoutInflater());

        //Adding custom title in the action bar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.additem_title);

        //Add Icon to the left of the title in the action bar in android
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(binding.getRoot());
        mdb = new MyDbHelper(this);

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

        ImageView Img = binding.addItemDummyImg;
        TextView txtName = binding.txtAddItemName;
        TextView txtPrice = binding.txtAddItemPrice;
        TextView txtLocation = binding.txtAddItemLocation;
        TextView txtDescription = binding.txtAddItemDescription;

        binding.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                byte[] image = imageViewToByte(Img);
                String name = txtName.getText().toString().trim();
                String price = txtPrice.getText().toString().trim();
                String location = txtLocation.getText().toString().trim();
                String descriptions = txtDescription.getText().toString().trim();

                //logics for inserting data in the table
                if (image==null||name.isEmpty()||price.isEmpty()||location.isEmpty()||descriptions.isEmpty()){
                    Toast.makeText(Add_Items_page.this, "Every Fields are mandatory !", Toast.LENGTH_SHORT).show();
                    return; //stop further execution
                }
                else if(mdb.dataExists(name)){
                    Toast.makeText(Add_Items_page.this, "Item data already exist. Try adding another !", Toast.LENGTH_SHORT).show();
                    return;// stop further execution
                }
                boolean inserted = mdb.insertRecord(image, name, price, location, descriptions);
                if (inserted){
                    Toast.makeText(Add_Items_page.this, "Item added successfully !", Toast.LENGTH_SHORT).show();

                    //clear edit texts and circle image
                    Img.setImageResource(R.drawable.dummy);
                    txtName.setText("");
                    txtPrice.setText("");
                    txtLocation.setText("");
                    txtDescription.setText("");
                }else{
                    Toast.makeText(Add_Items_page.this, "Insertion failed !", Toast.LENGTH_SHORT).show();
                }

            }
        });
        binding.addItemDummyImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(Add_Items_page.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Uri uri = data.getData();
            binding.addItemDummyImg.setImageURI(uri);
        }
    }

}