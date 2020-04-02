package com.redhat.developer.decision.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExecutionHeaderResponse {
    @JsonProperty("executionId")
    public String executionId;

    @JsonProperty("executionDate")
    public String executionDate;

    @JsonProperty("executionSucceeded")
    public boolean executionSucceeded;

    @JsonProperty("executorName")
    public String executorName;

    public ExecutionHeaderResponse(String executionId, String executionDate, boolean executionSucceeded, String executorName){
        this.executionId = executionId;
        this.executionDate = executionDate;
        this.executionSucceeded = executionSucceeded;
        this.executorName = executorName;
    }
}
