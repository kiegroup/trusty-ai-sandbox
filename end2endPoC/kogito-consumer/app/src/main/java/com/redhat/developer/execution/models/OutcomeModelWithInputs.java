package com.redhat.developer.execution.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OutcomeModelWithInputs extends OutcomeModel {

    @JsonProperty("outcomeInputs")
    public OutcomeModelWithInputs[] decisionInputs = new OutcomeModelWithInputs[]{new OutcomeModelWithInputs(), new OutcomeModelWithInputs()};

    public OutcomeModelWithInputs() {
        super();
    }
}
