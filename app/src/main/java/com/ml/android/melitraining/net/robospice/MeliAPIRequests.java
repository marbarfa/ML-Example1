package com.ml.android.melitraining.net.robospice;

import android.util.Log;
import com.ml.android.melitraining.dto.ItemDTO;
import com.ml.android.melitraining.dto.SearchResultDTO;
import com.ml.android.melitraining.net.MeliAPI;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;
import roboguice.util.temp.Ln;

/**
 * Created by mbarreto on 02/07/14.
 */
public class MeliAPIRequests {

    public static class GetItemsSpiceRequest extends RetrofitSpiceRequest<ItemDTO, MeliAPI.ItemsAPI>{

        private String itemId;

        public GetItemsSpiceRequest(String itemId) {
            super(ItemDTO.class, MeliAPI.ItemsAPI.class);
            this.itemId = itemId;
        }

        @Override
        public ItemDTO loadDataFromNetwork() {
            Ln.d("Call web service ");
            return getService().getItem(itemId);
        }
    }

    public static class GetProductSearchSpiceRequest extends RetrofitSpiceRequest<SearchResultDTO, MeliAPI.SearchAPI> {

        private String siteId;
        private String query;
        private int offset;
        private int limit;

        public GetProductSearchSpiceRequest(String siteId, String query, int offset, int limit) {
            super(SearchResultDTO.class, MeliAPI.SearchAPI.class);
            this.siteId = siteId;
            this.query = query;
            this.offset = offset;
            this.limit = limit;
        }

        @Override
        public SearchResultDTO loadDataFromNetwork() {
            Log.i("Call web service ", "WS");
            return getService().getProductSearch(siteId, query, offset, limit);
        }
    }
}
