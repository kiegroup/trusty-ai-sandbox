package com.redhat.developer.dmn.responses.inputs;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TypeComponent{
    @JsonProperty("name")
    public String name;

    @JsonProperty("typeRef")
    public String typeRef;

    @JsonProperty("isCollection")
    public boolean isCollection;

    @JsonProperty("hasConstraint")
    public boolean hasConstraint;

    @JsonProperty("constraint")
    public Constraint constraint;
}