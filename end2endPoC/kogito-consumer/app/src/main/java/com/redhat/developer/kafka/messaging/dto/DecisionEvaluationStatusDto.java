package com.redhat.developer.kafka.messaging.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum DecisionEvaluationStatusDto {
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

