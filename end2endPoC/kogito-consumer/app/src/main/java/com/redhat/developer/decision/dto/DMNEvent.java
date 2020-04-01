package com.redhat.developer.decision.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DMNEvent {

    @JsonProperty("data")
    public DMNData data;

    @JsonProperty("id")
    public String id;

    @JsonProperty("source")
    public String source;

    @JsonProperty("specversion")
    public String specversion;

    @JsonProperty("type")
    public String type;
}
