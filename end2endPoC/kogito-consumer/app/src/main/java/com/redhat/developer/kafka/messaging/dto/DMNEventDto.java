package com.redhat.developer.kafka.messaging.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DMNEventDto {

    @JsonProperty("data")
    public DMNDataDto data;

    @JsonProperty("id")
    public String id;

    @JsonProperty("source")
    public String source;

    @JsonProperty("specversion")
    public String specversion;

    @JsonProperty("type")
    public String type;
}
