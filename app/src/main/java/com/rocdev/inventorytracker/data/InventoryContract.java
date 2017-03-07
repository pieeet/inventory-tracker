package com.rocdev.inventorytracker.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by piet on 01-03-17.
 */

public class InventoryContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private InventoryContract() {}


    /**
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website.  A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * device.
     */
    public static final String CONTENT_AUTHORITY = "com.rocdev.inventory";


    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Possible path (appended to base content URI for possible URI's)
     * For instance, content://com.rocdev.inventoryTracker/articles/ is a valid path for
     * looking at article data. content://com.rocdev.inventoryTracker/suppliers/ will fail,
     * as the ContentProvider hasn't been given any information on what to do with "suppliers".
     */
    public static final String PATH_ARTICLES = "articles";
    public static final String PATH_SUPPLIERS = "suppliers";


    public static final class ArticleEntry implements BaseColumns {


        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_ARTICLES);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of articles.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ARTICLES;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single article.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ARTICLES;


        /** Name of database table for articles */
        public final static String TABLE_NAME = "articles";

        /**
         * Unique ID number for the article (only for use in the database table).
         *
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * name of the article
         *
         * Type: TEXT
         */
        public static final String COLUMN_ARTICLE_NAME = "name";


        /**
         * price of the article
         *
         * Type: REAL
         */
        public static final String COLUMN_ARTICLE_PRICE = "price";

        /**
         * quantity in stock
         *
         * Type: INTEGER
         */
        public static final String COLUMN_ARTICLE_QUANTITY = "quantity";

        /**
         * supplier
         *
         * Type: Integer (id of supplier)
         */
        public static final String COLUMN_ARTICLE_SUPPLIER = "supplier";


        //TODO Add picture to article

    }

    public static final class SupplierEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_SUPPLIERS);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of suppliers.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SUPPLIERS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single supplier.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SUPPLIERS;


        /** Name of database table for suppliers */
        public final static String TABLE_NAME = "suppliers";

        /**
         * Unique ID number for the supplier (only for use in the database table).
         *
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * name of the supplier
         *
         * Type: TEXT
         */
        public static final String COLUMN_SUPPLIER_NAME = "name";


        /**
         * email of the supplier
         *
         * Type: TEXT
         */
        public static final String COLUMN_SUPPLIER_EMAIL = "email";

        /**
         * telephone number of the supplier
         *
         * Type: TEXT
         */
        public static final String COLUMN_SUPPLIER_TELEPHONE = "telephone";

    }

}
