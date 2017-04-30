package com.krishan.balaji.fh.adaptetrs;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.krishan.balaji.fh.R;

/**
 * Created by balaji142857 on 6/8/16.
 */
public class NavigationListViewAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] itemname;
    private final Integer[] imgid;

    public NavigationListViewAdapter(Activity context, String[] itemname, Integer[] imgid) {
        super(context, R.layout.navigation_list_row, itemname);
        // TODO Auto-generated constructor stub
        this.context=context;
        this.itemname=itemname;
        this.imgid=imgid;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView= null;
        if(position == 0){
            rowView=inflater.inflate(R.layout.navigation_drawer_image,null,true);
        }else{
            rowView = inflater.inflate(R.layout.navigation_list_row, null,true);
            TextView txtTitle = (TextView) rowView.findViewById(R.id.nav_txt);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.nav_img);
            txtTitle.setText(itemname[position]);
            imageView.setImageResource(imgid[position]);
        }
        return rowView;

    };
}
