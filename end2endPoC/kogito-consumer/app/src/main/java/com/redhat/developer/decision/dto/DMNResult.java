package com.redhat.developer.decision.dto;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DMNResult {
    @JsonProperty("evaluationId")
    public String evaluationId;

    @JsonProperty("evaluationDate")
    public String evaluationDate;

    @JsonProperty("decisions")
    public List<DecisionResultDto> decisions;

    @JsonProperty("context")
    public Map<String, Object> context;

    @JsonProperty("modelNamespace")
    public String modelNamespace;

    @JsonProperty("modelName")
    public String modelName;
}

