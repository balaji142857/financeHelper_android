package com.krishan.balaji.fh.activities.asset;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.krishan.balaji.fh.R;
import com.krishan.balaji.fh.adaptetrs.TransferViewAdapter;
import com.krishan.balaji.fh.data.AssetEntry;
import com.krishan.balaji.fh.data.TransferEntry;
import com.krishan.balaji.fh.util.Util;

public class ViewTransferActivity extends AppCompatActivity {


    TransferViewAdapter transferAdapter;
    LoaderManager.LoaderCallbacks<Cursor> transferCallbacks;
    FloatingActionButton fab;
    ListView transfers;
    //final int transfer_added_result =10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_transfer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setUpLayout();
        initializeCallbacks();
        getSupportLoaderManager().initLoader(Util.ViewTrasnferActivity_assetCallBacksKey,null,transferCallbacks);
    }

    private void setUpLayout() {
        transfers= (ListView)findViewById(R.id.transfer_list);
        fab= (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewTransferActivity.this,NewTransferActivity.class);
                startActivity(intent);
                //startActivityForResult(intent,transfer_added_result);
            }
        });
    }

    private void initializeCallbacks() {
        String[] from ={TransferEntry.COL_COMMENT};
        int[] to = {R.id.transfer_comment};
        transferAdapter = new TransferViewAdapter(this,R.layout.transfer_list_row,null,from,to);
        transfers.setAdapter(transferAdapter);
        transferCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                return new CursorLoader(ViewTransferActivity.this,
                        TransferEntry.CONTENT_URI,
                        new String[]{TransferEntry.COL_ID,
                        "t2."+ AssetEntry.COL_NAME,
                        "t3."+ AssetEntry.COL_NAME,
                        TransferEntry.COL_AMOUNT,
                        TransferEntry.COL_DATETIME,
                        TransferEntry.COL_COMMENT},
                        null,null,null);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                Log.e("adfsf","viewactivity onloadFinished: "+data.getCount());
                transferAdapter.swapCursor(data);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                transferAdapter.swapCursor(null);
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader(Util.ViewTrasnferActivity_assetCallBacksKey,null,transferCallbacks);
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == transfer_added_result && resultCode==RESULT_OK){
            Snackbar.make(findViewById(R.id.root_view),"Transfer added successfully",Snackbar.LENGTH_LONG).show();
        }
    }*/
}