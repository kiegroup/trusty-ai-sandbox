package com.redhat.developer.dmn.models.input;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelInputStructure {

    @JsonProperty("inputData")
    public List<InputData> inputData;

    @JsonProperty("customTypes")
    public List<TypeDefinition> customTypes;

    public ModelInputStructure() {
    }
}
