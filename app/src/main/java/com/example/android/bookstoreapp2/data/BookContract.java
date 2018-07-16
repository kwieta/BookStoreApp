/* Created by kwieta in 07/2018 as a final project
 *  for Udacity and Google program
 *  Android Basics Nanodegree by Google. */

package com.example.android.bookstoreapp2.data;

import android.net.Uri;
import android.content.ContentResolver;
import android.provider.BaseColumns;

/**
 * API Contract for the Book Store app.
 */
public final class BookContract {

    /**
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website.  A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * device.
     */
    public static final String CONTENT_AUTHORITY = "com.example.android.bookstoreapp2";
    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    /**
     * Possible path (appended to base content URI for possible URI's)
     * For instance, content://com.example.android.books/books/ is a valid path for
     * looking at book data. content://com.example.android.books/sold/ will fail,
     * as the ContentProvider hasn't been given any information on what to do with "sold".
     */
    public static final String PATH_BOOKS = "books";

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private BookContract() {
    }

    /**
     * Inner class that defines constant values for the books database table.
     * Each entry in the table represents a single book.
     */
    public static final class BookEntry implements BaseColumns {

        /**
         * The content URI to access the book data in the provider
         */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_BOOKS);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of books.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single book.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;

        /**
         * Name of database table for books
         */
        public final static String TABLE_NAME = "books";

        /**
         * Unique ID number for the book (only for use in the database table).
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Title of the book.
         * Type: TEXT
         */
        public final static String COLUMN_BOOK_TITLE = "title";

        /**
         * Author of the book.
         * Type: TEXT
         */
        public final static String COLUMN_AUTHOR = "author";


        /**
         * Year of publish.
         * Type: INTEGER
         */

        public final static String COLUMN_YEAR = "year";

        /**
         * Publisher of the book.
         * Type: TEXT
         */
        public final static String COLUMN_PUBLISHER = "publisher";


        /**
         * Phone number of the publisher.
         * Type: TEXT
         */
        public static final String COLUMN_PHONE = "phone";

        /**
         * Number of pages of the book.
         * Type: INTEGER
         */
        public final static String COLUMN_PAGES = "pages";

        /**
         * Book cover type.
         * Type: INTEGER
         */
        public final static String COLUMN_COVER = "cover";

        /**
         * Possible values for the book cover type.
         */
        public static final int COVER_UNKNOWN = 0;
        public static final int COVER_HARD = 1;
        public static final int COVER_SOFT = 2;

        /**
         * Price of the book.
         * Type: INTEGER
         */
        public final static String COLUMN_PRICE = "price";

        /**
         * Quantity of the book in store.
         * Type: INTEGER
         */
        public final static String COLUMN_QUANTITY = "quantity";

    }
}