package com.redhat.developer.execution.responses.execution;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExecutionDetailResponse {

    @JsonProperty("header")
    public ExecutionHeaderResponse executionHeaderResponse;

    public ExecutionDetailResponse() {
    }

    public ExecutionDetailResponse(ExecutionHeaderResponse executionHeaderResponse) {
        this.executionHeaderResponse = executionHeaderResponse;
    }
}
