package com.ml.android.myapplication3.common;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by marbarfa on 4/20/14.
 */
public class HttpUtils {

    // check network connection
    public static boolean isConnected(ConnectivityManager connMgr){
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }
}
