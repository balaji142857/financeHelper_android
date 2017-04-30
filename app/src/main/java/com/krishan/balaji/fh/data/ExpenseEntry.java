package com.krishan.balaji.fh.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;

public class ExpenseEntry {

    public static final String TABLE_NAME = "expense";
    public static final String COL_ID = "_id";
    public static final String COL_DATE = "exp_date";
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
            COL_SUBCAT + " int, " +
            COL_ASSET + " int not null, " +
            COL_AMOUNT + "  text not null, " +
            " FOREIGN KEY(" + COL_ASSET + ") REFERENCES " + AssetEntry.TABLE_NAME + "(" + AssetEntry.COL_ID + "),"
            + " FOREIGN KEY(" + COL_CAT + ") REFERENCES " + ExpCatEntry.TABLE_NAME + "(" + ExpCatEntry.COL_ID + "),"
            + " FOREIGN KEY(" + COL_SUBCAT + ") REFERENCES " + ExpSubCatEntry.TABLE_NAME + "(" + ExpSubCatEntry.COL_ID + ")"
            + ");";
    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static final String PATH = "expense";
    public static final Uri CONTENT_URI = Uri.parse(FinanceContract.scheme + FinanceContract.authority + "/" + PATH);
    public static final String TYPE_DIR = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + FinanceContract.authority + "/" + PATH;
    public static final String TYPE_ITEM = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + FinanceContract.authority + "/" + PATH;


    public static final String path_graph_categories_split ="graph/categories_split";
    public static final String path_graph_trend_days ="#/sum";
    public static final String path_graph_trend_months ="#/months/sum";
    public static final String path_insert_bulk="insert/bulk";
    public static final String path_search = "search";
    public static final String path_rawrecord = "rawRecord";
    public static final String path_asset_usage ="asset_usage";

    public static final int URI_TABLE = 200;
    public static final int URI_ID = 202;
    public static final int URI_LISTVIEW = 203;
    public static final int URI_RAWRECORD = 204;
    public static final int URI_GRAPH = 205;
    public static final int URI_GRAPH_N_DAYS = 206;
    public static final int URI_GRAPH_N_MONTHS = 207;
    public static final int URI_BULK_INSERT = 208;
    public static final int URI_SEARCH = 209;
    public static final int URI_ASSET_USAGE=210;


    public static Uri buildUriWithId(long id) {
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }
}
