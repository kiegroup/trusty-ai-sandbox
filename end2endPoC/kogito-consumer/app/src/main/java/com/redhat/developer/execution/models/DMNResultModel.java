package com.redhat.developer.execution.models;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DMNResultModel {

    @JsonProperty("executionId")
    public String executionId;

    @JsonProperty("executionTimestamp")
    public Long executionTimestamp;

    @JsonProperty("decisions")
    public List<OutcomeModel> decisions;

    @JsonProperty("context")
    public Map<String, Object> context;

    @JsonProperty("modelNamespace")
    public String modelNamespace;

    @JsonProperty("modelName")
    public String modelName;

    @JsonProperty("modelId")
    public String modelId;

    public Long getExecutionTimestamp() {
        return executionTimestamp;
    }
}

