package com.rohit.android.newsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.renderscript.ScriptGroup;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{
    public NewsAdapter madapter;
    public static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivity.context = getApplicationContext();

        final Button button = findViewById(R.id.search);
        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                EditText searchbar = findViewById(R.id.searchbar);
                final String query = searchbar.getText().toString();
                //For checking whether the user is connected to internet
                ConnectivityManager cm = (ConnectivityManager)MainActivity.context.getSystemService(Context.CONNECTIVITY_SERVICE);
                if(query.equals("")){
                    Toast.makeText(MainActivity.this,"Please enter a topic of interest",Toast.LENGTH_LONG).show();
                }
                else if(cm.getActiveNetwork()== null){
                    Toast.makeText(MainActivity.context,"You are not connected to internet, Please connect to internet and try again",Toast.LENGTH_LONG).show();
                }
                else{
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(button.getWindowToken(),InputMethodManager.RESULT_UNCHANGED_SHOWN);
                fetchdata fla = new fetchdata();
                fla.execute(query);
                }
            }
        });


        madapter = new NewsAdapter(this, new ArrayList<News>());
        final ListView listView = findViewById(R.id.list);
        listView.setAdapter(madapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               News currentNews = madapter.getItem(i);
               Uri url = Uri.parse(currentNews.getUrl());
               Intent intent = new Intent(Intent.ACTION_VIEW);
               intent.setData(url);
               startActivity(intent);
            }
        });

       setupPreference();

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if(s.equals("bold")){
            LinearLayout bla = findViewById(R.id.linear);
            if(sharedPreferences.getBoolean(s,true)){
            bla.setBackgroundColor(getResources().getColor(R.color.blue));}
        }
    }

    public class fetchdata extends AsyncTask<String,Void,ArrayList<News>>{

        @Override
        protected ArrayList<News> doInBackground(String... strings) {
            return NetworkUtils.performOperations(strings[0]);

        }

        @Override
        protected void onPostExecute(ArrayList<News> news) {
            madapter.clear();
            madapter.addAll(news);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.settings_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_settings){
            Intent actionSettingsIntent = new Intent(this,SettingsActivity.class);
            startActivity(actionSettingsIntent);}
            return true;

    }

    public void setupPreference(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        if(sharedPreferences.getBoolean("bold",true)){
            LinearLayout bla = findViewById(R.id.linear);
            bla.setBackgroundColor(getResources().getColor(R.color.blue));
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }
}