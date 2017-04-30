package com.krishan.balaji.fh.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.krishan.balaji.fh.R;
import com.krishan.balaji.fh.activities.asset.ViewAssetActivity;
import com.krishan.balaji.fh.activities.asset.ViewTransferActivity;
import com.krishan.balaji.fh.activities.dashboard.DashboardActivity;
import com.krishan.balaji.fh.activities.expense.ViewExpenseActivity;
import com.krishan.balaji.fh.activities.io.IOActivity;
import com.krishan.balaji.fh.adaptetrs.NavigationListViewAdapter;



public class HomeActivity extends AppCompatActivity implements View.OnClickListener{


    private ListView drawerListView;
    private DrawerLayout drawerLayout;
    private ArrayAdapter<String> listViewAdapter;
    private ActionBarDrawerToggle drawerToggle;
    private String mActivityTitle;
    private Button expense,assets,transfers,importExport;//income
    //WebView webView;

    Integer[] itemIds ={R.drawable.logo_chart_line,
            R.drawable.logo_chart_line,
            R.drawable.logo_chart_pie,
            R.drawable.logo_chart_bar,
            R.drawable.logo_chart_bar};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mActivityTitle=getString(R.string.title_home_activity);
        setUpLayout();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }


    private void setUpLayout() {
        drawerListView = (ListView)findViewById(R.id.drawer_list);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        addDrawerItems();
        setupDrawer();
        setupWindowAnimations();
        expense = (Button) findViewById(R.id.nav_exp);
        assets = (Button) findViewById(R.id.nav_asset);
        transfers = (Button) findViewById(R.id.nav_transfer);
        importExport = (Button) findViewById(R.id.nav_impexp);;
        expense.setOnClickListener(this);
        assets.setOnClickListener(this);
        transfers.setOnClickListener(this);
        importExport.setOnClickListener(this);
    }

    private void addDrawerItems() {
        final String[] osArray = getResources().getStringArray(R.array.home_drawer_items);
        listViewAdapter = new NavigationListViewAdapter(this,osArray,itemIds);
        drawerListView.setAdapter(listViewAdapter);
        //TODO why hardcode the values
        drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(HomeActivity.this,DashboardActivity.class);
                intent.putExtra("position",position-1);
                startActivity(intent);
            }
        });
    }

    private void setupDrawer() {
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Activities");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(drawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // Activate the navigation drawer toggle
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == expense.getId()){
            Intent test = new Intent(HomeActivity.this, ViewExpenseActivity.class);
            startActivity(test);
        }
        else if(id == assets.getId()){
            Intent test = new Intent(HomeActivity.this, ViewAssetActivity.class);
            startActivity(test);
        }
        else if (id == importExport.getId()){
            Intent test = new Intent(HomeActivity.this, IOActivity.class);
            startActivity(test);
        }
        else if (id == transfers.getId()){
            Intent intent = new Intent(HomeActivity.this,ViewTransferActivity.class);
            startActivity(intent);
        }
    }
}

