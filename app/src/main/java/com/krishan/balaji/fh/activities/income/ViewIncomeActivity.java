/*
package com.krishan.balaji.fh.activities.income;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;

import com.krishan.balaji.fh.R;
import com.krishan.balaji.fh.adaptetrs.IncomeAdapter;
import com.krishan.balaji.fh.data.ExpenseEntry;
import com.krishan.balaji.fh.data.FinanceContract;
import com.krishan.balaji.fh.util.Util;
import com.krishan.balaji.fh.data.AssetEntry;
import com.krishan.balaji.fh.data.IncCatEntry;
import com.krishan.balaji.fh.data.IncSubCatEntry;
import com.krishan.balaji.fh.data.IncomeEntry;

public class ViewIncomeActivity extends AppCompatActivity {


    private ListView incomes;
    private LoaderManager.LoaderCallbacks<Cursor> incomeCallbacks;
    private IncomeAdapter incomeAdapter;
    private EditText searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_expense);
        setUpLayout();
        initializeLoader();
        getSupportLoaderManager().initLoader(Util.ViewIncomeActivity_income_loader_key,null, incomeCallbacks);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewIncomeActivity.this,NewIncomeActivity.class);
                ViewIncomeActivity.this.startActivityForResult(intent,1);
            }
        });
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        incomes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                bundle.putLong("id",l);
                Intent intent = new Intent(ViewIncomeActivity.this,EditIncomeActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent,2);
            }
        });
        getSupportLoaderManager().restartLoader(Util.ViewIncomeActivity_income_loader_key,null, incomeCallbacks);
        setupWindowAnimations();
    }

    private void setUpLayout() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        incomes = (ListView) findViewById(R.id.expense_list);
        searchText = (EditText) findViewById(R.id.editText1);
        searchText.setVisibility(View.INVISIBLE);
        searchText.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                Log.e("adskflads","text changed to "+s.toString());
                SimpleCursorAdapter filterAdapter = (SimpleCursorAdapter)incomes.getAdapter();
                filterAdapter.getFilter().filter(s.toString());
            }
        });
    }


    private void initializeLoader() {
        String[] from = {
                IncomeEntry.COL_DATE,
                IncCatEntry.COL_NAME,
                IncSubCatEntry.COL_NAME,
                IncomeEntry.COL_COMMENT,
                AssetEntry.COL_NAME,
                IncomeEntry.COL_AMOUNT};
        int [] to = {
                R.id.expense_date,
                R.id.expense_categoryy,
                R.id.expense_subcategory,
                R.id.expense_comment,
                R.id.expense_asset,
                R.id.expense_amount};
        incomeAdapter = new IncomeAdapter(this,R.layout.expense_list_row,null,from,to);
        incomeAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence constraint) {
                return getDirectoryList(constraint);
            }
        });
        incomes.setAdapter(incomeAdapter);
        //incomeAdapter = new SimpleCursorAdapter(this,R.layout.expense_list_row,null,from,to);

        incomeCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader onCreateLoader(int id, Bundle args) {

                return new CursorLoader(ViewIncomeActivity.this, IncomeEntry.CONTENT_URI,
                        new String[]{
                                "t1."+IncomeEntry.COL_ID,
                                "t1."+IncomeEntry.COL_AMOUNT,
                                "t1."+IncomeEntry.COL_COMMENT,
                                "t1."+IncomeEntry.COL_DATE,
                                "t4."+AssetEntry.COL_NAME,
                                "t2."+IncCatEntry.COL_NAME,
                                "t3."+IncSubCatEntry.COL_NAME,
                                "t3."+IncSubCatEntry.COL_IMG}
                  ,
                        null,null,null);
            }

            @Override
            public void onLoadFinished(Loader loader, Cursor data) {
                incomeAdapter.swapCursor(data);
            }

            @Override
            public void onLoaderReset(Loader loader) {
                incomeAdapter.swapCursor(null);
            }
        };
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.categories_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.categories_menu) {
            //Todo maybe show a popup for confirmation or a snackbar to undo it ?
            Intent intent = new Intent(this,IncomeCatActivity.class);
            startActivity(intent);
            return true;
        }
        if(id == R.id.search_menu){
            int visibility = searchText.getVisibility();
            if (visibility == View.VISIBLE)
                searchText.setVisibility(View.INVISIBLE);
            else
                searchText.setVisibility(View.VISIBLE);
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupWindowAnimations() {
        // Re-enter transition is executed when returning to this activity
        Slide slideTransition = new Slide();
        slideTransition.setSlideEdge(Gravity.LEFT);
        slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
        getWindow().setReenterTransition(slideTransition);
        getWindow().setExitTransition(slideTransition);
    }

    public Cursor getDirectoryList (CharSequence constraint)  {
        Cursor cursor = null;
        if (constraint == null  ||  constraint.length () == 0)  {
            Log.e("","oops");    //  Return the full list
            cursor = getContentResolver().query(IncomeEntry.CONTENT_URI,new String[]{
                    "t1."+IncomeEntry.COL_ID,
                    "t1."+IncomeEntry.COL_AMOUNT,
                    "t1."+IncomeEntry.COL_COMMENT,
                    "t1."+IncomeEntry.COL_DATE,
                    "t4."+AssetEntry.COL_NAME,
                    "t2."+IncCatEntry.COL_NAME,
                    "t3."+IncSubCatEntry.COL_NAME,
                    "t3."+IncSubCatEntry.COL_IMG
            },null,null,null);

        }  else  {
            Log.e("","yahoo2");
            //http://stackoverflow.com/questions/19167954/use-uri-builder-in-android-or-create-url-with-variables
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("content")
                    .authority(FinanceContract.authority)
                    .appendPath(IncomeEntry.PATH)
                    .appendPath(IncomeEntry.path_search)
                    .appendQueryParameter("q", constraint.toString());
            String myUrl = builder.build().toString();
            Log.e("","yahoo3  "+builder.build().toString());
            cursor = getContentResolver().query(builder.build(),new String[]{ExpenseEntry.COL_CAT},"",new String[]{},null);

        }
        return cursor;
    }
}*/
