package com.redhat.developer.decision.responses;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DecisionInputsResponse {

    @JsonProperty("input")
    public Object input;

    @JsonProperty("evaluationDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    public Date evaluationDate;

    @JsonProperty("id")
    public String id;

    public DecisionInputsResponse(String id, Date evaluationDate, Object input) {
        this.id = id;
        this.input = input;
        this.evaluationDate = evaluationDate;
    }
}
