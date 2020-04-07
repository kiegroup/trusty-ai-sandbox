package com.redhat.developer.execution.responses.decisions;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.redhat.developer.execution.responses.execution.ExecutionHeaderResponse;
import com.redhat.developer.execution.storage.model.DecisionResultModel;
import com.redhat.developer.execution.storage.model.DecisionResultModelWithInputs;

public class DecisionOutputsStructuredResponse extends DecisionOutputsResponse {
    @JsonProperty("decisions")
    public List<DecisionResultModelWithInputs> decisions;

    public DecisionOutputsStructuredResponse(ExecutionHeaderResponse executionHeaderResponse, List<DecisionResultModel> decisions) {
        super(executionHeaderResponse, decisions);
    }
}
