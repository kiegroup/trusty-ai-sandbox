package com.redhat.developer.decision.responses;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.redhat.developer.decision.dto.DecisionResultDto;

public class DecisionsResponse {
    @JsonProperty("availableRecords")
    public int availableRecords;

    @JsonProperty("returnedRecords")
    public int returnedRecords;

    @JsonProperty("offset")
    public int offset;

    // TODO Replace with dto
    @JsonProperty("data")
    public List<EvaluationResponse> data;

    public DecisionsResponse(int availableRecords, int returnedRecords, int offset, List<EvaluationResponse> evaluationResponse){
        this.availableRecords = availableRecords;
        this.returnedRecords = returnedRecords;
        this.offset = offset;
        this.data = evaluationResponse;
    }
}
