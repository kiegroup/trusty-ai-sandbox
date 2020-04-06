package com.redhat.developer.dmn.responses;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AvailableModelsResponse {

    @JsonProperty("availableModels")
    public List<ModelDetail> availableModels;

    public AvailableModelsResponse(List<ModelDetail> availableModels) {
        this.availableModels = availableModels;
    }
}
