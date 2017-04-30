package com.krishan.balaji.fh.activities.asset;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.krishan.balaji.fh.R;
import com.krishan.balaji.fh.data.AssetEntry;
import com.krishan.balaji.fh.data.ExpenseEntry;
//import com.krishan.balaji.fh.data.IncomeEntry;
import com.krishan.balaji.fh.data.TransferEntry;


public class EditAssetActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText name;
    private TextView lastModified,balance;
    private Button edit,cancel;
    private String existingName;
    private int assetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_asset);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        name  = (EditText) findViewById(R.id.asset_name);
        lastModified = (TextView) findViewById(R.id.asset_last_updated);
        balance = (TextView) findViewById(R.id.asset_balance);
        edit = (Button) findViewById(R.id.edit);
        cancel = (Button) findViewById(R.id.cancel);
        edit.setOnClickListener(this);
        cancel.setOnClickListener(this);
        Bundle bundle = getIntent().getExtras();
        lastModified.setText(bundle.getString("date"));
        balance.setText(bundle.getString("balance"));
        name.setText(bundle.getString("name"));
        assetId = bundle.getInt("id");
        existingName = name.getText().toString();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == edit.getId()){
            if(name.getText()==null || name.getText().toString().equals(""))
                Snackbar.make(findViewById(R.id.root_view),"Enter a valid name",Snackbar.LENGTH_LONG).show();
            else if (!name.getText().toString().equals(existingName)){
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result",name.getText().toString());
                returnIntent.putExtra("initial",existingName);
                returnIntent.putExtra("id",assetId);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }else{
                Snackbar.make(findViewById(R.id.root_view),"No edits were made",Snackbar.LENGTH_LONG).show();
            }
        }
        else if(view.getId() == cancel.getId()){
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.inc_exp_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.delete_menu) {
            //Todo maybe show a popup for confirmation or a snackbar to undo it ?
            int dependencies = 0;
            Cursor temp = getContentResolver().query(ExpenseEntry.CONTENT_URI,new String[]{},null,null,null);
            dependencies+=temp.getCount();
            temp.close();
            /*temp = getContentResolver().query(IncomeEntry.CONTENT_URI,new String[]{},null,null,null);
            dependencies+=temp.getCount();
            temp.close();*/
            temp = getContentResolver().query(TransferEntry.CONTENT_URI,new String[]{},null,null,null);
            dependencies+=temp.getCount();
            temp.close();
            if(dependencies == 0){
                getContentResolver().delete(AssetEntry.CONTENT_URI,AssetEntry.COL_ID+" = ? ",new String[]{assetId+""});
                finish();
            }else
                Snackbar.make(findViewById(R.id.root_view),"Cannot delete an asset which is associated with expense/income/transfers",Snackbar.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}