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

import java.text.ParseException;

public class ViewAssetAdapter extends SimpleCursorAdapter{

    private Context context;
    private Context appContext;
    private int layout;
    private Cursor cr;
    private final LayoutInflater inflater;


    public ViewAssetAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);
        this.layout=layout;
        this.context = context;
        this.inflater=LayoutInflater.from(context);
        this.cr=c;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);
        TextView name=(TextView)view.findViewById(R.id.asset_name);
        TextView balance =(TextView)view.findViewById(R.id.asset_balance);
        name.setText(cursor.getString(cursor.getColumnIndex(AssetEntry.COL_NAME)));
        balance.setText(cursor.getFloat(cursor.getColumnIndex(AssetEntry.COL_BALANCE))+ " " +Util.rupeeSymbol);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(layout, null);
    }


}
