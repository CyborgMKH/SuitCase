package com.example.suitcase._db;

public class Constants {
    //Database Name
    public static final String DB_NAME = "Suitcase_RECORDS.db";

    //Database Table Name
    public static final String TABLE_NAME = "ITEM_RECORDS_TABLE";

    //Purchased Table Name
    public static final String Purchase_Tbl_name= "purchased_tbl";

    // In Constants.java
    public static final int DB_VERSION = 2; // Increment the version number


    //Database Column
    public static final String C_IMAGE = "image";
    public static final String C_NAME = "name";
    public static final String C_PRICE = "price";
    public static final String C_DESCRIPTIONS = "descriptions";

    //Create table query
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + C_IMAGE + " BLOB, "
            + C_NAME + " TEXT PRIMARY KEY, "
            + C_PRICE + " PRICE, "
            + C_DESCRIPTIONS + " TEXT"
            + ")";

    //create purchase table
    //Create table query
    public static final String CREATE_PURCHASE_TABLE = "CREATE TABLE " + Purchase_Tbl_name + " ("
            + C_IMAGE + " BLOB, "
            + C_NAME + " TEXT PRIMARY KEY, "
            + C_PRICE + " PRICE, "
            + C_DESCRIPTIONS + " TEXT"
            + ")";
}

