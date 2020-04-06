package com.redhat.developer.explainability.responses.local;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DecisionExplanationResponse {

    @JsonProperty("explaination")
    public Object explaination;

    public DecisionExplanationResponse() {
    }
}
