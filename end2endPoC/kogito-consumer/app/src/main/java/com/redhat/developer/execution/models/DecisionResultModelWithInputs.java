package com.redhat.developer.execution.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DecisionResultModelWithInputs extends DecisionResultModel {

    @JsonProperty("decisionInputs")
    public DecisionResultModelWithInputs[] decisionInputs = new DecisionResultModelWithInputs[]{new DecisionResultModelWithInputs(), new DecisionResultModelWithInputs()};

    public DecisionResultModelWithInputs() {
        super();
    }
}
