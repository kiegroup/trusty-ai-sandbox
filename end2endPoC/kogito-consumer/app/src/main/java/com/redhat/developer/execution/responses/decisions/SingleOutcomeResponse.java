package com.redhat.developer.execution.responses.decisions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.redhat.developer.execution.models.OutcomeModel;
import com.redhat.developer.execution.models.OutcomeModelWithInputs;

public class SingleOutcomeResponse {

    @JsonProperty("outcome")
    public OutcomeModelWithInputs outcome;

    public SingleOutcomeResponse(OutcomeModel outcome) {
        //this.decision = decision;
    }
}
