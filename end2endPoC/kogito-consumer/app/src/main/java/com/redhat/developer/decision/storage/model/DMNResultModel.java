package com.redhat.developer.decision.storage.model;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DMNResultModel {
    @JsonProperty("evaluationId")
    public String evaluationId;

    @JsonProperty("evaluationDate")
    public String evaluationDate;

    @JsonProperty("decisions")
    public List<DecisionResultModel> decisions;

    @JsonProperty("context")
    public Map<String, Object> context;

    @JsonProperty("modelNamespace")
    public String modelNamespace;

    @JsonProperty("modelName")
    public String modelName;
}

