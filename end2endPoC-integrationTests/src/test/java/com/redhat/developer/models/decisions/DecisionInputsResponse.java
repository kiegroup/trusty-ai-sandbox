package com.redhat.developer.models.decisions;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DecisionInputsResponse {

    @JsonProperty("inputs")
    public Map<String, Object> inputs;
}