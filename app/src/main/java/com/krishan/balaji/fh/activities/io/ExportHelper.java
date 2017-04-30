package com.krishan.balaji.fh.activities.io;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.krishan.balaji.fh.data.AssetEntry;
import com.krishan.balaji.fh.data.ExpCatEntry;
import com.krishan.balaji.fh.data.ExpSubCatEntry;
import com.krishan.balaji.fh.data.ExpenseEntry;
/*import com.krishan.balaji.fh.data.IncCatEntry;
import com.krishan.balaji.fh.data.IncSubCatEntry;
import com.krishan.balaji.fh.data.IncomeEntry;*/
import com.krishan.balaji.fh.model.AssetModel;
import com.krishan.balaji.fh.model.CategoryModel;
import com.krishan.balaji.fh.model.ExpIncModel;
import com.krishan.balaji.fh.model.SubCategoryModel;
import com.krishan.balaji.fh.util.Util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * Created by balaji142857 on 5/8/16.
 */
public class ExportHelper {

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    public static String doSomething(IOActivity activity){
        verifyStoragePermissions(activity);
        FileOutputStream os = null;
        File file = null;
        Workbook workbook;
        InputStream is;
        int rowCount;
        try {
            is = activity.getAssets().open("export_template.xls");
            workbook  = new HSSFWorkbook(is);
            Sheet templateSheet,assetSheet,expenseSheet,expenseCatSheet;//incomeSheet,incomeCatSheet;
            templateSheet = workbook.getSheetAt(0);
            ExcelStyleHelper styler = ExcelStyleHelper.getInstance(templateSheet,ExpIncModel.class);
            expenseSheet = workbook.cloneSheet(1);
            //incomeSheet = workbook.cloneSheet(1);
            assetSheet = workbook.cloneSheet(1);
            expenseCatSheet = workbook.cloneSheet(1);
            //incomeCatSheet= workbook.cloneSheet(1);


            ///export expense///
            rowCount= 1;
            workbook.setSheetName(workbook.getSheetIndex(expenseSheet), "expense");
            styler.createCell(expenseSheet, rowCount, 1, "S.No#");
            styler.createCell(expenseSheet, rowCount, 2, "Date");
            styler.createCell(expenseSheet, rowCount, 3, "Asset");
            styler.createCell(expenseSheet, rowCount, 4, "CategoryModel");
            styler.createCell(expenseSheet, rowCount, 5, "Sub CategoryModel");
            styler.createCell(expenseSheet, rowCount, 6, "Amount");
            styler.createCell(expenseSheet, rowCount, 7, "Comment");
            rowCount++;
            //TODO check if this works - query is actually hardcoded in contentProvider for this URI
            Cursor cursor = activity.getContentResolver().query(ExpenseEntry.CONTENT_URI,new String[]{},null,null,null);
            boolean anyRecord=cursor.moveToFirst();
            while(anyRecord){
                ExpIncModel record = getExpense(cursor);
                styler.createCell(expenseSheet, rowCount, 1, rowCount-1);
                styler.createCell(expenseSheet, rowCount, 2, record.getDate());
                styler.createCell(expenseSheet, rowCount, 3, record.getAsset());
                styler.createCell(expenseSheet, rowCount, 4, record.getCategory());
                if(record.getSubCategory()==null)
                    styler.createCell(expenseSheet, rowCount, 5, "");
                else
                    styler.createCell(expenseSheet, rowCount, 5, record.getSubCategory());
                styler.createCell(expenseSheet, rowCount, 6, record.getAmount());
                if(record.getComment()!=null)
                    styler.createCell(expenseSheet, rowCount, 7, record.getComment());
                else
                    styler.createCell(expenseSheet, rowCount, 7, "");
                rowCount++;
                anyRecord=cursor.moveToNext();
            }
            cursor.close();
            //styler.autoSizeColumns(expenseSheet);
            ///export expense end/////


            ///export income/////
            /*styler = ExcelStyleHelper.getInstance(templateSheet,ExpIncModel.class);
            workbook.setSheetName(workbook.getSheetIndex(incomeSheet), "income");
            rowCount= 1;
            styler.createCell(incomeSheet, rowCount, 1, "S.No#");
            styler.createCell(incomeSheet, rowCount, 2, "Date");
            styler.createCell(incomeSheet, rowCount, 3, "Asset");
            styler.createCell(incomeSheet, rowCount, 4, "CategoryModel");
            styler.createCell(incomeSheet, rowCount, 5, "Sub CategoryModel");
            styler.createCell(incomeSheet, rowCount, 6, "Amount");
            styler.createCell(incomeSheet, rowCount, 7, "Comment");
            rowCount++;
            //TODO check if this works - query is actually hardcoded in contentProvider for this URI
            cursor = activity.getContentResolver().query(IncomeEntry.CONTENT_URI,new String[]{},null,null,null);
            anyRecord=cursor.moveToFirst();
            while(anyRecord){
                ExpIncModel record = getIncome(cursor);
                styler.createCell(incomeSheet, rowCount, 1, rowCount-1);
                styler.createCell(incomeSheet, rowCount, 2, record.getDate());
                styler.createCell(incomeSheet, rowCount, 3, record.getAsset());
                styler.createCell(incomeSheet, rowCount, 4, record.getCategory());
                if(record.getSubCategory()==null)
                    styler.createCell(incomeSheet, rowCount, 5, "");
                else
                    styler.createCell(incomeSheet, rowCount, 5, record.getSubCategory());
                styler.createCell(incomeSheet, rowCount, 6, record.getAmount());
                if(record.getComment()!=null)
                    styler.createCell(incomeSheet, rowCount, 7, record.getComment());
                else
                    styler.createCell(incomeSheet, rowCount, 7, "");
                rowCount++;
                anyRecord=cursor.moveToNext();
            }
            cursor.close();*/
            //styler.autoSizeColumns(incomeSheet);
            ///export income end/////


            /// export assets//
            styler = ExcelStyleHelper.getInstance(templateSheet,AssetModel.class);
            workbook.setSheetName(workbook.getSheetIndex(assetSheet), "assets");
            rowCount= 1;
            styler.createCell(assetSheet, rowCount, 1, "S.No#");
            styler.createCell(assetSheet, rowCount, 2, "Asset Name");
            styler.createCell(assetSheet, rowCount, 3, "Balance");
            rowCount++;
            //TODO check if this works - query is actually hardcoded in contentProvider for this URI
            cursor = activity.getContentResolver().query(AssetEntry.CONTENT_URI,new String[]{},null,null,null);
            anyRecord=cursor.moveToFirst();
            while(anyRecord){
                AssetModel record = getAsset(cursor);
                styler.createCell(assetSheet, rowCount, 1, rowCount-1);
                styler.createCell(assetSheet, rowCount, 2, record.getName());
                styler.createCell(assetSheet, rowCount, 3, record.getBalance());
                rowCount++;
                anyRecord=cursor.moveToNext();
            }
            cursor.close();


            /// export expense categories///
            styler = ExcelStyleHelper.getInstance(templateSheet,CategoryModel.class);
            workbook.setSheetName(workbook.getSheetIndex(expenseCatSheet), "expense Categories");
            rowCount= 1;
            styler.createCell(expenseCatSheet, rowCount, 1, "Category");
            styler.createCell(expenseCatSheet, rowCount, 2, "Sub Category");
            rowCount++;


            Map<String,CategoryModel> expenseCategories = new HashMap<>();
            cursor = activity.getContentResolver().query(ExpCatEntry.CONTENT_URI,new String[]{ExpCatEntry.COL_ID,ExpCatEntry.COL_NAME},null,null,null);
            mapResultToExpenseCategories(expenseCategories,cursor,activity);
            cursor.close();


            for(Map.Entry<String, CategoryModel> s: expenseCategories.entrySet()){
                if(s.getValue().getSubcategories()!=null)
                    for (Map.Entry<String, SubCategoryModel> sub:s.getValue().getSubcategories().entrySet()){
                        styler.createCell(expenseCatSheet, rowCount, 1, s.getValue().getName());
                        styler.createCell(expenseCatSheet, rowCount, 2, sub.getValue().getName());
                        rowCount++;
                    }
            }
            //styler.autoSizeColumns(expenseCatSheet);
            /// export expense categories end///


            /// export income categories///
            /*styler = ExcelStyleHelper.getInstance(templateSheet,CategoryModel.class);
            workbook.setSheetName(workbook.getSheetIndex(incomeCatSheet), "income Categories");
            rowCount= 1;
            styler.createCell(incomeCatSheet, rowCount, 1, "Category");
            styler.createCell(incomeCatSheet, rowCount, 2, "Sub Category");
            rowCount++;

            Map<String,CategoryModel> incomeCategories = new HashMap<>();
            cursor = activity.getContentResolver().query(IncCatEntry.CONTENT_URI,new String[]{IncCatEntry.COL_ID,IncCatEntry.COL_NAME},null,null,null);
            mapResultToIncomeCategories(expenseCategories,cursor,activity);
            cursor.close();


            for(Map.Entry<String, CategoryModel> s: incomeCategories.entrySet()){
                if(s.getValue().getSubcategories()!=null)
                    for (Map.Entry<String, SubCategoryModel> sub:s.getValue().getSubcategories().entrySet()){
                        styler.createCell(incomeCatSheet, rowCount, 1, s.getValue().getName());
                        styler.createCell(incomeCatSheet, rowCount, 2, sub.getValue().getName());
                        rowCount++;
                    }
            }*/
            /// export income categories end///



            workbook.removeSheetAt(0);
            workbook.removeSheetAt(0);
            File rootDir,appDir,exportDir;
            Calendar cal = Calendar.getInstance();
            String datePart = Util.exportDirFormat.format(cal.getTime());
            String timePart = Util.exportTimeFormat.format(cal.getTime());
            String fileName = "export_"+datePart+"_"+timePart+".xls";
            rootDir = android.os.Environment.getExternalStorageDirectory();
            appDir = new File (rootDir.getAbsolutePath() + "/financeHelper");
            if(!appDir.exists())
                appDir.mkdirs();
            exportDir = new File(appDir.getAbsolutePath(),datePart);
            if(!exportDir.exists())
                exportDir.mkdir();
            file = new File(exportDir.getAbsolutePath(),fileName);
            file.createNewFile();
            os = new FileOutputStream(file);
            workbook.write(os);
            os.close();
        } catch (IOException e) {
            Log.e("",e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            Log.e("",e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (null != os)
                    os.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return file.getName();
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    private static ExpIncModel getExpense(Cursor cursor) {
        ExpIncModel expense = new ExpIncModel();
        expense.setAmount(cursor.getFloat(cursor.getColumnIndex(ExpenseEntry.COL_AMOUNT)));
        expense.setComment(cursor.getString(cursor.getColumnIndex(ExpenseEntry.COL_COMMENT)));
        String date = cursor.getString(cursor.getColumnIndex(ExpenseEntry.COL_DATE));
        try {
            expense.setDate(Util.displayFormat.format(Util.dbFormat.parse(date)));
        } catch (ParseException e) {
            expense.setDate(date);
            e.printStackTrace();
        }
        expense.setCategory(cursor.getString(cursor.getColumnIndex(ExpCatEntry.COL_NAME)));
        expense.setSubCategory(cursor.getString(cursor.getColumnIndex(ExpSubCatEntry.COL_NAME)));
        expense.setAsset(cursor.getString(cursor.getColumnIndex(AssetEntry.COL_NAME)));
        return expense;
    }

   /* private static ExpIncModel getIncome(Cursor cursor) {
        ExpIncModel income = new ExpIncModel();
        income.setAmount(cursor.getFloat(cursor.getColumnIndex(IncomeEntry.COL_AMOUNT)));
        income.setComment(cursor.getString(cursor.getColumnIndex(IncomeEntry.COL_COMMENT)));
        String date = cursor.getString(cursor.getColumnIndex(IncomeEntry.COL_DATE));
        try {
            income.setDate(Util.displayFormat.format(Util.dbFormat.parse(date)));
        } catch (ParseException e) {
            income.setDate(date);
            e.printStackTrace();
        }
        income.setCategory(cursor.getString(cursor.getColumnIndex(IncCatEntry.COL_NAME)));
        income.setSubCategory(cursor.getString(cursor.getColumnIndex(IncSubCatEntry.COL_NAME)));
        income.setAsset(cursor.getString(cursor.getColumnIndex(AssetEntry.COL_NAME)));
        return income;
    }*/

    private static AssetModel getAsset(Cursor cursor) {
        AssetModel asset = new AssetModel();
        asset.setBalance(cursor.getFloat(cursor.getColumnIndex(AssetEntry.COL_BALANCE)));
        asset.setName(cursor.getString(cursor.getColumnIndex(AssetEntry.COL_NAME)));
        return asset;
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

   /* private static void mapResultToIncomeCategories(Map<String, CategoryModel> expenseCategories, Cursor cursor,Activity activity) {
        boolean anyRecord=cursor.moveToFirst();
        while(anyRecord){
            CategoryModel model = new CategoryModel();
            model.setId(cursor.getInt(cursor.getColumnIndex(IncCatEntry.COL_ID)));
            model.setName(cursor.getString(cursor.getColumnIndex(IncCatEntry.COL_NAME)));

            Cursor subCursor= activity.getContentResolver().query(IncSubCatEntry.CONTENT_URI,new String[]{ExpSubCatEntry.COL_ID,ExpSubCatEntry.COL_CAT_ID,IncSubCatEntry.COL_NAME},IncSubCatEntry.COL_CAT_ID+" = ? ",new String[]{model.getId()+""},null);
            boolean hasRecord=subCursor.moveToFirst();
            while(hasRecord){
                int id = subCursor.getInt(subCursor.getColumnIndex(IncSubCatEntry.COL_CAT_ID));
                if(model.getSubcategories() == null)
                    model.setSubcategories(new HashMap<String,SubCategoryModel>());
                SubCategoryModel subModel = new SubCategoryModel();
                subModel.setId(subCursor.getInt(subCursor.getColumnIndex(IncSubCatEntry.COL_ID)));
                subModel.setName(subCursor.getString(subCursor.getColumnIndex(IncSubCatEntry.COL_NAME)));
                model.getSubcategories().put(subModel.getName(),subModel);
                //map.put(model.getName(),model);
                hasRecord=subCursor.moveToNext();
            }
            subCursor.close();

            //map.put(model.getName(),model);
            expenseCategories.put(model.getName(),model);
            anyRecord=cursor.moveToNext();
        }
    }*/

}
