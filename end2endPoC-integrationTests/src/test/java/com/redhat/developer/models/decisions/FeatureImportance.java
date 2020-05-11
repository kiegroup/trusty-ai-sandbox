package com.redhat.developer.models.decisions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FeatureImportance {

    @JsonProperty("featureName")
    public String featureName;
    @JsonProperty("featureScore")
    public Double featureScore;
}
