package com.krishan.balaji.fh.activities.expense;

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
import com.krishan.balaji.fh.adaptetrs.ExpenseAdapter;
import com.krishan.balaji.fh.data.FinanceContract;
import com.krishan.balaji.fh.util.Util;
import com.krishan.balaji.fh.data.AssetEntry;
import com.krishan.balaji.fh.data.ExpCatEntry;
import com.krishan.balaji.fh.data.ExpSubCatEntry;
import com.krishan.balaji.fh.data.ExpenseEntry;

public class ViewExpenseActivity extends AppCompatActivity {


    private ListView expenses;
    //private TextView noExpenseText;
    private EditText searchText;
    private LoaderManager.LoaderCallbacks<Cursor> expenseCallbacks;

    private ExpenseAdapter expenseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_expense);

        setUpLayout();
        initializeLoader();
        getSupportLoaderManager().initLoader(Util.ViewExpenseActivity_expense_loader_key,null,expenseCallbacks);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(com.krishan.balaji.fh.activities.expense.ViewExpenseActivity.this, com.krishan.balaji.fh.activities.expense.NewExpenseActivity.class);
                com.krishan.balaji.fh.activities.expense.ViewExpenseActivity.this.startActivityForResult(intent,1);
            }
        });
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        expenses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor cursor = expenseAdapter.getCursor();
                Bundle bundle = new Bundle();
                bundle.putLong("id",l);
                Intent intent = new Intent(ViewExpenseActivity.this,EditExpenseActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent,2);
            }
        });

    }

    private void setUpLayout() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        expenses = (ListView) findViewById(R.id.expense_list);
        searchText = (EditText) findViewById(R.id.editText1);
        searchText.setVisibility(View.INVISIBLE);
        searchText.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                Log.e("adskflads","text changed to "+s.toString());
                SimpleCursorAdapter filterAdapter = (SimpleCursorAdapter)expenses.getAdapter();
                filterAdapter.getFilter().filter(s.toString());
            }
        });


    }

    private void initializeLoader() {
        String[] from = {
                ExpenseEntry.COL_DATE,
                ExpCatEntry.COL_NAME,
                ExpSubCatEntry.COL_NAME,
                ExpenseEntry.COL_COMMENT,
                AssetEntry.COL_NAME,
                ExpenseEntry.COL_AMOUNT};
        int [] to = {
                R.id.expense_date,
                R.id.expense_categoryy,
                R.id.expense_subcategory,
                R.id.expense_comment,
                R.id.expense_asset,
                R.id.expense_amount};
        //Todo This has to be a new adapter - cant use subcat here
        expenseAdapter = new ExpenseAdapter(this,R.layout.expense_list_row,null,from,to);
        //expenseAdapter = new SimpleCursorAdapter(this,R.layout.expense_list_row,null,from,to);
        expenseAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence constraint) {
                return getDirectoryList(constraint);
            }
        });
        expenses.setAdapter(expenseAdapter);
        expenseCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader onCreateLoader(int id, Bundle args) {

                /*
                *       new String[]{
                                ExpenseEntry.COL_ID,
                                ExpenseEntry.COL_DATE,
                                ExpenseEntry.COL_COMMENT,
                                ExpenseEntry.COL_CAT,
                                ExpenseEntry.COL_SUBCAT,
                                ExpenseEntry.COL_ASSET,
                                ExpenseEntry.COL_AMOUNT}
                */

                return new CursorLoader(ViewExpenseActivity.this, ExpenseEntry.CONTENT_URI,
                        new String[]{
                                "t1."+ExpenseEntry.COL_ID,
                                "t1."+ExpenseEntry.COL_AMOUNT,
                                "t1."+ExpenseEntry.COL_COMMENT,
                                "t1."+ExpenseEntry.COL_DATE,
                                "t4."+AssetEntry.COL_NAME,
                                "t2."+ExpCatEntry.COL_NAME,
                                "t3."+ExpSubCatEntry.COL_NAME,
                                "t3."+ExpSubCatEntry.COL_IMG}
                  ,
                        null,null,null);
            }

            @Override
            public void onLoadFinished(Loader loader, Cursor data) {
                expenseAdapter.swapCursor(data);

            }

            @Override
            public void onLoaderReset(Loader loader) {
                expenseAdapter.swapCursor(null);
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
            //Intent intent = new Intent(this, com.krishan.balaji.fh.activities.expense.ExpenseCatActivity.class);
            Intent intent = new Intent(this, ExpenseCatActivity.class);
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

    //http://stackoverflow.com/questions/5322412/listview-simplecursoradapter-an-an-edittext-filter-why-wont-it-do-anything
    public Cursor getDirectoryList (CharSequence constraint)  {
        Cursor cursor = null;
        Log.e("asdf"," yahoo1");
        if (constraint == null  ||  constraint.length () == 0)  {
            Log.e("","oops");    //  Return the full list
            cursor = getContentResolver().query(ExpenseEntry.CONTENT_URI,new String[]{
                    "t1."+ExpenseEntry.COL_ID,
                    "t1."+ExpenseEntry.COL_AMOUNT,
                    "t1."+ExpenseEntry.COL_COMMENT,
                    "t1."+ExpenseEntry.COL_DATE,
                    "t4."+AssetEntry.COL_NAME,
                    "t2."+ExpCatEntry.COL_NAME,
                    "t3."+ExpSubCatEntry.COL_NAME,
                    "t3."+ExpSubCatEntry.COL_IMG
            },null,null,null);
        }  else  {
            Log.e("","yahoo2");
            //http://stackoverflow.com/questions/19167954/use-uri-builder-in-android-or-create-url-with-variables
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("content")
                    .authority(FinanceContract.authority)
                    .appendPath(ExpenseEntry.PATH)
                    .appendPath(ExpenseEntry.path_search)
                    .appendQueryParameter("q", constraint.toString());
            String myUrl = builder.build().toString();
            Log.e("","yahoo3  "+builder.build().toString());
            cursor = getContentResolver().query(builder.build(),new String[]{ExpenseEntry.COL_CAT},"",new String[]{},null);
        }
        return cursor;
    }
}