package com.redhat.developer.decision.responses;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.redhat.developer.decision.storage.model.DecisionResultModel;

public class DecisionOutputsResponse {
    @JsonProperty("decisions")
    public List<DecisionResultModel> decisions;

    @JsonProperty("evaluationDate")
    public String evaluationDate;

    @JsonProperty("id")
    public String id;

    public DecisionOutputsResponse(String id, String evaluationDate, List<DecisionResultModel> decisions) {
        this.id = id;
        this.decisions = decisions;
        this.evaluationDate = evaluationDate;
    }
}
