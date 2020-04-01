package com.redhat.developer.decision.storage.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DMNEventModel {

    @JsonProperty("data")
    public DMNDataModel data;

    @JsonProperty("id")
    public String id;

    @JsonProperty("source")
    public String source;

    @JsonProperty("specversion")
    public String specversion;

    @JsonProperty("type")
    public String type;
}
