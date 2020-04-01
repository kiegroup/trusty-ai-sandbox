package com.redhat.developer.database.elastic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ElasticSearchResponse {

    @JsonProperty("took")
    public int took;

    @JsonProperty("timed_out")
    public boolean timed_out;

    @JsonProperty("shards")
    public Object shards;

    @JsonProperty("hits")
    public ElasticHits hits;

}

