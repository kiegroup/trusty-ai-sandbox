package com.redhat.developer.decision.responses;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.redhat.developer.decision.storage.model.DecisionResultModel;

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
