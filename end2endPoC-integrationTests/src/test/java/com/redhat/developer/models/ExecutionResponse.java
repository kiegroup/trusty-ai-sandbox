package com.redhat.developer.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExecutionResponse {

    @JsonProperty("total")
    public int total;

    @JsonProperty("limit")
    public int limit;

    @JsonProperty("offset")
    public int offset;

    @JsonProperty("headers")
    public List<ExecutionHeaderResponse> headers;
}
