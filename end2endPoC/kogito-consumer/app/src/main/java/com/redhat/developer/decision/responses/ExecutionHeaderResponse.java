package com.redhat.developer.decision.responses;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ExecutionHeaderResponse {
    @JsonProperty("executionId")
    public String executionId;

    @JsonProperty("executionDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    public Date executionDate;

    @JsonProperty("executionSucceeded")
    public boolean executionSucceeded;

    @JsonProperty("executorName")
    public String executorName;

    public ExecutionHeaderResponse(String executionId, Date executionDate, boolean executionSucceeded, String executorName){
        this.executionId = executionId;
        this.executionDate = executionDate;
        this.executionSucceeded = executionSucceeded;
        this.executorName = executorName;
    }
}
