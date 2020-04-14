package com.redhat.developer.database.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Hit<T> {

    @JsonProperty("_index")
    public String index;

    @JsonProperty("_type")
    public String type;

    @JsonProperty("_id")
    public String _id;

    @JsonProperty("_score")
    public double score;

    @JsonProperty("_source")
    public T source;
}
