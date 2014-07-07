package com.ml.android.melitraining.net.robospice;

import android.app.Application;
import com.ml.android.melitraining.net.MeliAPI;
import com.octo.android.robospice.persistence.CacheManager;
import com.octo.android.robospice.persistence.exception.CacheCreationException;
import com.octo.android.robospice.persistence.memory.LruCache;
import com.octo.android.robospice.persistence.memory.LruCacheObjectPersister;

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

    @Override
    public CacheManager createCacheManager(Application application) throws CacheCreationException {
        CacheManager manager = new CacheManager();
        LruCacheObjectPersister memoryPersister = new LruCacheObjectPersister(Object.class, new LruCache(1024 * 1024)) {
            @Override
            public boolean canHandleClass(Class clazz) {
                return true;
            }
        };
        manager.addPersister(memoryPersister);

        return manager;
    }

}