package com.redhat.developer.decision.storage.model;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DMNResultModel {
    @JsonProperty("executionId")
    public String executionId;

    @JsonProperty("executionDate")
    public String executionDate;

    @JsonProperty("decisions")
    public List<DecisionResultModel> decisions;

    @JsonProperty("context")
    public Map<String, Object> context;

    @JsonProperty("modelNamespace")
    public String modelNamespace;

    @JsonProperty("modelName")
    public String modelName;
}

