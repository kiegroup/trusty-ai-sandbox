package com.redhat.developer.execution.responses.decisions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.redhat.developer.execution.responses.execution.ExecutionHeaderResponse;

public class DecisionInputsResponse {

    @JsonProperty("input")
    public Object input;

    @JsonProperty("header")
    public ExecutionHeaderResponse executionHeaderResponse;

    public DecisionInputsResponse(ExecutionHeaderResponse executionHeaderResponse, Object input) {
        this.executionHeaderResponse = executionHeaderResponse;
        this.input = input;
    }
}
