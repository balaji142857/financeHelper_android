package com.krishan.balaji.fh.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;

public class ExpCatEntry {
    public static final String TABLE_NAME = "exp_cat";
    public static final String COL_ID = "_id";
    public static final String COL_NAME = "exp_cat_name";
    public static final String COL_IMG = "cat_img";
    public static final String COL_USAGE = "exp_cat_usage";
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ( "
            + COL_ID + " integer primary key autoincrement, "
            + COL_IMG + " text, "
            + COL_NAME + " text unique not null, "
            + COL_USAGE + " integer "
            +");";
    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static final String PATH = "expcat";
    public static final Uri CONTENT_URI = Uri.parse(FinanceContract.scheme + FinanceContract.authority + "/" + PATH);

    public static final int URI_TABLE = 400;
    public static final int URI_ID = 401;
    public static final int URI_NAME = 402;
    public static final int URI_BULK_INSERT = 403;
    public static String path_insert_bulk = "insert/bulk";

    public static Uri buildUriWithId(long id) {
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }

    public static final String CONTENT_TYPE_DIR = ContentResolver.CURSOR_DIR_BASE_TYPE + FinanceContract.authority + "/" + PATH;
    public static final String CONTENT_TYPE_ITEM = ContentResolver.CURSOR_ITEM_BASE_TYPE + FinanceContract.authority + "/" + PATH;
}
