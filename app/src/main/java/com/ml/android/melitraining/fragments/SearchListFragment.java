package com.ml.android.melitraining.fragments;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import com.ml.android.melitraining.app.R;
import com.ml.android.melitraining.common.ICallbackHandler;
import com.ml.android.melitraining.common.SearchAdapter;
import com.ml.android.melitraining.dto.SearchResultRowDTO;
import com.ml.android.melitraining.net.MeliSearchApi;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 */
public class SearchListFragment extends android.support.v4.app.Fragment {

    private static int offset = 0;
    private static int limit = 15;

    private AbsListView mListView;
    private static SearchAdapter listAdapter;
    private ProgressBar progressBar;

    private String searchString;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SearchListFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listAdapter = new SearchAdapter(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_result_fragment, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(R.id.resultlist);
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
                int position = firstVisibleItem + visibleItemCount;

                // Check if bottom has been reached
                if (position == totalItemCount && totalItemCount > 0) {
                    progressBar.setVisibility(View.VISIBLE);
                    searchMeliItems(null);
                }
            }
        });
        if (listAdapter == null){
            listAdapter = new SearchAdapter(getActivity());
        }else {
            mListView.setAdapter(listAdapter);
        }

        return view;
    }


    public void searchMeliItems(final String searchStr) {
        if (searchStr != null && (this.searchString == null ||
                !this.searchString.equals(searchStr))){
            //reset saved params.
            this.searchString = searchStr;
            listAdapter.searchResults = new ArrayList<SearchResultRowDTO>();
            offset = 0;
        }

        new AsyncTask<Void, Void, List<SearchResultRowDTO>>() {
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
                    listAdapter.searchResults.addAll(o);
                    if (mListView.getAdapter() == null){
                        mListView.setAdapter(listAdapter);
                    }
                    offset +=o.size();
                }
            }
        }.execute();
    }

    public void setOnItemClick(ICallbackHandler<SearchResultRowDTO, Void> callback){
        this.listAdapter.setItemClickHandler(callback);
    }


}
