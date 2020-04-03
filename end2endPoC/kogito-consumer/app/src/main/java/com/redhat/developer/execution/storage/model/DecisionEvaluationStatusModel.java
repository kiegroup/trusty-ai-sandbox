package com.redhat.developer.execution.storage.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum DecisionEvaluationStatusModel {
    @JsonProperty("NOT_EVALUATED")
    NOT_EVALUATED,
    @JsonProperty("EVALUATING")
    EVALUATING,
    @JsonProperty("SUCCEEDED")
    SUCCEEDED,
    @JsonProperty("SKIPPED")
    SKIPPED,
    @JsonProperty("FAILED")
    FAILED;
}

