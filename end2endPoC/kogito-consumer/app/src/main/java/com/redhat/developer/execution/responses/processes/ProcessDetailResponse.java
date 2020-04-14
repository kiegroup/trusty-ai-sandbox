package com.redhat.developer.execution.responses.processes;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.redhat.developer.execution.responses.execution.ExecutionHeaderResponse;

public class ProcessDetailResponse {

    @JsonProperty("header")
    public ExecutionHeaderResponse header;

    public ProcessDetailResponse(ExecutionHeaderResponse header) {
        this.header = header;
    }
}
