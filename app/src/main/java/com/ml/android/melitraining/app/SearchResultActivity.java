package com.ml.android.melitraining.app;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import com.ml.android.melitraining.common.SearchAdapter;
import com.ml.android.melitraining.dto.SearchResultRowDTO;
import com.ml.android.melitraining.net.MeliSearchApi;

import java.util.List;

public class SearchResultActivity extends android.app.Activity {

    private ListView resultList;
    private static SearchAdapter listAdapter;
    private static String lastSearchString;
    private static ProgressBar progressBar;

    private static int offset = 0;
    private static int limit = 15;

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_results);

        resultList = (ListView) findViewById(R.id.resultlist);
        progressBar = (ProgressBar)findViewById(R.id.search_items_progressbar);

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
        new android.os.AsyncTask<Void, Void, List<SearchResultRowDTO>>() {

            @Override
            protected List<SearchResultRowDTO> doInBackground(Void... voids) {
                List<SearchResultRowDTO> result = MeliSearchApi.getProductSearch(searchString.trim(), offset, limit);
                return result;
            }

            @Override
            protected void onPostExecute(List<SearchResultRowDTO> o) {
                super.onPostExecute(o);
                if (progressBar!=null){
                    progressBar.setVisibility(View.GONE);
                }
                if (o.size() > 0) {
                    if (listAdapter == null){
                        listAdapter = new SearchAdapter(getApplicationContext(),SearchResultActivity.this);
                        listAdapter.searchResults.addAll(o);
                        resultList.setAdapter(listAdapter);
                        resultList.setOnScrollListener(new AbsListView.OnScrollListener() {
                            @Override
                            public void onScrollStateChanged(AbsListView absListView, int i) {

                            }

                            @Override
                            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount,
                                                 int totalItemCount) {
                                int position = firstVisibleItem+visibleItemCount;

                                // Check if bottom has been reached
                                if( position == totalItemCount && totalItemCount>0)
                                {
                                    progressBar.setVisibility(View.VISIBLE);
                                    SearchResultActivity.this.searchForMeliItems(searchString);

                                }
                            }
                        });
                    }else{
                        listAdapter.searchResults.addAll(o);
                    }
                    offset +=o.size();
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
