package com.ml.android.melitraining.app;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
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

public class SearchResultActivity extends SherlockFragmentActivity implements ISpiceMgr {

    private enum EnumItemState {
        list,
        detail;
    }

    private SearchListFragment searchListFragment;
    private ItemVIPFragment itemVIPFragment;
    private SpiceManager spiceManager = new SpiceManager(MeliRetrofitSpiceService.class);
    private BookmarksDAO bookmarkDAO;

    private String searchString;
    private EnumItemState state;


    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_results_main);
        state = EnumItemState.list;

        bookmarkDAO = new BookmarksDAO(getApplicationContext());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent() != null && getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            this.searchString = extras.getString("search_string");
        }

        setUpViews();

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            searchListFragment.searchMeliItems(searchString);
        }

    }

    private void setUpViews() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            searchListFragment = (SearchListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_search_list);
            itemVIPFragment = (ItemVIPFragment) getSupportFragmentManager().findFragmentById(R.id.item_framgment_detail);
        }else{
            searchListFragment = new SearchListFragment();
            Bundle b = new Bundle();
            b.putString("search_string", searchString);
            searchListFragment.setArguments(b);

            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.search_results_container, searchListFragment);
            ft.disallowAddToBackStack();
            ft.commit();

            itemVIPFragment = new ItemVIPFragment();
            itemVIPFragment.setSpiceManager(spiceManager);
        }


        searchListFragment.setOnItemClick(new ICallbackHandler<SearchResultRowDTO, Void>() {
            @Override
            public Void apply(SearchResultRowDTO row) {
                navigateToDetail(row);
                return null;
            }
        });
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


    private void navigateToListItem(){
        FragmentManager fm = SearchResultActivity.this.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.remove(itemVIPFragment);
        ft.add(R.id.search_results_container, searchListFragment);
        ft.addToBackStack(null);
        ft.commit();

        state = EnumItemState.list;
        searchListFragment.searchMeliItems(this.searchString);

    }

    private void navigateToDetail(SearchResultRowDTO row) {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            itemVIPFragment.loadItem(row.id, SearchResultActivity.this);
        } else {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.search_results_container, itemVIPFragment);
            ft.addToBackStack(null);
            ft.commit();

            itemVIPFragment.loadItem(row.id, this);
            state = EnumItemState.detail;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (state == EnumItemState.detail &&
                getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            navigateToListItem();
            return true;
        }
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    // This activity is NOT part of this app's task, so create a new task
                    // when navigating up, with a synthesized back stack.
                    TaskStackBuilder.create(this)
                            .addNextIntentWithParentStack(upIntent)
                            .startActivities();
                } else {
                    // This activity is part of this app's task, so simply
                    // navigate up to the logical parent activity.
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
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
