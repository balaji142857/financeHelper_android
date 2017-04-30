/*
package com.krishan.balaji.fh.activities.income;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.krishan.balaji.fh.R;
import com.krishan.balaji.fh.adaptetrs.IncomeSubCatAdapter;
import com.krishan.balaji.fh.data.IncCatEntry;
import com.krishan.balaji.fh.data.IncSubCatEntry;
import com.krishan.balaji.fh.fragments.expense.EditSubCatFragment;
import com.krishan.balaji.fh.fragments.expense.NewSubCatFragment;
import com.krishan.balaji.fh.util.Util;

public class IncomeSubCatActivity extends AppCompatActivity {



    String initialCatName = null;
    long catId;
    EditText catName;
    Button saveButton;
    FloatingActionButton fab = null;
    //ListView expSubCategories;
    GridView expSubCategories;
    LoaderManager.LoaderCallbacks<Cursor> subCatCallBacks;
    IncomeSubCatAdapter incomeAdapter;

    public final int SELECT_PHOTO = 45;
    ImageView deleteImage;
    NewSubCatFragment dialogFragment;
    EditSubCatFragment editFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_sub_cat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setUpLayout();
        setUpCallbacks();
        getSupportLoaderManager().initLoader(Util.IncomeSubCatActivity_loader_key,null,subCatCallBacks);

    }


    @Override
    protected void onStart() {
        super.onStart();
        catName.setText(initialCatName);
        getSupportLoaderManager().restartLoader(Util.IncomeSubCatActivity_loader_key,null,subCatCallBacks);
    }

    private void setUpLayout() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initialCatName = getIntent().getStringExtra("categoryName");
        catId = getIntent().getLongExtra("categoryId",-1);
        catName = (EditText) findViewById(R.id.cat_text);
        saveButton = (Button) findViewById(R.id.cat_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                if(!initialCatName.equals(catName.getText().toString())){
                    ContentValues values = new ContentValues();
                    values.put(IncCatEntry.COL_NAME,catName.getText().toString());
                    getContentResolver().update(IncCatEntry.CONTENT_URI,values, IncCatEntry.COL_NAME+" = ? ",new String[]{initialCatName});
                }else{
                    Snackbar.make(view, "Update the category name & then click on save button", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
        deleteImage = (ImageView) findViewById(R.id.imageButton);
        deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO check if this is sufficient or should query the db to see if no dependencies
                if(expSubCategories.getCount() == 0){
                    ContentValues values = new ContentValues();
                    getContentResolver().delete(IncCatEntry.CONTENT_URI,IncCatEntry.COL_ID+" = ? ",new String[] {catId+""});
                    finish();
                }
                else
                    Snackbar.make(view, "Cannot delete the category without removing its subcategories", Snackbar.LENGTH_LONG)
                            .show();
            }
        });
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogFragment = NewSubCatFragment.newInstance(catId);
                dialogFragment.show(getFragmentManager(), "Sample Fragment");



                */
/*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*//*

            }
        });
        //expSubCategories = (ListView) findViewById(R.id.exp_subcat_list);
        expSubCategories = (GridView) findViewById(R.id.exp_subcat_grid);
    }

    private void setUpCallbacks() {
        String[] from = {IncSubCatEntry.COL_NAME};
        int [] to = {R.id.expense_category_row_text};
        incomeAdapter = new IncomeSubCatAdapter(this,R.layout.category_grid_layout,null,from,to);
        expSubCategories.setAdapter(incomeAdapter);
        expSubCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                editFragment = EditSubCatFragment.newInstance(l,((TextView)view.findViewById(R.id.expense_category_row_text)).getText().toString());
                editFragment.show(getFragmentManager(), "Sample Fragment");
            }
        });
        subCatCallBacks = new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                return new CursorLoader(IncomeSubCatActivity.this, IncSubCatEntry.CONTENT_URI,
                        new String[]{IncSubCatEntry.COL_ID,
                                IncSubCatEntry.COL_NAME,
                                IncSubCatEntry.COL_IMG},
                        IncSubCatEntry.COL_CAT_ID +" = ? ",
                        new String[]{catId+""},null);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                incomeAdapter.swapCursor(data);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                incomeAdapter.swapCursor(null);
            }
        };
    }



   */
/* @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Snackbar.make(findViewById(R.id.root_view), "StartActivityForResult is from Activity called", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        switch(requestCode) {

            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    dialogFragment.subCatImageUri = data.getData();
                    Snackbar.make(findViewById(R.id.root_view), "Selected image is " + dialogFragment.subCatImageUri, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    Drawable drawable;
                    InputStream imageStream = null;
                    try {
                        imageStream = getContentResolver().openInputStream(dialogFragment.subCatImageUri);
                        drawable = Drawable.createFromStream(imageStream,null);
                        dialogFragment.iv.setImageDrawable(drawable);
                        dialogFragment.yourSelectedImage = BitmapFactory.decodeStream(imageStream);
                    } catch (FileNotFoundException e) {
                        Log.e("fsdfsf",e.getMessage()+e.toString());
                        e.printStackTrace();
                    }

                }
        }
    }*//*




}*/
