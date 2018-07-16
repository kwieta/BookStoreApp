/* Created by kwieta in 07/2018 as a final project
 *  for Udacity and Google program
 *  Android Basics Nanodegree by Google. */

/* Created by kwieta in 07/2018 as a final project
 *  for Udacity and Google program
 *  Android Basics Nanodegree by Google. */

package com.example.android.bookstoreapp2;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bookstoreapp2.data.BookContract.BookEntry;

import static android.content.ContentValues.TAG;

/**
 * {@link BookCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of book data as its data source. This adapter knows
 * how to create list items for each row of book data in the {@link Cursor}.
 */
public class BookCursorAdapter extends CursorAdapter {

    /**
     * Constructs a new {@link BookCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public BookCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.book_item, parent, false);
    }

    /**
     * This method binds the book data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the title for the current book can be set on the title TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        TextView titleTextView = view.findViewById(R.id.title);
        TextView authorTextView = view.findViewById(R.id.author);
        TextView yearTextView = view.findViewById(R.id.year);
        TextView publisherTextView = view.findViewById(R.id.publisher);
        TextView priceTextView = view.findViewById(R.id.price);
        TextView quantityTextView = view.findViewById(R.id.quantity);

        // Find the columns of book attributes that we're interested in
        int titleColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_TITLE);
        int authorColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_AUTHOR);
        int yearColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_YEAR);
        int publisherColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_PUBLISHER);
        int priceColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_QUANTITY);

        // Read the book attributes from the Cursor for the current book
        final long id = cursor.getInt(cursor.getColumnIndexOrThrow(BookEntry._ID));
        String bookTitle = cursor.getString(titleColumnIndex);
        String bookAuthor = cursor.getString(authorColumnIndex);
        String bookYear = cursor.getString(yearColumnIndex);
        String bookPublisher = cursor.getString(publisherColumnIndex);
        String bookPrice = cursor.getString(priceColumnIndex);
        final int bookQuantity = cursor.getInt(quantityColumnIndex);

        // Update the TextViews with the attributes for the current book
        titleTextView.setText(bookTitle);
        authorTextView.setText(bookAuthor);
        yearTextView.setText(bookYear);
        publisherTextView.setText(bookPublisher);
        priceTextView.setText("$" + bookPrice);
        quantityTextView.setText(bookQuantity + "");

        if (bookQuantity == 0) {
            quantityTextView.setTextColor(Color.parseColor("#CCCCCC"));
        } else {
            quantityTextView.setTextColor(Color.parseColor("#6a1b9a"));
        }

        // Set visibility of the TextViews that might be shown with no user inputs
        if ((TextUtils.isEmpty(bookYear)) || (bookYear.equals(0))) {
            yearTextView.setVisibility(View.GONE);
        } else {
            yearTextView.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(bookPublisher)) {
            publisherTextView.setVisibility(View.GONE);
        } else {
            publisherTextView.setVisibility(View.VISIBLE);
        }

        // Sold 1 button to decrease quantity (Same effect as below btnDecr1
        Button btnSold = view.findViewById(R.id.btn_sold);
        btnSold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri currentBookUri = ContentUris.withAppendedId(BookEntry.CONTENT_URI, id);
                sellOne(context, currentBookUri, bookQuantity);
            }
        });
    }

    // sellOne substracts the book count by 1 (btnSold OnClick)
    private void sellOne(Context context, Uri currentBookUri, int currentQuantity) {
        int newQuantity;
        if (currentQuantity > 0) {
            newQuantity = currentQuantity - 1;
        } else {
            Toast.makeText(context, context.getString(R.string.book_not_sold),
                    Toast.LENGTH_SHORT).show();
            return;
        }
        ContentValues values = new ContentValues();
        values.put(BookEntry.COLUMN_QUANTITY, newQuantity);
        int rowsAffected = context.getContentResolver().update(currentBookUri, values, null, null);

        if (rowsAffected > 0) {
            Log.i(TAG, "Sold");
            Toast.makeText(context, context.getString(R.string.book_sold),
                    Toast.LENGTH_SHORT).show();
        } else {
            Log.i(TAG, "Could not sell this book");
        }
    }
}