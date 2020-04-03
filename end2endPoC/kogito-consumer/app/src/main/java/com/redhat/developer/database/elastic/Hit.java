package com.redhat.developer.database.elastic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.redhat.developer.execution.storage.model.DMNEventModel;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Hit {

    @JsonProperty("_index")
    public String index;

    @JsonProperty("_type")
    public String type;

    @JsonProperty("_id")
    public String _id;

    @JsonProperty("_score")
    public double score;

    @JsonProperty("_source")
    public DMNEventModel source;
}
