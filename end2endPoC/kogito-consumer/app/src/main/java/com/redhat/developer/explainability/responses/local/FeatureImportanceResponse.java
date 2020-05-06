package com.redhat.developer.explainability.responses.local;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FeatureImportanceResponse {
    @JsonProperty("featureName")
    public String featureName;

    @JsonProperty("featureValue")
    public Double featureValue;
}
