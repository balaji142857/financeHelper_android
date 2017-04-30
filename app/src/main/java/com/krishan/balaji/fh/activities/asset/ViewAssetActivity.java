package com.krishan.balaji.fh.activities.asset;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.krishan.balaji.fh.R;
import com.krishan.balaji.fh.adaptetrs.ViewAssetAdapter;
import com.krishan.balaji.fh.util.Util;
import com.krishan.balaji.fh.data.AssetEntry;

import static android.support.v4.app.ActivityOptionsCompat.makeSceneTransitionAnimation;

public class ViewAssetActivity extends AppCompatActivity {

    ListView assetList;
    //CursorAdapter assetAdapter;
    ViewAssetAdapter assetAdapter;
    LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_asset);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initializeLoaderManager();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewAssetActivity.this,NewAssetActivity.class);
                startActivityForResult(intent,1);
            }
        });

        assetList = (ListView) findViewById(R.id.asset_list);
        String[] from ={AssetEntry.COL_NAME,
                AssetEntry.COL_BALANCE};
        int[] to = {R.id.asset_name,R.id.asset_balance};
        assetAdapter = new ViewAssetAdapter(this,R.layout.asset_list_row,null,from,to);
        assetList.setAdapter(assetAdapter);
        getSupportLoaderManager().initLoader(Util.ViewAssetActivity_asset_loader_key, null, loaderCallbacks);
        assetList.addHeaderView(getLayoutInflater().inflate(R.layout.asset_list_header,null));
        assetList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor cursor = assetAdapter.getCursor();
                Bundle bundle = new Bundle();
                bundle.putString("balance",((TextView)view.findViewById(R.id.asset_balance)).getText().toString());
                bundle.putString("name",((TextView)view.findViewById(R.id.asset_name)).getText().toString());
                bundle.putInt("id",cursor.getInt(cursor.getColumnIndex(AssetEntry.COL_ID)));
                TextView assetBalance = (TextView)view.findViewById(R.id.asset_balance);
                Intent intent = new Intent(ViewAssetActivity.this,EditAssetActivity.class);
                /*ActivityOptionsCompat options =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(ViewAssetActivity.this, assetBalance, getString(R.string.activity_image_trans));
                startActivityForResult(intent, 2,options.toBundle());*/

                intent.putExtras(bundle);
                startActivityForResult(intent,2);
            }
        });
        setupWindowAnimations();
    }


    private void initializeLoaderManager() {
        loaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                return new CursorLoader(ViewAssetActivity.this, AssetEntry.CONTENT_URI,
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode ==1 && resultCode == Activity.RESULT_OK){
            Snackbar.make(findViewById(R.id.root_view), "New Asset " + data.getStringExtra("result") +" created", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
        if(requestCode ==2 && resultCode == Activity.RESULT_OK){
            String newAssetName = data.getStringExtra("result");
            int id = data.getIntExtra("id",-1);

            ContentValues values=  new ContentValues();
            values.put(AssetEntry.COL_NAME,newAssetName);
            getContentResolver().update(AssetEntry.CONTENT_URI,values,"_id = ?",new String[]{id+""});
            Snackbar.make(findViewById(R.id.root_view), "Asset " + data.getStringExtra("initial") +" has been edited to " + newAssetName, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    private void setupWindowAnimations() {
        // Re-enter transition is executed when returning to this activity
        Slide slideTransition = new Slide();
        slideTransition.setSlideEdge(Gravity.LEFT);
        slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
        getWindow().setReenterTransition(slideTransition);
        getWindow().setExitTransition(slideTransition);
    }

}