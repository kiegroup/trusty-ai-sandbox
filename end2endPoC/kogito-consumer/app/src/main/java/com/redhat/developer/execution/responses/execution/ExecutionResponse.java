package com.redhat.developer.execution.responses.execution;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExecutionResponse {

    @JsonProperty("total")
    public int total;

    @JsonProperty("limit")
    public int limit;

    @JsonProperty("offset")
    public int offset;

    @JsonProperty("headers")
    public List<ExecutionHeaderResponse> executionHeadersResponse;

    public ExecutionResponse(int total, int returnedRecords, int offset, List<ExecutionHeaderResponse> executionHeadersResponse) {
        this.total = total;
        this.limit = returnedRecords;
        this.offset = offset;
        this.executionHeadersResponse = executionHeadersResponse;
    }
}
