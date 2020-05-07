package com.redhat.developer.explainability.responses.local;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DecisionExplanationResponse {

    @JsonProperty("featureImportance")
    public List<FeatureImportanceResponse> featureImportance;

    public DecisionExplanationResponse() {
    }
}
