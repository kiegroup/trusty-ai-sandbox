package com.redhat.developer.dmn.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EvaluationRequestBody {

    @JsonProperty("inputs")
    public Object inputs;
}
