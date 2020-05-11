package com.redhat.developer.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FeatureImportanceResponse {
    @JsonProperty("featureName")
    public String featureName;
    @JsonProperty("featureScore")
    public Double featureScore;

    public FeatureImportanceResponse(){}

    public FeatureImportanceResponse(String featureName, Double featureScore){
        this.featureName = featureName;
        this.featureScore = featureScore;
    }
}
