package com.redhat.developer.dmn.responses;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EvaluationResponse {
    @JsonProperty("decisions")
    public List<Object> decisions;
}
