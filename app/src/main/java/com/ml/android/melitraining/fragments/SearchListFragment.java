package com.ml.android.melitraining.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import com.ml.android.melitraining.app.R;
import com.ml.android.melitraining.common.ICallbackHandler;
import com.ml.android.melitraining.common.SearchAdapter;
import com.ml.android.melitraining.dto.SearchResultDTO;
import com.ml.android.melitraining.dto.SearchResultRowDTO;
import com.ml.android.melitraining.net.robospice.ISpiceMgr;
import com.ml.android.melitraining.net.robospice.MeliAPIRequests;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.ArrayList;

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
    private static SearchAdapter listAdapter;

    private AbsListView mListView;
    private ProgressBar progressBar;

    private String searchString;


    private SpiceManager spiceManager;

    private ICallbackHandler<SearchResultRowDTO, Void> onItemClick;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_result_fragment, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(R.id.resultlist);
        progressBar = (ProgressBar) view.findViewById(R.id.search_items_progressbar);
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

        if (listAdapter == null) {
            listAdapter = new SearchAdapter(getActivity());
            setAdapterItemClick();
            if (getSearchString() != null) {
                searchMeliItems(getSearchString());
            }
        }else if (listAdapter.searchResults.size() > 0){
            mListView.setAdapter(listAdapter);
        }

        return view;
    }



    private String getSearchString() {
        if (getArguments() != null) {
            return getArguments().getString("search_string");
        }
        return null;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ISpiceMgr) {
            spiceManager = ((ISpiceMgr) activity).getSpiceManager();
        }
    }

    public void searchMeliItems(String searchStr) {

        if (searchStr != null && (this.searchString == null ||
                !this.searchString.equals(searchStr))) {
            //reset saved params.
            this.searchString = searchStr;
            listAdapter.searchResults = new ArrayList<SearchResultRowDTO>();
            offset = 0;
        }

        if (this.searchString == null) {
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        MeliAPIRequests.GetProductSearchSpiceRequest searchRequest =
                new MeliAPIRequests
                        .GetProductSearchSpiceRequest("MLU", searchString.trim(), offset, limit);

        String spiceRequestCacheKey = ("search/q=" + searchStr + "offset=" + offset + "limit=" + limit);

        spiceManager.execute(searchRequest, spiceRequestCacheKey, 1000 * 60, new RequestListener<SearchResultDTO>() {

                    public void onRequestFailure(SpiceException spiceException) {
                        Log.e("Something happened using robospice", spiceException.getMessage());
                    }

                    public void onRequestSuccess(SearchResultDTO result) {
                        if (progressBar != null) {
                            progressBar.setVisibility(View.GONE);
                        }
                        if (result.paging != null && result.paging.total > 0) {
                            listAdapter.searchResults.addAll(result.results);
                            if (mListView.getAdapter() == null) {
                                mListView.setAdapter(listAdapter);
                            }
                            offset += result.results.size();
                        }
                    }
                }
        );
    }

    public SearchResultRowDTO getFirstResult() {
        if (listAdapter != null && listAdapter.searchResults != null && listAdapter.searchResults.size() > 0) {
            return listAdapter.searchResults.get(0);
        }
        return null;
    }


    public void setOnItemClick(ICallbackHandler<SearchResultRowDTO, Void> callback) {
        this.onItemClick = callback;
        setAdapterItemClick();
    }

    private void setAdapterItemClick() {
        if (listAdapter != null && onItemClick != null) {
            listAdapter.setItemClickHandler(onItemClick);
        }
    }


}