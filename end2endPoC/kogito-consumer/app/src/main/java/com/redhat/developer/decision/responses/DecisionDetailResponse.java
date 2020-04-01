package com.redhat.developer.decision.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.redhat.developer.decision.storage.model.DMNEventModel;

public class DecisionDetailResponse {
    @JsonProperty("event")
    public DMNEventModel event;

    @JsonProperty("key")
    public String key;

    public DecisionDetailResponse(String key, DMNEventModel event){
        this.event = event;
        this.key = key;
    }
}
