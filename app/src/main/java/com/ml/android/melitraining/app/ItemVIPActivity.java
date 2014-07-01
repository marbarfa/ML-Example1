package com.ml.android.melitraining.app;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.MenuItem;
import com.ml.android.melitraining.fragments.ItemVIPFragment;

public class ItemVIPActivity extends android.app.Activity {

    private ItemVIPFragment itemVIPFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_vip_fragment);
        itemVIPFragment = (ItemVIPFragment)getFragmentManager().findFragmentById(R.id.item_vip_fragment);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //retrive search string
        if (getIntent() != null && getIntent().getExtras() != null) {

            Bundle extras = getIntent().getExtras();
            String itemId = extras.getString("item_id");
//            loadItem(itemId);

//        } else if (itemDTO != null) {
//            loadItemToUI();
        }
//        }else if (listAdapter == null){
//            listAdapter = new SearchAdapter(getApplicationContext());
//        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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


}
