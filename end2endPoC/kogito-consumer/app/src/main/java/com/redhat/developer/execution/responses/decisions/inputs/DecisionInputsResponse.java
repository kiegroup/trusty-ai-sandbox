package com.redhat.developer.execution.responses.decisions.inputs;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DecisionInputsResponse {

    @JsonProperty("inputs")
    public Map<String, Object> input;

    public DecisionInputsResponse(Map<String, Object> input) {
        this.input = input;
    }

    public DecisionInputsResponse() {
    }
}
