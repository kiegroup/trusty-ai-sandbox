package com.redhat.developer.execution.responses.decisions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.redhat.developer.execution.storage.model.InputType;

public class SingleDecisionInputResponse {
    @JsonProperty("inputName")
    public String inputName;

    @JsonProperty("inputType")
    public InputType inputType = InputType.STRING;

    @JsonProperty("inputValue")
    public Object inputValue;

    public SingleDecisionInputResponse(String inputName, Object inputValue){
        this.inputName = inputName;
        this.inputValue = inputValue;
    }
}
