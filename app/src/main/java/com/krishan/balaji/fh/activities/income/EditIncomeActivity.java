/*
package com.krishan.balaji.fh.activities.income;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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
import com.krishan.balaji.fh.data.AssetEntry;
import com.krishan.balaji.fh.data.IncCatEntry;
import com.krishan.balaji.fh.data.IncSubCatEntry;
import com.krishan.balaji.fh.data.IncomeEntry;
import com.krishan.balaji.fh.adaptetrs.IncomeSubCatAdapter;
import com.krishan.balaji.fh.util.Util;


import java.util.Calendar;

public class EditIncomeActivity extends AppCompatActivity implements  View.OnClickListener,  DatePickerDialog.OnDateSetListener{

    Spinner category, subCategory, asset;
    EditText dateText, amount, comment;
    Button create, cancel;
    ImageButton datePicker;
    Calendar selectedDate = Calendar.getInstance();
    long iCategory,iSubCategory,iAsset,iId;
    float iBalance;
    String iDate,iComment;

    CursorAdapter categoryAdapter;
    AssetSpinnerAdapter assetAdapter;
    IncomeSubCatAdapter subCategoryAdapter;
    long selectedCategoryId = -1;
    LoaderManager.LoaderCallbacks<Cursor> categoryLoaderCallbacks,subCategoryLoaderCallbacks,assetLoaderCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_new);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setUpLayout();
        saveInitialState();
        initializeAdaptersAndLoaders();
        initialToDefaultValues();
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
        dateText.setText(Util.displayFormat.format(selectedDate.getTime()));
        create.setOnClickListener(this);
        cancel.setOnClickListener(this);
        datePicker.setOnClickListener(this);
        dateText.setEnabled(false);
    }

    private void saveInitialState(){
        iId = getIntent().getLongExtra("id",-1);
        Uri uri = Uri.withAppendedPath(IncomeEntry.CONTENT_URI,"rawRecord");
        Cursor cursor = getContentResolver().query(uri,
                new String[]{
                        IncomeEntry.COL_DATE,
                        IncomeEntry.COL_COMMENT,
                        IncomeEntry.COL_CAT,
                        IncomeEntry.COL_SUBCAT,
                        IncomeEntry.COL_ASSET,
                        IncomeEntry.COL_AMOUNT},
                IncomeEntry.COL_ID +" = ? ",
                new String[]{iId+""},null);
        if(cursor.moveToFirst()){
            iCategory = cursor.getInt(cursor.getColumnIndex(IncomeEntry.COL_CAT));
            selectedCategoryId=iCategory;
            iDate = cursor.getString(cursor.getColumnIndex(IncomeEntry.COL_DATE));
            iSubCategory = cursor.getInt(cursor.getColumnIndex(IncomeEntry.COL_SUBCAT));
            iAsset = cursor.getInt(cursor.getColumnIndex(IncomeEntry.COL_ASSET));
            iBalance = cursor.getFloat(cursor.getColumnIndex(IncomeEntry.COL_AMOUNT));
            iComment = cursor.getString(cursor.getColumnIndex(IncomeEntry.COL_COMMENT));
            cursor.close();
        }else{
            //TODO handle error
        }
        //TODO set the values from the initial ones

    }
    private void initializeAdaptersAndLoaders() {
        initializeCategoryLoader();
        getSupportLoaderManager().initLoader(Util.EditIncomeActivity_category_loader_key, null, categoryLoaderCallbacks);
        initializeSubCategoryLoader();
        getSupportLoaderManager().initLoader(Util.EditIncomeActivity_subCategory_loader_key, null, subCategoryLoaderCallbacks);
        initializeAssetLoader();
        getSupportLoaderManager().initLoader(Util.EditIncomeActivity_asset_loader_key, null, assetLoaderCallbacks);
    }

    private void initializeCategoryLoader() {
        final String[] from ={IncCatEntry.COL_NAME};
        int[] to = {android.R.id.text1};
        categoryAdapter = new SimpleCursorAdapter(this,android.R.layout.simple_list_item_1,null,from,to);
        category.setAdapter(categoryAdapter);

        categoryLoaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                return new CursorLoader(EditIncomeActivity.this, IncCatEntry.CONTENT_URI,
                        new String[]{IncCatEntry.COL_ID,
                                IncCatEntry.COL_NAME
                        },
                        null,null,null);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {categoryAdapter.swapCursor(data);}

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                categoryAdapter.swapCursor(null);
            }
        };

        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCategoryId = l;
                //Snackbar.make(findViewById(R.id.root_view),"category selected. Updating subcategories",Snackbar.LENGTH_LONG).show();
                getSupportLoaderManager().restartLoader(Util.EditIncomeActivity_subCategory_loader_key,null,subCategoryLoaderCallbacks);
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
                return new CursorLoader(EditIncomeActivity.this, IncSubCatEntry.CONTENT_URI,
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
                return new CursorLoader(EditIncomeActivity.this, AssetEntry.CONTENT_URI,
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

    private void initialToDefaultValues() {
        create.setText("Update");
        dateText.setText(iDate);
        amount.setText(iBalance+"");
        comment.setText(iComment);
        category.post(new Runnable() {
            @Override
            public void run() {
                setDefaultSpinnerSelection(category,iCategory);
            }
        });
        asset.post(new Runnable() {
            @Override
            public void run() {
                setDefaultSpinnerSelection(asset,iAsset);
            }
        });
        subCategory.postDelayed(
            new Runnable() {
            @Override
            public void run() {
                setDefaultSpinnerSelection(subCategory,iSubCategory);
                //Snackbar.make(findViewById(R.id.root_view),"subCategory selected. Yahoooooo",Snackbar.LENGTH_LONG).show();
            }
        },10l);
    }

    private void setDefaultSpinnerSelection(Spinner spinner, long id) {
        //SimpleCursorAdapter adapter1 = (SimpleCursorAdapter) spinner.getAdapter();
        int count = spinner.getCount();
        for(int i = 0; i < count; i++)
        {
            if (spinner.getAdapter().getItemId(i) == id )
            {
                spinner.setSelection(i);
                break;
            }
        }
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
            boolean anyChange = false;
            ContentValues expenseContentValues = new ContentValues();
            if(iAsset != asset.getSelectedItemId() || !(iBalance+"").equals(amount.getText().toString())){
                anyChange = true;
                Cursor cursor = getContentResolver().query(AssetEntry.CONTENT_URI,new String[]{AssetEntry.COL_BALANCE},AssetEntry.COL_ID + " = ? ",new String[]{iAsset+""},null);
                cursor.moveToFirst();
                float currentAssetBalance = cursor.getFloat(cursor.getColumnIndex(AssetEntry.COL_BALANCE));
                cursor.close();
                //either asset or balance has changed
                //if asset is same, single update else two updates
                //TODO lastupdated date should also be updated

                if(iAsset==asset.getSelectedItemId()){
                    //only amount has changed
                    float newBalance = currentAssetBalance + iBalance - Float.parseFloat(amount.getText().toString());
                    ContentValues values = new ContentValues();
                    values.put(AssetEntry.COL_BALANCE,newBalance);
                    getContentResolver().update(AssetEntry.CONTENT_URI,values,AssetEntry.COL_ID+" = ? ",new String[]{iAsset+""});
                    expenseContentValues.put(IncomeEntry.COL_AMOUNT,newBalance);
                }else{
                    float currentAssetNewBalance = currentAssetBalance + iBalance;
                    ContentValues values = new ContentValues();
                    values.put(AssetEntry.COL_BALANCE,currentAssetNewBalance );
                    getContentResolver().update(AssetEntry.CONTENT_URI,values,AssetEntry.COL_ID+" = ? ",new String[]{iAsset+""});

                    Cursor newCursor = getContentResolver().query(AssetEntry.CONTENT_URI,new String[]{AssetEntry.COL_BALANCE},AssetEntry.COL_ID+" = ? ",new String[]{asset.getSelectedItemId()+""},null);
                    newCursor.moveToFirst();
                    float newAssetCurrentBalance = newCursor.getFloat(newCursor.getColumnIndex(AssetEntry.COL_BALANCE));
                    newCursor.close();
                    float newAssetNewBalance = newAssetCurrentBalance - Float.parseFloat(amount.getText().toString());
                    values.put(AssetEntry.COL_BALANCE,newAssetNewBalance);
                    getContentResolver().update(AssetEntry.CONTENT_URI,values,AssetEntry.COL_ID+" = ? ",new String[]{asset.getSelectedItemId()+""});

                    expenseContentValues.put(IncomeEntry.COL_ASSET,asset.getSelectedItemId());
                    expenseContentValues.put(IncomeEntry.COL_AMOUNT,amount.getText().toString());
                }
            }

            anyChange = checkForChange(expenseContentValues,anyChange);
            if(anyChange){
                getContentResolver().update(IncomeEntry.CONTENT_URI,expenseContentValues,IncomeEntry.COL_ID+" = ? ",new String[]{iId+""});
                //getContentResolver().insert(IncomeEntry.CONTENT_URI,values);
            }
            finish();
            //get asset from the above query
            //update balance and lastUpdated then insert into expense
        }
    }

    private boolean checkForChange(ContentValues values,boolean anyChanges) {
        boolean anyChange = anyChanges;
        if(!iDate.equals(dateText.getText())){
            anyChange = true;
            values.put(IncomeEntry.COL_DATE,Util.dbFormat.format(selectedDate.getTime()));
        }
        if(iCategory!=category.getSelectedItemId()){
            anyChange = true;
            values.put(IncomeEntry.COL_CAT,category.getSelectedItemId());
        }
        if(iSubCategory!=subCategory.getSelectedItemId()){
            anyChange = true;
            values.put(IncomeEntry.COL_SUBCAT,subCategory.getSelectedItemId());
        }
        if(iAsset!=asset.getSelectedItemId()){
            anyChange = true;
            values.put(IncomeEntry.COL_ASSET,asset.getSelectedItemId());
        }
        if(!(""+iBalance).equals(amount.getText().toString())){
            anyChange = true;
            values.put(IncomeEntry.COL_AMOUNT,Float.parseFloat(amount.getText().toString()));
        }
        if(!iComment.equals(comment.getText().toString())){
            anyChange = true;
            values.put(IncomeEntry.COL_COMMENT,comment.getText().toString());
        }
        return anyChange;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
        selectedDate.set(Calendar.YEAR,year);
        selectedDate.set(Calendar.MONTH,month);
        selectedDate.set(Calendar.DATE,date);
        dateText.setText(Util.displayFormat.format(selectedDate.getTime()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.inc_exp_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.delete_menu) {
            //Todo maybe show a popup for confirmation or a snackbar to undo it ?
            Cursor temp = getContentResolver().query(AssetEntry.CONTENT_URI,new String[]{AssetEntry.COL_BALANCE},AssetEntry.COL_ID+" = ? ",new String[]{iAsset+""},null);
            temp.moveToFirst();
            float balance = temp.getFloat(temp.getColumnIndex(AssetEntry.COL_BALANCE));
            ContentValues values = new ContentValues();
            values.put(AssetEntry.COL_BALANCE,balance+iBalance);
            getContentResolver().update(AssetEntry.CONTENT_URI,values,AssetEntry.COL_ID+" = ?",new String[]{iAsset+""});
            temp.close();
            getContentResolver().delete(IncomeEntry.CONTENT_URI,IncomeEntry.COL_ID + " = ? ",new String[]{iId+""});
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}*/
