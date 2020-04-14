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

    @JsonProperty("isComposite")
    public boolean isComposite;

    @JsonProperty("isCollection")
    public boolean isCollection;

    @JsonProperty("components")
    public List<List<SingleDecisionInputResponse>> components;

    public SingleDecisionInputResponse(String inputName, String typeRef, boolean isComposite, boolean isCollection, List<List<SingleDecisionInputResponse>> components, Object value) {
        this.inputName = inputName;
        this.typeRef = typeRef;
        this.isComposite = isComposite;
        this.isCollection = isCollection;
        this.components = components;
        this.value = value;
    }
}
