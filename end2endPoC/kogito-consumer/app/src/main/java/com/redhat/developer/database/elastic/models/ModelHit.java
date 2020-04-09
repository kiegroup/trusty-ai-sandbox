package com.redhat.developer.database.elastic.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.redhat.developer.dmn.storage.dto.DmnModel;
import com.redhat.developer.execution.storage.model.DMNEventModel;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ModelHit {

    @JsonProperty("_index")
    public String index;

    @JsonProperty("_type")
    public String type;

    @JsonProperty("_id")
    public String _id;

    @JsonProperty("_score")
    public double score;

    @JsonProperty("_source")
    public DmnModel source;
}
