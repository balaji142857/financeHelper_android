/*
package com.krishan.balaji.fh.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;

public class IncomeEntry {
    public static final String TABLE_NAME = "income";
    public static final String COL_ID = "_id";
    public static final String COL_DATE = "inc_date";
    public static final String COL_CAT = "cat";
    public static final String COL_SUBCAT = "subcat";
    public static final String COL_COMMENT = "comment";
    public static final String COL_ASSET = "asset";
    public static final String COL_AMOUNT = "amount";
    public static final String CREATE_TABLE = " CREATE TABLE " + TABLE_NAME + " ( " +
            COL_ID + " integer primary key autoincrement, " +
            COL_DATE + " text not null, " +
            COL_COMMENT + " text not null, " +
            COL_CAT + " int not null, " +
            //TOdo query method of contentprovider returns 0 records when subcat is null
            COL_SUBCAT + " int not null, " +
            COL_ASSET + " int not null, " +
            COL_AMOUNT + "  text not null, " +
            " FOREIGN KEY(" + COL_ASSET + ") REFERENCES " + AssetEntry.TABLE_NAME + "(" + AssetEntry.COL_ID + "),"
            + " FOREIGN KEY(" + COL_CAT + ") REFERENCES " + IncCatEntry.TABLE_NAME + "(" + IncCatEntry.COL_ID + "),"
            + " FOREIGN KEY(" + COL_SUBCAT + ") REFERENCES " + IncSubCatEntry.TABLE_NAME + "(" + IncSubCatEntry.COL_ID + ")"
            + ");";
    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static final String PATH = "income";
    public static final Uri CONTENT_URI = Uri.parse(FinanceContract.scheme + FinanceContract.authority + "/" + PATH);
    public static final String TYPE_DIR = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + FinanceContract.authority + "/" + PATH;
    public static final String TYPE_ITEM = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + FinanceContract.authority + "/" + PATH;

    public static final String path_graph_categories_split ="graph/categories_split";
    public static final String path_graph_trend_days ="#/sum";
    public static final String path_graph_trend_months ="#/months/sum";
    public static final String path_insert_bulk="insert/bulk";
    public static final String path_search = "search";

    public static final int URI_TABLE = 1200;
    public static final int URI_ID = 1202;
    public static final int URI_LISTVIEW = 1203;
    public static final int URI_RAWRECORD = 1204;
    public static final int URI_GRAPH = 1205;
    public static final int URI_GRAPH_N_DAYS = 1207;
    public static final int URI_GRAPH_N_MONTHS=1208;
    public static final int URI_BULK_INSERT = 1209;
    public static final int URI_SEARCH = 1210;


    public static Uri buildUriWithId(long id) {
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }
}
*/
