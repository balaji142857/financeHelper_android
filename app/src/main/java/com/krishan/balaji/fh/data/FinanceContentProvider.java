package com.krishan.balaji.fh.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.net.UrlQuerySanitizer;
import android.support.annotation.Nullable;
import android.util.Log;

import com.krishan.balaji.fh.util.Util;

import java.util.Calendar;

import static com.krishan.balaji.fh.data.FinanceContract.*;

public class FinanceContentProvider extends ContentProvider {

    FinanceDbHelper dbHelper;
    static final int WEATHER = 99100;
    static final int WEATHER_WITH_LOCATION = 99101;
    static final int WEATHER_WITH_LOCATION_AND_DATE = 99102;
    static final int LOCATION = 99300;

    @Override
    public boolean onCreate() {
        dbHelper = new FinanceDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        SQLiteQueryBuilder queryBuilder = null;
        String[] projections = null;
        int type = uriMatcher.match(uri);
        Log.e("matchedType", "type is "+type);
        String table_name = null;
        String sql= null;
        int irritation  = -1;

        Calendar today = Calendar.getInstance();
        String to = Util.dbFormat.format(Calendar.getInstance().getTime());
        today.set(Calendar.MONTH,0);
        String from = Util.dbFormat.format(today.getTime());
        UrlQuerySanitizer sanitizer = new UrlQuerySanitizer(uri.toString());
        String value = sanitizer.getValue("q");

        switch (type){
            case AssetEntry.URI_GRAPH:
                irritation = 1;
                //SELECT t2.exp_cat_name, sum(t1.amount) from expense t1,exp_cat t2 where t1.cat=t2._id group by t1.cat;
                sql="SELECT "+ AssetEntry.COL_NAME + ",sum("+AssetEntry.COL_BALANCE+") from "+AssetEntry.TABLE_NAME+" group by "+AssetEntry.COL_NAME;
                cursor = db.rawQuery(sql,null);
                break;
            case AssetEntry.URI_TABLE :
            case AssetEntry.URI_ID :
                table_name = AssetEntry.TABLE_NAME;
                break;
            /////////////////////////
            case ExpenseEntry.URI_GRAPH_N_DAYS:
                irritation=1;
                today = Calendar.getInstance();
                to = Util.dbFormat.format(Calendar.getInstance().getTime());
                today.add(Calendar.DAY_OF_YEAR,-20);
                from = Util.dbFormat.format(today.getTime());
                sql="SELECT "+ExpenseEntry.COL_DATE+", sum("+ExpenseEntry.COL_AMOUNT+") from "+ExpenseEntry.TABLE_NAME+" where "+ ExpenseEntry.COL_DATE+" between '"+from+"' and '"+to+"' group by "+ExpenseEntry.COL_DATE+";";
                cursor = db.rawQuery(sql,null);
                //SELECT exp_date, sum(amount) from expense where exp_date between '2016-07-27' and '2016-07-31' group by exp_date
                    break;
            case ExpenseEntry.URI_GRAPH_N_MONTHS:
                irritation=1;
                //SELECT  strftime('%m', exp_date), sum(amount) from expense where exp_date between '2016-05-00' and '2016-07-31'  GROUP BY strftime('%m', exp_date);
                today = Calendar.getInstance();
                to = Util.dbFormat.format(Calendar.getInstance().getTime());
                today.set(Calendar.MONTH,0);
                from = Util.dbFormat.format(today.getTime());
                sql="SELECT  strftime('%m', "+ExpenseEntry.COL_DATE+"), sum("+ExpenseEntry.COL_AMOUNT+") from "+ExpenseEntry.TABLE_NAME+" where "+ExpenseEntry.COL_DATE+" between '"+from+"' and '"+to+"'  group by strftime('%m', "+ExpenseEntry.COL_DATE+");";
                cursor = db.rawQuery(sql,null);
                break;
            case ExpenseEntry.URI_ASSET_USAGE:
                irritation =1;
                //SELECT  t2.asset_name, sum(t1.amount) from expense t1, asset t2 where t.asset=t2.id GROUP BY asset;
                sql="SELECT  t2.asset_name, sum(t1.amount) from expense t1, asset t2 where t1.asset=t2._id GROUP BY t1.asset;";
                cursor = db.rawQuery(sql,null);
                break;
            case  ExpenseEntry.URI_ID :
            case  ExpenseEntry.URI_LISTVIEW:
            case ExpenseEntry.URI_RAWRECORD:
                table_name=ExpenseEntry.TABLE_NAME;
                break;
            case ExpenseEntry.URI_GRAPH:
                irritation = 1;
                //SELECT t2.exp_cat_name, sum(t1.amount) from expense t1,exp_cat t2 where t1.cat=t2._id group by t1.cat;
                sql="SELECT t2."+ ExpCatEntry.COL_NAME + ",sum(t1."+ExpenseEntry.COL_AMOUNT+") from "+ExpenseEntry.TABLE_NAME+" t1, "+ExpCatEntry.TABLE_NAME+" t2 where t1."+ExpenseEntry.COL_CAT+"=t2."+ExpCatEntry.COL_ID+" group by "+ExpenseEntry.COL_CAT;
                cursor = db.rawQuery(sql,null);
                break;
            case ExpenseEntry.URI_TABLE:
                //SELECT t1.amount, t1.exp_date, t4.asset_name, t3.exp_subcat_name from expense t1, exp_cat t2, exp_subcat t3,  asset t4 where t1.cat = t2._id  AND t1.subcat = t3._id AND t1.asset =t4._id;
                irritation = 1;
                db = dbHelper.getReadableDatabase();
                queryBuilder = new SQLiteQueryBuilder();
                        queryBuilder.setTables(ExpenseEntry.TABLE_NAME+ " t1, "
                        + ExpCatEntry.TABLE_NAME + " t2, "
                        + ExpSubCatEntry.TABLE_NAME + " t3, "
                        + AssetEntry.TABLE_NAME +" t4 ");
                queryBuilder.appendWhere(" t1."+ExpenseEntry.COL_CAT+ "=" + "t2."+ExpCatEntry.COL_ID);
                queryBuilder.appendWhere(" AND t1."+ExpenseEntry.COL_SUBCAT + "=" + "t3."+ExpSubCatEntry.COL_ID);
                queryBuilder.appendWhere(" AND t1."+ExpenseEntry.COL_ASSET + "=" + "t4."+AssetEntry.COL_ID);
                projections = new String[]{
                        "t1."+ExpenseEntry.COL_ID,
                        "t1."+ExpenseEntry.COL_AMOUNT,
                        "t1."+ExpenseEntry.COL_COMMENT,
                        "t1."+ExpenseEntry.COL_DATE,
                        "t4."+AssetEntry.COL_NAME,
                        "t2."+ExpCatEntry.COL_NAME,
                        "t3."+ExpSubCatEntry.COL_NAME,
                        "t3."+ExpSubCatEntry.COL_IMG
                };
                cursor = queryBuilder.query(db,projections,selection,selectionArgs,null,null,ExpenseEntry.COL_DATE+" DESC");
                cursor.setNotificationUri(getContext().getContentResolver(),uri);
                break;
            case ExpenseEntry.URI_SEARCH:
                irritation=1;
                sanitizer = new UrlQuerySanitizer(uri.toString());
                value = sanitizer.getValue("q");
                //SELECT t1.amount, t1.exp_date, t4.asset_name, t3.exp_subcat_name from expense t1, exp_cat t2, exp_subcat t3,  asset t4 where t1.cat = t2._id  AND t1.subcat = t3._id AND t1.asset =t4._id AND (t4.asset_name LIKE '%<REPLACE>%' OR t3.exp_subcat_name LIKE '%<REPLACE>%' OR t2.exp_cat_name LIKE '%<REPLACE>%' OR t1.exp_date LIKE '%<REPLACE>%' OR t1.comment LIKE '%<REPLACE>%');
                sql = "SELECT t1."+ExpenseEntry.COL_AMOUNT+", t1."+ExpenseEntry.COL_ID+", t2."+ExpCatEntry.COL_NAME+", t1."+ExpenseEntry.COL_COMMENT+", t3."+ExpSubCatEntry.COL_IMG+", t1."+ExpenseEntry.COL_DATE+", t4."+AssetEntry.COL_NAME+", t3."+ExpSubCatEntry.COL_NAME+" from "+ExpenseEntry.TABLE_NAME+" t1, "+ExpCatEntry.TABLE_NAME+" t2, "+ExpSubCatEntry.TABLE_NAME+"  t3,  "+AssetEntry.TABLE_NAME+" t4 where t1."+ExpenseEntry.COL_CAT+" = t2."+ExpCatEntry.COL_ID+"  AND t1."+ExpenseEntry.COL_SUBCAT+" = t3."+ExpSubCatEntry.COL_ID+" AND t1."+ExpenseEntry.COL_ASSET+" =t4."+AssetEntry.COL_ID+" AND (t4."+AssetEntry.COL_NAME+" LIKE '%"+value+"%' OR t3."+ExpSubCatEntry.COL_NAME+" LIKE '%"+value+"%' OR t2."+ExpCatEntry.COL_NAME+" LIKE '%"+value+"%' OR t1."+ExpenseEntry.COL_DATE+" LIKE '%"+value+"%' OR t1."+ExpenseEntry.COL_COMMENT+" LIKE '%"+value+"%')";
                cursor = db.rawQuery(sql,null);
                break;
            case ExpCatEntry.URI_TABLE:
            case ExpCatEntry.URI_ID:
                table_name = ExpCatEntry.TABLE_NAME;
                break;
            case ExpSubCatEntry.URI_TABLE:
                table_name = ExpSubCatEntry.TABLE_NAME;
                break;
            case TransferEntry.URI_ID:
                table_name=TransferEntry.TABLE_NAME;
                break;
            case TransferEntry.URI_TABLE:
                irritation = 1;
                sql= "SELECT t1."+TransferEntry.COL_ID+", t2."+AssetEntry.COL_NAME+" as frmAsset , t3."+AssetEntry.COL_NAME+", t1."+TransferEntry.COL_DATETIME+", t1."+TransferEntry.COL_AMOUNT+", t1."+TransferEntry.COL_COMMENT+" from "+TransferEntry.TABLE_NAME+" t1, "+AssetEntry.TABLE_NAME+" t2, "+AssetEntry.TABLE_NAME+" t3 where t1."+TransferEntry.COL_FROM+" = t2."+AssetEntry.COL_ID+" and t1."+TransferEntry.COL_TO+" = t3."+AssetEntry.COL_ID+";";
                //SELECT t2.asset_name as frmAsset, t3.asset_name, t1.date_time, t1.tx_amount, t1.comment from transfers t1, asset t2, asset t3 where t1.from_asset = t2._id and t1.to_asset = t3._id;
                cursor = db.rawQuery(sql,null);
                break;
            default:
                throw new IllegalArgumentException("NO matching URI's found balaji... : "+uri.toString());
        }
        if(irritation == 1){
            return cursor;
        }
        else{
            Log.e("TableName $$$$$",table_name+":"+selection);
            cursor = db.query(table_name,projection,selection,selectionArgs,null,null,sortOrder);
            cursor.setNotificationUri(getContext().getContentResolver(),uri);
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        int type = uriMatcher.match(uri);
        String returnType = null;
        switch (type){
            case ExpenseEntry.URI_LISTVIEW:
            case ExpenseEntry.URI_TABLE:
                returnType = ExpenseEntry.TYPE_DIR;
                break;
            case ExpenseEntry.URI_ID:
                returnType = ExpenseEntry.TYPE_ITEM;
                break;
            case AssetEntry.URI_TABLE:
                returnType = AssetEntry.TYPE_DIR;
                break;
            case AssetEntry.URI_GRAPH:
            case AssetEntry.URI_ID:
                returnType = AssetEntry.TYPE_ITEM;
                break;
            case TransferEntry.URI_ID:
                returnType=TransferEntry.TYPE_ITEM;
                break;
            case TransferEntry.URI_TABLE:
                returnType=TransferEntry.TYPE_DIR;
                break;
            default:
                throw new IllegalArgumentException("NO matching URI's found balaji... : "+uri.toString());
        }
        return returnType;
    }



    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        int type = uriMatcher.match(uri);
        String table_name = null;
        long id= -1;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Uri returnUri = null;
        switch (type){
            case AssetEntry.URI_TABLE:
                id = db.insert(AssetEntry.TABLE_NAME,null,contentValues);
                returnUri = AssetEntry.buildUriWithId(id);
                break;
            case ExpenseEntry.URI_TABLE :
                id = db.insert(ExpenseEntry.TABLE_NAME,null,contentValues);
                returnUri = ExpenseEntry.buildUriWithId(id);
                break;
            case ExpCatEntry.URI_TABLE:
                id = db.insert(ExpCatEntry.TABLE_NAME,null,contentValues);
                returnUri = ExpCatEntry.buildUriWithId(id);
                break;
            case ExpSubCatEntry.URI_TABLE:
                id = db.insert(ExpSubCatEntry.TABLE_NAME,null,contentValues);
                returnUri = ExpSubCatEntry.buildUriWithId(id);
                break;
            case TransferEntry.URI_TABLE:
                Log.e("adsf","inserting transfer entry");
                id = db.insert(TransferEntry.TABLE_NAME,null,contentValues);
                returnUri=TransferEntry.buildWithId(id);
                Log.e("adslkf","inserted transfer entry, id is "+id+", notfying for uri: "+returnUri);
                break;
            default:
                throw new IllegalArgumentException("NO matching URI's found balaji... : "+uri.toString());
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        int type = uriMatcher.match(uri);
        int deletedRows=0;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (type){
            case AssetEntry.URI_TABLE:
                deletedRows = db.delete(AssetEntry.TABLE_NAME,s,strings);
                break;
            case ExpenseEntry.URI_TABLE :
                deletedRows = db.delete(ExpenseEntry.TABLE_NAME,s,strings);
                break;
            case ExpCatEntry.URI_TABLE:
                deletedRows=db.delete(ExpCatEntry.TABLE_NAME,s,strings);
                break;
            case ExpSubCatEntry.URI_TABLE:
                deletedRows = db.delete(ExpSubCatEntry.TABLE_NAME,s,strings);
                break;
            default:
                break;
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return deletedRows;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        int type = uriMatcher.match(uri);
        int updatedCount = 0;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (type){
            case AssetEntry.URI_TABLE:
                updatedCount = db.update(AssetEntry.TABLE_NAME,contentValues,s,strings);
                break;
            case ExpenseEntry.URI_TABLE :
                updatedCount = db.update(ExpenseEntry.TABLE_NAME,contentValues,s,strings);
                break;
            case ExpCatEntry.URI_TABLE:
                updatedCount = db.update(ExpCatEntry.TABLE_NAME,contentValues,s,strings);
                break;
            case ExpSubCatEntry.URI_TABLE:
                updatedCount=db.update(ExpSubCatEntry.TABLE_NAME,contentValues,s,strings);
                break;
            default:
                throw new IllegalArgumentException("NO matching URI's found balaji... : "+uri.toString());
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return updatedCount;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        int numInserted = 0;
        String table="";
        int uriType = uriMatcher.match(uri);
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (uriType) {
            case ExpenseEntry.URI_BULK_INSERT:
                table = ExpenseEntry.TABLE_NAME;
                break;
            case AssetEntry.URI_BULK_INSERT:
                table = AssetEntry.TABLE_NAME;
                break;
            case ExpCatEntry.URI_BULK_INSERT:
                table = ExpCatEntry.TABLE_NAME;
                break;
            case ExpSubCatEntry.URI_BULK_INSERT:
                table=ExpSubCatEntry.TABLE_NAME;
                break;
        }

        SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();
        sqlDB.beginTransaction();
        try {
            for (ContentValues cv : values) {
                long newID = sqlDB.insertOrThrow(table, null, cv);
                if (newID <= 0) {
                    throw new SQLException("Failed to insert row into " + uri);
                }
            }
            sqlDB.setTransactionSuccessful();
            getContext().getContentResolver().notifyChange(uri, null);
            numInserted = values.length;
        } finally {
            sqlDB.endTransaction();
        }
        return numInserted;
    }



}