package com.rocdev.inventorytracker.adapters;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.rocdev.inventorytracker.R;
import com.rocdev.inventorytracker.data.InventoryContract;

/**
 * Created by piet on 05-03-17.
 */

public class SuppliersCursorAdapter extends CursorAdapter {

    private static final String LOG_TAG = SuppliersCursorAdapter.class.getSimpleName();

    public SuppliersCursorAdapter(Context context, Cursor c) {
        super(context, c, 0/* flags */);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.supplier_list_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView name = (TextView) view.findViewById(R.id.supplierNameTextView);
        int nameColumnIndex = cursor.getColumnIndex(InventoryContract.SupplierEntry.COLUMN_SUPPLIER_NAME);
        Log.i(LOG_TAG, cursor.getString(nameColumnIndex));
        name.setText(cursor.getString(nameColumnIndex));
    }
}
