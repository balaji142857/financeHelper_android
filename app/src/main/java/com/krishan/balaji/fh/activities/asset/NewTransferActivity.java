package com.krishan.balaji.fh.activities.asset;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.krishan.balaji.fh.R;
import com.krishan.balaji.fh.adaptetrs.AssetSpinnerAdapter;
import com.krishan.balaji.fh.data.AssetEntry;
import com.krishan.balaji.fh.data.TransferEntry;
import com.krishan.balaji.fh.util.Util;

import java.util.Calendar;

public class NewTransferActivity extends AppCompatActivity implements View.OnClickListener{

    Spinner fromAsset,toAsset;
    Button transfer,cancel;
    EditText amount,comment;
    AssetSpinnerAdapter fromAdapter = null;
    LoaderManager.LoaderCallbacks<Cursor> fromCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_assset);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setUpLayout();
        setUpLoaders();
        getSupportLoaderManager().initLoader(Util.TrasnferAssetActivity_fromAssetCallBacksKey,null,fromCallbacks);
        //getSupportLoaderManager().initLoader(Util.TrasnferAssetActivity_toAssetCallBacksKey,null,toCallbacks);
    }



    private void setUpLayout() {
        fromAsset = (Spinner)findViewById(R.id.fromAsset);
        toAsset = (Spinner)findViewById(R.id.toAsset);
        transfer= (Button) findViewById(R.id.transfer);
        cancel = (Button)findViewById(R.id.cancel);
        amount = (EditText)findViewById(R.id.transferAmount);
        comment = (EditText)findViewById(R.id.transferComment);
        transfer.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    private void setUpLoaders() {
        String[] from = {AssetEntry.COL_NAME};
        int[] to ={android.R.id.text1};
        fromAdapter = new AssetSpinnerAdapter(NewTransferActivity.this,
                android.R.layout.simple_list_item_1,
                null,
                from,
                to);
        fromCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                return new CursorLoader(
                        NewTransferActivity.this,
                        AssetEntry.CONTENT_URI,
                        new String[]{AssetEntry.COL_ID,AssetEntry.COL_NAME,AssetEntry.COL_BALANCE},
                        null,
                        null,
                        null
                );
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                fromAdapter.swapCursor(data);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                fromAdapter.swapCursor(null);
            }
        };

        fromAsset.setAdapter(fromAdapter);
        toAsset.setAdapter(fromAdapter);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == transfer.getId()){
            if(fromAsset.getSelectedItemId()!=toAsset.getSelectedItemId()){
                if(!"".equals(amount.getText().toString())) {
                    float txAmount = Float.parseFloat(amount.getText().toString());
                    Cursor fromCursor = getContentResolver().query(AssetEntry.CONTENT_URI, new String[]{AssetEntry.COL_ID, AssetEntry.COL_NAME, AssetEntry.COL_BALANCE}, AssetEntry.COL_ID + " = ? ", new String[]{fromAsset.getSelectedItemId() + ""}, null);
                    Cursor toCursor = getContentResolver().query(AssetEntry.CONTENT_URI, new String[]{AssetEntry.COL_ID, AssetEntry.COL_NAME, AssetEntry.COL_BALANCE}, AssetEntry.COL_ID + " = ? ", new String[]{toAsset.getSelectedItemId() + ""}, null);
                    fromCursor.moveToFirst();
                    toCursor.moveToFirst();
                    float fromStart = fromCursor.getFloat(fromCursor.getColumnIndex(AssetEntry.COL_BALANCE));
                    float toStart = toCursor.getFloat(toCursor.getColumnIndex(AssetEntry.COL_BALANCE));
                    fromCursor.close();
                    toCursor.close();
                    if(fromStart >= txAmount)
                    {
                            ContentValues values = new ContentValues();
                            values.put(AssetEntry.COL_ID, fromAsset.getSelectedItemId());
                            values.put(AssetEntry.COL_BALANCE, fromStart - txAmount);
                            getContentResolver().update(AssetEntry.CONTENT_URI, values, AssetEntry.COL_ID + " = ? ", new String[]{fromAsset.getSelectedItemId() + ""});
                            values = new ContentValues();
                            values.put(AssetEntry.COL_ID, toAsset.getSelectedItemId());
                            values.put(AssetEntry.COL_BALANCE, toStart + txAmount);
                            getContentResolver().update(AssetEntry.CONTENT_URI, values, AssetEntry.COL_ID + " = ? ", new String[]{toAsset.getSelectedItemId() + ""});
                            values = new ContentValues();
                            values.put(TransferEntry.COL_AMOUNT,txAmount);
                            values.put(TransferEntry.COL_COMMENT,comment.getText().toString());
                            values.put(TransferEntry.COL_FROM,fromAsset.getSelectedItemId());
                            values.put(TransferEntry.COL_TO,toAsset.getSelectedItemId());
                            values.put(TransferEntry.COL_DATETIME,Util.dbFormat.format(Calendar.getInstance().getTime()));
                            getContentResolver().insert(TransferEntry.CONTENT_URI,values);
                            finish();
                    }
                    else
                        Snackbar.make(findViewById(R.id.root_view),"Insufficient balance to transfer",Snackbar.LENGTH_LONG).show();
                }
                else
                    Snackbar.make(findViewById(R.id.root_view),"Enter a valid Amount",Snackbar.LENGTH_LONG).show();
            }
            else
                Snackbar.make(findViewById(R.id.root_view),"Select different assets",Snackbar.LENGTH_LONG).show();
        }
        else if(view.getId()==cancel.getId())
            finish();
    }
}