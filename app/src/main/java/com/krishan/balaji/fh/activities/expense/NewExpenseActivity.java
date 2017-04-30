package com.krishan.balaji.fh.activities.expense;

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
import com.krishan.balaji.fh.fragments.DatePickerFragment;
import com.krishan.balaji.fh.R;
import com.krishan.balaji.fh.adaptetrs.ExpenseSubCatAdapter;
import com.krishan.balaji.fh.util.Util;
import com.krishan.balaji.fh.data.AssetEntry;
import com.krishan.balaji.fh.data.ExpCatEntry;
import com.krishan.balaji.fh.data.ExpSubCatEntry;
import com.krishan.balaji.fh.data.ExpenseEntry;

import java.text.ParseException;
import java.util.Calendar;

public class NewExpenseActivity extends AppCompatActivity implements  View.OnClickListener,  DatePickerDialog.OnDateSetListener{

    Spinner category,subCategory,asset;
    EditText dateText,amount,comment;
    Button create,cancel;
    ImageButton datePicker;
    Calendar selectedDate = Calendar.getInstance();

    CursorAdapter categoryAdapter;
    AssetSpinnerAdapter assetAdapter;
    ExpenseSubCatAdapter subCategoryAdapter;
    long selectedCategoryId = -1;
    LoaderManager.LoaderCallbacks<Cursor> categoryLoaderCallbacks,subCategoryLoaderCallbacks,assetLoaderCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_new);
        setUpLayout();
        initializeAdaptersAndLoaders();
        getSupportLoaderManager().initLoader(Util.NewExpenseActivity_category_loader_key, null, categoryLoaderCallbacks);
        getSupportLoaderManager().initLoader(Util.NewExpenseActivity_subCategory_loader_key, null, subCategoryLoaderCallbacks);
        getSupportLoaderManager().initLoader(Util.NewExpenseActivity_asset_loader_key, null, assetLoaderCallbacks);
    }

    private void initializeAdaptersAndLoaders() {
        initializeCategoryLoader();
        initializeSubCategoryLoader();
        initializeAssetLoader();
    }

    private void initializeCategoryLoader() {
        final String[] from ={ExpCatEntry.COL_NAME};
        int[] to = {android.R.id.text1};
        categoryAdapter = new SimpleCursorAdapter(this,android.R.layout.simple_list_item_1,null,from,to);
        category.setAdapter(categoryAdapter);

        categoryLoaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                return new CursorLoader(NewExpenseActivity.this, ExpCatEntry.CONTENT_URI,
                        new String[]{ExpCatEntry.COL_ID,
                                ExpCatEntry.COL_NAME
                        },
                        null,null,ExpCatEntry.COL_USAGE +" desc");
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
        String[] sfrom ={ExpSubCatEntry.COL_NAME};
        int[] sto = {R.id.expense_category_row_text};
        subCategoryAdapter = new ExpenseSubCatAdapter(this,R.layout.category_layout,null,sfrom,sto);
        subCategory.setAdapter(subCategoryAdapter);

        subCategoryLoaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {

            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                return new CursorLoader(NewExpenseActivity.this, ExpSubCatEntry.CONTENT_URI,
                        new String[]{ExpSubCatEntry.COL_ID,
                                ExpSubCatEntry.COL_CAT_ID,
                                ExpSubCatEntry.COL_IMG,
                                ExpSubCatEntry.COL_NAME
                        },
                        ExpSubCatEntry.COL_CAT_ID+" = ?",getSelectedCategoryId(),ExpSubCatEntry.COL_USAGE +" desc");
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
                return new CursorLoader(NewExpenseActivity.this, AssetEntry.CONTENT_URI,
                        new String[]{AssetEntry.COL_ID,
                                AssetEntry.COL_NAME,
                                AssetEntry.COL_BALANCE
                        },
                        null,null,AssetEntry.COL_USAGE +" desc");
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
                        Cursor assCursor = getContentResolver().query(AssetEntry.CONTENT_URI,new String[]{AssetEntry.COL_ID, AssetEntry.COL_NAME, AssetEntry.COL_BALANCE, AssetEntry.COL_USAGE}, AssetEntry.COL_ID+" = ? ",new String[]{asset.getSelectedItemId()+""},null);
                        if(assCursor.moveToFirst()){
                            float assetBalance = assCursor.getFloat(assCursor.getColumnIndex(AssetEntry.COL_BALANCE));
                            int assetUsage = assCursor.getInt(assCursor.getColumnIndex(AssetEntry.COL_USAGE));
                            assCursor.close();
                            float expenseAmount = 0f;
                            if(amount.getText()!=null && !amount.getText().toString().equals(""))
                                expenseAmount=Float.parseFloat(amount.getText().toString());
                            if(expenseAmount  >= 0){
                                if(assetBalance > expenseAmount || Util.allowNegativeBalance){
                                    ContentValues values = new ContentValues();
                                    values.put(AssetEntry.COL_BALANCE,assetBalance-expenseAmount);
                                    values.put(AssetEntry.COL_USAGE,assetUsage+1);
                                    getContentResolver().update(AssetEntry.CONTENT_URI,values, AssetEntry.COL_ID+" = ? ",new String[]{asset.getSelectedItemId()+""});

                                    values = new ContentValues();
                                    values.put(ExpenseEntry.COL_DATE,Util.dbFormat.format(selectedDate.getTime()));
                                    values.put(ExpenseEntry.COL_CAT,category.getSelectedItemId());
                                    values.put(ExpenseEntry.COL_SUBCAT,subCategory.getSelectedItemId());
                                    values.put(ExpenseEntry.COL_ASSET,asset.getSelectedItemId());
                                    values.put(ExpenseEntry.COL_AMOUNT,expenseAmount);
                                    if(comment.getText()!=null && !comment.getText().toString().equals(""))
                                        values.put(ExpenseEntry.COL_COMMENT,comment.getText().toString());
                                    else
                                        values.put(ExpenseEntry.COL_COMMENT,"");
                                    getContentResolver().insert(ExpenseEntry.CONTENT_URI,values);
                                    //get the current usage & then increment it by one for both category and subcategory
                                    //update category usage
                                    Cursor catCursor = getContentResolver().query(ExpCatEntry.CONTENT_URI,new String[]{ExpCatEntry.COL_USAGE},ExpCatEntry.COL_ID+" = ? ",new String[]{category.getSelectedItemId()+""},null);
                                    if(catCursor.moveToFirst()){
                                        int catUsage = catCursor.getInt(catCursor.getColumnIndex(ExpCatEntry.COL_USAGE));
                                        catCursor.close();
                                        ContentValues expCatValues = new ContentValues();
                                        expCatValues.put(ExpCatEntry.COL_ID,category.getSelectedItemId());
                                        expCatValues.put(ExpCatEntry.COL_USAGE,catUsage+1);
                                        getContentResolver().update(ExpCatEntry.CONTENT_URI,expCatValues,ExpCatEntry.COL_ID + " = ?",new String[]{category.getSelectedItemId()+""});
                                    }
                                    //update sbCategory usage
                                    Cursor subCatCursor = getContentResolver().query(ExpSubCatEntry.CONTENT_URI,new String[]{ExpSubCatEntry.COL_USAGE},ExpSubCatEntry.COL_ID+" = ? ",new String[]{subCategory.getSelectedItemId()+""},null);
                                    if(subCatCursor.moveToFirst()){
                                        int subCatUsage = subCatCursor.getInt(subCatCursor.getColumnIndex(ExpSubCatEntry.COL_USAGE));
                                        subCatCursor.close();
                                        ContentValues expCatValues = new ContentValues();
                                        expCatValues.put(ExpSubCatEntry.COL_ID,subCategory.getSelectedItemId());
                                        expCatValues.put(ExpSubCatEntry.COL_USAGE,subCatUsage+1);
                                        getContentResolver().update(ExpSubCatEntry.CONTENT_URI,expCatValues,ExpSubCatEntry.COL_ID + " = ?",new String[]{subCategory.getSelectedItemId()+""});
                                    }
                                    //update the category and subcategory usage by 1
                                    finish();
                                }
                                else
                                    Snackbar.make(findViewById(R.id.root_view),"Insufficient balance. Update preference to allow negative balance.",Snackbar.LENGTH_LONG).show();
                            }else
                                Snackbar.make(findViewById(R.id.root_view),"Negative expense amount not allowed",Snackbar.LENGTH_LONG).show();
                        }
                    }else
                            Snackbar.make(findViewById(R.id.root_view),"Asset & CategoryModel are requierd. Start adding expenses by creating them first",Snackbar.LENGTH_LONG).show();
                }else
                    Snackbar.make(findViewById(R.id.root_view),"Future expenses not allowed. Select present/past date",Snackbar.LENGTH_LONG).show();
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