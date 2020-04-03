package com.redhat.developer.execution.responses.execution;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.redhat.developer.execution.responses.execution.ExecutionHeaderResponse;

public class ExecutionDetailResponse {
    @JsonProperty("header")
    public ExecutionHeaderResponse executionHeaderResponse;

    public ExecutionDetailResponse(){
    }

    public ExecutionDetailResponse(ExecutionHeaderResponse executionHeaderResponse){
        this.executionHeaderResponse = executionHeaderResponse;
    }
}
