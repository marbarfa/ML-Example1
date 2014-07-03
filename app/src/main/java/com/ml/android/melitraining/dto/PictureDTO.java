package com.ml.android.melitraining.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Created by marbarfa on 4/27/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PictureDTO {

    public String id;
    public String url;
    public String size;
    public String max_size;

}
