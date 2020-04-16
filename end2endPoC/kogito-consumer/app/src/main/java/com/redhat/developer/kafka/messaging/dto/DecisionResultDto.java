package com.redhat.developer.kafka.messaging.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DecisionResultDto {

    @JsonProperty("decisionId")
    public String decisionId;

    @JsonProperty("decisionName")
    public String decisionName;

    @JsonProperty("evaluationStatus")
    public DecisionEvaluationStatusDto evaluationStatus;

    @JsonProperty("decisionResult")
    public Object result;

    // TODO: create dto properly
    @JsonProperty("messages")
    public List<String> messages;

    @JsonProperty("hasErrors")
    public boolean hasErrors;
}
