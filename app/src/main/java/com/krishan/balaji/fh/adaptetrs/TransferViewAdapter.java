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
import com.krishan.balaji.fh.data.TransferEntry;
import com.krishan.balaji.fh.util.Util;

import java.text.ParseException;

public class TransferViewAdapter extends SimpleCursorAdapter{

    private Context context;
    private Context appContext;
    private int layout;
    private Cursor cr;
    private final LayoutInflater inflater;


    public TransferViewAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);
        this.layout=layout;
        this.context = context;
        this.inflater=LayoutInflater.from(context);
        this.cr=c;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);
        int[] to = {R.id.asset_from,R.id.asset_to,R.id.transfer_amount,R.id.transfer_date,R.id.transfer_comment};
        ((TextView)view.findViewById(R.id.transfer_amount)).setText(cursor.getFloat(cursor.getColumnIndex(TransferEntry.COL_AMOUNT))+" "+Util.rupeeSymbol);
        TextView dateView = ((TextView)view.findViewById(R.id.transfer_date));
        String date = cursor.getString(cursor.getColumnIndex(TransferEntry.COL_DATETIME));
        //((TextView)view.findViewById(R.id.asset_from)).setText(cursor.getString(cursor.getColumnIndex("t2."+AssetEntry.COL_NAME)));
        ((TextView)view.findViewById(R.id.asset_from)).setText(cursor.getString(cursor.getColumnIndex("frmAsset")));
        ((TextView)view.findViewById(R.id.asset_to)).setText(cursor.getString(cursor.getColumnIndex(AssetEntry.COL_NAME)));
        try {
            dateView.setText(Util.displayFormat.format(Util.dbFormat.parse(date)));;
        } catch (ParseException e) {
            dateView.setText(date);
            e.printStackTrace();
        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(layout, null);
    }


}
