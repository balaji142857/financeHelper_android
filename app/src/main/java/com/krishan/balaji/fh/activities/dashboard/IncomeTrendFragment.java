/*
package com.krishan.balaji.fh.activities.dashboard;


import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.krishan.balaji.fh.R;
import com.krishan.balaji.fh.data.ExpCatEntry;
import com.krishan.balaji.fh.data.ExpenseEntry;
import com.krishan.balaji.fh.util.Util;

import java.text.ParseException;

public class IncomeTrendFragment extends Fragment{

    //TextView tv;
    WebView webView;
    public IncomeTrendFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_one, container, false);;
        String customHtml = Util.htmlHeader+Util.getExpenseByMonth(getActivity())+Util.htmlClosure;
        webView = (WebView) view.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.loadDataWithBaseURL("file:///android_asset/", customHtml, "text/html", "utf-8", "");
        return view;
    }

    private String loadLastTenDaysExpenses() {
        Uri graph = Uri.withAppendedPath(ExpenseEntry.CONTENT_URI,"15");
        Cursor cursor=getActivity().getContentResolver().query(Uri.withAppendedPath(graph,"sum"),new String[]{"t2."+ ExpCatEntry.COL_NAME,"t1."+ExpenseEntry.COL_AMOUNT},null,null,null);
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
                data.append(Util.displayFormat.format(Util.dbFormat.parse(cursor.getString(0))))
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

}
*/
