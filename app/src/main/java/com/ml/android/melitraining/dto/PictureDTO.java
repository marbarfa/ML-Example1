package com.ml.android.melitraining.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by marbarfa on 4/27/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PictureDTO implements Serializable{

    public String id;
    public String url;
    public String size;
    public String max_size;

}
