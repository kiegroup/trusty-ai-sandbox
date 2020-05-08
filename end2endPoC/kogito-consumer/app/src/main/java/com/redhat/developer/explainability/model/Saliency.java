package com.redhat.developer.explainability.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Saliency {
    @JsonProperty("featureImportance")
    public List<FeatureImportance> featureImportance;
}
