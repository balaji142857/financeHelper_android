/*
package com.krishan.balaji.fh.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;

public class IncSubCatEntry {

    public static final String TABLE_NAME = "inc_subcat";
    public static final String COL_ID = "_id";
    public static final String COL_CAT_ID = "cat_id";
    public static final String COL_NAME = "inc_subcat_name";
    public static final String COL_IMG = "subcat_img";
    public static final String PATH = "incsubcat";
    public static final String CONTENT_TYPE_ITEM = ContentResolver.CURSOR_ITEM_BASE_TYPE + FinanceContract.authority + "/" + PATH;
    public static final String CONTENT_TYPE_DIR = ContentResolver.CURSOR_DIR_BASE_TYPE + FinanceContract.authority + "/" + PATH;
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ( "
            + COL_ID + " integer primary key autoincrement, "
            + COL_CAT_ID + " int not null, "
            + COL_NAME + " text not null, "
            + COL_IMG + " text, "
            + " FOREIGN KEY ( " + COL_CAT_ID + ") REFERENCES " + IncCatEntry.TABLE_NAME + "(" + IncCatEntry.COL_ID + "));";
    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

    public static final int URI_TABLE = 1600;
    public static final int URI_ID = 1601;
    public static final int URI_NAME = 1603;
    public static final int URI_BULK_INSERT = 1604;
    public static final Uri CONTENT_URI = Uri.parse(FinanceContract.scheme + FinanceContract.authority + "/" + PATH);
    public static String path_insert_bulk ="insert/bulk";


    public static Uri buildUriWithId(long id) {
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }

}
*/
