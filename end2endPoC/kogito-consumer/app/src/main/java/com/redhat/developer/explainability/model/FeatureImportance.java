package com.redhat.developer.explainability.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FeatureImportance {

    @JsonProperty("featureName")
    public String featureName;
    @JsonProperty("featureScore")
    public Double featureScore;

    public FeatureImportance(){}

    public FeatureImportance(String featureName, Double featureScore){
        this.featureName = featureName;
        this.featureScore = featureScore;
    }
}
