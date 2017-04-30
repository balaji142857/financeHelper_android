package com.krishan.balaji.fh.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;

public class AssetEntry {

    public static final String TABLE_NAME = "asset";

    public static final String COL_ID = "_id";
    public static final String COL_NAME = "asset_name";
    public static final String COL_BALANCE = "balance";
    public static final String COL_USAGE = "usage";
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "( "
            + COL_ID + " integer primary key autoincrement, "
            + COL_NAME + " text unique not null, "
            + COL_BALANCE + " float,  "
            + COL_USAGE + " ineteger "
            + ");";
    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    public static final String PATH = "asset";
    public static final Uri CONTENT_URI = Uri.parse(FinanceContract.scheme + FinanceContract.authority + "/" + PATH);
    public static final String TYPE_DIR = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + FinanceContract.authority + "/" + PATH;
    public static final String TYPE_ITEM = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + FinanceContract.authority + "/" + PATH;

    public static final String path_insert_bulk="insert/bulk";
    //public static final String path_graph_usage ="graph/usage";

    public static final int URI_TABLE = 100;
    public static final int URI_ID = 101;
    public static final int URI_GRAPH = 102;
    public static final int URI_BULK_INSERT = 103;
    //public static final int URI_GRAPH_USAGE = 104;

    public static Uri buildUriWithId(long id) {
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }
}
