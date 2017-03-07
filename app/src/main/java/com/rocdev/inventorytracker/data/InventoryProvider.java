package com.rocdev.inventorytracker.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by piet on 02-03-17.
 *
 */

public class InventoryProvider extends ContentProvider {



    private InventoryDbHelper mDbHelper;

    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = InventoryProvider.class.getSimpleName();

    /**
     * URI matcher code for the content URI for the articles table
     */
    private static final int ARTICLES = 100;

    /**
     * URI matcher code for the content URI for a single article in the articles table
     */
    private static final int ARTICLE_ID = 101;

    /**
     * URI matcher code for the content URI for the suppliers table
     */
    private static final int SUPPLIERS = 200;

    /**
     * URI matcher code for the content URI for a single ssupplier in the supplier table
     */
    private static final int SUPPLIER_ID = 201;

    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {

        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.

        //Articles
        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY,
                InventoryContract.PATH_ARTICLES, ARTICLES);
        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY,
                InventoryContract.PATH_ARTICLES + "/#", ARTICLE_ID);

        //Suppliers
        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY,
                InventoryContract.PATH_SUPPLIERS, SUPPLIERS);
        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY,
                InventoryContract.PATH_SUPPLIERS + "/#", SUPPLIER_ID);
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new InventoryDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor cursor;
        switch (match) {
            case ARTICLES:
                // For the ARTICLES code, query the articles table directly with the given
                // projection, selection, selection arguments, and sort order. The cursor
                // could contain multiple rows of the articles table.
                cursor = database.query(InventoryContract.ArticleEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;

            case ARTICLE_ID:
                // For the PET_ID code, extract out the ID from the URI.
                // For an example URI such as "content://com.example.android.pets/pets/3",
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.
                selection = InventoryContract.ArticleEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(InventoryContract.ArticleEntry.TABLE_NAME, projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;

            case SUPPLIERS:
                cursor = database.query(InventoryContract.SupplierEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;

            case SUPPLIER_ID:
                selection = InventoryContract.SupplierEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(InventoryContract.SupplierEntry.TABLE_NAME, projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;

            default:
                throw new IllegalArgumentException("Cannot query unknown URI: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        int match = sUriMatcher.match(uri);
        switch (match) {
            case ARTICLES:
                return insertArticle(uri, contentValues);
            case SUPPLIERS:
                return insertSupplier(uri, contentValues);
            default:
                throw new IllegalArgumentException("Cannot insert unknown URI: " + uri);
        }
    }

    private Uri insertArticle(Uri uri, ContentValues contentValues) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        long rowId = database.insert(InventoryContract.ArticleEntry.TABLE_NAME, null, contentValues);
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, rowId);
    }

    private Uri insertSupplier(Uri uri, ContentValues contentValues) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        long rowId = database.insert(InventoryContract.SupplierEntry.TABLE_NAME, null, contentValues);
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, rowId);
    }



    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        int match = sUriMatcher.match(uri);
        switch (match) {
            case ARTICLE_ID:
                selection = InventoryContract.ArticleEntry._ID + " =?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                return updateArticle(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Cannot insert unknown URI: " + uri);

        }
    }

    private int updateArticle(Uri uri, ContentValues contentValues, String whereClause,
                              String[] whereArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int rows = database.update(InventoryContract.ArticleEntry.TABLE_NAME, contentValues,
                whereClause, whereArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return rows;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }
}
