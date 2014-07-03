package com.ml.android.melitraining.net.robospice;

import android.app.Application;
import com.octo.android.robospice.persistence.CacheManager;
import com.octo.android.robospice.persistence.exception.CacheCreationException;
import com.octo.android.robospice.persistence.retrofit.RetrofitObjectPersisterFactory;
import com.octo.android.robospice.retrofit.RetrofitSpiceService;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import retrofit.converter.Converter;

import java.io.File;

/**
 * Created by mbarreto on 02/07/14.
 */
public abstract class RetrofitJacksonSpiceService extends RetrofitSpiceService {

    @Override
    public CacheManager createCacheManager(Application application) throws CacheCreationException {
        CacheManager cacheManager = new CacheManager();
        cacheManager.addPersister(new RetrofitObjectPersisterFactory(application, getConverter(), getCacheFolder()));
        return cacheManager;
    }


    @Override
    protected Converter createConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return new JacksonConverter(objectMapper);
    }

    public File getCacheFolder() {
        return null;
    }

}
