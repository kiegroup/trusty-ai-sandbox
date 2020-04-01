package com.redhat.developer.decision.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.redhat.developer.decision.dto.DMNEvent;

public class DecisionDetailResponse {
    @JsonProperty("event")
    public DMNEvent event;

    @JsonProperty("key")
    public String key;

    public DecisionDetailResponse(String key, DMNEvent event){
        this.event = event;
        this.key = key;
    }
}
