package com.krishan.balaji.fh.fragments.expense;

import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.krishan.balaji.fh.util.Util;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class NewSubCatFragment extends DialogFragment {

    public ImageView iv;
    EditText editText;
    Button btn,cancel;
    public final int SELECT_PHOTO = 45;
    Uri subCatImageUri;
    Bitmap yourSelectedImage;
    long catId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_subcat_editor, container, false);
        getDialog().setTitle("Subcategory chooser");
        catId = getArguments().getLong("id");
        setUpLayout(rootView);
        return rootView;
    }

    private void setUpLayout(View rootView) {
        iv = (ImageView) rootView.findViewById(R.id.subCat_imageChooser);
        editText = (EditText) rootView.findViewById(R.id.subcat_text);
        cancel=(Button)rootView.findViewById(R.id.subcat_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        btn = (Button) rootView.findViewById(R.id.subcat_save);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(getActivity().findViewById(R.id.root_view),"tst1",Snackbar.LENGTH_LONG).show();
                if(editText.getText()!=null && !editText.getText().toString().equals("")){
                    Snackbar.make(getActivity().findViewById(R.id.root_view),"tst2",Snackbar.LENGTH_LONG).show();
                    ContentValues values = new ContentValues();
                    values.put(ExpSubCatEntry.COL_CAT_ID,catId);
                    values.put(ExpSubCatEntry.COL_NAME,editText.getText().toString());
                    Snackbar.make(getActivity().findViewById(R.id.root_view),"tst3",Snackbar.LENGTH_LONG).show();
                    if(yourSelectedImage!=null){
                        Snackbar.make(getActivity().findViewById(R.id.root_view),"tst4",Snackbar.LENGTH_LONG).show();
                        values.put(ExpSubCatEntry.COL_IMG, Util.getBitmapAsByteArray(yourSelectedImage));
                        Snackbar.make(getActivity().findViewById(R.id.root_view),"Added image",Snackbar.LENGTH_LONG).show();
                    }
                    else{
                        Snackbar.make(getActivity().findViewById(R.id.root_view),"tst5",Snackbar.LENGTH_LONG).show();
                    }

                    getActivity().getContentResolver().insert(ExpSubCatEntry.CONTENT_URI,
                            values);
                    dismiss();
                }

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



    public static NewSubCatFragment newInstance(long id) {
        NewSubCatFragment f = new NewSubCatFragment();
        Bundle args = new Bundle();
        args.putLong("id", id);
        f.setArguments(args);
        return f;
    }
}