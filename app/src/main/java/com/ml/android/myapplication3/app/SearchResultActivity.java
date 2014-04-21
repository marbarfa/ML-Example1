package com.ml.android.myapplication3.app;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ListView;
import com.ml.android.myapplication3.common.HttpClientHelper;
import com.ml.android.myapplication3.common.SearchAdapter;
import com.ml.android.myapplication3.common.SearchResultRow;

import java.util.List;

public class SearchResultActivity extends android.app.Activity {

    private ListView resultList;
    private static SearchAdapter listAdapter;
    private static String lastSearchString;

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_results);

        resultList = (ListView) findViewById(R.id.resultlist);

        //retrive search string
        if (getIntent() != null && getIntent().getExtras() != null) {

            Bundle extras = getIntent().getExtras();
            String searchString = extras.getString("search_string");

            if (listAdapter == null || (lastSearchString != null && !lastSearchString.equals(searchString))) {
                //resultlist
                searchForMeliItems(searchString);
                lastSearchString = searchString;
            } else {
                resultList.setAdapter(listAdapter);
            }

        } else if (listAdapter != null) {
            resultList.setAdapter(listAdapter);
        }
//        }else if (listAdapter == null){
//            listAdapter = new SearchAdapter(getApplicationContext());
//        }

    }

    private void searchForMeliItems(final String searchString) {
        final ProgressDialog dialog = new android.app.ProgressDialog(this);
        dialog.setMessage("Buscando: " + searchString);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        new android.os.AsyncTask<Void, Void, List<SearchResultRow>>() {

            @Override
            protected List<SearchResultRow> doInBackground(Void... voids) {
                List<SearchResultRow> result = HttpClientHelper.getProductSearch(searchString.trim());
                return result;
            }

            @Override
            protected void onPostExecute(List<SearchResultRow> o) {
                super.onPostExecute(o);
                if (dialog.isShowing())
                    dialog.dismiss();
                if (o.size() > 0) {
                    listAdapter = new SearchAdapter(getApplicationContext());
                    listAdapter.setSearchResults(o);
                    resultList.setAdapter(listAdapter);
                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(SearchResultActivity.this);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            SearchResultActivity.this.finish();
                        }
                    });
                    builder.setMessage("No se han encontrado resultados, intente nuevamente...");
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
                //UPDATE UI
            }


        }.execute();
    }


}
