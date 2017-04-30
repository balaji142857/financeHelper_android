package com.krishan.balaji.fh.activities.io;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.krishan.balaji.fh.R;

/**
 * Created by balaji142857 on 7/8/16.
 */
public class FileListAdapter extends ArrayAdapter<String>{

    private final Activity context;
    private final String[] itemname;

    public FileListAdapter(Activity context, String[] itemname) {
        super(context, R.layout.file_chooser_row, itemname);
        this.context=context;
        this.itemname=itemname;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView= null;
        rowView = inflater.inflate(R.layout.file_chooser_row, null,true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.file_name);
        Log.e("daf",itemname[position]);
        Log.e("daf",itemname[position].substring(itemname[position].lastIndexOf("/")));
        txtTitle.setText(itemname[position].substring(itemname[position].lastIndexOf("/")));
        return rowView;
    };
}