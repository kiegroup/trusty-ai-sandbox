package com.redhat.developer.execution.responses.decisions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.redhat.developer.execution.models.DecisionResultModel;
import com.redhat.developer.execution.models.DecisionResultModelWithInputs;

public class SingleDecisionOutputResponse {

    @JsonProperty("decision")
    public DecisionResultModelWithInputs decision;

    public SingleDecisionOutputResponse(DecisionResultModel decision) {
        //this.decision = decision;
    }
}
