package com.redhat.developer.execution.responses.decisions;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.redhat.developer.execution.responses.execution.ExecutionHeaderResponse;
import com.redhat.developer.execution.storage.model.DecisionResultModel;

public class DecisionOutputsResponse extends DecisionHeaderResponse {

    @JsonProperty("decisions")
    public List<DecisionResultModel> decisions;

    public DecisionOutputsResponse(ExecutionHeaderResponse executionHeaderResponse, List<DecisionResultModel> decisions) {
        super(executionHeaderResponse);
        this.decisions = decisions;
    }
}
