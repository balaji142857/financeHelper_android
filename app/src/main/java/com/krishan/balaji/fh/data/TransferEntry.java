package com.krishan.balaji.fh.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;

public class TransferEntry {

    public static final String TABLE_NAME="transfers";
    public static final String COL_ID="_id";
    public static final String COL_FROM="from_asset";
    public static final String COL_TO="to_asset";
    public static final String COL_DATETIME="date_time";
    public static final String COL_AMOUNT ="tx_amount";
    public static final String COL_COMMENT="comment";
    public static final String CREATE_TABLE="CREATE TABLE "+TABLE_NAME+" ( "
            + COL_ID+" integer primary key autoincrement, "
            + COL_FROM + " integer not null, "
            + COL_TO + " integer not null, "
            + COL_DATETIME +" text not null, "
            + COL_AMOUNT +" float not null, "
            + COL_COMMENT +" text, "
            + " FOREIGN KEY("+COL_FROM+") REFERENCES "+ AssetEntry.TABLE_NAME+"("+AssetEntry.COL_ID+"),"
            + " FOREIGN KEY("+COL_TO+") REFERENCES "+ AssetEntry.TABLE_NAME+"("+AssetEntry.COL_ID+"));";
    public static final String DROP_TABLE="DROP TABLE "+TABLE_NAME+";";

    public static final String PATH ="tx";
    public static final Uri CONTENT_URI = Uri.parse(FinanceContract.scheme+FinanceContract.authority+"/"+ PATH);

    public static final String TYPE_DIR = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + FinanceContract.authority + "/" + PATH;
    public static final String TYPE_ITEM = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + FinanceContract.authority + "/" + PATH;

    public static final int URI_TABLE = 90;
    public static final int URI_ID=91;


    public static Uri buildWithId(long id) {
        return ContentUris.withAppendedId(CONTENT_URI,id);
    }
}

