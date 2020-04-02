package com.redhat.developer.decision.responses;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DecisionDetailResponse {
    @JsonProperty("data")
    public ExecutionHeaderResponse data;

    @JsonProperty("evaluationDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    public Date evaluationDate;

    @JsonProperty("id")
    public String id;

    public DecisionDetailResponse(String id, Date evaluationDate, ExecutionHeaderResponse data){
        this.id = id;
        this.data = data;
        this.evaluationDate = evaluationDate;
    }
}
