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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.rocdev.inventorytracker.data.InventoryContract;

public class ArticleEditorActivity extends BaseActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private EditText articleNameEditText, inStockEditText, priceEditText;
    private Spinner supplierSpinner;
    private Button addEditButton;
    private Cursor cursor;

    private static final String LOG_TAG = ArticleEditorActivity.class.getSimpleName();

    private int loaderId;

    // This is the select criteria
    static final String SELECTION = "((" +
            InventoryContract.ArticleEntry.COLUMN_ARTICLE_NAME + " NOT NULL) AND (" +
            InventoryContract.ArticleEntry.COLUMN_ARTICLE_NAME + " != '' ))";

    static final String[] PROJECTION = {
            InventoryContract.SupplierEntry._ID,
            InventoryContract.SupplierEntry.COLUMN_SUPPLIER_NAME,
            InventoryContract.SupplierEntry.COLUMN_SUPPLIER_TELEPHONE,
            InventoryContract.SupplierEntry.COLUMN_SUPPLIER_EMAIL
    };

    static final String SORT_ORDER = InventoryContract.ArticleEntry.COLUMN_ARTICLE_NAME + " COLLATE NOCASE ASC";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_editor);
        Log.i(LOG_TAG, "oncreate strated");
        setTitle("Add article");
        initViews();
        initListeners();
        loaderId = 1;
        getLoaderManager().initLoader(loaderId, null, this);
    }

    private void initViews() {
        articleNameEditText = (EditText) findViewById(R.id.articlerNameEditText);
        supplierSpinner = (Spinner) findViewById(R.id.articleSupplierSpinner);
        inStockEditText = (EditText) findViewById(R.id.articleInStockEditText);
        priceEditText = (EditText) findViewById(R.id.articlerPriceEditText);
        addEditButton = (Button) findViewById(R.id.addEditArticleButton);
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
            Log.i(LOG_TAG, "" + quantity);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        double price = 0.0;
        try {
            price = Double.parseDouble(priceEditText.getText().toString());
        } catch (NumberFormatException ignored) {}
        int supplierPosition = supplierSpinner.getSelectedItemPosition();
        int supplierId = -1;
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "Add supplier(s) first", Toast.LENGTH_SHORT).show();
            return;
        }
        cursor.moveToPosition(supplierPosition);
        supplierId = cursor.getInt(cursor.getColumnIndex(InventoryContract.SupplierEntry._ID));
        ContentValues contentValues = new ContentValues();
        contentValues.put(InventoryContract.ArticleEntry.COLUMN_ARTICLE_NAME, articleName);
        contentValues.put(InventoryContract.ArticleEntry.COLUMN_ARTICLE_QUANTITY, quantity);
        contentValues.put(InventoryContract.ArticleEntry.COLUMN_ARTICLE_PRICE, price);
        contentValues.put(InventoryContract.ArticleEntry.COLUMN_ARTICLE_SUPPLIER, supplierId);
        Uri uri = getContentResolver().insert(InventoryContract.ArticleEntry.CONTENT_URI,
                contentValues);
        long result = ContentUris.parseId(uri);
        if (result == -1) {
            Toast.makeText(this, "Error with saving article", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Article saved succefylly", Toast.LENGTH_SHORT).show();
        }
        finish();
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this, InventoryContract.SupplierEntry.CONTENT_URI,
                PROJECTION, SELECTION, null, SORT_ORDER);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        this.cursor = cursor;
        String[] cols = {
                InventoryContract.SupplierEntry.COLUMN_SUPPLIER_NAME
        };
        int[] to = {android.R.id.text1};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_spinner_item, cursor, cols, to, 0);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        supplierSpinner.setAdapter(adapter);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
