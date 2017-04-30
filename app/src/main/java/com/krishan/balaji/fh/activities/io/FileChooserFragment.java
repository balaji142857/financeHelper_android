package com.krishan.balaji.fh.activities.io;

import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.krishan.balaji.fh.R;


public class FileChooserFragment  extends DialogFragment{

    Button cancel,select;
    ListView filesList;
    String[] files;
    int selectedPosition = -1;
    //onSubmitListener mListener;
    interface OnFileChoosen {
        void setOnFileChosen(String arg);
    }

    public static FileChooserFragment newInstance(String[] files) {
        FileChooserFragment f = new FileChooserFragment();
        Bundle args = new Bundle();
        args.putStringArray("files",files);
        f.setArguments(args);
        return f;
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity());
        //dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setTitle("Choose File to import");
        dialog.setContentView(R.layout.file_chooser);
        /*dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));*/
        dialog.show();
        files = getArguments().getStringArray("files");
        ArrayAdapter<String> fileListAdapter = new ArrayAdapter<String>(getActivity(),R.layout.file_chooser_row,R.id.file_name,files);
        filesList = (ListView) dialog.findViewById(R.id.files_list);
        filesList.setAdapter(fileListAdapter);
        filesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedPosition=position;
                view.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.primary_light));
                ((OnFileChoosen)getActivity()).setOnFileChosen(((TextView)view.findViewById(R.id.file_name)).getText().toString());
                dismiss();
                //Snackbar.make(getDialog().findViewById(R.id.root_view),files[selectedPosition],Snackbar.LENGTH_LONG).show();
            }
        });
        /*cancel = (Button) dialog.findViewById(R.id.button_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        select= (Button) dialog.findViewById(R.id.button_select);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedPosition!=-1){
                    Snackbar.make(getDialog().findViewById(R.id.root_view),files[selectedPosition],Snackbar.LENGTH_LONG).show();
                }else{
                    Snackbar.make(getDialog().findViewById(R.id.root_view),"Choose a file first",Snackbar.LENGTH_LONG).show();
                }
                dismiss();
            }
        });*/
        return dialog;
    }
}
