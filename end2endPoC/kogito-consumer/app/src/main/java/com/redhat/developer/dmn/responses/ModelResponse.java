package com.redhat.developer.dmn.responses;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelResponse {

    @JsonProperty("metainformation")
    public ModelDetail metainformation;

    @JsonProperty("inputStructure")
    public Object inputStructure;

    @JsonProperty("decisions")
    public List<Object> decisions;
}
