package com.redhat.developer.dmn.models.input;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Constraint {

    @JsonProperty("type")
    public ConstraintEnum type;

    @JsonProperty("text")
    public String text;
}
