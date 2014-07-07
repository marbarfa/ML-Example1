package com.ml.android.melitraining.net;

import com.ml.android.melitraining.dto.ItemDTO;
import com.ml.android.melitraining.dto.SearchResultDTO;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by mbarreto on 01/07/14.
 */
public final class MeliAPI {

    public interface ItemsAPI {

        @Headers({
                "Accept-Encoding: application/identity",
        })
        @GET("/items/{itemId}")
        public ItemDTO getItem(@Path("itemId") String itemId);
    }

    public interface SearchAPI {

        @GET("/sites/{siteId}/search")
        public SearchResultDTO getProductSearch(@Path("siteId") String siteId,
                                     @Query("q")  String searchQuery,
                                     @Query("offset") int offset,
                                     @Query("limit") int limit);
    }


}
