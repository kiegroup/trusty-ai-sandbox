package com.redhat.developer.models.decisions;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Saliency {
    @JsonProperty("featureImportance")
    public List<FeatureImportance> featureImportance;
}