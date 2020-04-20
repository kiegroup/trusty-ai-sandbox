package com.redhat.developer.execution.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.redhat.developer.execution.responses.decisions.DecisionInputStructured;
import com.redhat.developer.execution.responses.decisions.inputs.SingleDecisionInputResponse;

public class OutcomeModelWithInputs extends OutcomeModel {

    @JsonProperty("outcomeInputs")
    public List<DecisionInputStructured> decisionInputs;

    public OutcomeModelWithInputs(String outcomeId, String outcomeName, OutcomeEvaluationStatusModel evaluationStatus, SingleDecisionInputResponse result, List<String> messages, boolean hasErrors, List<DecisionInputStructured> inputs) {
        super(outcomeId, outcomeName, evaluationStatus, result, messages, hasErrors);
        this.decisionInputs = inputs;
    }
}
