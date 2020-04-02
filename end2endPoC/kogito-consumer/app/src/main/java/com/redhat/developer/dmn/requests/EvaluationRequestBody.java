package com.redhat.developer.dmn.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EvaluationRequestBody {

    @JsonProperty("input")
    public Object input;
}
