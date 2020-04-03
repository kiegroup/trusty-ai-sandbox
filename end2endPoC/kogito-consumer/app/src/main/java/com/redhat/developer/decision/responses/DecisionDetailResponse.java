package com.redhat.developer.decision.responses;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DecisionDetailResponse {
    @JsonProperty("header")
    public ExecutionHeaderResponse executionHeaderResponse;

    public DecisionDetailResponse(ExecutionHeaderResponse executionHeaderResponse){
        this.executionHeaderResponse = executionHeaderResponse;
    }
}
