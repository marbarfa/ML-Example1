package com.ml.android.myapplication3.common;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;
import com.ml.android.myapplication3.app.R;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter implements ListAdapter {

    private Context context;

    private List<SearchResultRow> searchResults = new ArrayList<SearchResultRow>();

    public SearchAdapter(Context context) {
        this.context = context;
    }


    public void setSearchResults(List<SearchResultRow> searchResults) {
        this.searchResults = searchResults;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public int getCount() {
          return searchResults.size();
    }

    @Override
    public Object getItem(int position) {
        if (searchResults.size() > position){
            return searchResults.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        // reuse views
        if (rowView == null) {
            rowView = LayoutInflater.from(context).inflate(R.layout.search_row, null);
            // configure view holder
            SearchViewHolder viewHolder = new SearchViewHolder();
            viewHolder.productTitle = (TextView) rowView.findViewById(R.id.product_title);
            viewHolder.productPrice = (TextView) rowView.findViewById(R.id.product_price);

            rowView.setTag(viewHolder);
        }

        // fill data
        SearchViewHolder holder = (SearchViewHolder) rowView.getTag();
        SearchResultRow resultRow = searchResults.get(position);

        holder.productTitle.setText(resultRow.productTitle);
        holder.productPrice.setText("$" + resultRow.productPrice.toString());

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
        TextView productTitle;
        TextView productPrice;
    }


}