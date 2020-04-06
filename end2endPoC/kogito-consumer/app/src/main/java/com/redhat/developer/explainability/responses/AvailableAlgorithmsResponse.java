package com.redhat.developer.explainability.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AvailableAlgorithmsResponse {

    @JsonProperty("availableAlgorithms")
    public String[] availableAlgorithms;

    public AvailableAlgorithmsResponse(String[] availableAlgorithms) {
        this.availableAlgorithms = availableAlgorithms;
    }
}
