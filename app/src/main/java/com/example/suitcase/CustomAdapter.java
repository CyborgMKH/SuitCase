package com.example.suitcase;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.suitcase._db.MyDbHelper;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<byte[]> image;
    private ArrayList<String> name;
    private ArrayList<String> price;
    private ArrayList<String> descriptions;
    private  ArrayList<String> location;

    //adding member variable for ItemTouchHelper
    private ItemTouchHelper itemTouchHelper;
    CustomAdapter(Context context,
                  ArrayList<byte[]> images,
                  ArrayList<String> names,
                  ArrayList<String> prices,
                  ArrayList<String> location,
                  ArrayList<String> descriptions) {
        this.context = context;
        this.image = images;
        this.name = names;
        this.price = prices;
        this.location = location;
        this.descriptions = descriptions;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.imageView.setImageBitmap(BitmapFactory.decodeByteArray(image.get(position), 0, image.get(position).length));
        holder.nameTextView.setText(name.get(position));
        holder.priceTextView.setText(price.get(position));
        holder.descriptionTextView.setText(descriptions.get(position));
        holder.locationtxt.setText(location.get(position));
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, View_Update_Delete.class);
                intent.putExtra("image", image.get(position)); // Pass image data as an extra
                intent.putExtra("name", name.get(position)); // Pass name as an extra
                intent.putExtra("price", price.get(position)); // Pass price as an extra
                intent.putExtra("descriptions", descriptions.get(position)); // Pass description as an extra
                intent.putExtra("location", location.get(position)); // Pass location as an extra
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return name.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        CircleImageView imageView;
        TextView nameTextView, priceTextView, descriptionTextView, locationtxt;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.circleImageView);
            nameTextView = itemView.findViewById(R.id.textView2);
            descriptionTextView = itemView.findViewById(R.id.textView3);
            priceTextView = itemView.findViewById(R.id.textView4);
            locationtxt = itemView.findViewById(R.id.textView5);
            linearLayout = itemView.findViewById(R.id.mainLayout);

        }
    }
    // Method to set up swipe-to-delete functionality
    public void setUpItemTouchHelper(RecyclerView recyclerView) {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                // Get the name of the item to be deleted
                String itemNameToDelete = name.get(position);

                // Delete the item from the main database and insert it into the purchased database
                moveItemToPurchasedDatabase(itemNameToDelete, position);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
    public void moveItemToPurchasedDatabase(String itemNameToDelete, int position) {
        // Get the data of the item to be moved
        byte[] itemImage = image.get(position);
        String itemName = name.get(position);
        String itemPrice = price.get(position);
        String itemLocation = location.get(position);
        String itemDescription = descriptions.get(position);

        // Insert the item into the purchased database
        MyDbHelper mdb;
        mdb = new MyDbHelper(context);
        boolean success = mdb.insertPurchasedRecord(itemImage, itemName, itemPrice, itemLocation, itemDescription);

        if (success) {
            // If insertion is successful, delete the item from the main database
            mdb.deleteRecord(itemName);

            // Remove the item from the adapter's data
            image.remove(position);
            name.remove(position);
            price.remove(position);
            location.remove(position);
            descriptions.remove(position);

            // Notify the adapter that an item has been removed
            notifyItemRemoved(position);

            // Optionally, you can notify the adapter of any data changes
            notifyDataSetChanged();
            Toast.makeText(context, itemName+" marked purchased !", Toast.LENGTH_SHORT).show();
        } else {
            // Handle insertion failure
            Toast.makeText(context, "Something went wrong !!!!", Toast.LENGTH_SHORT).show();
        }
    }
}