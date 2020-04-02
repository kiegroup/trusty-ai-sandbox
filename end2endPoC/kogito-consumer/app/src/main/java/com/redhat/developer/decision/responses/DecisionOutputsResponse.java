package com.redhat.developer.decision.responses;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.redhat.developer.decision.storage.model.DecisionResultModel;

public class DecisionOutputsResponse {
    @JsonProperty("decisions")
    public List<DecisionResultModel> decisions;

    @JsonProperty("evaluationDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    public Date evaluationDate;

    @JsonProperty("id")
    public String id;

    public DecisionOutputsResponse(String id, Date evaluationDate, List<DecisionResultModel> decisions) {
        this.id = id;
        this.decisions = decisions;
        this.evaluationDate = evaluationDate;
    }
}
