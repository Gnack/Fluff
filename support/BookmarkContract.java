package com.ru.gacklash.fluff.support;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by gnack_000 on 30.03.2016.
 */
public class BookmarkContract {

    public static final String CONTENT_AUTHORITY = "com.ru.gacklash.fluff";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_BOOKMARK = "bookmark";

    public static final class BookmarkEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_BOOKMARK).build();

        public static final String TABLE_NAME = "bookmark";

        public static final String COLUMN_BOOKMARK_NAME= "bookmark_name";

        public static final String COLUMN_BOOKMARK_ADDRESS= "bookmark_address";

        public static final String COLUMN_BOOKMARK_PROGRESS= "bookmark_progress";

        public static Uri buildBookmarkUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }
}
