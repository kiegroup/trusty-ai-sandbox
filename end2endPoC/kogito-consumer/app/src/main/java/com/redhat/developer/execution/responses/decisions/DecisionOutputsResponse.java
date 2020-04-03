package com.redhat.developer.execution.responses.decisions;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.redhat.developer.execution.responses.execution.ExecutionHeaderResponse;
import com.redhat.developer.execution.storage.model.DecisionResultModel;

public class DecisionOutputsResponse {
    @JsonProperty("decisions")
    public List<DecisionResultModel> decisions;

    @JsonProperty("header")
    public ExecutionHeaderResponse executionHeaderResponse;

    public DecisionOutputsResponse(ExecutionHeaderResponse executionHeaderResponse, List<DecisionResultModel> decisions) {
        this.decisions = decisions;
        this.executionHeaderResponse = executionHeaderResponse;
    }
}
