package com.redhat.developer.execution.responses.decisions;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.redhat.developer.execution.models.OutcomeModel;
import com.redhat.developer.execution.responses.execution.ExecutionHeaderResponse;

public class OutcomesResponse extends DecisionHeaderResponse {

    @JsonProperty("outcomes")
    public List<OutcomeModel> decisions;

    public OutcomesResponse(ExecutionHeaderResponse executionHeaderResponse, List<OutcomeModel> decisions) {
        super(executionHeaderResponse);
        this.decisions = decisions;
    }
}
