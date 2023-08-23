package com.example.suitcase._db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;


public class MyDbHelper extends SQLiteOpenHelper {
    private  Context context;
    public MyDbHelper(@Nullable Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
        this.context = context;
    }

    // Method to fetch all records from the database
    public Cursor getAllData() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM "+ Constants.TABLE_NAME;
        Cursor cursor = null;
        if (db !=null){
            cursor = db.rawQuery(query, null);
        } return cursor;
    }

    // Method to fetch all records from the database
    public Cursor getAllPurchasedData() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM "+ Constants.Purchase_Tbl_name;
        Cursor cursor = null;
        if (db !=null){
            cursor = db.rawQuery(query, null);
        } return cursor;
    }

    //create tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        //create database table using the query from the constants class
        db.execSQL(Constants.CREATE_TABLE);
        db.execSQL(Constants.CREATE_PURCHASE_TABLE);
    }

    //method for updating main data of the list
    public boolean updateRecord(byte[] image, String name, String price, String descriptions) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        boolean success = false;

        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("image", image);
            contentValues.put("name", name);
            contentValues.put("price", price);
            contentValues.put("descriptions", descriptions);

            // Specify the WHERE clause to update the record with the given name
            String whereClause = "name=?";
            String[] whereArgs = {name};

            // Perform the update
            int rowsUpdated = sqLiteDatabase.update(Constants.TABLE_NAME, contentValues, whereClause, whereArgs);

            // Check if the update was successful
            success = (rowsUpdated > 0);
            //Show a toast message based on the update result
            if (success) {
                Toast.makeText(context, "Updated successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Failed to update", Toast.LENGTH_SHORT).show();
            }
        } catch (SQLException e) {
            // Handle the exception
            e.printStackTrace();
        } finally {
            sqLiteDatabase.close();
        }
        return success;
    }
    //to delete one data from database
    void deleteOneRowMainDB(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(Constants.TABLE_NAME,Constants.C_NAME+"=?", new String[]{name} );
        if (result == -1){
            Toast.makeText(context, "Failed To delete", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Successfully Deleted", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //In case of a database upgrade, the schema changes here
        //for simplicity, let's drop the existing table and recreate it.
        db.execSQL("DROP TABLE IF EXISTS "+ Constants.TABLE_NAME);
        onCreate(db);
    }

    //methods to insert records
    public boolean insertRecord(byte[] image, String name, String price, String descriptions) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        boolean success = false;
        try{
            ContentValues contentValues = new ContentValues();
            contentValues.put("image", image);
            contentValues.put("name", name);
            contentValues.put("price", price);
            contentValues.put("descriptions", descriptions);

            long result = sqLiteDatabase.insert(Constants.TABLE_NAME, null, contentValues);
            success = (result != -1); // Check if the insert was successful
        }catch (SQLException e){
            //Handle the exception
            e.printStackTrace();
        }finally {
            sqLiteDatabase.close();
        }
        return success;
    }

    //methods to insert records
    public boolean insertPurchasedRecord(byte[] image, String name, String price, String descriptions) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        boolean success = false;
        try{
            ContentValues contentValues = new ContentValues();
            contentValues.put("image", image);
            contentValues.put("name", name);
            contentValues.put("price", price);
            contentValues.put("descriptions", descriptions);

            long result = sqLiteDatabase.insert(Constants.Purchase_Tbl_name, null, contentValues);
            success = (result != -1); // Check if the insert was successful
        }catch (SQLException e){
            //Handle the exception
            e.printStackTrace();
        }finally {
            sqLiteDatabase.close();
        }
        return success;
    }

    //methods to delete all records
    public void deleteAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " +Constants.TABLE_NAME);
    }

    //methods to delete all records
    public void deleteAllPurchasedData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " +Constants.Purchase_Tbl_name);
    }
    //methods to delete single records
    public void deleteRecord(String name) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(Constants.TABLE_NAME, Constants.C_NAME + "=?", new String[]{name});
        db.close();
    }

    //method to delete purchase item
    public void deleteOnePurchasedRow(String name){
        SQLiteDatabase db = getWritableDatabase();
        long result = db.delete(Constants.Purchase_Tbl_name,Constants.C_NAME+"=?", new String[]{name} );
        if (result == -1){
            Toast.makeText(context, "Failed To delete", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Successfully Deleted", Toast.LENGTH_SHORT).show();
        }
    }

    //method to delete item From main list item
    public void deleteOneMainRow(String name){
        SQLiteDatabase db = getWritableDatabase();
        long result = db.delete(Constants.TABLE_NAME,Constants.C_NAME+"=?", new String[]{name} );
        if (result == -1){
            Toast.makeText(context, "Failed To delete", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Successfully Deleted", Toast.LENGTH_SHORT).show();
        }
    }
    //To check if the data already exists or not
    public boolean dataExists(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            String query = "SELECT 1 FROM " + Constants.TABLE_NAME + " WHERE " + Constants.C_NAME + " = ?";
            cursor = db.rawQuery(query, new String[]{name});
            return cursor != null && cursor.moveToFirst();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
    }
}
