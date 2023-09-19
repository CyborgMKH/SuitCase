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

    // setting up Constructors
    public PurchaseCustomAdapter(Context context,
                                 ArrayList<byte[]> images,
                                 ArrayList<String> names,
                                 ArrayList<String> prices,
                                 ArrayList<String> descriptions) {
        this.context = context;
        this.image = images;
        this.name = names;
        this.price = prices;
        this.descriptions = descriptions;
    }

    //This custom adapter helps to retrieve data from purchased table and also helps to display row into recycler view
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.purchase_row, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        // Binding data for views
        holder.imageView.setImageBitmap(BitmapFactory.decodeByteArray(image.get(position), 0, image.get(position).length));
        holder.nameTextView.setText(name.get(position));
        holder.priceTextView.setText(price.get(position));
        holder.descriptionTextView.setText(descriptions.get(position));
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //putting extra for viewing information of rows individually in Purchased detail activity/page
                Intent intent = new Intent(context, ViewPurchaseItemDetails.class);
                intent.putExtra("image", image.get(position));
                intent.putExtra("name", name.get(position));
                intent.putExtra("price", price.get(position));
                intent.putExtra("descriptions", descriptions.get(position));
                context.startActivity(intent);
            }
        });
    }

    //returning the size of the data
    @Override
    public int getItemCount() {
        return name.size();
    }

    //declaring views for each items for recycler view
    //Initializing views based on their corresponding IDs
    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        CircleImageView imageView;
        TextView nameTextView, priceTextView, descriptionTextView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.circleImageView);
            nameTextView = itemView.findViewById(R.id.textView2);
            descriptionTextView = itemView.findViewById(R.id.textView3);
            priceTextView = itemView.findViewById(R.id.textView4);
            linearLayout = itemView.findViewById(R.id.mainLayout2);
        }
    }
}
