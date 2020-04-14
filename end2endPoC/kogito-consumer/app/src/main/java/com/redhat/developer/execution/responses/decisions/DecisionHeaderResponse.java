package com.redhat.developer.execution.responses.decisions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.redhat.developer.execution.responses.execution.ExecutionHeaderResponse;

public class DecisionHeaderResponse {

    @JsonProperty("header")
    public ExecutionHeaderResponse header;

    public DecisionHeaderResponse(ExecutionHeaderResponse response) {
        this.header = response;
    }
}
