package com.ml.android.melitraining.net.robospice;

import android.app.Activity;
import com.octo.android.robospice.SpiceManager;

/**
 * Created by mbarreto on 02/07/14.
 */
public class BaseSpiceActivity extends Activity {

    private SpiceManager spiceManager = new SpiceManager(MeliRetrofitSpiceService.class);

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

    protected SpiceManager getSpiceManager() {
        return spiceManager;
    }



}

