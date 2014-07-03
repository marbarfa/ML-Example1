package com.ml.android.melitraining.dto;

import java.util.ArrayList;

/**
 * Created by marbarfa on 4/20/14.
 */
public class SearchResultRowDTO {

    public String title;
    public Double price;
    public String thumbnail;
    public String id;
    public String site_id;

    public static class List extends ArrayList<SearchResultRowDTO>{

    }
}
