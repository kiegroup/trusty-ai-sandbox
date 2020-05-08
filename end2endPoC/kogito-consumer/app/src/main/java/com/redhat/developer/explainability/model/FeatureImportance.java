package com.redhat.developer.explainability.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FeatureImportance {

    @JsonProperty("featureName")
    public String featureName;
    @JsonProperty("featureScore")
    public Double featureScore;
}
