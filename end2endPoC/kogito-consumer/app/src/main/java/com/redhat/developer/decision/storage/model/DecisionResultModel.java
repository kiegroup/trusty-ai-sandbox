package com.redhat.developer.decision.storage.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DecisionResultModel {
    @JsonProperty("decisionId")
    public String decisionId;

    @JsonProperty("decisionName")
    public String decisionName;

    @JsonProperty("evaluationStatus")
    public DecisionEvaluationStatusModel evaluationStatus;

    @JsonProperty("decisionResult")
    public String result;

    // TODO: create dto properly
    @JsonProperty("messages")
    public List<String> messages;

    @JsonProperty("hasErrors")
    public boolean hasErrors;
}
