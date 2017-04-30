package com.krishan.balaji.fh.activities.io;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.krishan.balaji.fh.R;
import com.krishan.balaji.fh.data.AssetEntry;
import com.krishan.balaji.fh.data.ExpCatEntry;
import com.krishan.balaji.fh.data.ExpSubCatEntry;
import com.krishan.balaji.fh.fragments.DatePickerFragment;
import com.krishan.balaji.fh.model.AssetModel;
import com.krishan.balaji.fh.model.CategoryModel;
import com.krishan.balaji.fh.model.ExpIncModel;
import com.krishan.balaji.fh.model.SubCategoryModel;
import com.krishan.balaji.fh.util.DirectoryChooserDialog;
import com.krishan.balaji.fh.util.Util;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class IOActivity extends AppCompatActivity  implements  View.OnClickListener, DatePickerDialog.OnDateSetListener, FileChooserFragment.OnFileChoosen{

    Button destChooser,start;
    Spinner expImpChooser;
    EditText startDate,endDate;
    ImageButton startDateChooser,endDateChooser,fileChooser;
    LinearLayout selectedFileLayout;
    TextView selectedFileTextView;
    int id = 1;
    Calendar fromDate = null,toDate=null;
    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;
    private static final int START_DATE =1;
    private static final int END_DATE =2;
    private static final int NONE =-1;
    int datePickerUsed = NONE;
    String[] options ={"Export","Import"};
    int selectedPosition;
    String fileName;
    static List<File> entries = new ArrayList<>();

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expimp);
        setUpLayout();

    }

    private void setUpLayout() {
        selectedFileLayout = (LinearLayout) findViewById(R.id.selected_file_layout);
        //selectedFileLayout.setVisibility(View.INVISIBLE);
        selectedFileTextView = (TextView)selectedFileLayout.findViewById(R.id.output_file);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        start = (Button) findViewById(R.id.Start);
        start.setOnClickListener(this);
        expImpChooser = (Spinner)findViewById(R.id.exp_imp_choooser);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,android.R.id.text1,options);
        expImpChooser.setAdapter(adapter);
        expImpChooser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPosition = position;
                start.setText("Start " + options[position]);
                selectedFileTextView.setText("");
                if(selectedPosition==1)
                    fileChooser.setVisibility(View.VISIBLE);
                else
                    fileChooser.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        fileChooser = (ImageButton) findViewById(R.id.fileChooser);
        fileChooser.setVisibility(View.INVISIBLE);
        fileChooser.setOnClickListener(this);
        startDateChooser = (ImageButton)findViewById(R.id.start_date_button);
        endDateChooser = (ImageButton)findViewById(R.id.end_date_picker);
        startDateChooser.setOnClickListener(this);
        endDateChooser.setOnClickListener(this);
        startDate = (EditText)findViewById(R.id.start_date);
        endDate= (EditText)findViewById(R.id.end_date);

    }

    @Override
    public void onClick(View view) {
        if(view.getId()==start.getId()){
            mNotifyManager= (NotificationManager) this.getSystemService(this.NOTIFICATION_SERVICE);
            mBuilder = new NotificationCompat.Builder(IOActivity.this);
            mBuilder.setContentTitle(options[selectedPosition])
                    .setContentText(options[selectedPosition]+" in progress")
                    .setSmallIcon(R.drawable.logo_import_export);
            if(selectedPosition==1 && fileName==null)
                Snackbar.make(findViewById(R.id.root_view),"First Choose File to import",Snackbar.LENGTH_LONG).show();
            else
                new ExportImportManager().execute();
        }
        else if(view.getId() == startDateChooser.getId()){
            DialogFragment newFragment = new DatePickerFragment();
            newFragment.show(getSupportFragmentManager(), "datePicker");
            datePickerUsed=START_DATE;
        }
        else if(view.getId() == endDateChooser.getId()){
            DialogFragment newFragment = new DatePickerFragment();
            newFragment.show(getSupportFragmentManager(), "datePicker");
            datePickerUsed=END_DATE;
        }
        else if(view.getId()== fileChooser.getId()){
            verifyStoragePermissions(this);
            File rootDir, appDir;
            rootDir = android.os.Environment.getExternalStorageDirectory();
            appDir = new File(rootDir.getAbsolutePath() + "/financeHelper");
            if (appDir.exists()) {
                entries.clear();
                File[] dirs = appDir.listFiles();
                getFiles(dirs);
                String[] filesToDisplay = convertToStringArray(entries);
                if(filesToDisplay.length > 0){
                    FileChooserFragment fragment =  FileChooserFragment.newInstance(filesToDisplay);
                    fragment.show(getFragmentManager(), "FileChooser Fragment");
                }else{
                    Snackbar.make(findViewById(R.id.root_view),"No files found to import",Snackbar.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        if(datePickerUsed==START_DATE){
            if(null == fromDate)
                fromDate = Calendar.getInstance();
            fromDate.set(Calendar.YEAR,year);
            fromDate.set(Calendar.MONTH,monthOfYear);
            fromDate.set(Calendar.DATE,dayOfMonth);
            startDate.setText(Util.displayFormat.format(fromDate.getTime()));
        }
        else if(datePickerUsed==END_DATE){
            if(null == toDate)
                toDate = Calendar.getInstance();
            toDate.set(Calendar.YEAR,year);
            toDate.set(Calendar.MONTH,monthOfYear);
            toDate.set(Calendar.DATE,dayOfMonth);
            endDate.setText(Util.displayFormat.format(toDate.getTime()));
        }
        datePickerUsed=NONE;
    }

    @Override
    public void setOnFileChosen(String arg) {
        Log.e("onFileChosenCalled",arg+" is the argument");
        if(arg!=null && arg.trim().length()>0){
            fileName=arg;
            selectedFileLayout.setVisibility(View.VISIBLE);
            selectedFileTextView.setText(fileName);
        }

    }


    private class ExportImportManager extends AsyncTask<Void, Integer, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Displays the progress bar for the first time.
            start.setEnabled(false);
            mBuilder.setProgress(100, 0, false);
            mNotifyManager.notify(id, mBuilder.build());
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // Update progress
            mBuilder.setProgress(100, values[0], false);
            mNotifyManager.notify(id, mBuilder.build());
            super.onProgressUpdate(values);
        }

        @Override
        protected Integer doInBackground(Void... params) {
            try{
                if(selectedPosition==0)
                    fileName = ExportHelper.doSomething(IOActivity.this);
                else {
                    ImportHelper.doSomething(IOActivity.this,fileName);
                }
            }
            catch(Exception e){e.printStackTrace();}
            return null;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            mBuilder.setContentText(options[selectedPosition]+" complete");
            mBuilder.setProgress(0, 0, false);
            mNotifyManager.notify(id, mBuilder.build());
            start.setEnabled(true);
            if(fileName!=null){
                selectedFileLayout.setVisibility(View.VISIBLE);
                ((TextView)selectedFileLayout.findViewById(R.id.output_file)).setText(fileName);
            }

        }
    }

    private void checkExternalMedia(){
        boolean mExternalStorageAvailable = false;
        boolean mExternalStorageWriteable = false;
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // Can read and write the media
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // Can only read the media
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else {
            // Can't read or write
            mExternalStorageAvailable = mExternalStorageWriteable = false;
        }
        Log.e("afd","\n\nExternal Media: readable="
                +mExternalStorageAvailable+" writable="+mExternalStorageWriteable);
    }

    private void writeToSDFile(){

        // Find the root of the external storage.
        // See http://developer.android.com/guide/topics/data/data-  storage.html#filesExternal

        File root = android.os.Environment.getExternalStorageDirectory();
        Log.e("af","\nExternal file system root: "+root);

        // See http://stackoverflow.com/questions/3551821/android-write-to-sd-card-folder

        File dir = new File (root.getAbsolutePath() + "/download");
        dir.mkdirs();
        File file = new File(dir, "myData.txt");

        try {
            FileOutputStream f = new FileOutputStream(file);
            PrintWriter pw = new PrintWriter(f);
            pw.println("Hi , How are you");
            pw.println("Hello");
            pw.flush();
            pw.close();
            f.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i("adsf", "******* File not found. Did you" +
                    " add a WRITE_EXTERNAL_STORAGE permission to the   manifest?");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e("df","\n\nFile written to "+file);
    }

    private void readRaw(){
        Log.e("adsf","\nData read from res/raw/textfile.txt:");
        InputStream is = null;//this.getResources().openRawResource(R.raw.textfile);
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr, 8192);    // 2nd arg is buffer size

        // More efficient (less readable) implementation of above is the composite expression
    /*BufferedReader br = new BufferedReader(new InputStreamReader(
            this.getResources().openRawResource(R.raw.textfile)), 8192);*/

        try {
            String test;
            while (true){
                test = br.readLine();
                // readLine() returns null if no more lines in the file
                if(test == null) break;
                Log.e("sf","\n"+"    "+test);
            }
            isr.close();
            is.close();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e("sf","\n\nThat is all");
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    private static String[] convertToStringArray(List<File> files) {
        String[] result = new String[files.size()];
        int count=0;
        for(File file : files){
            result[count]=file.getAbsolutePath();
            count++;
        }

        return result;
    }

    private static void getFiles(File[] files) {
        for(File file : files){
            if(file.isDirectory())
                getFiles(file.listFiles());
            else if(file.getName().endsWith(".xls"))
                entries.add(file);
        }
    }



}

/*private void export(){
        DirectoryChooserDialog directoryChooserDialog =
                new DirectoryChooserDialog(IOActivity.this,
                        new DirectoryChooserDialog.ChosenDirectoryListener()
                        {
                            @Override
                            public void onChosenDir(String chosenDir)
                            {
                                m_chosenDir[0] = chosenDir;
                             }
                        });

        directoryChooserDialog.setNewFolderEnabled(m_newFolderEnabled);
        directoryChooserDialog.chooseDirectory(m_chosenDir[0]);
        m_newFolderEnabled = ! m_newFolderEnabled;
    }*/
