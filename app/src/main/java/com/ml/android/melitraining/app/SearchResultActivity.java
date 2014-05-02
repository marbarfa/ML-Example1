package com.ml.android.melitraining.app;


import android.content.Intent;
import android.os.Bundle;
import com.ml.android.melitraining.common.ICallbackHandler;
import com.ml.android.melitraining.dto.SearchResultRowDTO;
import com.ml.android.melitraining.fragments.SearchListFragment;

public class SearchResultActivity extends android.app.Activity {

    private SearchListFragment searchListFragment;

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result_fragment);

        searchListFragment = (SearchListFragment) getFragmentManager().findFragmentById(R.id.fragment_search_list);
        searchListFragment.setOnItemClick(new ICallbackHandler<SearchResultRowDTO, Void>() {
            @Override
            public Void apply(SearchResultRowDTO row) {
                Intent i = new Intent(SearchResultActivity.this, ItemVIPActivity.class);
                i.putExtra("item_id", row.itemId);
                startActivity(i);
                return null;
            }
        });

        if (getIntent() != null && getIntent().getExtras() != null) {

            Bundle extras = getIntent().getExtras();
            String searchString = extras.getString("search_string");
            searchListFragment.searchMeliItems(searchString);

        }
    }

}
