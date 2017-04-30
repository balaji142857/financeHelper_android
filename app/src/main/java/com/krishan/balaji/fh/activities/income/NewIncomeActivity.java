/*
package com.krishan.balaji.fh.activities.income;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.krishan.balaji.fh.adaptetrs.AssetSpinnerAdapter;
import com.krishan.balaji.fh.adaptetrs.IncomeSubCatAdapter;
import com.krishan.balaji.fh.data.IncCatEntry;
import com.krishan.balaji.fh.data.IncSubCatEntry;
import com.krishan.balaji.fh.data.IncomeEntry;
import com.krishan.balaji.fh.fragments.DatePickerFragment;
import com.krishan.balaji.fh.R;

import com.krishan.balaji.fh.util.Util;
import com.krishan.balaji.fh.data.AssetEntry;



import java.text.ParseException;
import java.util.Calendar;

public class NewIncomeActivity extends AppCompatActivity implements  View.OnClickListener,  DatePickerDialog.OnDateSetListener{

    Spinner category,subCategory,asset;
    EditText dateText,amount,comment;
    Button create,cancel;
    ImageButton datePicker;
    Calendar selectedDate = Calendar.getInstance();

    CursorAdapter categoryAdapter;
    AssetSpinnerAdapter assetAdapter;
    IncomeSubCatAdapter subCategoryAdapter;
    long selectedCategoryId = -1;
    LoaderManager.LoaderCallbacks<Cursor> categoryLoaderCallbacks,subCategoryLoaderCallbacks,assetLoaderCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_new);
        setUpLayout();
        initializeAdaptersAndLoaders();
        getSupportLoaderManager().initLoader(Util.NewIncomeActivity_category_loader_key, null, categoryLoaderCallbacks);
        getSupportLoaderManager().initLoader(Util.NewIncomeActivity_subCategory_loader_key, null, subCategoryLoaderCallbacks);
        getSupportLoaderManager().initLoader(Util.NewIncomeActivity_asset_loader_key, null, assetLoaderCallbacks);
    }

    private void initializeAdaptersAndLoaders() {
        initializeCategoryLoader();
        initializeSubCategoryLoader();
        initializeAssetLoader();
    }

    private void initializeCategoryLoader() {
        final String[] from ={IncCatEntry.COL_NAME};
        int[] to = {android.R.id.text1};
        categoryAdapter = new SimpleCursorAdapter(this,android.R.layout.simple_list_item_1,null,from,to);
        category.setAdapter(categoryAdapter);

        categoryLoaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                return new CursorLoader(NewIncomeActivity.this, IncCatEntry.CONTENT_URI,
                        new String[]{IncCatEntry.COL_ID,
                                IncCatEntry.COL_NAME
                        },
                        null,null,null);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                categoryAdapter.swapCursor(data);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                categoryAdapter.swapCursor(null);
            }
        };

        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCategoryId = l;
                getSupportLoaderManager().restartLoader(3,null,subCategoryLoaderCallbacks);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    private void initializeSubCategoryLoader() {
        String[] sfrom ={IncSubCatEntry.COL_NAME};
        int[] sto = {R.id.expense_category_row_text};
        subCategoryAdapter = new IncomeSubCatAdapter(this,R.layout.category_layout,null,sfrom,sto);
        subCategory.setAdapter(subCategoryAdapter);

        subCategoryLoaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {

            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                return new CursorLoader(NewIncomeActivity.this, IncSubCatEntry.CONTENT_URI,
                        new String[]{IncSubCatEntry.COL_ID,
                                IncSubCatEntry.COL_CAT_ID,
                                IncSubCatEntry.COL_IMG,
                                IncSubCatEntry.COL_NAME
                        },
                        IncSubCatEntry.COL_CAT_ID+" = ?",getSelectedCategoryId(),null);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                subCategoryAdapter.swapCursor(data);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                subCategoryAdapter.swapCursor(null);
            }
        };
    }

    private void initializeAssetLoader() {
        String[] from ={AssetEntry.COL_NAME};
        int[] to ={android.R.id.text1};
        assetAdapter = new AssetSpinnerAdapter(this,android.R.layout.simple_list_item_1,null,from,to);
        asset.setAdapter(assetAdapter);


        assetLoaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                return new CursorLoader(NewIncomeActivity.this, AssetEntry.CONTENT_URI,
                        new String[]{AssetEntry.COL_ID,
                                AssetEntry.COL_NAME,
                                AssetEntry.COL_BALANCE
                        },
                        null,null,null);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                assetAdapter.swapCursor(data);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                assetAdapter.swapCursor(null);
            }
        };
    }

    private String[] getSelectedCategoryId() {
        return new String[]{selectedCategoryId+""};
    }

    private void setUpLayout() {
        create = (Button) findViewById(R.id.expense_create);
        cancel = (Button) findViewById(R.id.cancel);
        amount = (EditText) findViewById(R.id.expense_amount);
        comment = (EditText) findViewById(R.id.expense_comment);
        category = (Spinner) findViewById(R.id.expense_category);
        subCategory = (Spinner) findViewById(R.id.expense_subcategory);
        asset = (Spinner) findViewById(R.id.expense_asset);
        dateText = (EditText) findViewById(R.id.expense_date);
        datePicker = (ImageButton) findViewById(R.id.expense_date_picker);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dateText.setText(Util.displayFormat.format(selectedDate.getTime()));
        dateText.setEnabled(false);
        create.setOnClickListener(this);
        cancel.setOnClickListener(this);
        datePicker.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if(view.getId() == datePicker.getId()){
            DialogFragment newFragment = new DatePickerFragment();
            newFragment.show(getSupportFragmentManager(), "datePicker");
        }
        else if(view.getId() == cancel.getId())
            finish();
        else if(view.getId() == create.getId()){
            //Snackbar.make(findViewById(R.id.root_view),asset.getSelectedItemId()+" is assetId",Snackbar.LENGTH_LONG).show();

            try {
                if(Calendar.getInstance().getTime().getTime() > Util.displayFormat.parse(dateText.getText().toString()).getTime()){
                    if(category.getSelectedItemId() > 0 && asset.getSelectedItemId()> 0){
                        Cursor assCursor = getContentResolver().query(AssetEntry.CONTENT_URI,new String[]{AssetEntry.COL_ID, AssetEntry.COL_NAME, AssetEntry.COL_BALANCE}, AssetEntry.COL_ID+" = ? ",new String[]{asset.getSelectedItemId()+""},null);
                        if(assCursor.moveToFirst()){
                            float assetBalance = assCursor.getFloat(assCursor.getColumnIndex(AssetEntry.COL_BALANCE));
                            assCursor.close();
                            float incomeAmount = 0f;
                            if(amount.getText()!=null && !amount.getText().toString().equals(""))
                                incomeAmount=Float.parseFloat(amount.getText().toString());
                            if(incomeAmount  >= 0){
                                ContentValues values = new ContentValues();
                                values.put(AssetEntry.COL_BALANCE,assetBalance+incomeAmount);
                                getContentResolver().update(AssetEntry.CONTENT_URI,values, AssetEntry.COL_ID+" = ? ",new String[]{asset.getSelectedItemId()+""});

                                values = new ContentValues();
                                values.put(IncomeEntry.COL_DATE,Util.dbFormat.format(selectedDate.getTime()));
                                values.put(IncomeEntry.COL_CAT,category.getSelectedItemId());
                                if(subCategory.getSelectedItemId() > 0)
                                    values.put(IncomeEntry.COL_SUBCAT,subCategory.getSelectedItemId());
                                values.put(IncomeEntry.COL_ASSET,asset.getSelectedItemId());
                                values.put(IncomeEntry.COL_AMOUNT,incomeAmount);
                                if(comment.getText()!=null && !comment.getText().toString().equals(""))
                                    values.put(IncomeEntry.COL_COMMENT,comment.getText().toString());
                                else
                                    values.put(IncomeEntry.COL_COMMENT,"");
                                getContentResolver().insert(IncomeEntry.CONTENT_URI,values);
                                finish();
                            }else
                                Snackbar.make(findViewById(R.id.root_view),"Negative income amount not allowed",Snackbar.LENGTH_LONG).show();
                        }
                    }else
                            Snackbar.make(findViewById(R.id.root_view),"Asset & CategoryModel are requierd. Start adding incomes by creating them first",Snackbar.LENGTH_LONG).show();
                }else
                    Snackbar.make(findViewById(R.id.root_view),"Future incomes not allowed. Select present/past date",Snackbar.LENGTH_LONG).show();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
        selectedDate.set(Calendar.YEAR,year);
        selectedDate.set(Calendar.MONTH,month);
        selectedDate.set(Calendar.DATE,date);
        dateText.setText(Util.displayFormat.format(selectedDate.getTime()));

    }
}
*/
