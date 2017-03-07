package com.rocdev.inventorytracker.adapters;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.rocdev.inventorytracker.R;
import com.rocdev.inventorytracker.data.InventoryContract;

import java.text.DecimalFormat;

/**
 * Created by piet on 02-03-17.
 */

public class ArticlesCursorAdapter extends CursorAdapter {

    private static String LOG_TAG = ArticlesCursorAdapter.class.getSimpleName();




    /**
     * Constructs a new {@link ArticlesCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public ArticlesCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context   app context
     * @param cursor    The cursor from which to get the data. The cursor is already
     *                  moved to the correct position.
     * @param viewGroup The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.article_list_item, viewGroup, false /* ? */);
    }

    /**
     * This method binds the pet data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current pet can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        TextView nameTextView = (TextView) view.findViewById(R.id.articleNameTextView);
        int nameColumnIndex = cursor.getColumnIndex(InventoryContract.ArticleEntry.COLUMN_ARTICLE_NAME);
        nameTextView.setText(cursor.getString(nameColumnIndex));
        TextView quantityTextView = (TextView) view.findViewById(R.id.articleQuantityTextView);
        int quantityColumnIndex = cursor.getColumnIndex(InventoryContract.ArticleEntry.COLUMN_ARTICLE_QUANTITY);
        int quantity = cursor.getInt(quantityColumnIndex);
        quantityTextView.setText(String.valueOf(quantity));
        TextView priceTextView = (TextView) view.findViewById(R.id.articlePriceTextView);
        int priceColumnIndex = cursor.getColumnIndex(InventoryContract.ArticleEntry.COLUMN_ARTICLE_PRICE);
        double price = cursor.getDouble(priceColumnIndex);
        DecimalFormat dc = new DecimalFormat("0.00");
        priceTextView.setText("€ " + dc.format(price));
        Button saleButton = (Button) view.findViewById(R.id.saleButton);
        int idColumnIndex = cursor.getColumnIndex(InventoryContract.ArticleEntry._ID);
        final long id = cursor.getLong(idColumnIndex);
        saleButton.setTag(id);
        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long id = (long) view.getTag();
                Uri articleUri = ContentUris.withAppendedId(InventoryContract.ArticleEntry.CONTENT_URI, id);
                String[] projection = {InventoryContract.ArticleEntry.COLUMN_ARTICLE_QUANTITY};
                Cursor quantityCursor = context.getContentResolver().query(articleUri, projection, null, null, null);
                quantityCursor.moveToFirst();
                int quantityColumnIndex = quantityCursor.getColumnIndex(InventoryContract.ArticleEntry.
                        COLUMN_ARTICLE_QUANTITY);
                int quantity = quantityCursor.getInt(quantityColumnIndex);
                if (quantity > 0) {
                    quantity--;
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(InventoryContract.ArticleEntry.COLUMN_ARTICLE_QUANTITY, quantity);
                    int rows = context.getContentResolver().update(articleUri, contentValues, null, null);
                }
            }
        });
    }
}
