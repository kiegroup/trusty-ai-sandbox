package com.redhat.developer.decision.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DecisionInputsResponse {

    @JsonProperty("input")
    public Object input;

    @JsonProperty("evaluationDate")
    public String evaluationDate;

    @JsonProperty("id")
    public String id;

    public DecisionInputsResponse(String id, String evaluationDate, Object input) {
        this.id = id;
        this.input = input;
        this.evaluationDate = evaluationDate;
    }
}
