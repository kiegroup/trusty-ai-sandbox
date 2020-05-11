package com.redhat.developer.explainability.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.redhat.developer.execution.responses.decisions.inputs.SingleDecisionInputResponse;

public class LimeExplanationRequest {
    @JsonProperty("inputs")
    public List<SingleDecisionInputResponse> input;

    @JsonProperty("outputs")
    public List<SingleDecisionInputResponse> outputs;

    @JsonProperty("modelName")
    public String modelName;

    public LimeExplanationRequest(){}

    public LimeExplanationRequest(List<SingleDecisionInputResponse> input, List<SingleDecisionInputResponse> outputs, String modelName){
        this.input = input;
        this.outputs = outputs;
        this.modelName = modelName;
    }
}
