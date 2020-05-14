package com.redhat.developer.execution.responses.execution;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.redhat.developer.execution.models.DMNResultModel;
import com.redhat.developer.execution.models.ExecutionEnum;

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

    @JsonProperty("executedModelName")
    public String executedModelName;

    @JsonProperty("executionType")
    public ExecutionEnum executionType = ExecutionEnum.DECISION;

    public ExecutionHeaderResponse() {
    }

    public ExecutionHeaderResponse(String executionId, Date executionDate, boolean executionSucceeded, String executorName, String executedModelName) {
        this.executionId = executionId;
        this.executionDate = executionDate;
        this.executionSucceeded = executionSucceeded;
        this.executorName = executorName;
        this.executedModelName = executedModelName;
    }

    public static ExecutionHeaderResponse fromDMNResultModel(DMNResultModel result) {
        return new ExecutionHeaderResponse(result.executionId, result.executionDate, !result.decisions.stream().anyMatch(y -> y.hasErrors), "testUser", result.modelName);
    }
}
