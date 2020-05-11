package com.redhat.developer.models.decisions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DecisionInputStructured extends SingleDecisionInputResponse {

    @JsonProperty("id")
    public String id;

    @JsonProperty("category")
    public String category;
}
