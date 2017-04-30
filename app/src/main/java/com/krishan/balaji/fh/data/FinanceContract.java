package com.krishan.balaji.fh.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;
import static com.krishan.balaji.fh.data.FinanceContract.*;

public class FinanceContract {

    public static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    public static final String authority = "com.krishnan.balaji.fh";
    public static final String scheme = "content://";



    static {
        uriMatcher.addURI(authority, AssetEntry.PATH, AssetEntry.URI_TABLE);
        uriMatcher.addURI(authority, AssetEntry.PATH + "/#", AssetEntry.URI_ID);
        uriMatcher.addURI(authority, AssetEntry.PATH + "/"+AssetEntry.path_insert_bulk, AssetEntry.URI_BULK_INSERT);

        uriMatcher.addURI(authority, ExpCatEntry.PATH, ExpCatEntry.URI_TABLE);
        uriMatcher.addURI(authority, ExpCatEntry.PATH + "/#", ExpCatEntry.URI_ID);
        uriMatcher.addURI(authority, ExpCatEntry.PATH +  "/"+ExpCatEntry.path_insert_bulk, ExpCatEntry.URI_BULK_INSERT);

        uriMatcher.addURI(authority, ExpenseEntry.PATH, ExpenseEntry.URI_TABLE);
        uriMatcher.addURI(authority, ExpenseEntry.PATH + "/#", ExpenseEntry.URI_ID);
        uriMatcher.addURI(authority, ExpenseEntry.PATH + "/"+ExpenseEntry.path_graph_trend_days, ExpenseEntry.URI_GRAPH_N_DAYS);
        uriMatcher.addURI(authority, ExpenseEntry.PATH+"/"+ExpenseEntry.path_graph_categories_split, ExpenseEntry.URI_GRAPH);
        uriMatcher.addURI(authority, ExpenseEntry.PATH + "/"+ExpenseEntry.path_graph_trend_months, ExpenseEntry.URI_GRAPH_N_MONTHS);
        uriMatcher.addURI(authority, ExpenseEntry.PATH +  "/"+ExpenseEntry.path_insert_bulk, ExpenseEntry.URI_BULK_INSERT);
        uriMatcher.addURI(authority, ExpenseEntry.PATH +  "/"+ExpenseEntry.path_search, ExpenseEntry.URI_SEARCH);
        uriMatcher.addURI(authority, ExpenseEntry.PATH +  "/"+ExpenseEntry.path_rawrecord, ExpenseEntry.URI_RAWRECORD);
        uriMatcher.addURI(authority, ExpenseEntry.PATH +  "/"+ExpenseEntry.path_asset_usage, ExpenseEntry.URI_ASSET_USAGE);

        uriMatcher.addURI(authority, ExpSubCatEntry.PATH, ExpSubCatEntry.URI_TABLE);
        uriMatcher.addURI(authority, ExpSubCatEntry.PATH + "/#", ExpSubCatEntry.URI_ID);
        uriMatcher.addURI(authority, ExpSubCatEntry.PATH +  "/"+ExpSubCatEntry.path_insert_bulk, ExpSubCatEntry.URI_BULK_INSERT);

        uriMatcher.addURI(authority,TransferEntry.PATH,TransferEntry.URI_TABLE);
        uriMatcher.addURI(authority,TransferEntry.PATH+"/#",TransferEntry.URI_ID);
    }






}