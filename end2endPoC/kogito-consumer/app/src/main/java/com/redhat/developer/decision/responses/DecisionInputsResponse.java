package com.redhat.developer.decision.responses;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

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
