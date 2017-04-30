/*
package com.krishan.balaji.fh.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;

public class IncCatEntry {
    public static final String TABLE_NAME = "inc_cat";
    public static final String COL_ID = "_id";
    public static final String COL_NAME = "inc_cat_name";
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ( "
            + COL_ID + " integer primary key autoincrement, "
            + COL_NAME + " text unique not null);";
    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static final String PATH = "inccat";
    public static final Uri CONTENT_URI = Uri.parse(FinanceContract.scheme + FinanceContract.authority + "/" + PATH);

    public static final int URI_TABLE = 1400;
    public static final int URI_ID = 1401;
    public static final int URI_NAME = 1402;
    public static final int URI_BULK_INSERT = 1403;
    public static String path_insert_bulk ="insert/bulk";

    public static Uri buildUriWithId(long id) {
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }

    public static final String CONTENT_TYPE_DIR = ContentResolver.CURSOR_DIR_BASE_TYPE + FinanceContract.authority + "/" + PATH;
    public static final String CONTENT_TYPE_ITEM = ContentResolver.CURSOR_ITEM_BASE_TYPE + FinanceContract.authority + "/" + PATH;
}
*/
