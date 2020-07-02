package com.redhat.developer.requests;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.redhat.developer.model.dmn.TypedData;

public class LocalExplanationRequest {

    @JsonProperty("inputs")
    public List<TypedData> input;

    @JsonProperty("outputs")
    public List<TypedData> outputs;

    @JsonProperty("modelName")
    public String modelName;

    public LocalExplanationRequest() {
    }
}
