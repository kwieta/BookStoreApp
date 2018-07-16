/* Created by kwieta in 07/2018 as a final project
 *  for Udacity and Google program
 *  Android Basics Nanodegree by Google. */

package com.example.android.bookstoreapp2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.bookstoreapp2.data.BookContract.BookEntry;

/*This is a helper for BookStoreApp.
Responsible for database creation and managing database versions.*/

public class BookDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = BookDbHelper.class.getSimpleName();
    /**
     * Name of the database file.
     */
    private static final String DATABASE_NAME = "bookstore.db";

    /**
     * Database version.
     * Increment the database version, if you change the database schema.
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructs a new instance of {@link BookDbHelper}.
     *
     * @param context of the app
     */
    public BookDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {


        // Creates a String that contains the SQL statement to create the books table
        String SQL_CREATE_BOOKS_TABLE = "CREATE TABLE " + BookEntry.TABLE_NAME + " ("
                + BookEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + BookEntry.COLUMN_BOOK_TITLE + " TEXT NOT NULL, "
                + BookEntry.COLUMN_AUTHOR + " TEXT NOT NULL, "
                + BookEntry.COLUMN_YEAR + " INTEGER, "
                + BookEntry.COLUMN_PUBLISHER + " TEXT, "
                + BookEntry.COLUMN_PHONE + " INTEGER, "
                + BookEntry.COLUMN_PAGES + " INTEGER, "
                + BookEntry.COLUMN_COVER + " INTEGER, "
                + BookEntry.COLUMN_PRICE + " INTEGER NOT NULL, "
                + BookEntry.COLUMN_QUANTITY + " INTEGER NOT NULL);";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_BOOKS_TABLE);
    }

    /**
     * Called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}