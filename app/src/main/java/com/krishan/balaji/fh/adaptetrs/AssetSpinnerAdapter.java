package com.krishan.balaji.fh.adaptetrs;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.krishan.balaji.fh.R;
import com.krishan.balaji.fh.data.AssetEntry;
import com.krishan.balaji.fh.util.Util;

/**
 * Created by balaji142857 on 31/7/16.
 */
public class AssetSpinnerAdapter extends SimpleCursorAdapter {

    private Context context;
    private Context appContext;
    private int layout;
    private Cursor cr;
    private final LayoutInflater inflater;


    public AssetSpinnerAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);
        this.layout=layout;
        this.context = context;
        this.inflater=LayoutInflater.from(context);
        this.cr=c;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);
        TextView name=(TextView)view.findViewById(android.R.id.text1);
        String nameValue = cursor.getString(cursor.getColumnIndex(AssetEntry.COL_NAME));
        String balanceAmount=cursor.getFloat(cursor.getColumnIndex(AssetEntry.COL_BALANCE))+ " " + Util.rupeeSymbol;
        name.setText(nameValue+" ( "+balanceAmount+" )");
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(layout, null);
    }
}