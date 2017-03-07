package com.rocdev.inventorytracker;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ListView;

import com.rocdev.inventorytracker.adapters.SuppliersCursorAdapter;
import com.rocdev.inventorytracker.data.InventoryContract;

public class SuppliersActivity extends BaseActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private int loaderId = 0;
    private SuppliersCursorAdapter mAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suppliers);
        setTitle("Suppliers");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SuppliersActivity.this, SupplierEditorActivity.class));
            }
        });
        mAdapter = new SuppliersCursorAdapter(this, null);
        ListView listView = (ListView) findViewById(R.id.suppliersListView);
        listView.setAdapter(mAdapter);
        getLoaderManager().initLoader(loaderId, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                InventoryContract.SupplierEntry._ID,
                InventoryContract.SupplierEntry.COLUMN_SUPPLIER_NAME
        };
        return new CursorLoader(this, InventoryContract.SupplierEntry.CONTENT_URI, projection,
                null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

}
