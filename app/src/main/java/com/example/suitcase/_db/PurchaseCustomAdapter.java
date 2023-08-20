package com.example.suitcase._db;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.suitcase.R;
import com.example.suitcase.ViewPurchaseItemDetails;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PurchaseCustomAdapter extends RecyclerView.Adapter<PurchaseCustomAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<byte[]> image;
    private ArrayList<String> name;
    private ArrayList<String> price;
    private ArrayList<String> descriptions;
    private ArrayList<String> location;

    // Constructor
    public PurchaseCustomAdapter(Context context,
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
        View view = inflater.inflate(R.layout.purchase_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // Bind your data to the views here
        holder.imageView.setImageBitmap(BitmapFactory.decodeByteArray(image.get(position), 0, image.get(position).length));
        holder.nameTextView.setText(name.get(position));
        holder.priceTextView.setText(price.get(position));
        holder.descriptionTextView.setText(descriptions.get(position));
        holder.locationtxt.setText(location.get(position));
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewPurchaseItemDetails.class);
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
        return name.size(); // Return the size of your data list
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
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
            linearLayout = itemView.findViewById(R.id.mainLayout2);
        }
    }
}
