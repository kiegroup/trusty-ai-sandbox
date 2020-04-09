package com.redhat.developer.database.elastic.decisions;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.redhat.developer.execution.storage.model.DMNEventModel;

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

