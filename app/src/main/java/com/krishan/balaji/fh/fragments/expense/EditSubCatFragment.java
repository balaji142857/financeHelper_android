package com.krishan.balaji.fh.fragments.expense;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.krishan.balaji.fh.R;
import com.krishan.balaji.fh.data.ExpSubCatEntry;
import com.krishan.balaji.fh.data.ExpenseEntry;
import com.krishan.balaji.fh.util.Util;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class EditSubCatFragment extends DialogFragment{

    public ImageView iv;
    EditText editText;
    Button btn,cancel,delete;
    public final int SELECT_PHOTO = 46;
    Uri subCatImageUri;
    Bitmap yourSelectedImage;

    long subCatId;
    int idrawable;
    String isubCat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_subcat_editor, container, false);
        getDialog().setTitle("Edit Subcategory");
        subCatId = getArguments().getLong("id");
        idrawable = getArguments().getInt("image");
        isubCat = getArguments().getString("name");
        setUpLayout(rootView);
        return rootView;
    }

    private void setUpLayout(View rootView) {
        Cursor cursor = getActivity().getContentResolver().query(ExpSubCatEntry.CONTENT_URI,new String[]{ExpSubCatEntry.COL_IMG},ExpSubCatEntry.COL_ID+" = ? ",new String[]{subCatId+""},null);
        cursor.moveToFirst();
        String imgStr = cursor.getString(cursor.getColumnIndex(ExpSubCatEntry.COL_IMG));
        Bitmap bitmap = Util.convertToBitmap(imgStr);
        iv = (ImageView) rootView.findViewById(R.id.subCat_imageChooser);
        iv.setImageDrawable(new BitmapDrawable(getResources(), bitmap));
        editText = (EditText) rootView.findViewById(R.id.subcat_text);
        editText.setText(isubCat);
        btn = (Button) rootView.findViewById(R.id.subcat_save);
        btn.setText("Update");
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });
        cancel=(Button)rootView.findViewById(R.id.subcat_cancel);
        delete=(Button)rootView.findViewById(R.id.subcat_delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getContentResolver().delete(ExpSubCatEntry.CONTENT_URI,ExpSubCatEntry.COL_ID + " = ?",new String[]{subCatId+""});
                dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean anyChange = false;
                ContentValues values = new ContentValues();
                if(!isubCat.equals(editText.getText().toString())){
                    anyChange = true;
                    values.put(ExpSubCatEntry.COL_NAME,editText.getText().toString());
                }
                if(yourSelectedImage!=null){
                    anyChange=true;
                    values.put(ExpSubCatEntry.COL_IMG, Util.getBitmapAsByteArray(yourSelectedImage));
                }
                if (anyChange){
                    getActivity().getContentResolver().update(ExpSubCatEntry.CONTENT_URI,values,ExpSubCatEntry.COL_ID+" = ? ",new String[]{subCatId+""});
                    dismiss();
                }
                else
                    Snackbar.make(getActivity().findViewById(R.id.root_view), "No changes to update", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Snackbar.make(getActivity().findViewById(R.id.root_view), "StartActivityForResult from fragment is called", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        switch(requestCode) {

            case SELECT_PHOTO:
                if(resultCode == getActivity().RESULT_OK){
                    subCatImageUri = data.getData();
                    Drawable drawable;
                    InputStream imageStream = null;
                    try {
                        imageStream = getActivity().getContentResolver().openInputStream(subCatImageUri);
                        drawable = Drawable.createFromStream(imageStream,null);
                        iv.setImageDrawable(drawable);
                        imageStream = getActivity().getContentResolver().openInputStream(subCatImageUri);
                        yourSelectedImage = BitmapFactory.decodeStream(imageStream);
                    } catch (FileNotFoundException e) {
                        Log.e("fsdfsf",e.getMessage()+e.toString());
                        e.printStackTrace();
                    }

                }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null)
        {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT    ;
            dialog.getWindow().setLayout(width, height);
        }
    }

    public static EditSubCatFragment newInstance(long id, String name) {
        EditSubCatFragment f = new EditSubCatFragment();
        Bundle args = new Bundle();
        args.putLong("id", id);
        args.putString("name",name);
        f.setArguments(args);
        return f;
    }
}
