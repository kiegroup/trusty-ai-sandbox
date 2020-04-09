package com.redhat.developer.database.elastic.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ElasticModelHits {

    @JsonProperty("hits")
    public List<ModelHit> hits;

    @JsonProperty("total")
    public Object total;

    @JsonProperty("max_score")
    public double max_score;
}
