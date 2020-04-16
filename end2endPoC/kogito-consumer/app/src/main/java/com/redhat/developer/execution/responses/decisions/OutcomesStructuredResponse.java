package com.redhat.developer.execution.responses.decisions;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.redhat.developer.execution.models.OutcomeModel;
import com.redhat.developer.execution.models.OutcomeModelWithInputs;
import com.redhat.developer.execution.responses.execution.ExecutionHeaderResponse;

public class OutcomesStructuredResponse extends OutcomesResponse {

    @JsonProperty("outcomes")
    public List<OutcomeModelWithInputs> decisions;

    public OutcomesStructuredResponse(ExecutionHeaderResponse executionHeaderResponse, List<OutcomeModel> decisions) {
        super(executionHeaderResponse, decisions);
    }
}
