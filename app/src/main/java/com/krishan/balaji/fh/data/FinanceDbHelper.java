package com.krishan.balaji.fh.data;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.krishan.balaji.fh.R;
import com.krishan.balaji.fh.util.Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class FinanceDbHelper extends SQLiteOpenHelper{

    private static final String DB_NAME ="fh_db";
    private static final int DB_VERSION = 1;
    Context context;

    public FinanceDbHelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);
        this.context=context;
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Map<String,Boolean> test = new HashMap<>();
        sqLiteDatabase.execSQL("PRAGMA foreign_keys=ON;");
        //TODO call the create table statements here
        sqLiteDatabase.execSQL(AssetEntry.CREATE_TABLE);

        sqLiteDatabase.execSQL(ExpCatEntry.CREATE_TABLE);
        sqLiteDatabase.execSQL(ExpSubCatEntry.CREATE_TABLE);
        sqLiteDatabase.execSQL(ExpenseEntry.CREATE_TABLE);
        sqLiteDatabase.execSQL(TransferEntry.CREATE_TABLE);

        //sqLiteDatabase.execSQL("INSERT INTO "+IncCatEntry.TABLE_NAME+"("+IncCatEntry.COL_NAME+") VALUES ('SALARY');");
        /*File path = new File("/storage/sdcard/finance");//context.getFilesDir();
        File file = new File(path, "queries.sql");
        Log.e("filepath",file.getAbsolutePath());*/

        //StringBuilder queries = new StringBuilder();
        final R.drawable drawableResources = new R.drawable();
        final Class<R.drawable> c = R.drawable.class;
        final Field[] fields = c.getDeclaredFields();
        FileOutputStream stream = null;
        Set<String> unknown = new HashSet<>();
        Map<String,Integer> catIds = new HashMap<>();
        int catCount = 1;
        for (int i = 0, max = fields.length; i < max; i++) {
            String fileName =fields[i].getName();
            if(fileName.startsWith("x")){
                int resourceId;
                try {
                    resourceId = fields[i].getInt(drawableResources);
                    String cat = fileName.substring(fileName.indexOf("_") + 1, fileName.lastIndexOf("_"));
                    Bitmap icon = BitmapFactory.decodeResource(context.getResources(), resourceId);
                    String s = "INSERT INTO " + ExpCatEntry.TABLE_NAME + "(" + ExpCatEntry.COL_NAME + "," + ExpCatEntry.COL_IMG + ") VALUES('" + cat.toUpperCase() + "','" + Util.convertToBase64(icon) + "'); ";
                    Log.e("dfads", s);
                    sqLiteDatabase.execSQL(s);
                   /* if (cat.equalsIgnoreCase("travel"))
                        catIds.put("tx", catCount);
                    else*/
                        catIds.put(cat.toLowerCase(), catCount);
                    catCount++;
                }catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
            }
        }
        for (int i = 0, max = fields.length; i < max; i++) {
            String fileName =fields[i].getName();
            Log.e("dbHelper###"," "+fileName);
            if((fileName.startsWith("ic_") || fileName.startsWith("inc_")) && !fileName.contains("ic_coin"))
            {
                Log.e("dbHelper#####"," "+fileName);
                int resourceId;
                try {
                    resourceId = fields[i].getInt(drawableResources);
                    String cat = fileName.substring(fileName.indexOf("_") + 1, fileName.lastIndexOf("_"));
                    //int catCount = 1;
                        String subcatName = fileName.substring(fileName.lastIndexOf("_") + 1);
                        subcatName = subcatName.replaceAll("3", " ");
                        subcatName = subcatName.replaceAll("1", "(");
                        fileName = subcatName.replaceAll("2", ")");
                        int catid = 1;
                    //Log.e("afsdf","cat is "+cat+", catid is "+catIds.get(cat));

                        if (cat.equalsIgnoreCase("salary"))
                            catid=21;
                        if (catid < 20) {
                            //catIds.get(cat);
                            Bitmap icon = BitmapFactory.decodeResource(context.getResources(), resourceId);
                            fileName = fileName.substring(fileName.lastIndexOf("_") + 1);
                            //String s = "INSERT INTO " + ExpSubCatEntry.TABLE_NAME + "(" + ExpSubCatEntry.COL_CAT_ID + "," + ExpSubCatEntry.COL_NAME + "," + ExpSubCatEntry.COL_IMG + ") VALUES(" + catid + ", '" + fileName + "','" + Util.convertToBase64(icon) + "'); ";
                            String s = "INSERT INTO " + ExpSubCatEntry.TABLE_NAME + "(" + ExpSubCatEntry.COL_CAT_ID + "," + ExpSubCatEntry.COL_NAME + "," + ExpSubCatEntry.COL_IMG + ") VALUES(" + catIds.get(cat) + ", '" + fileName + "','" + Util.convertToBase64(icon) + "'); ";
                            sqLiteDatabase.execSQL(s);
                        } /*else if (catid > 20) {
                            catid -= 20;
                            Bitmap icon = BitmapFactory.decodeResource(context.getResources(), resourceId);
                            fileName = fileName.substring(fileName.lastIndexOf("_") + 1);
                            String s = "INSERT INTO " + IncSubCatEntry.TABLE_NAME + "(" + IncSubCatEntry.COL_CAT_ID + "," + IncSubCatEntry.COL_NAME + "," + IncSubCatEntry.COL_IMG + ") VALUES(" + catid + ", '" + fileName + "','" + Util.convertToBase64(icon) + "'); ";
                            sqLiteDatabase.execSQL(s);
                        }*/
                    //queries.append(s);
                } catch (Exception e) {

                    Log.e("aslkfdsad","exception occured, fileNamee is "+fileName);
                    e.printStackTrace();
                    continue;
                }
            }
        }
        /*for (String un : unknown)
            Log.e("wasting time on", un);
        for(String a : catIds.keySet())
            Log.e("xxxxxxxxxxxx",a+" value is "+catIds.get(a));*/
      /*  try {
            stream = new FileOutputStream(file);
            stream.write(queries.toString().getBytes());
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}
