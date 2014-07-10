package com.ml.android.melitraining.net.robospice;

import android.app.Application;
import com.octo.android.robospice.persistence.CacheManager;
import com.octo.android.robospice.persistence.exception.CacheCreationException;
import com.octo.android.robospice.persistence.memory.LruCache;
import com.octo.android.robospice.persistence.memory.LruCacheObjectPersister;
import com.octo.android.robospice.retrofit.RetrofitSpiceService;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import retrofit.converter.Converter;

/**
 * Created by mbarreto on 02/07/14.
 */
public abstract class RetrofitJacksonSpiceService extends RetrofitSpiceService {

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


    @Override
    protected Converter createConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return new JacksonConverter(objectMapper);
    }


}
