package com.krishan.balaji.fh.adaptetrs;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.krishan.balaji.fh.R;
import com.krishan.balaji.fh.data.ExpSubCatEntry;
import com.krishan.balaji.fh.data.ExpenseEntry;
import com.krishan.balaji.fh.util.Util;

import org.w3c.dom.Text;

import java.text.ParseException;

public class ExpenseSubCatAdapter extends SimpleCursorAdapter {

    private Context mContext;
    private int layout;
    private Cursor cr;
    private final LayoutInflater inflater;


    public ExpenseSubCatAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context,layout,c,from,to);
        this.layout=layout;
        this.mContext = context;
        this.inflater=LayoutInflater.from(context);
        this.cr=c;
    }

    @Override
    public View newView (Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(layout, null);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);
        //byte[] blob = cursor.getBlob(cursor.getColumnIndex("t3."+FinanceContract.ExpSubCatEntry.COL_IMG));
        //create a separate adapter seriously
        // .....

        String base64Image = null;
        if(layout == R.layout.category_layout || layout == R.layout.category_grid_layout)
            base64Image= cursor.getString(cursor.getColumnIndex(ExpSubCatEntry.COL_IMG));
        else if(layout == R.layout.expense_list_row)
            base64Image= cursor.getString(cursor.getColumnIndex( ExpSubCatEntry.COL_IMG));
            //base64Image= cursor.getString(cursor.getColumnIndex("t3."+ ExpSubCatEntry.COL_IMG));
        if(base64Image != null){
            Bitmap bitmap = Util.convertToBitmap(base64Image);
            Drawable d = new BitmapDrawable(mContext.getResources(), bitmap);
            ((ImageView)view.findViewById(R.id.expense_category_img)).setImageDrawable(d);
        }

    }

}