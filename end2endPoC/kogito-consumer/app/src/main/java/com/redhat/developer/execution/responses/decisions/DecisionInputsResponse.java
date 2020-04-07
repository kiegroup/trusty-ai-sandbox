package com.redhat.developer.execution.responses.decisions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.redhat.developer.execution.responses.execution.ExecutionHeaderResponse;

public class DecisionInputsResponse extends DecisionHeaderResponse {

    @JsonProperty("input")
    public Object input;

    public DecisionInputsResponse(ExecutionHeaderResponse executionHeaderResponse, Object input) {
        super(executionHeaderResponse);
        this.input = input;
    }
}
