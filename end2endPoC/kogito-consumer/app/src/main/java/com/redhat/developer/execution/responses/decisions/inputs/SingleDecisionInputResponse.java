package com.redhat.developer.execution.responses.decisions.inputs;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SingleDecisionInputResponse {
    @JsonProperty("name")
    public String inputName;

    @JsonProperty("typeRef")
    public String typeRef;

    @JsonProperty("value")
    public Object value;

    @JsonProperty("components")
    public Object components;

    public SingleDecisionInputResponse(String inputName, String typeRef, Object components, Object value) {
        this.inputName = inputName;
        this.typeRef = typeRef;
        this.components = components;
        this.value = value;
    }

    public SingleDecisionInputResponse(){}
}
