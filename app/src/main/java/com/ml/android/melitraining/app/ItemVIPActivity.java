package com.ml.android.melitraining.app;


import android.os.Bundle;
import com.ml.android.melitraining.fragments.ItemVIPFragment;

public class ItemVIPActivity extends android.app.Activity {

    private ItemVIPFragment itemVIPFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_vip_fragment);
        itemVIPFragment = (ItemVIPFragment)getFragmentManager().findFragmentById(R.id.item_vip_fragment);

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


}
