package com.redhat.developer.execution.responses.decisions;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.redhat.developer.execution.storage.model.DecisionResultModel;
import com.redhat.developer.execution.storage.model.DecisionResultModelWithInputs;

public class SingleDecisionOutputResponse {

    @JsonProperty("decision")
    public DecisionResultModelWithInputs decision;

    public SingleDecisionOutputResponse(DecisionResultModel decision) {
        //this.decision = decision;
    }
}
