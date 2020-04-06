package com.redhat.developer.pmml.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AvailablePmmlModelsResponse {

    @JsonProperty("availableModels")
    public String[] availableModels = new String[]{"TEST"};

    public AvailablePmmlModelsResponse() {
    }
}
