package com.ru.gacklash.fluff;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.content.CursorLoader;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ru.gacklash.fluff.support.BookmarkAdapter;
import com.ru.gacklash.fluff.support.BookmarkContract;
import com.ru.gacklash.fluff.support.BookmarkDBHelper;

import java.net.URI;

/**
 * Created by gnack_000 on 29.03.2016.
 */
public class BookmarkActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private CursorAdapter mBookmarkAdapter;
    private ListView mBookView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
//        BookmarkDBHelper bookmarkDBHelper = new BookmarkDBHelper(this);
//        SQLiteDatabase db = bookmarkDBHelper.getWritableDatabase();
//        Cursor bookmarkCursor = db.rawQuery("SELECT  * FROM "+ BookmarkContract.BookmarkEntry.TABLE_NAME, null);
        mBookmarkAdapter = new CursorAdapter(this, null, 0) {
//            private static class ViewHolder {
//                public final TextView bookmarkTextView;
//
//
//                  ViewHolder(View view) {
//                    bookmarkTextView = (TextView) view.findViewById(R.id.bookmarkTextView);
//                }
//            }

            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                int layoutId = R.layout.bookmark_list_item;
                View view = LayoutInflater.from(context).inflate(layoutId, parent, false);

//                ViewHolder viewHolder = new ViewHolder(view);
//                view.setTag(viewHolder);
                return view;
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
//                ViewHolder viewHolder = (ViewHolder) view.getTag();

                String Header = cursor.getString(cursor.getColumnIndex(BookmarkContract.BookmarkEntry.COLUMN_BOOKMARK_NAME));
                String Adress = cursor.getString(2);
                String Progress = cursor.getString(3);
                int layoutId = R.id.bookmarkTextView;
                TextView bookmarkTextView=(TextView)view.findViewById(layoutId);
                bookmarkTextView.setText(Header);

                // Find TextView and set formatted date on it
//                viewHolder.bookmarkTextView.setText(Header+Adress+Progress);
            }
        };
        mBookView=(ListView) findViewById(R.id.bookListView);
        mBookView.setAdapter(mBookmarkAdapter);
        // We'll call our MainActivity
        mBookView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // CursorAdapter returns a cursor at the correct position for getItem(), or null
                // if it cannot seek to that position.
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {
                    Uri.Builder uribuilder=new Uri.Builder();
                    uribuilder.path(Integer.toString(position));
                    //getIntent().setData(uribuilder.build());
                    setResult(RESULT_OK, getIntent().setData(uribuilder.build()));
                    finish();
                }
            }
        });
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Sort order:  Ascending, by date.
        String sortOrder = BookmarkContract.BookmarkEntry._ID + " ASC";
        Uri Uri = BookmarkContract.BookmarkEntry.CONTENT_URI;

        return new CursorLoader(this,
                Uri,
                null,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mBookmarkAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mBookmarkAdapter.swapCursor(null);
    }
}
