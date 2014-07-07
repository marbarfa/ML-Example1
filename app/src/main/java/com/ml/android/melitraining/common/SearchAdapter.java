package com.ml.android.melitraining.common;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import com.ml.android.melitraining.app.R;
import com.ml.android.melitraining.app.SearchResultActivity;
import com.ml.android.melitraining.dto.SearchResultRowDTO;
import com.ml.android.melitraining.imgutils.BitmapCache;
import com.ml.android.melitraining.imgutils.ImgDownloader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter implements ListAdapter {

    private Context context;

    public List<SearchResultRowDTO> searchResults = new ArrayList<SearchResultRowDTO>();
    private BitmapCache bitmapCache;
    private ImgDownloader imgDownloader;
    private SearchResultActivity activity;
    private Bitmap mPlaceholderBitmap;

    private ICallbackHandler<SearchResultRowDTO, Void> itemClickHandler;

    public SearchAdapter(Context context) {
        this.context = context;
        bitmapCache = new BitmapCache();
        imgDownloader = new ImgDownloader(bitmapCache);
    }


    public void setSearchResults(List<SearchResultRowDTO> searchResults) {
        this.searchResults = searchResults;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    public void setItemClickHandler(ICallbackHandler<SearchResultRowDTO, Void> itemClickHandler) {
        this.itemClickHandler = itemClickHandler;
    }

    @Override
    public int getCount() {
        return searchResults.size();
    }

    @Override
    public Object getItem(int position) {
        if (searchResults.size() > position) {
            return searchResults.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public int getItemViewType(int i) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return searchResults.size();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        final SearchResultRowDTO resultRow = searchResults.get(position);
        // reuse views
        if (rowView == null) {
            rowView = LayoutInflater.from(context).inflate(R.layout.search_row, null);
            // configure view holder
            SearchViewHolder viewHolder = new SearchViewHolder();
            viewHolder.productTitle = (TextView) rowView.findViewById(R.id.product_title);
            viewHolder.productPrice = (TextView) rowView.findViewById(R.id.product_price);
            viewHolder.thumbnail = (ImageView) rowView.findViewById(R.id.product_thumbnail);
            rowView.setTag(viewHolder);

            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SearchViewHolder viewTag = (SearchViewHolder)view.getTag();
                    itemClickHandler.apply(viewTag.searchResultRowDTO);
                }
            });

        }

        // fill data
        SearchViewHolder holder = (SearchViewHolder) rowView.getTag();
        holder.productTitle.setText(resultRow.title);
        holder.searchResultRowDTO = resultRow;
        holder.productPrice.setText("$" + resultRow.price.toString());
        Picasso
                .with(context)
                .load(resultRow.thumbnail)
                .placeholder(R.drawable.placeholder)
                .fit()
//                .resize(100, 100)
                .centerCrop()
                .into(holder.thumbnail);

        return rowView;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int i) {
        return false;
    }


    private class SearchViewHolder {
        ImageView thumbnail;
        TextView productTitle;
        TextView productPrice;
        SearchResultRowDTO searchResultRowDTO;
    }


}