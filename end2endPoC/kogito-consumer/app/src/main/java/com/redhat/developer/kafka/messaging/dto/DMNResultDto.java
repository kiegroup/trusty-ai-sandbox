package com.redhat.developer.kafka.messaging.dto;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DMNResultDto {

    @JsonProperty("evaluationId")
    public String evaluationId;

    @JsonProperty("evaluationTimestamp")
    public Long evaluationTimestamp;

    @JsonProperty("decisions")
    public List<DecisionResultDto> decisions;

    @JsonProperty("context")
    public Map<String, Object> context;

    @JsonProperty("modelNamespace")
    public String modelNamespace;

    @JsonProperty("modelName")
    public String modelName;
}

