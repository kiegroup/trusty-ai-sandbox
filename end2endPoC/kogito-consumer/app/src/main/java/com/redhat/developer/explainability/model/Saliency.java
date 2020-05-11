package com.redhat.developer.explainability.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Saliency {
    @JsonProperty("executionId")
    public String executionId;

    @JsonProperty("featureImportance")
    public List<FeatureImportance> featureImportance;
}
