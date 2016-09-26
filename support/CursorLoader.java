package com.ru.gacklash.fluff.support;

import android.content.Context;
import android.net.Uri;
import android.widget.CursorAdapter;

import com.ru.gacklash.fluff.WebActivity;

/**
 * Created by gnack_000 on 31.03.2016.
 */
public class CursorLoader extends android.content.CursorLoader {
    public CursorLoader(WebActivity webActivity, Uri uri, Object o1, Object o, Context context, String sortOrder) {
        super(context, null, null, null, null, BookmarkContract.BookmarkEntry._ID);
        BookmarkDBHelper openHelper=new BookmarkDBHelper(context);

    }
}
