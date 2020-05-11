package com.redhat.developer.dmn.responses;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EvaluationResponse {

    @JsonProperty("decisions")
    public  Map<String, Object>  decisions;

    public EvaluationResponse( Map<String, Object> o) {
        this.decisions = o;
    }
}
