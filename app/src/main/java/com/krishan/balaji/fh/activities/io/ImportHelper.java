package com.krishan.balaji.fh.activities.io;


import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;


import com.krishan.balaji.fh.data.AssetEntry;
import com.krishan.balaji.fh.data.ExpCatEntry;
import com.krishan.balaji.fh.data.ExpSubCatEntry;
import com.krishan.balaji.fh.data.ExpenseEntry;
import com.krishan.balaji.fh.model.AssetModel;
import com.krishan.balaji.fh.model.CategoryModel;
import com.krishan.balaji.fh.model.ExpIncModel;
import com.krishan.balaji.fh.model.SubCategoryModel;
import com.krishan.balaji.fh.util.Util;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.prefs.Preferences;


public class ImportHelper {


    public static void doSomething(Activity activity,String fileName) {
        InputStream is = null;
        try {
            is = new FileInputStream(new File(fileName));
            HSSFWorkbook workbook = new HSSFWorkbook(is);
            HSSFSheet sheet;
            Iterator<Row> rowIterator;
            int rowCount = 0;
            ContentValues[] values = null;
            ContentValues value;
            Uri returnUri = null;

            Map<String,AssetModel> assetModelSet  = new HashMap<>();
            Cursor cursor = activity.getContentResolver().query(AssetEntry.CONTENT_URI,new String[]{
                    AssetEntry.COL_ID,
                    AssetEntry.COL_NAME
            },
                    null,null,null);
            mapResultToAssets(assetModelSet,cursor);
            cursor.close();
            System.out.println(assetModelSet);

            for (String key : assetModelSet.keySet()) {
                Log.e("afdx",key);
            }

            Map<String,CategoryModel> expenseCategories = new HashMap<>();
            cursor = activity.getContentResolver().query(ExpCatEntry.CONTENT_URI,new String[]{ExpCatEntry.COL_ID,ExpCatEntry.COL_NAME},null,null,null);
            mapResultToExpenseCategories(expenseCategories,cursor,activity);
            cursor.close();


            for (String key : expenseCategories.keySet()) {
                Log.e("afdx",key);
            }
            Set<ExpIncModel> expenses = new HashSet<>();
            sheet = workbook.getSheet("expense");
            rowIterator = sheet.iterator();
            rowCount = 0;
            while(rowIterator.hasNext()) {
                Row row = rowIterator.next();
                rowCount++;
                if(rowCount<2)
                    continue;
                ExpIncModel model  = new ExpIncModel();
                String date = row.getCell(2).getStringCellValue();
                Log.e("woiuper",date);
                try {
                    model.setDate(Util.dbFormat.format(Util.displayFormat.parse(date)));;
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                model.setComment(row.getCell(7).getStringCellValue());
                model.setAmount((float)row.getCell(6).getNumericCellValue());
                String asset =row.getCell(3).getStringCellValue();//asset
                model.setAsset(assetModelSet.get(asset).getId()+"");
                String cat = row.getCell(4).getStringCellValue();
                model.setCategory(expenseCategories.get(cat).getId()+"");
                String subCat = row.getCell(5).getStringCellValue();
                model.setSubCategory(expenseCategories.get(cat).getSubcategories().get(subCat).getId()+"");
                Log.e("expCat",model.getDate()+" "+asset+" "+subCat);
                expenses.add(model);

            }
            values = new ContentValues[expenses.size()];
            int recCount =0;
            for(ExpIncModel a : expenses){
                value = new ContentValues();
                value.put(ExpenseEntry.COL_AMOUNT,a.getAmount());
                value.put(ExpenseEntry.COL_ASSET,a.getAsset());
                value.put(ExpenseEntry.COL_DATE,a.getDate());
                value.put(ExpenseEntry.COL_CAT,a.getCategory());
                value.put(ExpenseEntry.COL_COMMENT,a.getComment());
                value.put(ExpenseEntry.COL_SUBCAT,a.getSubCategory());
                values[recCount]=value;
                recCount++;
            }
            System.out.println(values);
            Uri bulkInserUri = Uri.withAppendedPath(ExpenseEntry.CONTENT_URI,ExpenseEntry.path_insert_bulk);
            Log.e("xvzcv",bulkInserUri.toString());
            activity.getContentResolver().bulkInsert(bulkInserUri,values);
            is.close();
        } catch (FileNotFoundException e) {
            Log.e("zxcvzvxce",e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("eezxcvzvxce",e.getMessage());
            e.printStackTrace();
        }
    }

    private static void mapResultToExpenseCategories(Map<String, CategoryModel> expenseCategories, Cursor cursor,Activity activity) {
        boolean anyRecord=cursor.moveToFirst();
        while(anyRecord){
            CategoryModel model = new CategoryModel();
            model.setId(cursor.getInt(cursor.getColumnIndex(ExpCatEntry.COL_ID)));
            model.setName(cursor.getString(cursor.getColumnIndex(ExpCatEntry.COL_NAME)));

            Cursor subCursor= activity.getContentResolver().query(ExpSubCatEntry.CONTENT_URI,new String[]{ExpSubCatEntry.COL_ID,ExpSubCatEntry.COL_CAT_ID,ExpSubCatEntry.COL_NAME},ExpSubCatEntry.COL_CAT_ID+" = ? ",new String[]{model.getId()+""},null);
            boolean hasRecord=subCursor.moveToFirst();
            while(hasRecord){
                int id = subCursor.getInt(subCursor.getColumnIndex(ExpSubCatEntry.COL_CAT_ID));
                if(model.getSubcategories() == null)
                    model.setSubcategories(new HashMap<String,SubCategoryModel>());
                SubCategoryModel subModel = new SubCategoryModel();
                subModel.setId(subCursor.getInt(subCursor.getColumnIndex(ExpSubCatEntry.COL_ID)));
                subModel.setName(subCursor.getString(subCursor.getColumnIndex(ExpSubCatEntry.COL_NAME)));
                model.getSubcategories().put(subModel.getName(),subModel);
                //map.put(model.getName(),model);
                hasRecord=subCursor.moveToNext();
            }
            subCursor.close();

            //map.put(model.getName(),model);
            expenseCategories.put(model.getName(),model);
            anyRecord=cursor.moveToNext();
        }
    }

    private static void mapResultToAssets(Map<String, AssetModel> assetModelSet, Cursor cursor) {
        boolean hasRecords = cursor.moveToFirst();
        while(hasRecords){
            AssetModel asset = new AssetModel();
            asset.setName(cursor.getString(cursor.getColumnIndex(AssetEntry.COL_NAME)));
            asset.setId(cursor.getInt(cursor.getColumnIndex(AssetEntry.COL_ID)));
            assetModelSet.put(asset.getName(),asset);
            hasRecords=cursor.moveToNext();
        }
    }

}