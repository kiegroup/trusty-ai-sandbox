package com.redhat.developer.decision.responses;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.redhat.developer.decision.storage.model.DecisionResultModel;

public class EvaluationResponse{
    @JsonProperty("id")
    public String id;

    @JsonProperty("evaluationDate")
    public String evaluationDate;

    // TODO REPLACE WITH DTO
    @JsonProperty("decisions")
    public List<DecisionResultModel> decisions;

    public EvaluationResponse(String id, String evaluationDate, List<DecisionResultModel> decisions){
        this.id = id;
        this.evaluationDate = evaluationDate;
        this.decisions = decisions;
    };
}
