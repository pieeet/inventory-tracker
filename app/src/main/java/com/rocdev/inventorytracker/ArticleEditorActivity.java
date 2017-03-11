package com.rocdev.inventorytracker;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.rocdev.inventorytracker.data.InventoryContract;
import com.rocdev.inventorytracker.models.Supplier;

import java.util.ArrayList;

public class ArticleEditorActivity extends BaseActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private EditText articleNameEditText, inStockEditText, priceEditText;
    private Spinner supplierSpinner;
    private Button addEditButton;
    private Cursor cursor;

    private Uri articleUri;
    private ArrayList<Supplier> suppliers;
    private SimpleCursorAdapter adapter;
    private static final String LOG_TAG = ArticleEditorActivity.class.getSimpleName();
    private static final int SUPPLIER_LOADER_ID = 1;
    private static final int ARTICLE_LOADER_ID = 2;

    // This is the select criteria
    private static final String SELECTION = "((" +
            InventoryContract.ArticleEntry.COLUMN_ARTICLE_NAME + " NOT NULL) AND (" +
            InventoryContract.ArticleEntry.COLUMN_ARTICLE_NAME + " != '' ))";

    private static final String[] SUPPLIER_PROJECTION = {
            InventoryContract.SupplierEntry._ID,
            InventoryContract.SupplierEntry.COLUMN_SUPPLIER_NAME
    };
    private static final String[] ARTICLE_PROJECTION = {
            InventoryContract.ArticleEntry._ID,
            InventoryContract.ArticleEntry.COLUMN_ARTICLE_NAME,
            InventoryContract.ArticleEntry.COLUMN_ARTICLE_PRICE,
            InventoryContract.ArticleEntry.COLUMN_ARTICLE_QUANTITY,
            InventoryContract.ArticleEntry.COLUMN_ARTICLE_SUPPLIER
    };

    private static final String SORT_ORDER_SUPPLIER_NAMES_ASC = InventoryContract.SupplierEntry
            .COLUMN_SUPPLIER_NAME + " COLLATE NOCASE ASC";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_editor);
        Intent intent = getIntent();
        articleUri = intent.getData();
        if (articleUri == null) {
            setTitle("Add article");
        } else {
            setTitle("Edit article");
        }
        initViews();
        initListeners();
        getLoaderManager().initLoader(SUPPLIER_LOADER_ID, null, this);
    }

    private void initViews() {
        articleNameEditText = (EditText) findViewById(R.id.articlerNameEditText);
        supplierSpinner = (Spinner) findViewById(R.id.articleSupplierSpinner);
        inStockEditText = (EditText) findViewById(R.id.articleInStockEditText);
        priceEditText = (EditText) findViewById(R.id.articlerPriceEditText);
        addEditButton = (Button) findViewById(R.id.addEditArticleButton);
        if (articleUri != null) {
            addEditButton.setText("Save article");
        }
    }

    private void initListeners() {
        addEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveArticle();
            }
        });
    }

    private void saveArticle() {
        String articleName = articleNameEditText.getText().toString();
        if (TextUtils.isEmpty(articleName)) {
            Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        int quantity = 0;
        try {
            quantity = Integer.parseInt(inStockEditText.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        double price = 0.0;
        try {
            price = Double.parseDouble(priceEditText.getText().toString());
        } catch (NumberFormatException ignored) {}
        int supplierPosition = supplierSpinner.getSelectedItemPosition();
        long supplierId = -1;
        if (suppliers.isEmpty()) {
            Toast.makeText(this, "Add supplier(s) first", Toast.LENGTH_SHORT).show();
            return;
        }
        supplierId = suppliers.get(supplierPosition).getId();
        ContentValues contentValues = new ContentValues();
        contentValues.put(InventoryContract.ArticleEntry.COLUMN_ARTICLE_NAME, articleName);
        contentValues.put(InventoryContract.ArticleEntry.COLUMN_ARTICLE_QUANTITY, quantity);
        contentValues.put(InventoryContract.ArticleEntry.COLUMN_ARTICLE_PRICE, price);
        contentValues.put(InventoryContract.ArticleEntry.COLUMN_ARTICLE_SUPPLIER, supplierId);
        long result;
        if (articleUri == null) {
            Uri uri = getContentResolver().insert(InventoryContract.ArticleEntry.CONTENT_URI,
                    contentValues);
            result = ContentUris.parseId(uri);
        } else {
            result = getContentResolver().update(articleUri, contentValues, null, null);
        }
        if (result < 1) {
            Toast.makeText(this, "Error with saving article", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Article saved succefylly", Toast.LENGTH_SHORT).show();
        }
        finish();
    }


    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {
        if (loaderId == SUPPLIER_LOADER_ID) {
            return new CursorLoader(this, InventoryContract.SupplierEntry.CONTENT_URI,
                    SUPPLIER_PROJECTION, SELECTION, null, SORT_ORDER_SUPPLIER_NAMES_ASC);
        } else {
            return new CursorLoader(this, articleUri, ARTICLE_PROJECTION, null, null, null);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if (loader.getId() == SUPPLIER_LOADER_ID) {
            String[] cols = {
                    InventoryContract.SupplierEntry.COLUMN_SUPPLIER_NAME
            };
            int[] to = {android.R.id.text1};
            adapter = new SimpleCursorAdapter(this,
                    android.R.layout.simple_spinner_item, cursor, cols, to, 0);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            supplierSpinner.setAdapter(adapter);
            suppliers = new ArrayList<>();
            cursor.moveToPosition(-1);
            while (cursor.moveToNext()) {
                int idColumnIndex = cursor.getColumnIndex(InventoryContract.SupplierEntry._ID);
                long id = cursor.getLong(idColumnIndex);
                int nameColumnIndex = cursor.getColumnIndex(InventoryContract.SupplierEntry
                        .COLUMN_SUPPLIER_NAME);
                String name = cursor.getString(nameColumnIndex);
                suppliers.add(new Supplier(id, name));
            }
            if (articleUri != null) {
                getLoaderManager().initLoader(ARTICLE_LOADER_ID, null, this);
            }
        } else if (loader.getId() == ARTICLE_LOADER_ID) {
            cursor.moveToFirst();
            int nameColumnIndex = cursor.getColumnIndex(InventoryContract.ArticleEntry
                    .COLUMN_ARTICLE_NAME);
            articleNameEditText.setText(cursor.getString(nameColumnIndex));
            int priceColumnIndex = cursor.getColumnIndex(InventoryContract.ArticleEntry
                    .COLUMN_ARTICLE_PRICE);
            double price = cursor.getDouble(priceColumnIndex);
            priceEditText.setText(String.valueOf(price));
            int quantityColumnIndex = cursor.getColumnIndex(InventoryContract.ArticleEntry
                    .COLUMN_ARTICLE_QUANTITY);
            int quantity = cursor.getInt(quantityColumnIndex);
            inStockEditText.setText(String.valueOf(quantity));
            int supplierColumnIndex = cursor.getColumnIndex(InventoryContract.ArticleEntry
                    .COLUMN_ARTICLE_SUPPLIER);
            long supplierId = cursor.getLong(supplierColumnIndex);
            for (int i = 0; i < suppliers.size(); i++) {
                if (suppliers.get(i).getId() == supplierId) {
                    supplierSpinner.setSelection(i, true);
                    break;
                }
            }
            cursor.close();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
