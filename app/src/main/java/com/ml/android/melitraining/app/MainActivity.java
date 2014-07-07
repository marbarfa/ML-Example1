package com.ml.android.melitraining.app;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.ml.android.melitraining.common.HttpUtils;
import com.ml.android.melitraining.services.StartServiceReceiver;
import com.squareup.picasso.Picasso;

import java.util.Calendar;


public class MainActivity extends SherlockActivity {

    // restart service every 30 seconds
    private static final long REPEAT_TIME = 1000 * 30;
    private static final String SEARCH_QUERY_KEY = "SEARCH_QUERY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String s = Environment.getExternalStorageDirectory().getAbsolutePath();
        Log.i("MAIN-TRAINING", "External Absolute Path: "+s);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.button1).setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchStr = ((AutoCompleteTextView) findViewById(R.id.input)).getText().toString();
                saveSearchQuery(searchStr);
                searchAction(searchStr);
            }
        });
        Picasso.with(this).setDebugging(true);

        String lastQuery = getSearchQuery();
        if (lastQuery != null){
            ((AutoCompleteTextView)findViewById(R.id.input)).setCompletionHint(lastQuery);
        }

        startBookmarksService();
    }


    private void saveSearchQuery(String query){
        SharedPreferences sp = this.getSharedPreferences(SEARCH_QUERY_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(SEARCH_QUERY_KEY, query);
        editor.commit();
    }

    private String getSearchQuery(){
        SharedPreferences sp = this.getSharedPreferences(SEARCH_QUERY_KEY, Context.MODE_PRIVATE);
        String query = sp.getString(SEARCH_QUERY_KEY, null);
        return query;
    }

    private void startBookmarksService(){
        AlarmManager service = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(this, StartServiceReceiver.class);
        PendingIntent pending = PendingIntent.getBroadcast(this, 0, i,
                PendingIntent.FLAG_CANCEL_CURRENT);
        Calendar cal = Calendar.getInstance();
        // start 30 seconds after boot completed
        cal.add(Calendar.SECOND, 30);
        // fetch every 30 seconds
        // InexactRepeating allows Android to optimize the energy consumption
        service.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                cal.getTimeInMillis(), REPEAT_TIME, pending);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        getSupportMenuInflater().inflate(R.menu.home_menu, menu);
        setupSearchActionbar(menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search: {
//                 searchAction();
                return false;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void setupSearchActionbar(final Menu menu) {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        menu.findItem(R.id.search).setActionView(new SearchView(this));
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setQueryHint("Buscar...");
        SearchableInfo info = searchManager.getSearchableInfo(getComponentName());
        searchView.setSearchableInfo(info);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                menu.findItem(R.id.search).collapseActionView();
                searchAction(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                menu.findItem(R.id.search).collapseActionView();
                return false;
            }
        });

    }


    private void searchAction(String query) {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(MainActivity.this.CONNECTIVITY_SERVICE);
        if (HttpUtils.isConnected(connMgr)) {
            android.content.Intent i = new android.content.Intent(MainActivity.this, SearchResultActivity.class);
            i.putExtra("search_string", query);
            startActivity(i);
        } else {
            Toast t = Toast.makeText(MainActivity.this, "You are not connected to the internet!", Toast.LENGTH_LONG);
            t.show();
        }
    }

}
