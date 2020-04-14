package com.redhat.developer.execution.responses.execution;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExecutionDetailResponse {

    @JsonProperty("header")
    public ExecutionHeaderResponse header;

    public ExecutionDetailResponse() {
    }

    public ExecutionDetailResponse(ExecutionHeaderResponse header) {
        this.header = header;
    }
}
