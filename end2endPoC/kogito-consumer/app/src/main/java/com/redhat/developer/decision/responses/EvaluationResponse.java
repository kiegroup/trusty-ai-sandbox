package com.redhat.developer.decision.responses;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.redhat.developer.decision.dto.DecisionResultDto;

public class EvaluationResponse{
    @JsonProperty("id")
    public String id;

    @JsonProperty("evaluationDate")
    public String evaluationDate;

    // TODO REPLACE WITH DTO
    @JsonProperty("decisions")
    public List<DecisionResultDto> decisions;

    public EvaluationResponse(String id, String evaluationDate, List<DecisionResultDto> decisions){
        this.id = id;
        this.evaluationDate = evaluationDate;
        this.decisions = decisions;
    };
}
