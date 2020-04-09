package com.redhat.developer.dmn.responses.inputs;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InputData{
    @JsonProperty("name")
    public String name;

    @JsonProperty("typeRef")
    public String typeRef;
}