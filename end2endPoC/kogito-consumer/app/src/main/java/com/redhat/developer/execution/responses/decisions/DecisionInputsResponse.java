package com.redhat.developer.execution.responses.decisions;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.redhat.developer.execution.responses.execution.ExecutionHeaderResponse;

public class DecisionInputsResponse extends DecisionHeaderResponse {

    @JsonProperty("inputs")
    public List<SingleDecisionInputResponse> input;

    public DecisionInputsResponse(ExecutionHeaderResponse executionHeaderResponse, List<SingleDecisionInputResponse> input) {
        super(executionHeaderResponse);
        this.input = input;
    }
}
