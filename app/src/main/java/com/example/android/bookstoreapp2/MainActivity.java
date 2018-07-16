/* Created by kwieta in 07/2018 as a final project
 *  for Udacity and Google program
 *  Android Basics Nanodegree by Google. */

package com.example.android.bookstoreapp2;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import static android.provider.BaseColumns._ID;
import static com.example.android.bookstoreapp2.data.BookContract.*;
import static com.example.android.bookstoreapp2.data.BookContract.BookEntry.CONTENT_URI;

/**
 * Displays a list of books that are stored in the app.
 */

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * Identifier for the book data loader
     */
    private static final int BOOK_LOADER = 0;

    /**
     * Adapter for the ListView
     */
    BookCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /** Creates and sets up a bookFab that opens EditorActivity (to add a new book).*/

        FloatingActionButton bookfab = findViewById(R.id.book_fab);
        bookfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        // Find the ListView which will be populated with the book data
        ListView bookListView = findViewById(R.id.list);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        bookListView.setEmptyView(emptyView);

        // Setup an Adapter to create a list item for each row of book data in the Cursor.
        // There is no book data yet (until the loader finishes) so pass in null for the Cursor.
        mCursorAdapter = new BookCursorAdapter(this, null);
        bookListView.setAdapter(mCursorAdapter);

        // Setup the item click listener
        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent detailsIntent = new Intent(view.getContext(), Book.class);
                Uri currentBookUri = ContentUris.withAppendedId(BookEntry.CONTENT_URI, id);

                // Set the URI on the data field of the intent
                detailsIntent.setData(currentBookUri);
                // Starts activity (EditorActivity)
                view.getContext().startActivity(detailsIntent);
            }
        });

        // Init the loader
        getLoaderManager().initLoader(BOOK_LOADER, null, this);
    }

    /**
     * Helper method to insert hardcoded dummy data into the database. For debugging purposes only.
     */

    private void insertBook() {
        // Create a ContentValues object where column names are the keys,
        // and this dummy book attributes are the values.

        ContentValues yummyValues = new ContentValues();
        yummyValues.put(BookEntry.COLUMN_BOOK_TITLE, "Yummy Title");
        yummyValues.put(BookEntry.COLUMN_AUTHOR, "Ooooooh Genius");
        yummyValues.put(BookEntry.COLUMN_YEAR, 1988);
        yummyValues.put(BookEntry.COLUMN_PUBLISHER, "Yummy Publisher");
        yummyValues.put(BookEntry.COLUMN_PHONE, "+48221112233");
        yummyValues.put(BookEntry.COLUMN_PAGES, 404);
        yummyValues.put(BookEntry.COLUMN_COVER, "softcover");
        yummyValues.put(BookEntry.COLUMN_PRICE, 39.90);
        yummyValues.put(BookEntry.COLUMN_QUANTITY, 5);

        ContentValues gummyValues = new ContentValues();
        gummyValues.put(BookEntry.COLUMN_BOOK_TITLE, "Gummy Title");
        gummyValues.put(BookEntry.COLUMN_AUTHOR, "Another Genius");
        gummyValues.put(BookEntry.COLUMN_YEAR, 1998);
        gummyValues.put(BookEntry.COLUMN_PUBLISHER, "Gummy Publisher");
        gummyValues.put(BookEntry.COLUMN_PHONE, "+48111222333");
        gummyValues.put(BookEntry.COLUMN_PAGES, 401);
        gummyValues.put(BookEntry.COLUMN_COVER, "hardcover");
        gummyValues.put(BookEntry.COLUMN_PRICE, 59);
        gummyValues.put(BookEntry.COLUMN_QUANTITY, 10);

        ContentValues dummyValues = new ContentValues();
        dummyValues.put(BookEntry.COLUMN_BOOK_TITLE, "Dummy Title");
        dummyValues.put(BookEntry.COLUMN_AUTHOR, "A Very Stable Genius");
        dummyValues.put(BookEntry.COLUMN_YEAR, 1946);
        dummyValues.put(BookEntry.COLUMN_PUBLISHER, "Dummy Publisher");
        dummyValues.put(BookEntry.COLUMN_PHONE, "+48444555666");
        dummyValues.put(BookEntry.COLUMN_PAGES, 666);
        dummyValues.put(BookEntry.COLUMN_COVER, "softcover");
        dummyValues.put(BookEntry.COLUMN_PRICE, 34.50);
        dummyValues.put(BookEntry.COLUMN_QUANTITY, 15);

        // Insert a new row for dummy book into the provider using the ContentResolver.
        // Use the {@link BookEntry#CONTENT_URI} to indicate that we want to insert
        // into the books database table.
        // Receive the new content URI that will allow us to access dummy book data in the future.
        Uri dummyUri = getContentResolver().insert(BookEntry.CONTENT_URI, dummyValues);
        Uri yummyUri = getContentResolver().insert(BookEntry.CONTENT_URI, yummyValues);
        Uri gummyUri = getContentResolver().insert(BookEntry.CONTENT_URI, gummyValues);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_main.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_book_data:
                insertBook();
                return true;
            // Respond to a click on the "Delete all entries" menu option
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                BookEntry._ID,
                BookEntry.COLUMN_BOOK_TITLE,
                BookEntry.COLUMN_AUTHOR,
                BookEntry.COLUMN_YEAR,
                BookEntry.COLUMN_PUBLISHER,
                BookEntry.COLUMN_PAGES,
                BookEntry.COLUMN_COVER,
                BookEntry.COLUMN_PRICE,
                BookEntry.COLUMN_QUANTITY};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                BookEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Update {@link BookCursorAdapter} with this new cursor containing updated book data
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Callback called when the data needs to be deleted
        mCursorAdapter.swapCursor(null);
    }
}