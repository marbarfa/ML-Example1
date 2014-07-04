package com.ml.android.melitraining.net.robospice;

import com.ml.android.melitraining.net.MeliAPI;

public class MeliRetrofitSpiceService extends RetrofitJacksonSpiceService {
    private final static String BASE_URL = "https://api.mercadolibre.com/";

    @Override
    public void onCreate() {
        super.onCreate();
        addRetrofitInterface(MeliAPI.ItemsAPI.class);
        addRetrofitInterface(MeliAPI.SearchAPI.class);
    }

    @Override
    protected String getServerUrl() {
        return BASE_URL;
    }

}