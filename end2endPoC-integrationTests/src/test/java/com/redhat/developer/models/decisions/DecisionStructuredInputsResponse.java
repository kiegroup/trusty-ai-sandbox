package com.redhat.developer.models.decisions;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DecisionStructuredInputsResponse {

    @JsonProperty("inputs")
    public List<SingleDecisionInputResponse> input;
}
