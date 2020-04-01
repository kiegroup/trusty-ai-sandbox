package com.redhat.developer.database.elastic;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ElasticHits{
    @JsonProperty("hits")
    public List<Hit> hits;

    @JsonProperty("total")
    public Object total;

    @JsonProperty("max_score")
    public double max_score;
}
