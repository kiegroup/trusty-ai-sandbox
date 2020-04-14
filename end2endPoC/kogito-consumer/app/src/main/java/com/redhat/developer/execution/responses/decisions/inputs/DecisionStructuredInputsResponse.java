package com.redhat.developer.execution.responses.decisions.inputs;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DecisionStructuredInputsResponse {

    @JsonProperty("inputs")
    public List<SingleDecisionInputResponse> input;

    public DecisionStructuredInputsResponse() {
    }
}
