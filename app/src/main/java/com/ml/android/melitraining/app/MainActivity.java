package com.ml.android.melitraining.app;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.ml.android.melitraining.common.HttpUtils;


public class MainActivity extends SherlockActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button1).setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                searchAction();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        com.actionbarsherlock.view.MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);

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

                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void setupSearchActionbar(final Menu menu) {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setQueryHint("Buscar...");
        SearchableInfo info = searchManager.getSearchableInfo(getComponentName());
        searchView.setSearchableInfo(info);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                menu.findItem(R.id.search).collapseActionView();
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


    private void searchAction() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(MainActivity.this.CONNECTIVITY_SERVICE);
        if (HttpUtils.isConnected(connMgr)) {
            String searchStr = ((TextView) findViewById(R.id.input)).getText().toString();
            android.content.Intent i = new android.content.Intent(MainActivity.this, SearchResultActivity.class);
            i.putExtra("search_string", searchStr);
            startActivity(i);
        } else {
            Toast t = Toast.makeText(MainActivity.this, "You are not connected to the internet!", Toast.LENGTH_LONG);
            t.show();
        }
    }

}
