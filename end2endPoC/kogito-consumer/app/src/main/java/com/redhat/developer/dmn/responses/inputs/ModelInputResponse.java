package com.redhat.developer.dmn.responses.inputs;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelInputResponse {
    @JsonProperty("inputData")
    public List<InputData> inputData;

    @JsonProperty("customTypes")
    public List<TypeDefinition> customTypes;


    public ModelInputResponse(){}
}
