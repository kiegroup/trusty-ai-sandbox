package com.redhat.developer.dmn.requests;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EvaluationRequestBody {

    @JsonProperty("inputs")
    public Map<String, Object> inputs;
}
