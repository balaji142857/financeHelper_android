package com.krishan.balaji.fh.activities.expense;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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
import android.widget.ListView;
import android.widget.TextView;

import com.krishan.balaji.fh.R;
import com.krishan.balaji.fh.data.ExpCatEntry;
import com.krishan.balaji.fh.util.Util;

public class ExpenseCatActivity extends AppCompatActivity implements  View.OnClickListener{

    ListView expenseCategories;
    LoaderManager.LoaderCallbacks<Cursor> catCallbacks;

    CursorAdapter catAdapter;
    TextView category;
    Button addButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_cat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setUpLayout();
        initalizeLoaders();
        getSupportLoaderManager().initLoader(Util.ExpenseCatActivity_catCallBacksKey, null, catCallbacks);

    }

    private void setUpLayout() {
        expenseCategories = (ListView) findViewById(R.id.expense_cat_list);
        category = (TextView) findViewById(R.id.new_cat);
        addButton = (Button) findViewById(R.id.new_cat_add);
        addButton.setOnClickListener(this);
    }

    private void initalizeLoaders() {
        catCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                return new CursorLoader(ExpenseCatActivity.this, ExpCatEntry.CONTENT_URI,
                        new String[]{ExpCatEntry.COL_ID,
                        ExpCatEntry.COL_NAME},
                        null,null,null);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                catAdapter.swapCursor(data);

            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                catAdapter.swapCursor(null);
            }
        };
        String[] from ={ExpCatEntry.COL_NAME};
        int[] to  = {android.R.id.text1};
        catAdapter = new SimpleCursorAdapter(ExpenseCatActivity.this,android.R.layout.simple_list_item_1,null,from,to);
        expenseCategories.setAdapter(catAdapter);

        expenseCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor cursor = catAdapter.getCursor();
                Bundle bundle = new Bundle();
                bundle.putString("categoryName",((TextView)view.findViewById(android.R.id.text1)).getText().toString());
                bundle.putLong("categoryId",l);
                Intent intent = new Intent(ExpenseCatActivity.this,ExpenseSubCatActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent,2);

            }
        });
    }


    @Override
    public void onClick(View view) {
        if(view.getId() == addButton.getId()){
            ContentValues values = new ContentValues();;
            values.put(ExpCatEntry.COL_NAME,category.getText().toString());
            getContentResolver().insert(ExpCatEntry.CONTENT_URI,values);
            Snackbar.make(findViewById(R.id.root_view),"New CategoryModel "+category.getText().toString()+ " added",Snackbar.LENGTH_LONG).show();
            category.setText("");
        }
    }
}