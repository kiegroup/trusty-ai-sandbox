package com.redhat.developer.database.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ElasticSearchResponse<T> {

    @JsonProperty("took")
    public int took;

    @JsonProperty("timed_out")
    public boolean timed_out;

    @JsonProperty("shards")
    public Object shards;

    @JsonProperty("hits")
    public ElasticHits<T> hits;
}

