package com.redhat.developer.execution.responses.decisions;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.redhat.developer.execution.responses.decisions.inputs.SingleDecisionInputResponse;

public class DecisionInputStructured extends SingleDecisionInputResponse {

    @JsonProperty("id")
    public String id;

    @JsonProperty("category")
    public String category;


    public DecisionInputStructured(String id, String category, String inputName, String typeRef, List<List<SingleDecisionInputResponse>> components, Object value) {
        super(inputName, typeRef, components, value);
        this.category = category;
        this.id = id;
    }

    public DecisionInputStructured(String id, String category, SingleDecisionInputResponse singleDecisionInputResponse) {
        this(id, category, singleDecisionInputResponse.inputName, singleDecisionInputResponse.typeRef, singleDecisionInputResponse.components, singleDecisionInputResponse.value);
    }
}
