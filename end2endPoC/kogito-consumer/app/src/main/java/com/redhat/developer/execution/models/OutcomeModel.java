package com.redhat.developer.execution.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OutcomeModel {

    @JsonProperty("outcomeId")
    public String outcomeId;

    @JsonProperty("outcomeName")
    public String outcomeName;

    @JsonProperty("evaluationStatus")
    public OutcomeEvaluationStatusModel evaluationStatus;

    @JsonProperty("outcomeResult")
    public Object result;

    // TODO: create dto properly
    @JsonProperty("messages")
    public List<String> messages;

    @JsonProperty("hasErrors")
    public boolean hasErrors;

    public OutcomeModel() {
    }

    public OutcomeModel(String outcomeId, String outcomeName, OutcomeEvaluationStatusModel evaluationStatus, Object result, List<String> messages, boolean hasErrors) {
        this.outcomeId = outcomeId;
        this.outcomeName = outcomeName;
        this.evaluationStatus = evaluationStatus;
        this.result = result;
        this.messages = messages;
        this.hasErrors = hasErrors;
    }
}

