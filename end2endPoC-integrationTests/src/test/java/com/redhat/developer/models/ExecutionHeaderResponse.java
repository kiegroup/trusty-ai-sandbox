package com.redhat.developer.models;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExecutionHeaderResponse {

    @JsonProperty("executionId")
    public String executionId;

    @JsonProperty("executionDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    public Date executionDate;

    @JsonProperty("executionSucceeded")
    public boolean executionSucceeded;

    @JsonProperty("executorName")
    public String executorName;

    @JsonProperty("executionType")
    public ExecutionEnum executionType = ExecutionEnum.DECISION;
}