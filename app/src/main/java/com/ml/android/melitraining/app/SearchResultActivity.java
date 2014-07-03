package com.ml.android.melitraining.app;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.ml.android.melitraining.common.ICallbackHandler;
import com.ml.android.melitraining.dto.SearchResultRowDTO;
import com.ml.android.melitraining.fragments.SearchListFragment;
import com.ml.android.melitraining.net.robospice.ISpiceMgr;
import com.ml.android.melitraining.net.robospice.MeliRetrofitSpiceService;
import com.octo.android.robospice.SpiceManager;

public class SearchResultActivity extends FragmentActivity implements ISpiceMgr {

    private SearchListFragment searchListFragment;
    private SpiceManager spiceManager = new SpiceManager(MeliRetrofitSpiceService.class);

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_results_main);

        searchListFragment = (SearchListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_search_list);
        searchListFragment.setOnItemClick(new ICallbackHandler<SearchResultRowDTO, Void>() {
            @Override
            public Void apply(SearchResultRowDTO row) {
                Intent i = new Intent(SearchResultActivity.this, ItemVIPActivity.class);
                i.putExtra("item_id", row.id);
                i.putExtra("item_title", row.title);
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




    @Override
    protected void onStart() {
        spiceManager.start(this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        spiceManager.shouldStop();
        super.onStop();
    }

    public SpiceManager getSpiceManager() {
        return spiceManager;
    }

}
