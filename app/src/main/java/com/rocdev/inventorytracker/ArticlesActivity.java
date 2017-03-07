package com.rocdev.inventorytracker;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.rocdev.inventorytracker.adapters.ArticlesCursorAdapter;
import com.rocdev.inventorytracker.data.InventoryContract;

/**
 * Activity that shows list of articles
 *
 * For explanation see https://developer.android.com/guide/topics/ui/layout/listview.html
 */

public class ArticlesActivity extends BaseActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private int loaderId = 1;

    private static final String LOG_TAG = ArticlesActivity.class.getSimpleName();

    // This is the Adapter being used to display the list's data
    private ArticlesCursorAdapter mAdapter;

    // These are the Articles columns that we will retrieve
    private static final String[] PROJECTION = {
            InventoryContract.ArticleEntry._ID,
            InventoryContract.ArticleEntry.COLUMN_ARTICLE_NAME,
            InventoryContract.ArticleEntry.COLUMN_ARTICLE_QUANTITY,
            InventoryContract.ArticleEntry.COLUMN_ARTICLE_PRICE
    };

    // This is the select criteria
    static final String SELECTION = "((" +
            InventoryContract.ArticleEntry.COLUMN_ARTICLE_NAME + " NOT NULL) AND (" +
            InventoryContract.ArticleEntry.COLUMN_ARTICLE_NAME + " != '' ))";

    static final String SORT_ORDER = InventoryContract.ArticleEntry.COLUMN_ARTICLE_NAME + " COLLATE NOCASE ASC";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles);
        setTitle("Articles");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            startActivity(new Intent(ArticlesActivity.this, ArticleEditorActivity.class));
            }
        });
        ListView listView = (ListView) findViewById(R.id.articlesListView);

        // Create an empty adapter we will use to display the loaded data.
        // We pass null for the cursor, then update it in onLoadFinished()
        mAdapter = new ArticlesCursorAdapter(this, null);
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Uri uri = ContentUris.withAppendedId(InventoryContract.ArticleEntry.CONTENT_URI, id);
                Intent intent = new Intent(getApplicationContext(), ArticleEditorActivity.class);
                intent.setData(uri);
                startActivity(intent);
            }
        });

        // Prepare the loader.  Either re-connect with an existing one,
        // or start a new one.
        getLoaderManager().initLoader(loaderId, null, this);
    }

    // Called when a new Loader needs to be created
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this, InventoryContract.ArticleEntry.CONTENT_URI, PROJECTION,
                SELECTION, null, SORT_ORDER);
    }

    // Called when a previously created loader has finished loading
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mAdapter.swapCursor(cursor);
    }

    // Called when a previously created loader is reset, making the data unavailable
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
