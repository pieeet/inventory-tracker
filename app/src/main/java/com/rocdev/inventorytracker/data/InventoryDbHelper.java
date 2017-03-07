package com.rocdev.inventorytracker.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by piet on 02-03-17.
 *
 */

public class InventoryDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = InventoryDbHelper.class.getSimpleName();

    /** Name of the database file */
    private static final String DATABASE_NAME = "inventory.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;


    public InventoryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the pets table
        String SQL_CREATE_ARTICLES_TABLE =  "CREATE TABLE " + InventoryContract.ArticleEntry.TABLE_NAME + " ("
                + InventoryContract.ArticleEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + InventoryContract.ArticleEntry.COLUMN_ARTICLE_NAME + " TEXT NOT NULL, "
                + InventoryContract.ArticleEntry.COLUMN_ARTICLE_PRICE + " REAL, "
                + InventoryContract.ArticleEntry.COLUMN_ARTICLE_QUANTITY + " INTEGER NOT NULL DEFAULT 0, "
                + InventoryContract.ArticleEntry.COLUMN_ARTICLE_SUPPLIER + " INTEGER);";
        String SQL_CREATE_SUPPLIERS_TABLE = "CREATE TABLE " + InventoryContract.SupplierEntry.TABLE_NAME + " ("
                + InventoryContract.SupplierEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + InventoryContract.SupplierEntry.COLUMN_SUPPLIER_NAME + " TEXT NOT NULL, "
                + InventoryContract.SupplierEntry.COLUMN_SUPPLIER_EMAIL + " TEXT NOT NULL, "
                + InventoryContract.SupplierEntry.COLUMN_SUPPLIER_TELEPHONE + " TEXT);";




        // Execute the SQL statement
        db.execSQL(SQL_CREATE_ARTICLES_TABLE);
        db.execSQL(SQL_CREATE_SUPPLIERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
