package com.ml.android.melitraining.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by marbarfa on 5/5/14.
 */
public class StartServiceReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, MeliBookmarksService.class);
        context.startService(service);
    }
}
