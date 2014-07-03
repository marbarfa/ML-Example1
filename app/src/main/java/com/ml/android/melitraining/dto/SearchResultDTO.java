package com.ml.android.melitraining.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by marbarfa on 4/27/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchResultDTO {

    public String site_id;
    public String query;
    public SearchPagingDTO paging;
    public List<SearchResultRowDTO> results;
}
