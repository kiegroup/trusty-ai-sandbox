package com.redhat.developer.dmn.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EvaluationResponse {

    @JsonProperty("decisions")
    public Object decisions;

    public EvaluationResponse(Object o) {
        this.decisions = o;
    }
}
