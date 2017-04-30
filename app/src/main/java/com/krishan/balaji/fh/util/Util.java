package com.krishan.balaji.fh.util;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;

import com.krishan.balaji.fh.data.AssetEntry;
import com.krishan.balaji.fh.data.ExpCatEntry;
import com.krishan.balaji.fh.data.ExpenseEntry;
/*import com.krishan.balaji.fh.data.IncCatEntry;
import com.krishan.balaji.fh.data.IncomeEntry;*/

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Util {

    public static final boolean allowNegativeBalance =false;
    public static DateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static DateFormat displayFormat = new SimpleDateFormat("dd-MMM-yyyy");
    public static DateFormat shortDisplayFormat = new SimpleDateFormat("dd-MMM");
    public static DateFormat exportDirFormat = new SimpleDateFormat("yyyyMMdd");
    public static DateFormat exportTimeFormat = new SimpleDateFormat("HHmmss");

    public static final String rupeeSymbol ="₹";
    //Loader Keys
    public static final int ViewAssetActivity_asset_loader_key = 1;
    public static final int NewExpenseActivity_category_loader_key = 2;
    public static final int NewExpenseActivity_subCategory_loader_key= 3;
    public static final int NewExpenseActivity_asset_loader_key = 4;
    public static final int ViewExpenseActivity_expense_loader_key=5;
    public static final int EditExpenseActivity_category_loader_key = 6;
    public static final int EditExpenseActivity_subCategory_loader_key= 7;
    public static final int EditExpenseActivity_asset_loader_key = 8;
    public static final int ExpenseCatActivity_catCallBacksKey =10;
    public static final int TrasnferAssetActivity_fromAssetCallBacksKey=11;
    public static final int ExpenseSubCatActivity_loader_key=20;
    public static final int ViewTrasnferActivity_assetCallBacksKey=200;


    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    //http://stackoverflow.com/questions/9357668/how-to-store-image-in-sqlite-database
    public static String convertToBase64(Bitmap bitmap) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,os);
        byte[] byteArray = os.toByteArray();
        return Base64.encodeToString(byteArray, 0);
    }

    public static Bitmap convertToBitmap(String base64String) {
        byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
        Bitmap bitmapResult = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return bitmapResult;
    }


    public static final String htmlHeader = "<!DOCTYPE html><html ng-app=\"scotchApp\"><head><meta charset=\"UTF-8\">"+
            "<link rel=\"stylesheet\" href=\"bootstrap-3.3.4/css/bootstrap.min.css\"></link>"+
            "<script type=\"text/javascript\" src=\"js/FusionCharts.js\"></script>"+
            "<script type=\"text/javascript\" src=\"js/fusioncharts.charts.js\"></script>"+
            "</head><body ng-controller=\"mainController\">";
    public static final String htmlClosure="<script type=\"text/javascript\" src=\"js/fusioncharts.charts.js\"></script>"+
            "<script type=\"text/javascript\" src=\"js/jquery.min.js\"></script>"+
            "<script type=\"text/javascript\" src=\"bootstrap-3.3.4/js/bootstrap.min.js\"></script>"+
            "<script type=\"text/javascript\" src=\"js/angular.js\"></script>" +
            "<script type=\"text/javascript\" src=\"js/angular-fusioncharts.min.js\"></script>"+
            "<script type=\"text/javascript\" src=\"js/app.js\"></script></body></html>";



    public static String getTwoWeekExpense(Activity activity){
        String twoWeekExpense ="<div id=\"chart-container4\">ExpenseTrend will load here</div>";
        twoWeekExpense+="<script>FusionCharts.ready(function () {var visitChart = new FusionCharts({type: 'line',renderAt: 'chart-container4',width: '550',height: '250',dataFormat: 'json',dataSource: {";
        twoWeekExpense+="\"chart\": {\"caption\": \"Expense Trend\",\"subCaption\": \"15 days\",\"xAxisName\": \"Day\",\"yAxisName\": \"Amount spent\",\"lineThickness\" : \"2\",\"paletteColors\" : \"#0075c2\",\"baseFontColor\" : \"#333333\",\"baseFont\" : \"Helvetica Neue,Arial\",";
        twoWeekExpense+="\"captionFontSize\" : \"14\",\"subcaptionFontSize\" : \"14\",\"subcaptionFontBold\" : \"0\",\"showBorder\" : \"0\",\"bgColor\" : \"#ffffff\",\"showShadow\" : \"0\",\"canvasBgColor\" : \"#ffffff\",\"canvasBorderAlpha\" : \"0\",";
        twoWeekExpense+="\"divlineAlpha\" : \"100\",\"divlineColor\" : \"#999999\",\"divlineThickness\" : \"1\",\"divLineIsDashed\" : \"1\",\"divLineDashLen\" : \"1\",\"divLineGapLen\" : \"1\",\"showXAxisLine\" : \"1\",\"xAxisLineThickness\" : \"1\",";
        twoWeekExpense+="\"xAxisLineColor\" : \"#999999\",\"showAlternateHGridColor\" : \"0\",},";
        twoWeekExpense+="\"data\": "+loadLastTenDaysExpenses(activity);
        twoWeekExpense+="}});visitChart.render();});</script>";
        return twoWeekExpense;
    }


    private static String loadLastTenDaysExpenses(Activity activity) {
        Uri graph = Uri.withAppendedPath(ExpenseEntry.CONTENT_URI,"15");
        Cursor cursor=activity.getContentResolver().query(Uri.withAppendedPath(graph,"sum"),new String[]{"t2."+ ExpCatEntry.COL_NAME,"t1."+ExpenseEntry.COL_AMOUNT},null,null,null);
        boolean hasRecords = cursor.moveToFirst();
        StringBuilder data = new StringBuilder("[");
        int count =0;
        while(hasRecords){
            for(String name:cursor.getColumnNames())
                Log.e("fasdf",name);
            if(count==0)
                data.append("{\"label\":'");
            else
                data.append(",{\"label\":'");
            //TODO whyhardcode the index
            try {
                data.append(Util.shortDisplayFormat.format(Util.dbFormat.parse(cursor.getString(0))))
                        .append("', \"value\":")
                        .append(cursor.getFloat(1))
                        .append("}");
            } catch (ParseException e) {
                data.append(cursor.getString(0))
                        .append("', \"value\":")
                        .append(cursor.getFloat(1))
                        .append("}");
                e.printStackTrace();
            }
            hasRecords=cursor.moveToNext();
            count++;
        }
        cursor.close();
        data.append("]");
        return data.toString();
    }

    public static String getAssetInfo(Activity activity){
        String assetContent="<div id=\"chart-container2\">Assets data will get loaded here</div>"+
                "<script>FusionCharts.ready(function () {var demographicsChart = new FusionCharts({type: 'column3d',renderAt: 'chart-container2',width: '600',height: '250',dataFormat: 'json',dataSource: {"+
                "\"chart\": {\"subCaption\": \"Assets\","+
                "\"showLabels\": \"1\","+
                "\"showLegend\": \"0\",\"showcanvasbg\" : \"0\","+
                "\"bgColor\": \"#ffffff\",\"showcanvasbg\" : \"0\","+
                "\"showPercentValues\": \"0\",\"legendPosition\": \"RIGHT\","+
                "\"showPercentInTooltip\": \"1\"},"+
                "\"data\": "+loadAssets(activity)+"}});"+
                "demographicsChart.render();});</script>";
        return  assetContent;
    }

    private static String loadAssets(Activity activity) {
        //Uri graph = Uri.withAppendedPath(AssetEntry.CONTENT_URI,"graph");
        //Uri.withAppendedPath(graph,"balance")
        Cursor cursor=activity.getContentResolver().query(AssetEntry.CONTENT_URI,new String[]{AssetEntry.COL_NAME,AssetEntry.COL_BALANCE},null,null,null);
        boolean hasRecords = cursor.moveToFirst();
        StringBuilder data = new StringBuilder("[");
        int count =0;
        while(hasRecords){
            for(String name:cursor.getColumnNames())
                Log.e("fasdf",name);
            if(count==0)
                data.append("{\"label\":'");
            else
                data.append(",{\"label\":'");
            //TODO whyhardcode the index
            data.append(cursor.getString(0))
                    .append("', \"value\":")
                    .append(cursor.getFloat(1))
                    .append("}");
            hasRecords=cursor.moveToNext();
            count++;
        }
        cursor.close();
        data.append("]");
        return data.toString();
    }

    public static String getAssetUsageInfo(Activity activity){
        String assetContent="<div id=\"chart-container2\">Assets data will get loaded here</div>"+
                "<script>FusionCharts.ready(function () {var demographicsChart = new FusionCharts({type: 'column3d',renderAt: 'chart-container2',width: '600',height: '250',dataFormat: 'json',dataSource: {"+
                "\"chart\": {\"subCaption\": \"Assets Usage\","+
                "\"showLabels\": \"1\","+
                "\"showLegend\": \"0\",\"showcanvasbg\" : \"0\","+
                "\"bgColor\": \"#ffffff\",\"showcanvasbg\" : \"0\","+
                "\"showPercentValues\": \"0\",\"legendPosition\": \"RIGHT\","+
                "\"showPercentInTooltip\": \"1\"},"+
                "\"data\": "+loadAssetsUsage(activity)+"}});"+
                "demographicsChart.render();});</script>";
        return  assetContent;
    }


    private static String loadAssetsUsage(Activity activity) {
        Uri graph = Uri.withAppendedPath(ExpenseEntry.CONTENT_URI,ExpenseEntry.path_asset_usage);
        //TODO
        Cursor cursor=activity.getContentResolver().query(graph,new String[]{"t2."+ AssetEntry.COL_NAME,"t1."+ExpenseEntry.COL_AMOUNT},null,null,null);
        boolean hasRecords = cursor.moveToFirst();
        StringBuilder data = new StringBuilder("[");
        int count =0;
        while(hasRecords){
            if(count==0)
            for(String name:cursor.getColumnNames())
                Log.e("fasdf",name);
            if(count==0)
                data.append("{\"label\":'");
            else
                data.append(",{\"label\":'");
            //TODO whyhardcode the index
            data.append(cursor.getString(0))
                    .append("', \"value\":")
                    .append(cursor.getFloat(1))
                    .append("}");
            hasRecords=cursor.moveToNext();
            count++;
        }
        cursor.close();
        data.append("]");
        return data.toString();
    }

    public static String getExpenseCategoriesInfo(Activity activity){
        String expenseContent="<div id=\"chart-container1\">Expenses</div>"+
                "<script>FusionCharts.ready(function () {var demographicsChart = new FusionCharts({type: 'doughnut3d',renderAt: 'chart-container1',width: '600',height: '250',dataFormat: 'json',dataSource: {"+
                "\"chart\": {\"subCaption\": \"Expenses\","+
                "\"startingAngle\": \"120\",\"showLabels\": \"1\","+
                "\"showLegend\": \"1\",\"showcanvasbg\" : \"0\","+
                "\"legendPosition\": \"right\","+
                "\"bgColor\": \"#ffffff\","+
                "\"enableMultiSlicing\": \"0\",\"slicingDistance\": \"15\","+
                "\"showPercentValues\": \"0\","+
                "\"showPercentInTooltip\": \"1\"},"+
                "\"data\": "+loadExpenseCategories(activity)+"}});"+
                "demographicsChart.render();});</script>";
        return expenseContent;
    }

    private static String loadExpenseCategories(Activity activity) {

        Uri graph = Uri.withAppendedPath(ExpenseEntry.CONTENT_URI,ExpenseEntry.path_graph_categories_split);
        Cursor cursor=activity.getContentResolver().query(graph,new String[]{"t2."+ ExpCatEntry.COL_NAME,"t1."+ExpenseEntry.COL_AMOUNT},null,null,null);
        boolean hasRecords = cursor.moveToFirst();
        StringBuilder data = new StringBuilder("[");
        int count =0;
        while(hasRecords){
            for(String name:cursor.getColumnNames())
                Log.e("fasdf",name);
            if(count==0)
                data.append("{\"label\":'");
            else
                data.append(",{\"label\":'");
            //TODO whyhardcode the index
            data.append(cursor.getString(0))
                    .append("', \"value\":")
                    .append(cursor.getFloat(1))
                    .append("}");
            hasRecords=cursor.moveToNext();
            count++;
        }
        cursor.close();
        data.append("]");
        return data.toString();
    }

}