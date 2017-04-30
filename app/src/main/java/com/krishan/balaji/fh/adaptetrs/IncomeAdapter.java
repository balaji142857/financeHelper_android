/*
package com.krishan.balaji.fh.adaptetrs;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.krishan.balaji.fh.R;
import com.krishan.balaji.fh.data.IncSubCatEntry;
import com.krishan.balaji.fh.data.IncomeEntry;
import com.krishan.balaji.fh.util.Util;

import java.text.ParseException;

public class IncomeAdapter extends SimpleCursorAdapter {

    public IncomeAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);
        ((TextView)view.findViewById(R.id.expense_amount)).setText(cursor.getFloat(cursor.getColumnIndex(IncomeEntry.COL_AMOUNT))+" "+Util.rupeeSymbol);
        String base64Image = cursor.getString(cursor.getColumnIndex( IncSubCatEntry.COL_IMG));
        if(base64Image != null){
            Bitmap bitmap = Util.convertToBitmap(base64Image);
            Drawable d = new BitmapDrawable(mContext.getResources(), bitmap);
            ((ImageView)view.findViewById(R.id.expense_category_img)).setImageDrawable(d);
        }
        TextView dateView = (TextView) view.findViewById(R.id.expense_date);
        try {
            dateView.setText(Util.displayFormat.format(Util.dbFormat.parse(cursor.getString(cursor.getColumnIndex(IncomeEntry.COL_DATE)))));;
        } catch (ParseException e) {
            dateView.setText(cursor.getString(cursor.getColumnIndex(IncomeEntry.COL_DATE)));
            e.printStackTrace();
        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return super.newView(context, cursor, parent);
    }
}*/
