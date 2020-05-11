package com.redhat.developer.models.decisions;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OutcomeModel {

    @JsonProperty("outcomeId")
    public String outcomeId;

    @JsonProperty("outcomeName")
    public String outcomeName;

    @JsonProperty("outcomeResult")
    public Object result;

    // TODO: create dto properly
    @JsonProperty("messages")
    public List<String> messages;

    @JsonProperty("hasErrors")
    public boolean hasErrors;

}