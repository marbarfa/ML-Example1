package com.ml.android.melitraining.app;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import com.ml.android.melitraining.common.ICallbackHandler;
import com.ml.android.melitraining.database.dao.BookmarksDAO;
import com.ml.android.melitraining.database.entities.Bookmark;
import com.ml.android.melitraining.dto.ItemDTO;
import com.ml.android.melitraining.dto.SearchResultRowDTO;
import com.ml.android.melitraining.fragments.ItemVIPFragment;
import com.ml.android.melitraining.fragments.SearchListFragment;
import com.ml.android.melitraining.net.robospice.ISpiceMgr;
import com.ml.android.melitraining.net.robospice.MeliRetrofitSpiceService;
import com.octo.android.robospice.SpiceManager;

import java.sql.SQLException;

public class SearchResultActivity extends FragmentActivity implements ISpiceMgr {

    private static SearchListFragment searchListFragment;
    private ItemVIPFragment itemVIPFragment;
    private SpiceManager spiceManager = new SpiceManager(MeliRetrofitSpiceService.class);
    private BookmarksDAO bookmarkDAO;

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_results_main);

        bookmarkDAO = new BookmarksDAO(getApplicationContext());

        searchListFragment = (SearchListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_search_list);
        itemVIPFragment = (ItemVIPFragment) getSupportFragmentManager().findFragmentById(R.id.item_framgment_detail);

        searchListFragment.setOnItemClick(new ICallbackHandler<SearchResultRowDTO, Void>() {
            @Override
            public Void apply(SearchResultRowDTO row) {
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    itemVIPFragment.loadItem(row.id, SearchResultActivity.this);
                } else {
                    Intent i = new Intent(SearchResultActivity.this, ItemVIPActivity.class);
                    i.putExtra("item_id", row.id);
                    i.putExtra("item_title", row.title);
                    startActivity(i);
                }
                return null;
            }
        });

        if (getIntent() != null && getIntent().getExtras() != null) {

            Bundle extras = getIntent().getExtras();
            String searchString = extras.getString("search_string");
            searchListFragment.searchMeliItems(searchString);
        }

        if (itemVIPFragment != null) {
//            if (searchListFragment.getFirstResult()!=null){
//                itemVIPFragment.loadItem(searchListFragment.getFirstResult().id, this);
//            }
            itemVIPFragment.setOnItemBookmark(new ICallbackHandler<ItemDTO, Void>() {
                @Override
                public Void apply(ItemDTO item) {
                    try {
                        Bookmark b = bookmarkDAO.getBookmarkByItem(item.id);
                        if (b != null) {
                            bookmarkDAO.getDAO().delete(b);
                        } else {
                            b = new Bookmark();
                            b.setChecked(false);
                            b.setItemId(item.id);
                            b.setItemEndDate(item.stop_time);

                            bookmarkDAO.getDAO().create(b);
                        }
                    } catch (SQLException e) {
                        Log.e("ItemVIPActivity", "Error deleting of saving bookmark", e);
                    }
                    return null;
                }
            });
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
