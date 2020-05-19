package com.redhat.developer.models.decisions;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SingleDecisionInputResponse {
    @JsonProperty("name")
    public String inputName;

    @JsonProperty("typeRef")
    public String typeRef;

    @JsonProperty("value")
    public Object value;

    @JsonProperty("components")
    public Object components;

}
