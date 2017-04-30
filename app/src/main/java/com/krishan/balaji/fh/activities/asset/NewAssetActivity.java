package com.krishan.balaji.fh.activities.asset;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.krishan.balaji.fh.R;
import com.krishan.balaji.fh.util.Util;
import com.krishan.balaji.fh.data.AssetEntry;

import java.util.Calendar;

public class NewAssetActivity extends AppCompatActivity implements View.OnClickListener{


    private EditText name,balance;
    private Button create,cancel;
    Calendar createdDate = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_new);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        name = (EditText) findViewById(R.id.asset_name);
        balance= (EditText) findViewById(R.id.asset_balance);
        create = (Button) findViewById(R.id.asset_create);
        cancel = (Button) findViewById(R.id.cancel);
        create.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == create.getId()){
            String a = name.getText().toString();
            float amt = 0f;
            if(name.getText().toString() != null && !name.getText().toString().toString().equals("")){
                if(balance.getText().toString() ==null || balance.getText().toString().toString().equals(""))
                    amt = 0f;
                else
                    amt = Float.parseFloat(balance.getText().toString());
                if(amt<0 && !Util.allowNegativeBalance){
                    Snackbar.make(view, "Negative Balance not accepted.", Snackbar.LENGTH_LONG).show();
                }else{
                    ContentResolver cr = getContentResolver ();
                    ContentValues values = new ContentValues();
                    values.put(AssetEntry.COL_NAME, a);
                    values.put(AssetEntry.COL_BALANCE, amt);
                    Uri uri = cr.insert(AssetEntry.CONTENT_URI,values);
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result",a);
                    setResult(Activity.RESULT_OK,returnIntent);
                    finish();
                }
            }
            else
                Snackbar.make(view, "Asset Name is required", Snackbar.LENGTH_LONG).show();
        }
        if(view.getId() == cancel.getId()){
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_OK,returnIntent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}