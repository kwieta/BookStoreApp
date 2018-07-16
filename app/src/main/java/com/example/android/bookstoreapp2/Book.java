/* Created by kwieta in 07/2018 as a final project
*  for Udacity and Google program
*  Android Basics Nanodegree by Google. */

package com.example.android.bookstoreapp2;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bookstoreapp2.data.BookContract;

import static android.content.ContentValues.TAG;
import static com.example.android.bookstoreapp2.data.BookContract.BookEntry.CONTENT_URI;

public class Book extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * Identifier for the book data loader
     */
    private static final int BOOK_LOADER = 1;
    public Uri currentBookUri;
    BookCursorAdapter cursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_item_details);

        Intent intent = getIntent();
        currentBookUri = intent.getData();

        if (currentBookUri != null) {
            setTitle(R.string.details);
            getSupportLoaderManager().initLoader(BOOK_LOADER, null, this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                this,   // Parent activity context
                currentBookUri,        // Table to query
                null,     // Projection to return
                null,            // No selection clause
                null,            // No selection arguments
                null             // Default sort order
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        TextView titleTextView = findViewById(R.id.title);
        TextView authorTextView = findViewById(R.id.author);
        TextView yearTextView = findViewById(R.id.year);
        TextView publisherTextView = findViewById(R.id.publisher);
        TextView pagesTextView = findViewById(R.id.pages);
        TextView coverTextView = findViewById(R.id.cover);
        TextView priceTextView = findViewById(R.id.price);
        TextView quantityTextView = findViewById(R.id.quantity);

        if (cursor != null && cursor.moveToFirst()) {
            int idColumnIndex = cursor.getColumnIndex(BookContract.BookEntry._ID);
            int titleColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_TITLE);
            int authorColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_AUTHOR);
            int yearColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_YEAR);
            int publisherColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_PUBLISHER);
            int phoneColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_PHONE);
            int pagesColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_PAGES);
            int coverColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_COVER);
            int priceColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_QUANTITY);

            // Read the book attributes from the Cursor for the current book
            final long id = cursor.getLong(idColumnIndex);
            String bookTitle = cursor.getString(titleColumnIndex);
            String bookAuthor = cursor.getString(authorColumnIndex);
            String bookYear = cursor.getString(yearColumnIndex);
            String bookPublisher = cursor.getString(publisherColumnIndex);
            final long bookPhone = cursor.getLong(phoneColumnIndex);
            String bookPages = cursor.getString(pagesColumnIndex);
            String bookCover = cursor.getString(coverColumnIndex);
            String bookPrice = cursor.getString(priceColumnIndex);
            final int bookQuantity = cursor.getInt(quantityColumnIndex);


            // Set visibility of the TextViews that might be shown with no user inputs
            if ((TextUtils.isEmpty(bookYear)) || (bookYear.equals("0"))) {
                yearTextView.setVisibility(View.GONE);
            } else {
                yearTextView.setVisibility(View.VISIBLE);
            }
            if (TextUtils.isEmpty(bookPublisher)) {
                publisherTextView.setVisibility(View.GONE);
            } else {
                publisherTextView.setVisibility(View.VISIBLE);
            }
            if ((TextUtils.isEmpty(bookPages)) || (bookPages.equals("0"))) {
                pagesTextView.setVisibility(View.GONE);
            } else {
                pagesTextView.setVisibility(View.VISIBLE);
            }
            if (bookCover.equals("unknown")) {
                coverTextView.setVisibility(View.GONE);
            } else {
                coverTextView.setVisibility(View.VISIBLE);
            }

            // Update the TextViews with the attributes for the current book
            titleTextView.setText(bookTitle + "");
            authorTextView.setText(bookAuthor + "");
            yearTextView.setText("" + bookYear);
            publisherTextView.setText("" + bookPublisher);
            pagesTextView.setText(bookPages + " pages");
            coverTextView.setText(bookCover);
            priceTextView.setText("$" + bookPrice);
            quantityTextView.setText("" + bookQuantity);


            /** EDIT Button
             * Find view (button) with ID btn_edit_details,
             * set OnClickListener on it & overrides onClick method
             * to create an Intent to open EditorActivity */

            Button btnEditDetails = findViewById(R.id.btn_edit_details);
            btnEditDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent btnEditDetailsIntent = new Intent(view.getContext(), EditorActivity.class);
                    Uri currentBookUri = ContentUris.withAppendedId(CONTENT_URI, id);

                    // Set the URI on the data field of the intent
                    btnEditDetailsIntent.setData(currentBookUri);
                    // Starts activity (EditorActivity)
                    view.getContext().startActivity(btnEditDetailsIntent);
                }
            });

            /** -1 button to decrease current book quantity */

            Button btnDecr1 = findViewById(R.id.decr);
            btnDecr1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri currentBookUri = ContentUris.withAppendedId(BookContract.BookEntry.CONTENT_URI, id);
                    substractOne(getApplicationContext(), currentBookUri, bookQuantity);
                }
            });

            /** +1 button to increase current book quantity */

            Button btnIncr1 = findViewById(R.id.incr);
            btnIncr1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri currentBookUri = ContentUris.withAppendedId(BookContract.BookEntry.CONTENT_URI, id);
                    addOne(getApplicationContext(), currentBookUri, bookQuantity);
                }
            });

            /** ORDER button to call a publisher (phone number is taken from the database */

            Button btnOrderCall = findViewById(R.id.btn_order_call);
            btnOrderCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent btnOrderCallIntent = new Intent(Intent.ACTION_DIAL);
                    btnOrderCallIntent.setData(Uri.parse("tel:" + bookPhone));

                    view.getContext().startActivity(btnOrderCallIntent);
                }
            });

            /** DELETE button */

            Button btnDeleteBook = findViewById(R.id.btn_delete_book);
            btnDeleteBook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDeleteConfirmationDialog();
                }
            });
            if (bookQuantity == 0) {
                quantityTextView.setTextColor(Color.parseColor("#CCCCCC"));
            } else {
                quantityTextView.setTextColor(Color.parseColor("#6a1b9a"));
            }
        }
    }

    /**
     * Pop up an alert dialog with 2 options (Cancel/Delete)
     * after user has clicked "Delete" from overflow menu
     */

    private void showDeleteConfirmationDialog() {
        // Creates dialog to be shown to the user when they click the Delete option from menu
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // If user clicks Delete in alert, delete current book
                deleteBook(currentBookUri);
                finish();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // If user clicks "Cancel", hide dialog and sent user back to edition
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Deletes current book from the database
     */

    private int deleteBook(Uri bookUri) {
        return getContentResolver().delete(bookUri, null, null);
    }

    /**
     * Decrease book count by 1
     */

    private void substractOne(Context context, Uri currentBookUri, int currentQuantity) {
        int newQuantity;
        if (currentQuantity > 0) {
            newQuantity = currentQuantity - 1;
        } else {
            Toast.makeText(context, context.getString(R.string.quantity_not_decreased),
                    Toast.LENGTH_SHORT).show();
            return;
        }
        ContentValues values = new ContentValues();
        values.put(BookContract.BookEntry.COLUMN_QUANTITY, newQuantity);
        int rowsAffected = context.getContentResolver().update(currentBookUri, values, null, null);
        if (rowsAffected > 0) {
            Log.i(TAG, "Added one.");
            Toast.makeText(context, context.getString(R.string.quantity_decreased),
                    Toast.LENGTH_SHORT).show();
        } else {
            Log.i(TAG, "Could not update the quantity. \nYou have 0 books already.");
        }
    }

    /**
     * Increase book count by 1
     */

    private void addOne(Context context, Uri currentBookUri, int currentQuantity) {
        int newQuantity;
        if (currentQuantity < 100) {
            newQuantity = currentQuantity + 1;
        } else {
            Toast.makeText(context, context.getString(R.string.quantity_not_increased),
                    Toast.LENGTH_SHORT).show();
            return;
        }
        ContentValues values = new ContentValues();
        values.put(BookContract.BookEntry.COLUMN_QUANTITY, newQuantity);
        int rowsAffected = context.getContentResolver().update(currentBookUri, values, null, null);
        if (rowsAffected > 0) {
            Log.i(TAG, "Added one.");
            Toast.makeText(context, context.getString(R.string.quantity_increased),
                    Toast.LENGTH_SHORT).show();
        } else {
            Log.i(TAG, "Could not add more than 100.");
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Callback called when the data needs to be deleted
        cursorAdapter.swapCursor(null);
    }
}